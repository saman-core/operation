package io.samancore.operation.service;

import io.samancore.common.model.PageData;
import io.samancore.common.page.PageRequest;
import io.samancore.operation.model.Operation;
import io.samancore.operation.request.OperationFilterRequest;
import io.smallrye.mutiny.Uni;

public interface OperationService {

    Uni<Operation> getById(Long id);

    Uni<PageData<Operation>> getByFilters(OperationFilterRequest request, PageRequest pageRequest);
}