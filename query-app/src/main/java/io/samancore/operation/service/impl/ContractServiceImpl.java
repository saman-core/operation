package io.samancore.operation.service.impl;

import io.samancore.common.error.message.TechnicalExceptionsEnum;
import io.samancore.common.error.util.ExceptionHandler;
import io.samancore.common.model.PageData;
import io.samancore.common.page.PageRequest;
import io.samancore.common.page.PageUtil;
import io.samancore.operation.entity.OperationEntity;
import io.samancore.operation.model.Contract;
import io.samancore.operation.model.Operation;
import io.samancore.operation.model.OperationStatus;
import io.samancore.operation.repository.ContractRepository;
import io.samancore.operation.repository.OperationRepository;
import io.samancore.operation.service.ContractService;
import io.samancore.operation.transformer.ContractTransformer;
import io.samancore.operation.transformer.OperationTransformer;
import io.samancore.operation.util.Util;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class ContractServiceImpl implements ContractService {

    @Inject
    ContractRepository repository;

    @Inject
    OperationRepository operationRepository;

    @Inject
    ContractTransformer transformer;

    @Inject
    OperationTransformer operationTransformer;

    @Inject
    Logger log;

    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

    @Override
    public Uni<Contract> getById(Long id) {
        log.debugf("ContractServiceImpl.getById %d ", id);
        return repository.getById(id)
                .onItem().transform(contractEntity -> transformer.toModel(contractEntity));
    }

    @Override
    public Uni<Contract> getByOperationId(long operationId) {
        log.debugf("ContractServiceImpl.getByOperationId %d ", operationId);
        return operationRepository.getById(operationId)
                .onItem().transform(operationEntity -> transformer.toModel(operationEntity.getContract()));
    }

    @Override
    public Uni<List<Contract>> getByRangeDate(String startDateStr, String endDateStr) {
        log.debugf("ContractServiceImpl.getByRangeDate %s %s", startDateStr, endDateStr);
        Date startDate = convertStrDateToDate(startDateStr);
        Date endDate = Util.getEndDate(convertStrDateToDate(endDateStr));

        return operationRepository.getByRangeDate(startDate, endDate)
                .onItem().transform(operationEntityList -> operationEntityList.stream().map(OperationEntity::getContract).collect(Collectors.toSet()).stream().toList())
                .onItem().transform(contractEntityList -> transformer.toModelList(contractEntityList));
    }

    @Override
    public Uni<PageData<Operation>> getOperationByContractId(long id, PageRequest pageRequest) {
        return operationRepository.getByContractIdAndStatus(id, OperationStatus.APPLIED)
                .onItem().transform(operationEntityPageData -> PageUtil.toPageModel(operationEntityPageData, operationTransformer::toModel));
    }

    @Override
    public Uni<PageData<Contract>> getByKeyWords(Map<String, List<String>> params, PageRequest pageRequest) {
        return repository.getByKeyWords(params, pageRequest)
                .onItem().transform(contractEntityPageData -> PageUtil.toPageModel(contractEntityPageData, transformer::toModel));
    }

    public Date convertStrDateToDate(String dateStr) {
        Date date = null;
        try {
            date = formatter.parse(dateStr);
        } catch (ParseException e) {
            log.error("ERROR parse date ", e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.INVALID_REQUEST, new Exception("Error converting StrDate to Date"));
        }
        return date;
    }
}