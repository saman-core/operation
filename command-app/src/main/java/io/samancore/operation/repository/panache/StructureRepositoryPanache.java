package io.samancore.operation.repository.panache;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.samancore.common.error.message.TechnicalExceptionsEnum;
import io.samancore.common.error.util.ExceptionHandler;
import io.samancore.operation.entity.StructureEntity;
import io.samancore.operation.model.GeneralStatus;
import io.samancore.operation.repository.StructureRepository;
import io.samancore.operation.transformer.StructureTransformer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.stream.Collectors;


@ApplicationScoped
public class StructureRepositoryPanache implements PanacheRepositoryBase<StructureEntity, Long>, StructureRepository {

    @Inject
    StructureTransformer transformer;

    @Inject
    Logger log;

    @Override
    public StructureEntity create(StructureEntity entity) {
        log.debugf("StructureRepositoryPanache.create %s ", entity);
        entity.setId(null);
        try {
            persistAndFlush(entity);
            return entity;
        }catch (Exception e){
            log.error("ERROR: StructureRepositoryPanache.create", e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.REPOSITORY_ERROR, e);
        }
    }

    @Override
    public StructureEntity update(StructureEntity entity) {
        log.debugf("StructureRepositoryPanache.update %s ", entity);
        try {
            var requestAttached = getById(entity.getId());
            entity = transformer.copyToAttached(entity, requestAttached);
            persistAndFlush(entity);
            return entity;
        }catch (Exception e){
            log.error("ERROR: StructureRepositoryPanache.update", e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.REPOSITORY_ERROR, e);
        }
    }

    @Override
    public List<StructureEntity> updateAll(List<StructureEntity> entityList) {
        log.debugf("StructureRepositoryPanache.updateAll %d ", entityList);
        try {
            persist(entityList);
            return entityList;
        }catch (Exception e){
            log.error("ERROR: StructureRepositoryPanache.update", e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.REPOSITORY_ERROR, e);
        }
    }

    @Override
    public StructureEntity getById(Long id) {
        log.debugf("StructureRepositoryPanache.getById id: %d", id);
        try {
            return stream("id = ?1 and status = ?2", id, GeneralStatus.ACTIVE)
                    .findFirst().orElseThrow(NotFoundException::new);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.REPOSITORY_ERROR, e);
        }
    }

    @Override
    public List<StructureEntity> getByIds(List<Long> idList) {
        log.debugf("StructureRepositoryPanache.getByIds %d", idList);
        try {
            return find("from StructureEntity s WHERE s.id in ?1 and s.status = ?2", idList, GeneralStatus.ACTIVE)
                    .list();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.REPOSITORY_ERROR, e);
        }
    }

    @Override
    public List<StructureEntity> getByOperationId(Long operationId) {
        log.debugf("StructureRepositoryPanache.getById id: %s", operationId);
        try {
            return find(" from StructureEntity s JOIN ReferenceEntity r ON s.id = r.structureId " +
                    " WHERE r.operationId = ?1 and s.status = ?2", operationId, GeneralStatus.ACTIVE)
                    .list();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.REPOSITORY_ERROR, e);
        }
    }
}