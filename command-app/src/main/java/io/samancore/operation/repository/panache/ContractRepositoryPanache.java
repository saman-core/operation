package io.samancore.operation.repository.panache;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.samancore.common.error.message.TechnicalExceptionsEnum;
import io.samancore.common.error.util.ExceptionHandler;
import io.samancore.operation.entity.ContractEntity;
import io.samancore.operation.model.GeneralStatus;
import io.samancore.operation.repository.ContractRepository;
import io.samancore.operation.transformer.ContractTransformer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.jboss.logging.Logger;


@ApplicationScoped
public class ContractRepositoryPanache implements PanacheRepositoryBase<ContractEntity, Long>, ContractRepository {

    @Inject
    ContractTransformer transformer;

    @Inject
    Logger log;

    @Override
    public ContractEntity create(ContractEntity entity) {
        log.debugf("ContractRepositoryPanache.create %s ", entity);
        entity.setId(null);
        try {
            persistAndFlush(entity);
            return entity;
        }catch (Exception e){
            log.error("ERROR: ContractRepositoryPanache.create", e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.REPOSITORY_ERROR, e);
        }
    }

    @Override
    public ContractEntity update(ContractEntity entity) {
        log.debugf("ContractRepositoryPanache.update %s ", entity);
        try {
            var requestAttached = getById(entity.getId());
            entity = transformer.copyToAttached(entity, requestAttached);
            persistAndFlush(entity);
            return entity;
        }catch (Exception e){
            log.error("ERROR: ContractRepositoryPanache.update", e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.REPOSITORY_ERROR, e);
        }
    }

    @Override
    public ContractEntity getById(Long id) {
        log.debugf("ContractRepositoryPanache.getById id: %s", id);
        try {
            return stream("id = ?1 and status = ?2", id, GeneralStatus.ACTIVE)
                    .findFirst().orElseThrow(NotFoundException::new);
        } catch (Exception error) {
            log.error(error.getMessage(), error);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.REPOSITORY_ERROR, error);
        }
    }
}