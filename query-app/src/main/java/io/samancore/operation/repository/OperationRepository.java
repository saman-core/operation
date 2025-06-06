package io.samancore.operation.repository;

import io.samancore.operation.entity.ContractEntity;
import io.samancore.operation.entity.OperationEntity;
import io.smallrye.mutiny.Uni;

import java.util.Date;
import java.util.List;

public interface OperationRepository {
    Uni<OperationEntity> getById(Long id);

    Uni<List<OperationEntity>> getByContractId(long contractId);

    Uni<List<OperationEntity>> getByContractIdAndRangeDate(long contractId, Date startDate, Date endDate);

    Uni<List<OperationEntity>> getByContractIdAndRangeDate(long contractId, Date date);

    Uni<List<OperationEntity>> getByRangeDate(Date startDate, Date endDate);
}