package io.samancore.operation.transformer;

import io.samancore.common.error.message.TechnicalExceptionsEnum;
import io.samancore.common.error.util.ExceptionHandler;
import io.samancore.common.transformer.GenericTransformer;
import io.samancore.operation.entity.OperationEntity;
import io.samancore.operation.entity.OperationEntity;
import io.samancore.operation.entity.TransitionAppliedEntity;
import io.samancore.operation.model.Operation;
import io.samancore.operation.model.Operation;
import io.samancore.operation.model.TransitionApplied;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.commons.lang3.tuple.Pair;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.function.Function;

@ApplicationScoped
public class OperationTransformer extends GenericTransformer<OperationEntity, Operation> {

    @Inject
    Logger log;

    @Inject
    TransitionAppliedTransformer transitionAppliedTransformer;

    public OperationEntity toEntity(Operation model) {
        try {
            log.debugf("OperationTransformer.toEntity model: %s", model);
            var pair = Pair.of("transitionApplied", (Function<TransitionApplied, ?>) transitionAppliedTransformer::toEntity);
            return transformToEntity(model, pair);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.TRANSFORMER_OBJECT_ERROR, e);
        }
    }

    public List<OperationEntity> toEntityList(List<Operation> models) {
        try {
            log.debugf("OperationTransformer.toEntityList entities.size: %s", models.size());
            var pair = Pair.of("transitionApplied", (Function<TransitionApplied, ?>) transitionAppliedTransformer::toEntity);
            return toEntityList( models, pair);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.TRANSFORMER_OBJECT_ERROR, e);
        }
    }

    public Operation toModel(OperationEntity entity) {
        try {
            log.debugf("OperationTransformer.toModel entity: %s", entity);
            var pair = Pair.of("transitionApplied", (Function<TransitionAppliedEntity, ?>) transitionAppliedTransformer::toModel);
            return transformToModel(entity, pair);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.TRANSFORMER_OBJECT_ERROR, e);
        }
    }

    public List<Operation> toModelList(List<OperationEntity> entities) {
        try {
            log.debugf("OperationTransformer.toModelList entities.size: %s", entities.size());
            var pair = Pair.of("transitionApplied", (Function<TransitionAppliedEntity, ?>) transitionAppliedTransformer::toModel);
            return toModelList(entities, pair);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.TRANSFORMER_OBJECT_ERROR, e);
        }
    }

    public OperationEntity copyToAttached(OperationEntity detached, OperationEntity attached) {
        try {
            log.debugf("OperationTransformer.copyToAttached");
            return transformCopyToAttached(detached, attached);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.TRANSFORMER_OBJECT_ERROR, e);
        }
    }

        }