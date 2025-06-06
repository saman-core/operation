package io.samancore.operation.repository.panache;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.samancore.common.error.message.TechnicalExceptionsEnum;
import io.samancore.common.error.util.ExceptionHandler;
import io.samancore.operation.entity.OperationEntity;
import io.samancore.operation.model.OperationStatus;
import io.samancore.operation.repository.OperationRepository;
import io.samancore.operation.transformer.OperationTransformer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.jboss.logging.Logger;


@ApplicationScoped
public class OperationRepositoryPanache implements PanacheRepositoryBase<OperationEntity, Long>, OperationRepository {

    @Inject
    OperationTransformer transformer;

    @Inject
    Logger log;

    @Override
    public OperationEntity create(OperationEntity entity) {
        log.debugf("OperationRepositoryPanache.create %s ", entity);
        entity.setId(null);
        try {
            persistAndFlush(entity);
            return entity;
        }catch (Exception e){
            log.error("ERROR: OperationRepositoryPanache.create", e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.REPOSITORY_ERROR, e);
        }
    }

    @Override
    public OperationEntity update(OperationEntity entity) {
        log.debugf("OperationRepositoryPanache.update %s ", entity);
        try {
            var requestAttached = getById(entity.getId());
            entity = transformer.copyToAttached(entity, requestAttached);
            persistAndFlush(entity);
            return entity;
        }catch (Exception e){
            log.error("ERROR: OperationRepositoryPanache.update", e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.REPOSITORY_ERROR, e);
        }
    }

    @Override
    public OperationEntity getById(Long id) {
        log.debugf("OperationRepositoryPanache.getById id: %s", id);
        try {
            return stream("id = ?1 ", id)
                    .findFirst().orElseThrow(NotFoundException::new);
        } catch (Exception error) {
            log.error(error.getMessage(), error);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.REPOSITORY_ERROR, error);
        }
    }

    @Override
    public void cancelOtherOperationsByContractId(Long operationId, Long contractId) {
        log.debugf("OperationRepositoryPanache.cancelOperations  %d %d", operationId, contractId);
        var entities = find(" id != ?1 AND contractId = ?2", operationId, contractId).list();
        entities.forEach(operationEntity -> operationEntity.setStatus(OperationStatus.CANCELED));
        persist(entities);
    }
}