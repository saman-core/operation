package io.samancore.operation.service;

import io.samancore.common.model.PageData;
import io.samancore.common.page.PageRequest;
import io.samancore.operation.model.Contract;
import io.samancore.operation.model.Operation;
import io.smallrye.mutiny.Uni;

import java.util.List;
import java.util.Map;

public interface ContractService {

    Uni<Contract> getById(Long id);

    Uni<Contract> getByOperationId(long operationId);

    Uni<List<Contract>> getByRangeDate(String startDateStr, String endDateStr);

    Uni<PageData<Operation>> getOperationByContractId(long id, PageRequest pageRequest);

    Uni<PageData<Contract>> getByKeyWords(Map<String, List<String>> paramss, PageRequest pageRequest);
}