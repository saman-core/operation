package io.samancore.operation.repository.reactive;

import io.samancore.common.error.message.TechnicalExceptionsEnum;
import io.samancore.common.error.util.ExceptionHandler;
import io.samancore.operation.entity.ContractEntity;
import io.samancore.operation.repository.ContractRepository;
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
public class ContractRepositoryReactive implements ContractRepository {

    @Inject
    ContractTransformer transformer;

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
        return sessionFactory.withSession(session -> session.createQuery(" from ContractEntity c JOIN c.operations o " +
            "  where o.id = :operationId ", ContractEntity.class)
            .setParameter("operationId", operationId)
            .getSingleResult())
            .onFailure().transform(e -> ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.REPOSITORY_ERROR, e))
            .onItem().ifNull().failWith(NotFoundException::new);
    }

    @Override
    public Uni<ContractEntity> getByOperationIdAndDate(long operationId, Date date) {
        log.debugf("ContractRepositoryReactive.getByOperationIdAndDate %d %s", operationId, date);
        return sessionFactory.withSession(session -> session.createQuery(" from ContractEntity c JOIN c.operations o " +
                "  where o.createdDate = :date ", ContractEntity.class)
                .setParameter("date", date)
                .getSingleResult())
                .onFailure().transform(e -> ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.REPOSITORY_ERROR, e))
                .onItem().ifNull().failWith(NotFoundException::new);
    }

    @Override
    public Uni<List<ContractEntity>> getByRangeDate(Date startDate, Date endDate) {
        log.debugf("ContractRepositoryReactive.getByRangeDate %s %s", startDate, endDate);
        return sessionFactory.withSession(session -> session.createQuery(" from ContractEntity c JOIN c.operations o " +
                "  where o.createdDate BETWEEN :startDate AND :endDate ", ContractEntity.class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList())
                .onFailure().transform(e -> ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.REPOSITORY_ERROR, e))
                .onItem().ifNull().failWith(NotFoundException::new);
    }

}