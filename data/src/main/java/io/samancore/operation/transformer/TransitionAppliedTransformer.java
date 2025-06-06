package io.samancore.operation.transformer;

import io.samancore.common.error.message.TechnicalExceptionsEnum;
import io.samancore.common.error.util.ExceptionHandler;
import io.samancore.common.transformer.GenericTransformer;
import io.samancore.operation.entity.TransitionAppliedEntity;
import io.samancore.operation.model.TransitionApplied;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

@ApplicationScoped
public class TransitionAppliedTransformer extends GenericTransformer<TransitionAppliedEntity, TransitionApplied> {

    @Inject
    Logger log;


    public TransitionAppliedEntity toEntity(TransitionApplied model) {
        try {
            log.debugf("TransitionAppliedTransformer.toEntity model: %s", model);
            return transformToEntity(model);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.TRANSFORMER_OBJECT_ERROR, e);
        }
    }

    public TransitionApplied toModel(TransitionAppliedEntity entity) {
        try {
            log.debugf("TransitionAppliedTransformer.toModel entity: %s", entity);
            return transformToModel(entity);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.TRANSFORMER_OBJECT_ERROR, e);
        }
    }

    public TransitionAppliedEntity copyToAttached(TransitionAppliedEntity detached, TransitionAppliedEntity attached) {
        try {
            log.debugf("TransitionAppliedTransformer.copyToAttached");
            return transformCopyToAttached(detached, attached);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.TRANSFORMER_OBJECT_ERROR, e);
        }
    }

        }