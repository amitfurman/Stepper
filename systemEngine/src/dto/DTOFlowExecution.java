package dto;

import flow.execution.FlowExecutionResult;

import java.util.List;
import java.util.Map;

public interface DTOFlowExecution {
    String getUniqueId();
    String getFlowName();
    String getUserString();
    FlowExecutionResult getFlowExecutionResult();
    Map<String,Object> getDataValues();
    List<String> getFormalFlowOutputs();
}
