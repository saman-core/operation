package io.samancore.operation.transformer;

import io.samancore.common.error.message.TechnicalExceptionsEnum;
import io.samancore.common.error.util.ExceptionHandler;
import io.samancore.common.transformer.GenericTransformer;
import io.samancore.operation.entity.OutsourceEntity;
import io.samancore.operation.entity.ReferenceEntity;
import io.samancore.operation.entity.ReferenceEntity;
import io.samancore.operation.entity.StructureEntity;
import io.samancore.operation.model.Outsource;
import io.samancore.operation.model.Reference;
import io.samancore.operation.model.Reference;
import io.samancore.operation.model.Structure;
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

    @Inject
    StructureTransformer structureTransformer;


    public ReferenceEntity toEntity(Reference model) {
        try {
            log.debugf("ReferenceTransformer.toEntity model: %s", model);
            var pairOutsource = Pair.of("outsource", (Function<Outsource, ?>) outsourceTransformer::toEntity);
            var pairStructure = Pair.of("structure", (Function<Structure, ?>) structureTransformer::toEntity);
            return transformToEntity(model, pairOutsource, pairStructure);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.TRANSFORMER_OBJECT_ERROR, e);
        }
    }

    public List<ReferenceEntity> toEntityList(List<Reference> models) {
        try {
            log.debugf("ReferenceTransformer.toEntityList entities.size: %s", models.size());
            var pairOutsource = Pair.of("outsource", (Function<Outsource, ?>) outsourceTransformer::toEntity);
            var pairStructure = Pair.of("structure", (Function<Structure, ?>) structureTransformer::toEntity);
            return toEntityList( models, pairOutsource, pairStructure);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.TRANSFORMER_OBJECT_ERROR, e);
        }
    }

    public Reference toModel(ReferenceEntity entity) {
        try {
            log.debugf("ReferenceTransformer.toModel entity: %s", entity);
            var pairOutsource = Pair.of("outsource", (Function<OutsourceEntity, ?>) outsourceTransformer::toModel);
            var pairStructure = Pair.of("structure", (Function<StructureEntity, ?>) structureTransformer::toModel);
            return transformToModel(entity, pairOutsource, pairStructure);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw ExceptionHandler.throwNotFoundOrLocal(TechnicalExceptionsEnum.TRANSFORMER_OBJECT_ERROR, e);
        }
    }


    public List<Reference> toModelList(List<ReferenceEntity> entities) {
        try {
            log.debugf("ReferenceTransformer.toModelList entities.size: %s", entities.size());
            var pairOutsource = Pair.of("outsource", (Function<OutsourceEntity, ?>) outsourceTransformer::toModel);
            var pairStructure = Pair.of("structure", (Function<StructureEntity, ?>) structureTransformer::toModel);
            return toModelList(entities, pairOutsource, pairStructure);
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