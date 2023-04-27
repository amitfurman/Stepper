package flow.execution;

import flow.api.FlowDefinition;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class FlowExecution {
    private final UUID uniqueId;
    private final FlowDefinition flowDefinition;
    private Duration totalTime;
    private Instant startTime;
    private Instant endTime;
    private FlowExecutionResult flowExecutionResult;
    private Map<String,Object> dataValues;
    private List<StepExecutionData> stepExecutionDataList;

    // lots more data that needed to be stored while flow is being executed...

    public FlowExecution(FlowDefinition flowDefinition) {
        this.uniqueId = UUID.randomUUID();
        this.flowDefinition = flowDefinition;
        this.dataValues = new HashMap<>();
        this.stepExecutionDataList = new LinkedList<>();
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

    public void setStartTime(Instant startTime) { this.startTime = startTime;}

    public void setEndTime(Instant endTime) { this.endTime = endTime;}

    public void setTotalTime(Duration totalTime) {this.totalTime = totalTime;}

    public void setFlowExecutionResult(FlowExecutionResult result){ this.flowExecutionResult = result;}

    public  List<StepExecutionData> getStepExecutionDataList(){ return stepExecutionDataList;}

    public Map<String,Object> getDataValues(){ return dataValues;}
    public void setDataValues(Map<String,Object> dataValues){ this.dataValues = dataValues;}
}