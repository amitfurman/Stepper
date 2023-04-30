package flow.execution;

import flow.api.FlowDefinition;
import flow.api.FlowIO.SingleFlowIOData;

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
    private Map<String, Object > freeInputsValues;
    private List<SingleFlowIOData> freeInputsList;
    private final List<SingleFlowIOData> IOlist;

    // lots more data that needed to be stored while flow is being executed...

    public FlowExecution(FlowDefinition flowDefinition) {
        this.uniqueId = UUID.randomUUID();
        this.flowDefinition = flowDefinition;
        this.dataValues = new HashMap<>();
        this.stepExecutionDataList = new LinkedList<>();
        this.freeInputsValues = new HashMap<>();
        this.freeInputsList = new LinkedList<>(flowDefinition.getFlowFreeInputs());
        this.IOlist = new LinkedList<>(flowDefinition.getIOlist());
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
    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }
    public void setEndTime(Instant endTime) { this.endTime = endTime;}
    public void setTotalTime(Duration totalTime) {this.totalTime = totalTime;}
    public void setFlowExecutionResult(FlowExecutionResult result){ this.flowExecutionResult = result;}
    public void setStepExecutionDataList(List<StepExecutionData> stepExecutionDataList){this.stepExecutionDataList = stepExecutionDataList;}
    public  List<StepExecutionData> getStepExecutionDataList(){ return stepExecutionDataList;}
    public Map<String,Object> getDataValues(){ return dataValues;}
    public void setDataValues(Map<String,Object> dataValues){ this.dataValues = dataValues;}
    public Instant getStartTime() { return startTime; }
    public Duration getTotalTime() { return totalTime; }
    public Map<String, Object> getFreeInputsValues() { return freeInputsValues; }
    public void setFreeInputsValues(Map<String, Object> freeInputs) { this.freeInputsValues = freeInputs; }
    public List<SingleFlowIOData> getFreeInputsList() { return freeInputsList; }
    public List<SingleFlowIOData> getIOlist() { return IOlist; }
}