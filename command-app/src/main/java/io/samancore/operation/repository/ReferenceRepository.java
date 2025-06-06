package io.samancore.operation.repository;

import io.samancore.operation.entity.ReferenceEntity;
import io.samancore.operation.entity.StructureEntity;

import java.util.List;

public interface ReferenceRepository {
    ReferenceEntity create(ReferenceEntity entity);
    ReferenceEntity update(ReferenceEntity entity);
    List<ReferenceEntity> createAll(List<ReferenceEntity> entityList);
    List<ReferenceEntity> updateAll(List<ReferenceEntity> entityList);
    ReferenceEntity getById(Long id);

    List<ReferenceEntity> getByIds(List<Long> idList);

    List<ReferenceEntity> getByOperationId(Long operationId);
}