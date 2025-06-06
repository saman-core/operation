package io.samancore.operation.repository.reactive;

import io.samancore.common.error.message.TechnicalExceptionsEnum;
import io.samancore.common.error.util.ExceptionHandler;
import io.samancore.operation.entity.ContractEntity;
import io.samancore.operation.entity.OperationEntity;
import io.samancore.operation.entity.OperationEntity;
import io.samancore.operation.repository.ContractRepository;
import io.samancore.operation.repository.OperationRepository;
import io.samancore.operation.transformer.ContractTransformer;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.hibernate.reactive.mutiny.Mutiny;
import org.jboss.logging.Logger;

import java.util.Date;
import java.util.List;

@ApplicationScoped
public class OperationRepositoryReactive implements OperationRepository {

    @Inject
    ContractTransformer transformer;

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
    public Uni<List<OperationEntity>> getByContractId(long contractId) {
        log.debugf("ContractRepositoryReactive.getByContractId %d ", contractId);
        return sessionFactory.withSession(session -> session.createQuery(" from OperationEntity o where o.contractId = :contractId ", OperationEntity.class)
            .setParameter("contractId", contractId)
            .getResultList())
            .onFailure().transform(e -> ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.REPOSITORY_ERROR, e))
            .onItem().ifNull().failWith(NotFoundException::new);
    }

    @Override
    public Uni<List<OperationEntity>> getByContractIdAndRangeDate(long contractId, Date startDate, Date endDate) {
        log.debugf("ContractRepositoryReactive.getByRangeDate %s %s %s", contractId, startDate, endDate);
        return sessionFactory.withSession(session -> session.createQuery(" from OperationEntity o  where o.contractId = :contractId and o.createdDate BETWEEN :startDate AND :endDate ", OperationEntity.class)
                .setParameter("contractId", contractId)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList())
                .onFailure().transform(e -> ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.REPOSITORY_ERROR, e))
                .onItem().ifNull().failWith(NotFoundException::new);
    }


    @Override
    public Uni<List<OperationEntity>> getByContractIdAndRangeDate(long contractId, Date date) {
        log.debugf("ContractRepositoryReactive.getByContractIdAndDate %d %s", contractId, date);
        return sessionFactory.withSession(session -> session.createQuery(" from OperationEntity o  where o.createdDate = :date and o.contractId = :contractId", OperationEntity.class)
                .setParameter("date", date)
                .setParameter("contractId", contractId)
                .getResultList())
                .onFailure().transform(e -> ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.REPOSITORY_ERROR, e))
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
}