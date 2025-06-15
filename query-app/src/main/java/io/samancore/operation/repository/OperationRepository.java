package io.samancore.operation.repository;

import io.samancore.common.model.PageData;
import io.samancore.common.page.PageRequest;
import io.samancore.operation.entity.OperationEntity;
import io.samancore.operation.model.OperationStatus;
import io.samancore.operation.request.OperationFilterRequest;
import io.smallrye.mutiny.Uni;

import java.util.Date;
import java.util.List;

public interface OperationRepository {
    Uni<OperationEntity> getById(Long id);

    Uni<PageData<OperationEntity>> getByContractIdAndStatus(long contractId, OperationStatus status);

    Uni<List<OperationEntity>> getByRangeDate(Date startDate, Date endDate);

    Uni<PageData<OperationEntity>> getByFilters(OperationFilterRequest request, PageRequest pageRequest);
}