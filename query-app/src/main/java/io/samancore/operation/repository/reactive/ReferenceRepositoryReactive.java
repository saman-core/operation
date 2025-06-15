package io.samancore.operation.repository.reactive;

import io.samancore.common.error.message.TechnicalExceptionsEnum;
import io.samancore.common.error.util.ExceptionHandler;
import io.samancore.operation.entity.ReferenceEntity;
import io.samancore.operation.model.GeneralStatus;
import io.samancore.operation.repository.ReferenceRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.hibernate.reactive.mutiny.Mutiny;
import org.jboss.logging.Logger;

import java.util.List;

@ApplicationScoped
public class ReferenceRepositoryReactive implements ReferenceRepository {

    @Inject
    Logger log;

    @Inject
    Mutiny.SessionFactory sessionFactory;

    @Override
    public Uni<ReferenceEntity> getRootByOperationId(Long operationId) {
        return sessionFactory.withSession(session -> session.createQuery(" from ReferenceEntity r JOIN StructureEntity s ON s.id = r.structureId" +
                " WHERE r.operationId = :operationId AND s.parentId is NULL ", ReferenceEntity.class)
                .setParameter("operationId", operationId)
                .getSingleResult())
                .onFailure().transform(e -> ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.REPOSITORY_ERROR, e))
                .onItem().ifNull().failWith(NotFoundException::new);
    }

    @Override
    public Uni<List<ReferenceEntity>> getByOperationId(Long operationId) {
        return sessionFactory.withSession(session -> session.createQuery(" from ReferenceEntity r JOIN StructureEntity s ON s.id = r.structureId" +
                " WHERE r.operationId = :operationId and s.status = :status ", ReferenceEntity.class)
                .setParameter("operationId", operationId)
                .setParameter("status", GeneralStatus.ACTIVE)
                .getResultList())
                .onFailure().transform(e -> ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.REPOSITORY_ERROR, e))
                .onItem().ifNull().failWith(NotFoundException::new);
    }

    @Override
    public Uni<List<ReferenceEntity>> getChildrenByParams(Long parentId, Long operationId, Long relationshipId) {
        return sessionFactory.withSession(session -> session.createQuery(" from ReferenceEntity r JOIN StructureEntity s ON s.id = r.structureId" +
                " WHERE r.operationId = :operationId AND s.parentId = :parentId AND s.relationshipId = :relationshipId", ReferenceEntity.class)
                .setParameter("operationId", operationId)
                .setParameter("parentId", parentId)
                .setParameter("relationshipId", relationshipId)
                .getResultList())
                .onFailure().transform(e -> ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.REPOSITORY_ERROR, e))
                .onItem().ifNull().failWith(NotFoundException::new);
    }

    @Override
    public Uni<List<ReferenceEntity>> getChildrenByParentId(Long parentId) {
        return sessionFactory.withSession(session -> session.createQuery(" from ReferenceEntity r JOIN StructureEntity s ON s.id = r.structureId" +
                " WHERE s.deletedOperationId is NULL AND s.parentId = :parentId ", ReferenceEntity.class)
                .setParameter("parentId", parentId)
                .getResultList())
                .onFailure().transform(e -> ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.REPOSITORY_ERROR, e))
                .onItem().ifNull().failWith(NotFoundException::new);
    }
}