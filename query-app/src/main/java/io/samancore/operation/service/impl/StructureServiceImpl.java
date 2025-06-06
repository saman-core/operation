package io.samancore.operation.service.impl;

import io.samancore.operation.entity.StructureEntity;
import io.samancore.operation.model.Structure;
import io.samancore.operation.repository.StructureRepository;
import io.samancore.operation.service.StructureService;
import io.samancore.operation.transformer.StructureTransformer;
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
public class StructureServiceImpl implements StructureService {

    @Inject
    StructureRepository repository;

    @Inject
    StructureTransformer transformer;

    @Inject
    Logger log;

    @Override
    public Uni<Structure> getRootByOperationId(Long operationId) {
        log.debugf("StructureServiceImpl.getRootByOperationId %d ", operationId);
        return repository.getRootByOperationId(operationId)
                .onItem().transform(entity -> transformer.toModel(entity));
    }

    @Override
    public Uni<List<Structure>> getChildrenByParams(Long parentId, Long operationId, Long relationshipId) {
        log.debugf("StructureServiceImpl.getChildrenByParams %d %d %d", parentId, operationId, relationshipId);
        return repository.getByOperationId( operationId)
            .onItem().transform(entities ->{
                var structureMap = entities.stream()
                        .filter(structureEntity -> structureEntity.getParentId() != null)
                        .collect(Collectors.groupingBy(structureEntity -> structureEntity.getParentId()));
                var firstLevelChildren = entities.stream()
                        .filter(structureEntity -> structureEntity.getParentId() != null)
                        .filter(structureEntity -> structureEntity.getParentId().equals(parentId) && structureEntity.getRelationshipId().equals(relationshipId))
                        .toList();
                var children =  firstLevelChildren.stream()
                        .map(structureEntity -> getChildren(structureEntity.getId(), structureMap))
                        .flatMap(List::stream)
                        .collect(Collectors.toList());
                List<StructureEntity> structureEntityList = new ArrayList<>();
                structureEntityList.addAll(firstLevelChildren);
                structureEntityList.addAll(children);
                return structureEntityList;
        })
        .onItem().transform(entities -> transformer.toModelList(entities));
    }

    @Override
    public Uni<List<Structure>> getParentsByParams(Long childId, Long operationId) {
        log.debugf("StructureServiceImpl.getParentsByParams %d %d ", childId, operationId);
        return repository.getByOperationId( operationId)
            .onItem().transform(entities -> {
                var structureMap = entities.stream().collect(Collectors.toMap(structureEntity -> structureEntity.getId(), structureEntity -> structureEntity));
                var child =  entities.stream()
                        .filter(structureEntity -> structureEntity.getId().equals(childId))
                        .findFirst().get();
                var parents = getParent(child.getParentId(), structureMap);
                parents.add(child);
                return parents;
            })
            .onItem().transform(entities -> transformer.toModelList(entities));
    }

    @Override
    public Uni<Structure> getAllByOperationId(Long operationId) {
        return repository.getByOperationId( operationId)
                .onItem().transform(entities -> transformer.toModelList(entities))
            .onItem().transform(structureList -> {
                structureList = structureList.stream().map(structure -> structure.toBuilder().setChildren(new ArrayList<>()).build()).collect(Collectors.toList());
                AtomicReference<Structure> root = new AtomicReference<>();
                var structureMap = structureList.stream()
                        .collect(Collectors.toMap(structureEntity -> structureEntity.getId(), structureEntity -> structureEntity));
                structureList.forEach(entity -> {
                    if (entity.getParentId() != null) {
                        Structure parent = structureMap.get(entity.getParentId());
                        if (parent != null) {
                            parent.getChildren().add(entity);
                        }
                    } else {
                        root.set(entity);
                    }
                });
                return root.get();
            });
    }

    public List<StructureEntity> getChildren(Long parentId, Map<Long, List<StructureEntity>> structureMap) {
        List<StructureEntity> children = structureMap.get(parentId);
        if(children == null) return List.of();
        for (StructureEntity child : new ArrayList<>(children)) {
            children.addAll(getChildren(child.getId(), structureMap));
        }
        return children;

    }

    public List<StructureEntity> getParent(Long childId, Map<Long, StructureEntity> structureMap) {
        List<StructureEntity> parents = new ArrayList<>();
        StructureEntity parent = structureMap.get(childId);
        parents.add(parent);
        while (parent.getParentId() != null){
            parent = structureMap.get(parent.getParentId());
            parents.add(parent);
        }
        return parents;
    }

}