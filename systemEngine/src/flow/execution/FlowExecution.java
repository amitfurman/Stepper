package flow.execution;

import flow.api.FlowDefinition;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FlowExecution {
    private final UUID uniqueId;
    private final FlowDefinition flowDefinition;
    private Duration totalTime;
    private long startTime;
    private long endTime;
    private FlowExecutionResult flowExecutionResult;
    private Map<String,Object> dataValues;
    private List<StepExecutionData> stepExecutionDataList;

    // lots more data that needed to be stored while flow is being executed...

    public FlowExecution(FlowDefinition flowDefinition) {
        this.uniqueId = UUID.randomUUID();
        this.flowDefinition = flowDefinition;
        this.startTime = System.currentTimeMillis();
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public FlowDefinition getFlowDefinition() {
        return flowDefinition;
    }

    public FlowExecutionResult getFlowExecutionResult() {
        return flowExecutionResult;
    }
}