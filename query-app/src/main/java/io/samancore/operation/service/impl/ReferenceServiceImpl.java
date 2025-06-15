package io.samancore.operation.service.impl;

import io.samancore.operation.entity.ReferenceEntity;
import io.samancore.operation.model.Reference;
import io.samancore.operation.repository.ReferenceRepository;
import io.samancore.operation.service.ReferenceService;
import io.samancore.operation.transformer.ReferenceTransformer;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@ApplicationScoped
public class ReferenceServiceImpl implements ReferenceService {

    @Inject
    ReferenceRepository repository;

    @Inject
    ReferenceTransformer transformer;

    @Inject
    Logger log;

    @Override
    public Uni<Reference> getRootByOperationId(Long operationId) {
        log.debugf("ReferenceServiceImpl.getRootByOperationId %d ", operationId);
        return repository.getRootByOperationId(operationId)
                .onItem().transform(entity -> transformer.toModel(entity));
    }

    @Override
    public Uni<List<Reference>> getChildrenByParams(Long parentId, Long operationId, Long relationshipId) {
        log.debugf("ReferenceServiceImpl.getChildrenByParams %d %d %d", parentId, operationId, relationshipId);
        return repository.getByOperationId(operationId)
                .onItem().transform(entities -> {
                    var referenceByStructureParentIdMap = entities.stream()
                            .filter(referenceEntity -> referenceEntity.getStructure().getParentId() != null)
                            .collect(Collectors.groupingBy(referenceEntity -> referenceEntity.getStructure().getParentId()));
                    var firstLevelChildren = entities.stream()
                            .filter(referenceEntity -> referenceEntity.getStructure().getParentId() != null)
                            .filter(referenceEntity -> referenceEntity.getStructure().getParentId().equals(parentId) && referenceEntity.getStructure().getRelationshipId().equals(relationshipId))
                            .toList();
                    var children = firstLevelChildren.stream()
                            .map(structureEntity -> getChildren(structureEntity.getId(), referenceByStructureParentIdMap))
                            .flatMap(List::stream)
                            .collect(Collectors.toList());
                    List<ReferenceEntity> structureEntityList = new ArrayList<>();
                    structureEntityList.addAll(firstLevelChildren);
                    structureEntityList.addAll(children);
                    return structureEntityList;
                })
                .onItem().transform(entities -> transformer.toModelList(entities));
    }

    @Override
    public Uni<List<Reference>> getParentsByParams(Long childId, Long operationId) {
        log.debugf("ReferenceServiceImpl.getParentsByParams %d %d ", childId, operationId);
        return repository.getByOperationId(operationId)
                .onItem().transform(entities -> {
                    var referenceEntityByStructureIdMap = entities.stream().collect(Collectors.toMap(referenceEntity -> referenceEntity.getStructure().getId(), referenceEntity -> referenceEntity));
                    var child = entities.stream()
                            .filter(referenceEntity -> referenceEntity.getStructure().getId().equals(childId))
                            .findFirst().get();
                    var parents = getParent(child.getStructure().getParentId(), referenceEntityByStructureIdMap);
                    parents.add(child);
                    return parents;
                })
                .onItem().transform(entities -> transformer.toModelList(entities));
    }

    @Override
    public Uni<List<Reference>> getAllByOperationId(Long operationId) {
        return repository.getByOperationId(operationId)
                .onItem().transform(entities -> transformer.toModelList(entities));
    }

    public List<ReferenceEntity> getChildren(Long parentId, Map<Long, List<ReferenceEntity>> structureMap) {
        List<ReferenceEntity> children = structureMap.get(parentId);
        if (children == null) return List.of();
        for (ReferenceEntity child : new ArrayList<>(children)) {
            children.addAll(getChildren(child.getId(), structureMap));
        }
        return children;

    }

    public List<ReferenceEntity> getParent(Long childId, Map<Long, ReferenceEntity> referenceEntityMap) {
        List<ReferenceEntity> parents = new ArrayList<>();
        ReferenceEntity parent = referenceEntityMap.get(childId);
        parents.add(parent);
        while (parent.getStructure().getParentId() != null) {
            parent = referenceEntityMap.get(parent.getStructure().getParentId());
            parents.add(parent);
        }
        return parents;
    }

}