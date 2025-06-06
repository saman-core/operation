package io.samancore.operation.api;

import io.samancore.operation.model.Structure;
import io.samancore.operation.service.StructureService;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriInfo;
import org.jboss.logging.Logger;

import java.util.List;

@Path("structures")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class StructureApi {

    @Inject
    Logger log;

    @Context
    UriInfo uriInfo;

    @Inject
    StructureService service;

    @GET
    @Path("/{operationId}")
    public Uni<Structure> getRootByOperationId(@PathParam("operationId") long operationId) {
        log.debugf("StructureApi.getRootByOperationId %d ", operationId);
        return service.getRootByOperationId(operationId);
    }

    @GET
    @Path("/children/{operationId}/{parentId}/{relationshipId}")
    public Uni<List<Structure>> getChildrenByParams(
        @PathParam("operationId") long operationId,
        @PathParam("parentId") long parentId,
        @PathParam("relationshipId") long relationshipId) {
        log.debugf("StructureApi.getChildrenByParams %d %d %d ", operationId, parentId, relationshipId);
        return service.getChildrenByParams( parentId,  operationId,  relationshipId);
    }

    @GET
    @Path("/parent/{operationId}/{childId}")
    public Uni<List<Structure>> getParentsByParams(
        @PathParam("operationId") long operationId,
        @PathParam("childId") long childId) {
        log.debugf("StructureApi.getParentsByParams %d %d", operationId, childId);
        return service.getParentsByParams( childId, operationId);
    }

    @GET
    @Path("/all/{operationId}")
    public Uni<Structure> getAllByOperationId(@PathParam("operationId") long operationId) {
        log.debugf("StructureApi.getAllByOperationId %d", operationId);
        return service.getAllByOperationId( operationId);
    }
}