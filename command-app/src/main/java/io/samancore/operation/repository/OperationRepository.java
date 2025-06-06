package io.samancore.operation.repository;

import io.samancore.operation.entity.OperationEntity;

public interface OperationRepository {

    OperationEntity create(OperationEntity entity);
    OperationEntity update(OperationEntity entity);
    OperationEntity getById(Long id);
    void cancelOtherOperationsByContractId(Long operationId, Long contractId);
}