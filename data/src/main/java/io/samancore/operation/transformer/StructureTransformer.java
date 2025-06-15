package io.samancore.operation.transformer;

import io.samancore.common.error.message.TechnicalExceptionsEnum;
import io.samancore.common.error.util.ExceptionHandler;
import io.samancore.common.transformer.GenericTransformer;
import io.samancore.operation.entity.StructureEntity;
import io.samancore.operation.model.Structure;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.List;

@ApplicationScoped
public class StructureTransformer extends GenericTransformer<StructureEntity, Structure> {

    @Inject
    Logger log;

    public StructureEntity toEntity(Structure model) {
        try {
            log.debugf("StructureTransformer.toEntity model: %s", model);
            return transformToEntity(model);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.TRANSFORMER_OBJECT_ERROR, e);
        }
    }

    public List<StructureEntity> toEntityList(List<Structure> models) {
        try {
            log.debugf("StructureTransformer.toEntityList entities.size: %s", models.size());
            return toEntityList( models);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.TRANSFORMER_OBJECT_ERROR, e);
        }
    }

    public Structure toModel(StructureEntity entity) {
        try {
            log.debugf("StructureTransformer.toModel entity: %s", entity);
            return transformToModel(entity);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.TRANSFORMER_OBJECT_ERROR, e);
        }
    }

    public List<Structure> toModelList(List<StructureEntity> entities) {
        try {
            log.debugf("StructureTransformer.toModelList entities.size: %s", entities.size());
            return toModelList(entities);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.TRANSFORMER_OBJECT_ERROR, e);
        }
    }

    public StructureEntity copyToAttached(StructureEntity detached, StructureEntity attached) {
        try {
            log.debugf("StructureTransformer.copyToAttached");
            return transformCopyToAttached(detached, attached);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.TRANSFORMER_OBJECT_ERROR, e);
        }
    }

        }