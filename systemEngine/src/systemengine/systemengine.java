package systemengine;

import dto.DTOFlowExecution;
import dto.DTOFreeInputs;
import flow.api.FlowDefinition;

public interface systemengine {

    DTOFlowExecution activateFlow(FlowDefinition currFlow, DTOFreeInputs freeInputs);
}
