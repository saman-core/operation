package io.samancore.operation.repository.reactive;

import io.samancore.common.error.message.TechnicalExceptionsEnum;
import io.samancore.common.error.util.ExceptionHandler;
import io.samancore.common.model.PageData;
import io.samancore.common.page.PageRequest;
import io.samancore.operation.entity.ContractEntity;
import io.samancore.operation.repository.ContractRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.hibernate.reactive.mutiny.Mutiny;
import org.jboss.logging.Logger;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class ContractRepositoryReactive implements ContractRepository {

    @Inject
    Logger log;

    @Inject
    Mutiny.SessionFactory sessionFactory;

    @Override
    public Uni<ContractEntity> getById(Long id) {
        log.debugf("ContractRepositoryReactive.getById %d ", id);
        return sessionFactory.withSession(session -> session.find(ContractEntity.class, id))
                .onFailure().transform(e -> ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.REPOSITORY_ERROR, e))
                .onItem().ifNull().failWith(NotFoundException::new);
    }

    @Override
    public Uni<ContractEntity> getByOperationId(long operationId) {
        log.debugf("ContractRepositoryReactive.getByOperationId %d ", operationId);
        return sessionFactory.withSession(session -> session.createQuery(" from ContractEntity c JOIN OperationEntity o ON o.contractId = c.id " +
                "  where o.id = :operationId ", ContractEntity.class)
                .setParameter("operationId", operationId)
                .getSingleResult())
                .onFailure().transform(e -> ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.REPOSITORY_ERROR, e))
                .onItem().ifNull().failWith(NotFoundException::new);
    }

    @Override
    public Uni<ContractEntity> getByOperationIdAndDate(long operationId, Date date) {
        log.debugf("ContractRepositoryReactive.getByOperationIdAndDate %d %s", operationId, date);
        return sessionFactory.withSession(session -> session.createQuery(" from ContractEntity c  JOIN OperationEntity o ON o.contractId = c.id  " +
                "  where o.createdDate = :date ", ContractEntity.class)
                .setParameter("date", date)
                .getSingleResult())
                .onFailure().transform(e -> ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.REPOSITORY_ERROR, e))
                .onItem().ifNull().failWith(NotFoundException::new);
    }

    @Override
    public Uni<List<ContractEntity>> getByRangeDate(Date startDate, Date endDate) {
        log.debugf("ContractRepositoryReactive.getByRangeDate %s %s", startDate, endDate);
        return sessionFactory.withSession(session -> session.createQuery(" from ContractEntity c  JOIN OperationEntity o ON o.contractId = c.id  " +
                "  where o.createdDate BETWEEN :startDate AND :endDate ", ContractEntity.class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList())
                .onFailure().transform(e -> ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.REPOSITORY_ERROR, e))
                .onItem().ifNull().failWith(NotFoundException::new);
    }

    @Override
    public Uni<PageData<ContractEntity>> getByKeyWords(Map<String, List<String>> paramsRequest, PageRequest pageRequest) {
        var query = new StringBuilder(" FROM ContractEntity c ");
        var params = new HashMap<String, Object>();

        var conditions = new StringBuilder("");

        paramsRequest.entrySet().forEach(stringListEntry -> {
            var alias = "k".concat(stringListEntry.getKey());
            query.append(" INNER JOIN ContractKeywordEntity ").append(alias).append(" on ").append(alias).append(".contractId = c.id   ");
            if (!params.isEmpty()) {
                conditions.append(" AND ");
            } else conditions.append(" WHERE ");
            conditions.append(alias).append(".name = :name").append(stringListEntry.getKey()).append(" AND ").append(alias).append(".value in :value").append(stringListEntry.getKey());
            params.put("name".concat(stringListEntry.getKey()), stringListEntry.getKey());
            params.put("value".concat(stringListEntry.getKey()), stringListEntry.getValue());
        });
        query.append(conditions);
        return sessionFactory.withSession(session -> {
            var selectionQuery = session.createQuery(query.toString(), ContractEntity.class);
            params.entrySet().forEach(stringObjectEntry -> selectionQuery.setParameter(stringObjectEntry.getKey(), stringObjectEntry.getValue()));
            var list = selectionQuery
                    .getResultList();
            var count = selectionQuery
                    .getResultCount();
            return Uni.combine().all().unis(list, count).asTuple()
                    .onItem().transform(objects -> PageData.<ContractEntity>newBuilder().setData(objects.getItem1()).setCount(objects.getItem2()).build());
        }).onFailure().transform(e -> ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.REPOSITORY_ERROR, e))
                .onItem().ifNull().failWith(NotFoundException::new);
    }

}