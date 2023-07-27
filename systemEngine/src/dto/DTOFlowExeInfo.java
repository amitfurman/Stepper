package dto;

import flow.execution.FlowExecutionResult;

import java.time.Duration;
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

    public DTOFlowExeInfo(String userName,String flowName, String ID, String startTime, Duration totalTime, FlowExecutionResult resultExecute, List<DTOFreeInputs> freeInputs, List<DTOStepsInFlow> steps, List<DTOOutput> outputs){
        System.out.println("DTO Username "+userName);
        this.userName = userName;
        System.out.println("DTO Flowname "+flowName);
        this.flowName = flowName;
        System.out.println("DTO id "+ID);
        this.ID = ID;
        System.out.println("DTO start time "+startTime);
        this.startTime = startTime;
        System.out.println("DTO total time "+totalTime);
        this.totalTime = totalTime;
        //this.totalTime = (totalTime != null) ? totalTime : null;//need to insert null?
        System.out.println("DTO resultExecute: " +resultExecute);
      //  this.resultExecute = (resultExecute!=null) ? resultExecute : null;
        this.resultExecute = resultExecute;
        this.freeInputs = freeInputs;
        System.out.println("DTO freeInputs: " + freeInputs);
        this.steps = steps;
        System.out.println("DTO steps: " + steps);
        this.outputs = outputs;
        System.out.println("DTO outputs: " + outputs);
        System.out.println("end of DTOFlowExeInfo constructor");
    }

    public DTOFlowExeInfo(String userName, String flowName, String startTime, FlowExecutionResult resultExecute){
        this.userName = userName;
        this.flowName = flowName;
        this.startTime = startTime;
        this.resultExecute = resultExecute;
    }
    public String getUserName() {return userName;}
    public String getFlowName() {return flowName;}
    public String getID() {return ID;}
    public String getStartTime() {return startTime;}
    public Duration getTotalTime() {return totalTime;}
    public FlowExecutionResult getResultExecute() {return resultExecute;}
    public List<DTOFreeInputs> getFreeInputs() {return freeInputs;}
    public List<DTOStepsInFlow> getSteps() {return steps;}
    public List<DTOOutput> getOutputs() {return outputs;}
}
