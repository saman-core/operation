package io.samancore.operation.service.impl;

import io.samancore.operation.entity.*;
import io.samancore.operation.model.*;
import io.samancore.operation.repository.*;
import io.samancore.operation.request.*;
import io.samancore.operation.service.OperationService;
import io.samancore.operation.transformer.ContractTransformer;
import io.samancore.operation.transformer.OperationTransformer;
import io.samancore.operation.transformer.ReferenceTransformer;
import io.samancore.operation.transformer.StructureTransformer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class OperationServiceImpl implements OperationService {


    @Inject
    ContractRepository contractRepository;

    @Inject
    ContractTransformer contractTransformer;

    @Inject
    OperationRepository operationRepository;

    @Inject
    OperationTransformer operationTransformer;

    @Inject
    StructureRepository structureRepository;

    @Inject
    StructureTransformer structureTransformer;

    @Inject
    ReferenceRepository referenceRepository;

    @Inject
    ReferenceTransformer referenceTransformer;


    @Inject
    OutsourceRepository outsourceRepository;


    @Inject
    Logger log;

    @Transactional
    @Override
    public Contract createContract(CreateContractRequest createContractRequest) {
        log.debugf("OperationServiceImpl.createContract %s ", createContractRequest);
        var contractEntity = new ContractEntity();
        contractEntity.setCreatedDate(new Date());
        contractEntity.setProductId(createContractRequest.getProductId());
        contractEntity.setStateId(createContractRequest.getStateId());
        contractEntity.setStatus(GeneralStatus.ACTIVE);
        var contractEntityResult = contractRepository.create(contractEntity);
        return contractTransformer.toModel(contractEntityResult);
    }

    @Transactional
    @Override
    public Operation createTransition(CreateTransitionRequest createTransitionRequest) {
        log.debugf("OperationServiceImpl.createTransition %s ", createTransitionRequest);
        var contractEntity = contractRepository.getById(createTransitionRequest.getContractId());
        var operationEntity = new OperationEntity();
        operationEntity.setContractId(createTransitionRequest.getContractId());
        operationEntity.setStatus(OperationStatus.PENDING);
        operationEntity.setCreatedDate(new Date());
        var transitionAppliedEntity = new TransitionAppliedEntity();
        transitionAppliedEntity.setCdeId(createTransitionRequest.getCdeId());
        transitionAppliedEntity.setCdeTemplate(createTransitionRequest.getCdeTemplate());
        transitionAppliedEntity.setTransitionId(createTransitionRequest.getTransitionId());
        operationEntity.setTransitionApplied(transitionAppliedEntity);
        operationEntity.setParentId(contractEntity.getLastOperationId() != null ? contractEntity.getLastOperationId(): null);
        var operationEntityResult = operationRepository.create(operationEntity);
        if(contractEntity.getLastOperationId() != null){
            var referenceEntityList = referenceRepository.getByOperationId(contractEntity.getLastOperationId());
            var referenceEntityUpdatedList = referenceEntityList.stream().map(referenceEntity -> copyReference(referenceEntity, operationEntityResult.getId())).toList();
            referenceRepository.createAll(referenceEntityUpdatedList);
        }
        return operationTransformer.toModel(operationEntityResult);
    }

    private ReferenceEntity copyReference(ReferenceEntity referenceEntity, Long operationId) {
        var copy = new ReferenceEntity();
        copy.setOperationId(operationId);
        copy.setStructureId(referenceEntity.getStructureId());

        copy.setStructure(referenceEntity.getStructure());
        copy.setStatus(GeneralStatus.ACTIVE);
        copy.setCdeId(referenceEntity.getCdeId());
        copy.setCdeTemplate(referenceEntity.getCdeTemplate());
        copy.setOutsourceId(referenceEntity.getOutsourceId());
        return copy;
    }

    @Transactional
    @Override
    public Operation applyTransition(Long operationId) {
        log.debugf("OperationServiceImpl.applyTransition %d ", operationId);
        var operationEntity = operationRepository.getById(operationId);
        operationRepository.cancelOtherOperationsByContractId( operationId, operationEntity.getContractId());
        operationEntity.setStatus(OperationStatus.APPLIED);
        var today = new Date();
        operationEntity.setAppliedDate(today);
        operationRepository.update(operationEntity);
        var contractEntity = contractRepository.getById(operationEntity.getContractId());
        contractEntity.setLastOperationId(operationEntity.getId());
        contractEntity.setLastUpdatedDate(today);
        contractRepository.update(contractEntity);
        return operationTransformer.toModel(operationEntity);
    }

    @Transactional
    @Override
    public Structure createStructure(CreateStructureRequest createStructureRequest) {
        var operationEntity = operationRepository.getById(createStructureRequest.getOperationId());
        if(!operationEntity.getStatus().equals(OperationStatus.PENDING)){
            throw new RuntimeException("change not allowed, operation can not be updated");
        }
        StructureEntity structureEntity = buildStructureEntity(createStructureRequest);

        var structureEntityResult =structureRepository.create(structureEntity);
        ReferenceEntity referenceEntity = buildReferenceEntity(createStructureRequest, structureEntityResult);
        var referenceEntityResult = referenceRepository.create(referenceEntity);
        structureEntityResult.setReferences(List.of(referenceEntityResult));
        return structureTransformer.toModel(structureEntityResult);
    }

    private ReferenceEntity buildReferenceEntity(CreateStructureRequest createStructureRequest, StructureEntity structureEntityResult) {
        var referenceEntity = new ReferenceEntity();
        referenceEntity.setOperationId(createStructureRequest.getOperationId());
        referenceEntity.setStatus(GeneralStatus.ACTIVE);
        referenceEntity.setStructure(structureEntityResult);
        return referenceEntity;
    }

    private StructureEntity buildStructureEntity(CreateStructureRequest createStructureRequest) {
        var structureEntity = new StructureEntity();
        structureEntity.setName(createStructureRequest.getName());
        structureEntity.setEntityId(createStructureRequest.getEntityId());
        structureEntity.setRelationshipId(createStructureRequest.getRelationshipId());
        structureEntity.setCreatedOperationId(createStructureRequest.getOperationId());
        structureEntity.setStatus(GeneralStatus.ACTIVE);
        structureEntity.setParentId(createStructureRequest.getParentId());
        return structureEntity;
    }

    @Transactional
    @Override
    public List<Structure> deleteStructure(DeleteStructureRequest deleteStructureRequest) {
        var operationEntity = operationRepository.getById(deleteStructureRequest.getOperationId());
        if(!operationEntity.getStatus().equals(OperationStatus.PENDING)){
            throw new RuntimeException("change not allowed, operation can not be updated");
        }
        var entities = structureRepository.getByOperationId(deleteStructureRequest.getOperationId());
        var structureMap = entities.stream().filter(structureEntity -> structureEntity.getParentId()!= null).collect(Collectors.groupingBy(structureEntity -> structureEntity.getParentId()));
        var rootStructure = entities.stream().filter(structureEntity -> structureEntity.getId().equals(deleteStructureRequest.getStructureId())).findFirst().get();
        if(rootStructure.getStatus().equals(GeneralStatus.INACTIVE)){
            throw new RuntimeException("change not allowed, structure can not be updated");
        }
        var children = getChildren(deleteStructureRequest.getStructureId(), structureMap);
        List<StructureEntity> structureEntityList = new ArrayList<>();
        structureEntityList.add(rootStructure);
        structureEntityList.addAll(children);
        structureEntityList.forEach(structureEntity -> {
            structureEntity.setDeletedOperationId(deleteStructureRequest.getOperationId());
            structureEntity.setStatus(GeneralStatus.INACTIVE);
        });
        var result = structureRepository.updateAll(structureEntityList);
        return structureTransformer.toModelList(result);
    }

    @Transactional
    @Override
    public Reference updateReference( Long referenceId, UpdateReferenceRequest updateReferenceRequest) {
        var referenceEntity = referenceRepository.getById(referenceId);
        if(referenceEntity.getStatus().equals(GeneralStatus.INACTIVE)){
            throw new RuntimeException("change not allowed, reference can not be updated");
        }
        var operationEntity = operationRepository.getById(referenceEntity.getOperationId());
        if(!operationEntity.getStatus().equals(OperationStatus.PENDING)){
            throw new RuntimeException("change not allowed, operation can not be updated");
        }
        referenceEntity.setOperationId(updateReferenceRequest.getOperationId());
        if(referenceEntity.getOutsource() == null){
            if(updateReferenceRequest.getCdeId() != null && updateReferenceRequest.getCdeTemplate() != null){
                referenceEntity.setCdeId(updateReferenceRequest.getCdeId());
                referenceEntity.setCdeTemplate(updateReferenceRequest.getCdeTemplate());
            }else {
                referenceEntity.setCdeId(null);
                referenceEntity.setCdeTemplate(null);
                OutsourceEntity outsourceEntity = buildOutsourceEntity(updateReferenceRequest);
                referenceEntity.setOutsource(outsourceEntity);
            }
        }else {
            if (updateReferenceRequest.getCdeId() != null && updateReferenceRequest.getCdeTemplate() != null) {
                referenceEntity.setCdeId(updateReferenceRequest.getCdeId());
                referenceEntity.setCdeTemplate(updateReferenceRequest.getCdeTemplate());
                referenceEntity.setOutsource(null);
                outsourceRepository.delete(referenceEntity.getOutsourceId());
            } else {
                referenceEntity.getOutsource().setOperationId(updateReferenceRequest.getOutsourceOperationId());
            }
        }
        var referenceEntityResult = referenceRepository.update(referenceEntity);
        return referenceTransformer.toModel(referenceEntityResult);
    }

    private OutsourceEntity buildOutsourceEntity(UpdateReferenceRequest updateReferenceRequest) {
        var outsourceEntity = new OutsourceEntity();
        outsourceEntity.setId(null);
        outsourceEntity.setOperationId(updateReferenceRequest.getOutsourceOperationId());
        outsourceEntity.setModule(updateReferenceRequest.getOutsourceModule());
        outsourceEntity.setContractId(updateReferenceRequest.getOutsourceContractId());
        outsourceEntity.setStructureId(updateReferenceRequest.getOutsourceStructureId());
        return outsourceEntity;
    }

    public List<StructureEntity> getChildren(Long parentId, Map<Long, List<StructureEntity>> structureMap) {
        List<StructureEntity> children = structureMap.get(parentId);
        if(children == null) return List.of();
        for (StructureEntity child : new ArrayList<>(children)) {
            children.addAll(getChildren(child.getId(), structureMap));
        }
        return children;
    }
}