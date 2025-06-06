package io.samancore.operation.repository.panache;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.samancore.common.error.message.TechnicalExceptionsEnum;
import io.samancore.common.error.util.ExceptionHandler;
import io.samancore.operation.entity.OutsourceEntity;
import io.samancore.operation.model.GeneralStatus;
import io.samancore.operation.repository.OutsourceRepository;
import io.samancore.operation.transformer.OutsourceTransformer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.jboss.logging.Logger;


@ApplicationScoped
public class OutsourceRepositoryPanache implements PanacheRepositoryBase<OutsourceEntity, Long>, OutsourceRepository {

    @Inject
    OutsourceTransformer transformer;

    @Inject
    Logger log;

    @Override
    public OutsourceEntity create(OutsourceEntity entity) {
        log.debugf("OutsourceRepositoryPanache.create %s ", entity);
        entity.setId(null);
        try {
            persistAndFlush(entity);
            return entity;
        }catch (Exception e){
            log.error("ERROR: OutsourceRepositoryPanache.create", e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.REPOSITORY_ERROR, e);
        }
    }

    @Override
    public OutsourceEntity update(OutsourceEntity entity) {
        log.debugf("OutsourceRepositoryPanache.update %s ", entity);
        try {
            var requestAttached = getById(entity.getId());
            entity = transformer.copyToAttached(entity, requestAttached);
            persistAndFlush(entity);
            return entity;
        }catch (Exception e){
            log.error("ERROR: OutsourceRepositoryPanache.update", e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.REPOSITORY_ERROR, e);
        }
    }

    @Override
    public OutsourceEntity getById(Long id) {
        log.debugf("OutsourceRepositoryPanache.getById id: %s", id);
        try {
            return stream("id = ?1 and status = ?2", id, GeneralStatus.ACTIVE)
                    .findFirst().orElseThrow(NotFoundException::new);
        } catch (Exception error) {
            log.error(error.getMessage(), error);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.REPOSITORY_ERROR, error);
        }
    }

    @Override
    public void delete(Long id) {
        log.debugf("OutsourceRepositoryPanache.delete id: %s", id);
        try {
            deleteById(id);
        } catch (Exception error) {
            log.error(error.getMessage(), error);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.REPOSITORY_ERROR, error);
        }
    }
}