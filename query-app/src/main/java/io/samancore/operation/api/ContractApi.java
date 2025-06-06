package io.samancore.operation.api;

import io.samancore.operation.model.Contract;
import io.samancore.operation.service.ContractService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriInfo;
import org.jboss.logging.Logger;

import java.util.List;

@Path("contracts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ContractApi {

    @Inject
    Logger log;

    @Context
    UriInfo uriInfo;

    @Inject
    ContractService service;

    @GET
    @Path("/{id}")
    public Uni<Contract> getById(@PathParam("id") long id) {
        log.debugf("ContractApi.getById %d ", id);
        return service.getById(id);
    }

    @GET
    @Path("/operation/{operationId}")
    public Uni<Contract> getByOperationId(@PathParam("operationId") long operationId) {
        log.debugf("ContractApi.getByOperationId %d ", operationId);
        return service.getByOperationId(operationId);
    }

    @GET
    @Path("/contractid-date/{id}/{date}")
    public Uni<Contract> getByIdAndDate(@PathParam("id") long id,
                                                 @PathParam("date") String date) {
        log.debugf("ContractApi.getByIdAndDate %d %s", id, date);
        return service.getByIdAndDate(id, date);
    }

    @GET
    @Path("/range/{startDate}/{endDate}")
    public Uni<List<Contract>> getByRangeDate(@PathParam("startDate") String startDate,
                                              @PathParam("endDate") String endDate) {
        log.debugf("ContractApi.getByRangeDate %s %s", startDate, endDate);
        return service.getByRangeDate(startDate, endDate);
    }
}