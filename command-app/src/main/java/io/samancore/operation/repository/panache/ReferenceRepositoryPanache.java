package io.samancore.operation.repository.panache;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.samancore.common.error.message.TechnicalExceptionsEnum;
import io.samancore.common.error.util.ExceptionHandler;
import io.samancore.operation.entity.ReferenceEntity;
import io.samancore.operation.entity.StructureEntity;
import io.samancore.operation.model.GeneralStatus;
import io.samancore.operation.repository.ReferenceRepository;
import io.samancore.operation.transformer.ReferenceTransformer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.stream.Collectors;


@ApplicationScoped
public class ReferenceRepositoryPanache implements PanacheRepositoryBase<ReferenceEntity, Long>, ReferenceRepository {

    @Inject
    ReferenceTransformer transformer;

    @Inject
    Logger log;

    @Override
    public ReferenceEntity create(ReferenceEntity entity) {
        log.debugf("ReferenceRepositoryPanache.create %s ", entity);
        entity.setId(null);
        try {
            persistAndFlush(entity);
            return entity;
        }catch (Exception e){
            log.error("ERROR: ReferenceRepositoryPanache.create", e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.REPOSITORY_ERROR, e);
        }
    }

    @Override
    public ReferenceEntity update(ReferenceEntity entity) {
        log.debugf("ReferenceRepositoryPanache.update %s ", entity);
        try {
            var requestAttached = getById(entity.getId());
            entity = transformer.copyToAttached(entity, requestAttached);
            persistAndFlush(entity);
            return entity;
        }catch (Exception e){
            log.error("ERROR: ReferenceRepositoryPanache.update", e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.REPOSITORY_ERROR, e);
        }
    }

    @Override
    public List<ReferenceEntity> createAll(List<ReferenceEntity> entityList) {
        log.debugf("StructureRepositoryPanache.createAll %d ", entityList);
        try {
            entityList.forEach(referenceEntity -> referenceEntity.setId(null));
            persist(entityList);
            return entityList;
        }catch (Exception e){
            log.error("ERROR: StructureRepositoryPanache.update", e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.REPOSITORY_ERROR, e);
        }
    }

    @Override
    public List<ReferenceEntity> updateAll(List<ReferenceEntity> entityList) {
        log.debugf("StructureRepositoryPanache.updateAll %d ", entityList);
        try {
            var idList = entityList.stream().map(structureEntity -> structureEntity.getId()).collect(Collectors.toList());
            var requestAttachedList = getByIds(idList);
            var requestAttachedMap = requestAttachedList.stream().collect(Collectors.toMap(structureEntity -> structureEntity.getId(), structureEntity -> structureEntity));
            entityList.forEach(structureEntity -> structureEntity = transformer.copyToAttached(structureEntity, requestAttachedMap.get(structureEntity.getId())));
            persist(entityList);
            return entityList;
        }catch (Exception e){
            log.error("ERROR: StructureRepositoryPanache.update", e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.REPOSITORY_ERROR, e);
        }
    }

    @Override
    public ReferenceEntity getById(Long id) {
        log.debugf("ReferenceRepositoryPanache.getById id: %s", id);
        try {
            return stream("id = ?1 and status = ?2", id, GeneralStatus.ACTIVE)
                    .findFirst().orElseThrow(NotFoundException::new);
        } catch (Exception error) {
            log.error(error.getMessage(), error);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.REPOSITORY_ERROR, error);
        }
    }

    @Override
    public List<ReferenceEntity> getByIds(List<Long> idList) {
        log.debugf("StructureRepositoryPanache.getByIds %d", idList);
        try {
            return find("id in ?1 and status = ?2", idList, GeneralStatus.ACTIVE)
                    .list();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.REPOSITORY_ERROR, e);
        }
    }

    @Override
    public List<ReferenceEntity> getByOperationId(Long operationId) {
        log.debugf("ReferenceRepositoryPanache.getByOperationId id: %s", operationId);
        try {
            return find(" WHERE operationId = ?1 and status = ?2", operationId, GeneralStatus.ACTIVE)
                    .list();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.REPOSITORY_ERROR, e);
        }
    }
}