package io.samancore.operation.repository;

import io.samancore.operation.entity.StructureEntity;
import io.smallrye.mutiny.Uni;

import java.util.List;

public interface StructureRepository {

    Uni<StructureEntity> getRootByOperationId(Long operationId);
    Uni<List<StructureEntity>> getByOperationId(Long operationId);
    Uni<List<StructureEntity>> getChildrenByParams(Long parentId, Long operationId, Long relationshipId);
    Uni<List<StructureEntity>> getChildrenByParentId(Long parentId);
}