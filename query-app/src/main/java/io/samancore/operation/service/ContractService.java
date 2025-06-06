package io.samancore.operation.service;

import io.samancore.operation.model.Contract;
import io.smallrye.mutiny.Uni;

import java.util.List;

public interface ContractService {

    Uni<Contract> getById(Long id);

    Uni<Contract> getByOperationId(long operationId);

    Uni<Contract> getByIdAndDate(long operationId, String date);

    Uni<List<Contract>> getByRangeDate(String startDateStr, String endDateStr);
}