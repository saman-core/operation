package io.samancore.operation.transformer;

import io.samancore.common.error.message.TechnicalExceptionsEnum;
import io.samancore.common.error.util.ExceptionHandler;
import io.samancore.common.transformer.GenericTransformer;
import io.samancore.operation.entity.ContractEntity;
import io.samancore.operation.entity.OperationEntity;
import io.samancore.operation.entity.ReferenceEntity;
import io.samancore.operation.entity.StructureEntity;
import io.samancore.operation.model.Contract;
import io.samancore.operation.model.Operation;
import io.samancore.operation.model.Reference;
import io.samancore.operation.model.Structure;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.commons.lang3.tuple.Pair;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.function.Function;

@ApplicationScoped
public class ContractTransformer extends GenericTransformer<ContractEntity, Contract> {

    @Inject
    Logger log;

    @Inject
    OperationTransformer operationTransformer;


    public ContractEntity toEntity(Contract model) {
        try {
            log.debugf("ContractTransformer.toEntity model: %s", model);
            var pairOperationToEntity = Pair.of("operation", (Function<Operation, ?>) operationTransformer::toEntity);
            return transformToEntity(model, pairOperationToEntity);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.TRANSFORMER_OBJECT_ERROR, e);
        }
    }

    public List<ContractEntity> toEntityList(List<Contract> models) {
        try {
            log.debugf("ContractTransformer.toEntityList entities.size: %s", models.size());
            var pairOperationToEntity = Pair.of("operation", (Function<Operation, ?>) operationTransformer::toEntity);
            return toEntityList( models, pairOperationToEntity);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.TRANSFORMER_OBJECT_ERROR, e);
        }
    }

    public Contract toModel(ContractEntity entity) {
        try {
            log.debugf("ContractTransformer.toModel entity: %s", entity);
            var pairOperationToModel = Pair.of("operation", (Function<OperationEntity, ?>) operationTransformer::toModel);
            return transformToModel(entity, pairOperationToModel);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.TRANSFORMER_OBJECT_ERROR, e);
        }
    }

    public List<Contract> toModelList(List<ContractEntity> entities) {
        try {
            log.debugf("ContractTransformer.toModelList entities.size: %s", entities.size());
            var pairOperationToModel = Pair.of("operation", (Function<OperationEntity, ?>) operationTransformer::toModel);
            return toModelList(entities, pairOperationToModel);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.TRANSFORMER_OBJECT_ERROR, e);
        }
    }

    public ContractEntity copyToAttached(ContractEntity detached, ContractEntity attached) {
        try {
            log.debugf("ContractTransformer.copyToAttached");
            return transformCopyToAttached(detached, attached);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.TRANSFORMER_OBJECT_ERROR, e);
        }
    }

        }