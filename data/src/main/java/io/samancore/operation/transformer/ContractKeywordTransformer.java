package io.samancore.operation.transformer;

import io.samancore.common.error.message.TechnicalExceptionsEnum;
import io.samancore.common.error.util.ExceptionHandler;
import io.samancore.common.transformer.GenericTransformer;
import io.samancore.operation.entity.ContractKeywordEntity;
import io.samancore.operation.entity.ContractKeywordEntity;
import io.samancore.operation.model.ContractKeyword;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

@ApplicationScoped
public class ContractKeywordTransformer extends GenericTransformer<ContractKeywordEntity, ContractKeyword> {

    @Inject
    Logger log;


    public ContractKeywordEntity toEntity(ContractKeyword model) {
        try {
            log.debugf("ContractKeywordTransformer.toEntity model: %s", model);
            return transformToEntity(model);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.TRANSFORMER_OBJECT_ERROR, e);
        }
    }

    public ContractKeyword toModel(ContractKeywordEntity entity) {
        try {
            log.debugf("ContractKeywordTransformer.toModel entity: %s", entity);
            return transformToModel(entity);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.TRANSFORMER_OBJECT_ERROR, e);
        }
    }

    public ContractKeywordEntity copyToAttached(ContractKeywordEntity detached, ContractKeywordEntity attached) {
        try {
            log.debugf("ContractKeywordTransformer.copyToAttached");
            return transformCopyToAttached(detached, attached);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.TRANSFORMER_OBJECT_ERROR, e);
        }
    }

        }