package io.samancore.operation.service;

import io.samancore.operation.model.Contract;
import io.samancore.operation.model.Operation;
import io.samancore.operation.model.Reference;
import io.samancore.operation.model.Structure;
import io.samancore.operation.request.*;

import java.util.List;

public interface OperationService {
    Contract createContract(CreateContractRequest createContractRequest);
    Operation createTransition(CreateTransitionRequest createTransitionRequest);
    Operation applyTransition(Long operationId);
    Structure createStructure(CreateStructureRequest createStructureRequest);
    List<Structure> deleteStructure(DeleteStructureRequest deleteStructureRequest);
    Reference updateReference(Long referenceId, UpdateReferenceRequest updateReferenceRequest);
}