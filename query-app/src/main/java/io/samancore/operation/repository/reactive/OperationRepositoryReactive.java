package io.samancore.operation.repository.reactive;

import io.samancore.common.error.message.TechnicalExceptionsEnum;
import io.samancore.common.error.util.ExceptionHandler;
import io.samancore.common.model.PageData;
import io.samancore.common.page.PageRequest;
import io.samancore.operation.entity.OperationEntity;
import io.samancore.operation.model.OperationStatus;
import io.samancore.operation.repository.OperationRepository;
import io.samancore.operation.request.OperationFilterRequest;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.hibernate.reactive.mutiny.Mutiny;
import org.jboss.logging.Logger;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@ApplicationScoped
public class OperationRepositoryReactive implements OperationRepository {

    @Inject
    Logger log;

    @Inject
    Mutiny.SessionFactory sessionFactory;

    @Override
    public Uni<OperationEntity> getById(Long id) {
        log.debugf("ContractRepositoryReactive.getById %d ", id);
        return sessionFactory.withSession(session -> session.find(OperationEntity.class, id))
                .onFailure().transform(e -> ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.REPOSITORY_ERROR, e))
                .onItem().ifNull().failWith(NotFoundException::new);
    }

    @Override
    public Uni<PageData<OperationEntity>> getByContractIdAndStatus(long contractId, OperationStatus status) {
        log.debugf("ContractRepositoryReactive.getByContractId %d ", contractId);
        var query = " from OperationEntity o where o.contractId = :contractId and o.status = :status";
        return sessionFactory.withSession(session -> {
            var selectionQuery = session.createQuery(query, OperationEntity.class)
                    .setParameter("contractId", contractId)
                    .setParameter("status", status);
            var list = selectionQuery
                    .getResultList();
            var count = selectionQuery
                    .getResultCount();
            return Uni.combine().all().unis(list, count).asTuple()
                    .onItem().transform(objects -> PageData.<OperationEntity>newBuilder().setData(objects.getItem1()).setCount(objects.getItem2()).build());
        }).onFailure().transform(e -> ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.REPOSITORY_ERROR, e))
                .onItem().ifNull().failWith(NotFoundException::new);
    }

    @Override
    public Uni<List<OperationEntity>> getByRangeDate(Date startDate, Date endDate) {
        log.debugf("ContractRepositoryReactive.getByRangeDate %s %s", startDate, endDate);
        return sessionFactory.withSession(session -> session.createQuery(" from OperationEntity o  where o.createdDate BETWEEN :startDate AND :endDate ", OperationEntity.class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList())
                .onFailure().transform(e -> ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.REPOSITORY_ERROR, e))
                .onItem().ifNull().failWith(NotFoundException::new);
    }

    @Override
    public Uni<PageData<OperationEntity>> getByFilters(OperationFilterRequest request, PageRequest pageRequest) {
        var query = new StringBuilder(" from OperationEntity o  ");
        var params = new HashMap<String, Object>();

        if (request.getStatus() != null) {
            query.append(" WHERE o.status = :status ");
            params.put("status", request.getStatus());
        }
        if (request.getCreatedDateStart() != null && request.getCreatedDateEnd() != null) {
            if (params.isEmpty()) {
                query.append(" WHERE ");
            } else query.append(" AND ");
            query.append(" o.createdDate BETWEEN :createdDateStart AND :createdDateEnd");
            params.put("createdDateStart", request.getCreatedDateStart());
            params.put("createdDateEnd", request.getCreatedDateEnd());
        }
        if (request.getAppliedDateStart() != null && request.getAppliedDateEnd() != null) {
            if (params.isEmpty()) {
                query.append(" WHERE ");
            } else query.append(" AND ");
            query.append(" o.appliedDate BETWEEN :appliedDateStart AND :appliedDateEnd");
            params.put("appliedDateStart", request.getAppliedDateStart());
            params.put("appliedDateEnd", request.getAppliedDateEnd());
        }
        return sessionFactory.withSession(session -> {
            var selectionQuery = session.createQuery(query.toString(), OperationEntity.class);
            params.entrySet().forEach(stringObjectEntry -> selectionQuery.setParameter(stringObjectEntry.getKey(), stringObjectEntry.getValue()));
            var list = selectionQuery
                    .getResultList();
            var count = selectionQuery
                    .getResultCount();
            return Uni.combine().all().unis(list, count).asTuple()
                    .onItem().transform(objects -> PageData.<OperationEntity>newBuilder().setData(objects.getItem1()).setCount(objects.getItem2()).build());
        }).onFailure().transform(e -> ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.REPOSITORY_ERROR, e))
                .onItem().ifNull().failWith(NotFoundException::new);
    }
}