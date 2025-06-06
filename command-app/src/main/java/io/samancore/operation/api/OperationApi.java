package io.samancore.operation.api;

import io.samancore.operation.model.Contract;
import io.samancore.operation.model.Operation;
import io.samancore.operation.model.Reference;
import io.samancore.operation.model.Structure;
import io.samancore.operation.request.*;
import io.samancore.operation.service.OperationService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.jboss.logging.Logger;

import java.util.List;

@Path("")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OperationApi {

    @Inject
    Logger log;

    @Context
    UriInfo uriInfo;

    @Inject
    OperationService service;

    @POST
    @Path("/contract")
    public Contract createContract(@RequestBody CreateContractRequest createContractRequest) {
        log.debugf("OperationApi.create %s", createContractRequest);
        return service.createContract(createContractRequest);
    }

    @POST
    @Path("/transition")
    public Operation createTransition(@RequestBody CreateTransitionRequest createTransitionRequest) {
        log.debugf("OperationApi.createTransition %s", createTransitionRequest);
        return service.createTransition(createTransitionRequest);
    }

    @PUT
    @Path("/apply-transition/{operationId}")
    public Operation applyTransition(@PathParam("operationId") Long operationId) {
        log.debugf("OperationApi.applyTransition %d", operationId);
        return service.applyTransition(operationId);
    }

    @POST
    @Path("/structure")
    public Structure createStructure(@RequestBody CreateStructureRequest createStructureRequest) {
        log.debugf("OperationApi.createStructure %s", createStructureRequest);
        return service.createStructure(createStructureRequest);
    }

    @DELETE
    @Path("/structure")
    public List<Structure> deleteStructure(@RequestBody DeleteStructureRequest deleteStructureRequest) {
        log.debugf("OperationApi.deleteStructure %s", deleteStructureRequest);
        return service.deleteStructure(deleteStructureRequest);
    }

    @PUT
    @Path("/reference/{referenceId}")
    public Reference updateReference(
            @PathParam("referenceId") Long referenceId,
            @RequestBody UpdateReferenceRequest updateReferenceRequest) {
        log.debugf("OperationApi.updateReference %d %s", referenceId, updateReferenceRequest);
        return service.updateReference(referenceId, updateReferenceRequest);
    }
}