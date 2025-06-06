package io.samancore.operation.repository;

import io.samancore.operation.entity.ContractEntity;

public interface ContractRepository {

    ContractEntity create(ContractEntity entity);
    ContractEntity update(ContractEntity entity);
    ContractEntity getById(Long id);
}