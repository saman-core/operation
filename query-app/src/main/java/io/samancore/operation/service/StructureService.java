package io.samancore.operation.service;

import io.samancore.operation.model.Structure;
import io.smallrye.mutiny.Uni;

import java.util.List;

public interface StructureService {

    Uni<Structure> getRootByOperationId(Long operationId);
    Uni<List<Structure>> getChildrenByParams(Long parentId, Long operationId, Long relationshipId);
    Uni<List<Structure>> getParentsByParams(Long childId, Long operationId);
    Uni<Structure> getAllByOperationId(Long operationId);
}