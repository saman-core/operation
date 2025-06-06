package io.samancore.operation.repository;

import io.samancore.operation.entity.ContractEntity;
import io.smallrye.mutiny.Uni;

import java.util.Date;
import java.util.List;

public interface ContractRepository {

    Uni<ContractEntity> getById(Long id);

    Uni<ContractEntity> getByOperationId(long operationId);

    Uni<ContractEntity> getByOperationIdAndDate(long operationId, Date date);

    Uni<List<ContractEntity>> getByRangeDate(Date startDate, Date endDate);
}