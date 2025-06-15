package io.samancore.operation.api;

import io.samancore.common.model.PageData;
import io.samancore.common.page.PageUtil;
import io.samancore.operation.model.Operation;
import io.samancore.operation.request.OperationFilterRequest;
import io.samancore.operation.service.OperationService;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriInfo;
import org.jboss.logging.Logger;

@Path("operations")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OperationApi {

    @Inject
    Logger log;

    @Context
    UriInfo uriInfo;

    @Inject
    OperationService service;

    @GET
    @Path("/{id}")
    @RolesAllowed({"admin"})
    public Uni<Operation> getById(@PathParam("id") long id) {
        log.debugf("OperationApi.getById %d ", id);
        return service.getById(id);
    }

    @POST
    @Path("/")
    @RolesAllowed({"admin"})
    public Uni<PageData<Operation>> getByFilters(OperationFilterRequest request) {
        log.debugf("OperationApi.getByFilters %s ", request);
        var pageRequest = PageUtil.getPage(uriInfo.getQueryParameters());
        return service.getByFilters(request, pageRequest);
    }

}