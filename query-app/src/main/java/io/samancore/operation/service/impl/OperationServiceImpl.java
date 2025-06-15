package io.samancore.operation.service.impl;

import io.samancore.common.model.PageData;
import io.samancore.common.page.PageRequest;
import io.samancore.common.page.PageUtil;
import io.samancore.operation.model.Operation;
import io.samancore.operation.repository.OperationRepository;
import io.samancore.operation.request.OperationFilterRequest;
import io.samancore.operation.service.OperationService;
import io.samancore.operation.transformer.OperationTransformer;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.text.SimpleDateFormat;

@ApplicationScoped
public class OperationServiceImpl implements OperationService {

    @Inject
    OperationRepository repository;

    @Inject
    OperationTransformer transformer;

    @Inject
    Logger log;

    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

    @Override
    public Uni<Operation> getById(Long id) {
        log.debugf("OperationServiceImpl.getById %d ", id);
        return repository.getById(id)
                .onItem().transform(contractEntity -> transformer.toModel(contractEntity));
    }

    @Override
    public Uni<PageData<Operation>> getByFilters(OperationFilterRequest request, PageRequest pageRequest) {
        log.debugf("OperationServiceImpl.getByFilters %s ", request);
        return repository.getByFilters(request, pageRequest)
                .onItem().transform(operationEntityPageData -> PageUtil.toPageModel(operationEntityPageData, transformer::toModel));
    }
}