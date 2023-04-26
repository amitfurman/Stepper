package dto;

import flow.api.FlowDefinition;
import flow.execution.FlowExecutionResult;

import java.time.Duration;
import java.util.Map;

public class DTOFlowExecutionImpl implements DTOFlowExecution {
    private final DTOFlowDefinition flowDefinition;
    private Duration totalTime;
    private FlowExecutionResult flowExecutionResult;
    private DTOFreeInputs freeInputs;

    // lots more data that needed to be stored while flow is being executed...

    public DTOFlowExecutionImpl(DTOFlowDefinition flowDefinition, DTOFreeInputs freeInputs) {
        this.flowDefinition = flowDefinition;
        this.freeInputs = freeInputs;
    }

    public DTOFlowExecutionImpl(DTOFlowDefinition flowDefinition, Duration totalTime, FlowExecutionResult flowExecutionResult, DTOFreeInputs freeInputs) {
        this.flowDefinition = flowDefinition;
        this.totalTime = totalTime;
        this.flowExecutionResult = flowExecutionResult;
        this.freeInputs = freeInputs;
    }

/*    public String getUniqueId() {
        return uniqueId;
    }*/

    public DTOFlowDefinition getFlowDefinition() {
        return flowDefinition;
    }

    public FlowExecutionResult getFlowExecutionResult() {
        return flowExecutionResult;
    }

}
