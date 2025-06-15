package io.samancore.operation.api;

import io.samancore.operation.model.Reference;
import io.samancore.operation.service.ReferenceService;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jboss.logging.Logger;

import java.util.List;

@Path("references")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ReferenceApi {

    @Inject
    Logger log;

    @Inject
    ReferenceService service;

    @GET
    @Path("/{operationId}")
    @RolesAllowed({"admin"})
    public Uni<Reference> getRootByOperationId(@PathParam("operationId") long operationId) {
        log.debugf("StructureApi.getRootByOperationId %d ", operationId);
        return service.getRootByOperationId(operationId);
    }

    @GET
    @Path("/children/{operationId}/{parentId}/{relationshipId}")
    @RolesAllowed({"admin"})
    public Uni<List<Reference>> getChildrenByParams(
            @PathParam("operationId") long operationId,
            @PathParam("parentId") long parentId,
            @PathParam("relationshipId") long relationshipId) {
        log.debugf("StructureApi.getChildrenByParams %d %d %d ", operationId, parentId, relationshipId);
        return service.getChildrenByParams(parentId, operationId, relationshipId);
    }

    @GET
    @Path("/parent/{operationId}/{childId}")
    @RolesAllowed({"admin"})
    public Uni<List<Reference>> getParentsByParams(
            @PathParam("operationId") long operationId,
            @PathParam("childId") long childId) {
        log.debugf("StructureApi.getParentsByParams %d %d", operationId, childId);
        return service.getParentsByParams(childId, operationId);
    }

    @GET
    @Path("/all/{operationId}")
    @RolesAllowed({"admin"})
    public Uni<List<Reference>> getAllByOperationId(@PathParam("operationId") long operationId) {
        log.debugf("StructureApi.getAllByOperationId %d", operationId);
        return service.getAllByOperationId(operationId);
    }
}