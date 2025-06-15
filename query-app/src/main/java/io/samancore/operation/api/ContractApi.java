package io.samancore.operation.api;

import io.samancore.common.model.PageData;
import io.samancore.common.page.PageUtil;
import io.samancore.operation.model.Contract;
import io.samancore.operation.model.Operation;
import io.samancore.operation.service.ContractService;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriInfo;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    @RolesAllowed({"admin"})
    public Uni<Contract> getById(@PathParam("id") long id) {
        log.debugf("ContractApi.getById %d ", id);
        return service.getById(id);
    }

    @GET
    @Path("/{id}/operation")
    @RolesAllowed({"admin"})
    public Uni<PageData<Operation>> getOperationByContractId(@PathParam("id") long id) {
        log.debugf("ContractApi.getOperationByContractId %d ", id);
        var pageRequest = PageUtil.getPage(uriInfo.getQueryParameters());
        return service.getOperationByContractId(id, pageRequest);
    }

    @GET
    @Path("/operation/{operationId}")
    @RolesAllowed({"admin"})
    public Uni<Contract> getByOperationId(@PathParam("operationId") long operationId) {
        log.debugf("ContractApi.getByOperationId %d ", operationId);
        return service.getByOperationId(operationId);
    }

    @GET
    @Path("/range/{startDate}/{endDate}")
    @RolesAllowed({"admin"})
    public Uni<List<Contract>> getByRangeDate(@PathParam("startDate") String startDate,
                                              @PathParam("endDate") String endDate) {
        log.debugf("ContractApi.getByRangeDate %s %s", startDate, endDate);
        return service.getByRangeDate(startDate, endDate);
    }

    @GET
    @Path("/key-words")
    @RolesAllowed({"admin"})
    public Uni<PageData<Contract>> getByKeyWords() {
        var params = uriInfo.getQueryParameters();
        log.debugf("ContractApi.getByKeyWords %s %s", params);
        Set<String> pageRequestParams = Set.of("_sort", "_order", "_page", "_limit");
        var paramsFiltered = params.entrySet().stream().filter(stringListEntry -> !pageRequestParams.contains(stringListEntry.getKey()))
                .collect(Collectors.toMap(
                    e -> e.getKey(),
                    e -> e.getValue()));
        var pageRequest = PageUtil.getPage(uriInfo.getQueryParameters());
        return service.getByKeyWords(paramsFiltered, pageRequest);
    }

}