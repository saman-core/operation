package io.samancore.operation.transformer;

import io.samancore.common.error.message.TechnicalExceptionsEnum;
import io.samancore.common.error.util.ExceptionHandler;
import io.samancore.common.transformer.GenericTransformer;
import io.samancore.operation.entity.OutsourceEntity;
import io.samancore.operation.entity.ReferenceEntity;
import io.samancore.operation.entity.ReferenceEntity;
import io.samancore.operation.model.Outsource;
import io.samancore.operation.model.Reference;
import io.samancore.operation.model.Reference;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.commons.lang3.tuple.Pair;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.function.Function;

@ApplicationScoped
public class ReferenceTransformer extends GenericTransformer<ReferenceEntity, Reference> {

    @Inject
    Logger log;

    @Inject
    OutsourceTransformer outsourceTransformer;


    public ReferenceEntity toEntity(Reference model) {
        try {
            log.debugf("ReferenceTransformer.toEntity model: %s", model);
            var pair = Pair.of("outsource", (Function<Outsource, ?>) outsourceTransformer::toEntity);
            return transformToEntity(model, pair);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.TRANSFORMER_OBJECT_ERROR, e);
        }
    }

    public List<ReferenceEntity> toEntityList(List<Reference> models) {
        try {
            log.debugf("ReferenceTransformer.toEntityList entities.size: %s", models.size());
            var pair = Pair.of("outsource", (Function<Outsource, ?>) outsourceTransformer::toEntity);
            return toEntityList( models, pair);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.TRANSFORMER_OBJECT_ERROR, e);
        }
    }

    public Reference toModel(ReferenceEntity entity) {
        try {
            log.debugf("ReferenceTransformer.toModel entity: %s", entity);
            var pair = Pair.of("outsource", (Function<OutsourceEntity, ?>) outsourceTransformer::toModel);
            return transformToModel(entity, pair);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.TRANSFORMER_OBJECT_ERROR, e);
        }
    }


    public List<Reference> toModelList(List<ReferenceEntity> entities) {
        try {
            log.debugf("ReferenceTransformer.toModelList entities.size: %s", entities.size());
            var pair = Pair.of("outsource", (Function<OutsourceEntity, ?>) outsourceTransformer::toModel);
            return toModelList(entities, pair);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.TRANSFORMER_OBJECT_ERROR, e);
        }
    }

    public ReferenceEntity copyToAttached(ReferenceEntity detached, ReferenceEntity attached) {
        try {
            log.debugf("ReferenceTransformer.copyToAttached");
            return transformCopyToAttached(detached, attached);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.TRANSFORMER_OBJECT_ERROR, e);
        }
    }

        }