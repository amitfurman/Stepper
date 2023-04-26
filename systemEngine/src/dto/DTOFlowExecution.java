package dto;

import flow.execution.FlowExecutionResult;

public interface DTOFlowExecution {
    DTOFlowDefinition getFlowDefinition();
    FlowExecutionResult getFlowExecutionResult();
}
