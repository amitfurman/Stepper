package dto;

import flow.execution.FlowExecutionResult;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DTOFlowExeInfo {
    String userName;
    String flowName;
    String ID;
    String startTime;
    Duration totalTime;
    FlowExecutionResult resultExecute;
    List<DTOFreeInputs> freeInputs;
    List<DTOStepsInFlow> steps;
    List<DTOOutput> outputs;

    DTOFlowAndStepStatisticData statisticData;

    public DTOFlowExeInfo(String userName,String flowName, String ID, String startTime, Duration totalTime, FlowExecutionResult resultExecute, List<DTOFreeInputs> freeInputs, List<DTOStepsInFlow> steps, List<DTOOutput> outputs){
        this.userName = userName;
        this.flowName = flowName;
        this.ID = ID;
        this.startTime = startTime;
        this.totalTime = totalTime;
        this.resultExecute = resultExecute;
        this.freeInputs = freeInputs;
        this.steps = steps;
        this.outputs = outputs;
    }

    public DTOFlowExeInfo(String userName, String flowName, String startTime, FlowExecutionResult resultExecute ,DTOFlowAndStepStatisticData statisticData ){
        this.userName = userName;
        this.flowName = flowName;
        this.startTime = startTime;
        this.resultExecute = resultExecute;
        this.statisticData = statisticData;
    }
    public String getUserName() {return userName;}
    public String getFlowName() {return flowName;}
    public String getID() {return ID;}
/*    public String getStartTimeFormatted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.ofInstant(startTime, ZoneId.systemDefault());

        return localDateTime.format(formatter);
    }*/
    public String getStartTime(){return startTime;}
    public Duration getTotalTime() {return totalTime;}
    public FlowExecutionResult getResultExecute() {return resultExecute;}
    public List<DTOFreeInputs> getFreeInputs() {return freeInputs;}
    public List<DTOStepsInFlow> getSteps() {return steps;}
    public List<DTOOutput> getOutputs() {return outputs;}
    public DTOFlowAndStepStatisticData getStatisticData() {return statisticData;}

}
