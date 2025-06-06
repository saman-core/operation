package io.samancore.operation.repository;

import io.samancore.operation.entity.StructureEntity;

import java.util.List;

public interface StructureRepository {

    StructureEntity create(StructureEntity entity);
    StructureEntity update(StructureEntity entity);
    List<StructureEntity> updateAll(List<StructureEntity> entityList);
    StructureEntity getById(Long id);

    List<StructureEntity> getByIds(List<Long> idList);

    List<StructureEntity> getByOperationId(Long operationId);
}