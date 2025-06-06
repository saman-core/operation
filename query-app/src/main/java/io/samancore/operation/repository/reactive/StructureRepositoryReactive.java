package io.samancore.operation.repository.reactive;

import io.samancore.common.error.message.TechnicalExceptionsEnum;
import io.samancore.common.error.util.ExceptionHandler;
import io.samancore.operation.entity.ContractEntity;
import io.samancore.operation.entity.StructureEntity;
import io.samancore.operation.model.GeneralStatus;
import io.samancore.operation.repository.ContractRepository;
import io.samancore.operation.repository.StructureRepository;
import io.samancore.operation.transformer.ContractTransformer;
import io.samancore.operation.transformer.StructureTransformer;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.hibernate.reactive.mutiny.Mutiny;
import org.jboss.logging.Logger;

import java.util.Date;
import java.util.List;

@ApplicationScoped
public class StructureRepositoryReactive implements StructureRepository {

    @Inject
    StructureTransformer transformer;

    @Inject
    Logger log;

    @Inject
    Mutiny.SessionFactory sessionFactory;

    @Override
    public Uni<StructureEntity> getRootByOperationId(Long operationId) {
        return sessionFactory.withSession(session -> session.createQuery(" from StructureEntity s JOIN ReferenceEntity r ON s.id = r.structureId" +
                " WHERE r.operationId = :operationId AND s.parentId is NULL " , StructureEntity.class)
            .setParameter("operationId", operationId)
            .getSingleResult())
            .onFailure().transform(e -> ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.REPOSITORY_ERROR, e))
            .onItem().ifNull().failWith(NotFoundException::new);
    }

    @Override
    public Uni<List<StructureEntity>> getByOperationId(Long operationId) {
        return sessionFactory.withSession(session -> session.createQuery(" from StructureEntity s JOIN ReferenceEntity r ON s.id = r.structureId" +
                " WHERE r.operationId = :operationId and s.status = :status " , StructureEntity.class)
                .setParameter("operationId", operationId)
                .setParameter("status", GeneralStatus.ACTIVE)
                .getResultList())
                .onFailure().transform(e -> ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.REPOSITORY_ERROR, e))
                .onItem().ifNull().failWith(NotFoundException::new);
    }

    @Override
    public Uni<List<StructureEntity>> getChildrenByParams(Long parentId, Long operationId, Long relationshipId) {
        return sessionFactory.withSession(session -> session.createQuery(" from StructureEntity s JOIN ReferenceEntity r ON s.id = r.structureId" +
                " WHERE r.operationId = :operationId AND s.parentId = :parentId AND s.relationshipId = :relationshipId" , StructureEntity.class)
                .setParameter("operationId", operationId)
                .setParameter("parentId", parentId)
                .setParameter("relationshipId", relationshipId)
                .getResultList())
                .onFailure().transform(e -> ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.REPOSITORY_ERROR, e))
                .onItem().ifNull().failWith(NotFoundException::new);
    }

    @Override
    public Uni<List<StructureEntity>> getChildrenByParentId(Long parentId) {
        return sessionFactory.withSession(session -> session.createQuery(" from StructureEntity s " +
                " WHERE s.deletedOperationId is NULL AND s.parentId = :parentId " , StructureEntity.class)
                .setParameter("parentId", parentId)
                .getResultList())
                .onFailure().transform(e -> ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.REPOSITORY_ERROR, e))
                .onItem().ifNull().failWith(NotFoundException::new);
    }
}