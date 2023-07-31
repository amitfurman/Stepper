package flow.execution;

import dto.DTOStepExecutionData;
import flow.api.FlowDefinition;
import flow.api.FlowIO.SingleFlowIOData;
import statistic.FlowAndStepStatisticData;
import statistic.StatisticData;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FlowExecution  implements Serializable {
    private String userName;
    private final UUID uniqueId;
    private String flowName;
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

    public FlowExecution(FlowDefinition flowDefinition) {
        this.uniqueId = UUID.randomUUID();
        this.flowName = flowDefinition.getName();
        this.flowDefinition = flowDefinition;
        this.dataValues = new HashMap<>();
        this.stepExecutionDataList = new LinkedList<>();
        this.freeInputsValues = new HashMap<>();
        this.freeInputsList = new LinkedList<>(flowDefinition.getFlowFreeInputs());
        this.IOlist = new LinkedList<>(flowDefinition.getIOlist());
    }

    public FlowExecution(String userName, FlowDefinition flowDefinition) {
        this.userName = userName;
        this.uniqueId = UUID.randomUUID();
        this.flowName = flowDefinition.getName();
        this.flowDefinition = flowDefinition;
        this.dataValues = new HashMap<>();
        this.stepExecutionDataList = new LinkedList<>();
        this.freeInputsValues = new HashMap<>();
        this.freeInputsList = new LinkedList<>(flowDefinition.getFlowFreeInputs());
        this.IOlist = new LinkedList<>(flowDefinition.getIOlist());
    }

    public String getUserName() {return userName;}
    public UUID getUniqueId() {
        return uniqueId;
    }
    public String getFlowName() {
        return flowName;
    }
    public UUID getUniqueIdByUUID() { return uniqueId; }
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
    public void setStepExecutionDataList(List<StepExecutionData> stepExecutionDataList){this.stepExecutionDataList = stepExecutionDataList;}
    synchronized public  List<StepExecutionData> getStepExecutionDataList(){ return stepExecutionDataList;}
    public Map<String,Object> getDataValues(){ return dataValues;}
    public void setDataValues(Map<String,Object> dataValues){ this.dataValues = dataValues;}
    public Instant getStartTime() { return startTime; }
    public Duration getTotalTime() { return totalTime; }
    public Map<String, Object> getFreeInputsValues() { return freeInputsValues; }
    public void setFreeInputsValues(Map<String, Object> freeInputs) { this.freeInputsValues =new HashMap<>(freeInputs); }
    public List<SingleFlowIOData> getFreeInputsList() { return freeInputsList; }
    public List<SingleFlowIOData> getIOlist() { return IOlist; }

    public boolean isComplete() {
        for (StepExecutionData stepExecutionData : stepExecutionDataList) {
            if (!stepExecutionData.isExecuted()) {
                return false;
            }
        }
        return flowExecutionResult != null;
    }

    public String getStartTimeFormatted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.ofInstant(startTime, ZoneId.systemDefault());

        return localDateTime.format(formatter);
    }
    public void addStepExecution(StepExecutionData currInvokingStep) {
        stepExecutionDataList.add(currInvokingStep);
    }
}