package dto;

import flow.api.FlowIO.IO;
import flow.api.FlowIO.SingleFlowIOData;
import flow.execution.FlowExecution;
import flow.execution.FlowExecutionResult;
import flow.execution.StepExecutionData;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DTOFlowExecution {
    private UUID uniqueId;
    private String flowName;
    private Instant startTime;
    private Duration totalTime;
    private List<String> formalFlowOutputs;
    private List<DTOFlowOutputExecution> flowOutputExecutionList;
    private FlowExecutionResult flowExecutionResult;
    private Map<String,Object> dataValues;
    private Map<String, Object > freeInputsValues;
    private List<DTOSingleFlowIOData> freeInputsList;
    private List<DTOSingleFlowIOData> IOlist;
    private List<DTOStepExecutionData> stepExecutionDataList;

    public DTOFlowExecution(FlowExecution flowExecution) {
        this.uniqueId = flowExecution.getUniqueId();
        this.flowName = flowExecution.getFlowDefinition().getName();
        this.startTime = flowExecution.getStartTime();
        this.totalTime = flowExecution.getTotalTime();
        this.formalFlowOutputs = flowExecution.getFlowDefinition().getFlowFormalOutputs();
        this.flowOutputExecutionList = new LinkedList<>();
        if (formalFlowOutputs.size()!=0) {
            for (String formalFlowOutput : formalFlowOutputs) {
                SingleFlowIOData output = flowExecution.getFlowDefinition().getIOlist()
                        .stream()
                        .filter(io -> io.getFinalName().equals(formalFlowOutput))
                        .findFirst()
                        .orElse(null);

                if(output!=null){
                    flowOutputExecutionList.add(new DTOFlowOutputExecution(formalFlowOutput, output.getUserString(), output.getDD(), flowExecution.getDataValues().get(formalFlowOutput)));
                }
            }
        }
        this.flowExecutionResult = flowExecution.getFlowExecutionResult();
        this.dataValues = flowExecution.getDataValues();
        this.freeInputsValues = new HashMap<>(flowExecution.getFreeInputsValues());
        this.freeInputsList = new LinkedList<>();
        for (SingleFlowIOData freeInput :flowExecution.getFreeInputsList()) {//change to freeInput.getOriginalName() instead of  freeInput.getFinalName()
            freeInputsList.add(new DTOSingleFlowIOData(freeInput, freeInputsValues.get(freeInput.getStepName() + "." + freeInput.getOriginalName())));
        }
        this.IOlist = new LinkedList<>();

        for (SingleFlowIOData io :flowExecution.getIOlist()) {
            if(dataValues.get(io.getFinalName()) == null && io.getType().equals(IO.INPUT) && !(io.getOptionalOutput().isEmpty())        ) {
                IOlist.add(new DTOSingleFlowIOData(io, dataValues.get(io.getOptionalOutput().get(0).getFinalName())));
            }else{
                IOlist.add(new DTOSingleFlowIOData(io, dataValues.get(io.getFinalName())));
            }
        }
        this.stepExecutionDataList = new LinkedList<>();
        for(StepExecutionData step : flowExecution.getStepExecutionDataList()){
            stepExecutionDataList.add(new DTOStepExecutionData(step));
        }

    }
    public String getUniqueId() { return uniqueId.toString(); }
    public UUID getUniqueIdByUUID() { return uniqueId; }
    public String getFlowName() { return flowName; }
    public List<DTOFlowOutputExecution> getFlowOutputExecutionList() { return flowOutputExecutionList; }
    public FlowExecutionResult getFlowExecutionResult() {return flowExecutionResult;}
    public Map<String,Object> getDataValues(){ return dataValues;}
    public List<String> getFormalFlowOutputs(){return formalFlowOutputs;}
    public String getStartTimeFormatted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.ofInstant(startTime, ZoneId.systemDefault());

        return localDateTime.format(formatter);
    }
    public Duration getTotalTime() { return totalTime; }
    public Map<String, Object> getFreeInputsValues() { return freeInputsValues; }
    public List<DTOSingleFlowIOData> getFreeInputsList() { return freeInputsList; }
    public List<DTOSingleFlowIOData> getIOlist(){return IOlist;};
    public List<DTOStepExecutionData>getStepExecutionDataList(){return stepExecutionDataList;}
/*    public FlowExecutionResult isComplete() {
        for (DTOStepExecutionData stepExecutionData : stepExecutionDataList) {
            if (!stepExecutionData.isExecuted()) {
                return false;
            }
        }
        //return flowExecutionResult != null ? flowExecutionResult : null;
    }*/
    public boolean isComplete() {
        for (DTOStepExecutionData stepExecutionData : stepExecutionDataList) {
            if (!stepExecutionData.isExecuted()) {
                return false;
            }
        }
        return true;
        //return flowExecutionResult != null ? flowExecutionResult : null;
    }


}
