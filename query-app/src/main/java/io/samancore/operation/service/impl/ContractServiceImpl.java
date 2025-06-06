package io.samancore.operation.service.impl;

import io.samancore.common.error.message.TechnicalExceptionsEnum;
import io.samancore.common.error.util.ExceptionHandler;
import io.samancore.operation.model.Contract;
import io.samancore.operation.repository.ContractRepository;
import io.samancore.operation.repository.OperationRepository;
import io.samancore.operation.service.ContractService;
import io.samancore.operation.transformer.ContractTransformer;
import io.samancore.operation.transformer.OperationTransformer;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
        var contractEntity = repository.getById(id);
        var operationEntityList = operationRepository.getByContractId(id);
        return Uni.combine().all().unis(contractEntity, operationEntityList).asTuple()
            .onItem().transform(items  -> {
                var contract = transformer.toModel(items.getItem1());
                if(items.getItem2() != null && !items.getItem2().isEmpty()){
                    var operationList = operationTransformer.toModelList(items.getItem2());
                    contract = contract.toBuilder().setOperations(operationList).build();
                    }
                    return contract;
            });
    }

    @Override
    public Uni<Contract> getByOperationId(long operationId) {
        log.debugf("ContractServiceImpl.getByOperationId %d ", operationId);
        return operationRepository.getById(operationId)
            .onItem().transform(operationEntity -> {
                var contract = transformer.toModel(operationEntity.getContract());
                var operation = operationTransformer.toModel(operationEntity);
                return contract.toBuilder().setOperations(List.of(operation)).build();
            });
    }

    @Override
    public Uni<Contract> getByIdAndDate(long id, String dateStr) {
        log.debugf("ContractServiceImpl.getByIdAndDate %d %s", id, dateStr);
        var date = convertStrDateToDate(dateStr);
        Date endDate = getEndDate(date);

        var contractEntity = repository.getById(id);
        var operationEntityList = operationRepository.getByContractIdAndRangeDate(id, date, endDate);
        return Uni.combine().all().unis(contractEntity, operationEntityList).asTuple()
            .onItem().transform(items  -> {
                var contract = transformer.toModel(items.getItem1());
                if(items.getItem2() != null && !items.getItem2().isEmpty()){
                    var operationList = operationTransformer.toModelList(items.getItem2());
                    contract = contract.toBuilder().setOperations(operationList).build();
                }
                return contract;
            });
    }

    private Date getEndDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        var endDate = calendar.getTime();
        return endDate;
    }

    @Override
    public Uni<List<Contract>> getByRangeDate(String startDateStr, String endDateStr) {
        log.debugf("ContractServiceImpl.getByRangeDate %s %s", startDateStr, endDateStr);
        Date startDate = convertStrDateToDate(startDateStr);
        Date endDate = getEndDate(convertStrDateToDate(endDateStr));

        return operationRepository.getByRangeDate(startDate, endDate)
                .onItem().transform(operationEntityList -> {
                    var contractEntityList = operationEntityList.stream().map(operationEntity -> operationEntity.getContract()).collect(Collectors.toSet()).stream().toList();
                    var operationMap = operationEntityList.stream().collect(Collectors.groupingBy(operationEntity -> operationEntity.getContract().getId()));
                    var contractList = transformer.toModelList(contractEntityList);
                    return contractList.stream().map(contract -> contract.toBuilder().setOperations(operationTransformer.toModelList(operationMap.get(contract.getId()))).build()).toList();
                });
    }

    public Date convertStrDateToDate(String dateStr){
        Date date = null;
        try {
            date = formatter.parse(dateStr);
        } catch (ParseException e) {
            log.error("ERROR parse date ",  e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.INVALID_REQUEST,new Exception("Error converting StrDate to Date"));
        }
        return date;
    }
}