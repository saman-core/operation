package io.samancore.operation.service;

import io.samancore.operation.model.Reference;
import io.smallrye.mutiny.Uni;

import java.util.List;

public interface ReferenceService {

    Uni<Reference> getRootByOperationId(Long operationId);

    Uni<List<Reference>> getChildrenByParams(Long parentId, Long operationId, Long relationshipId);

    Uni<List<Reference>> getParentsByParams(Long childId, Long operationId);

    Uni<List<Reference>> getAllByOperationId(Long operationId);
}