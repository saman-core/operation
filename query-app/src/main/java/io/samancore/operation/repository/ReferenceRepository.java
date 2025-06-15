package io.samancore.operation.repository;

import io.samancore.operation.entity.ReferenceEntity;
import io.smallrye.mutiny.Uni;

import java.util.List;

public interface ReferenceRepository {

    Uni<ReferenceEntity> getRootByOperationId(Long operationId);

    Uni<List<ReferenceEntity>> getByOperationId(Long operationId);

    Uni<List<ReferenceEntity>> getChildrenByParams(Long parentId, Long operationId, Long relationshipId);

    Uni<List<ReferenceEntity>> getChildrenByParentId(Long parentId);
}