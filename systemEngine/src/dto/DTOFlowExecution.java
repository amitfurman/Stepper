package dto;

import flow.api.FlowIO.SingleFlowIOData;
import flow.execution.FlowExecution;
import flow.execution.FlowExecutionResult;

import java.util.*;

public class DTOFlowExecution {
    private UUID uniqueId;
    private String flowName;
    private List<String> formalFlowOutputs;
    private List<DTOFlowOutputExecution> flowOutputExecutionList;
    private FlowExecutionResult flowExecutionResult;
    private Map<String,Object> dataValues;


    public DTOFlowExecution(FlowExecution flowExecution) {
        this.uniqueId = flowExecution.getUniqueId();
        this.flowName = flowExecution.getFlowDefinition().getName();
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
    }
    public String getUniqueId() { return uniqueId.toString(); }
    public String getFlowName() { return flowName; }
    public List<DTOFlowOutputExecution> getFlowOutputExecutionList() { return flowOutputExecutionList; }
    public FlowExecutionResult getFlowExecutionResult() {return flowExecutionResult;}
    public Map<String,Object> getDataValues(){ return dataValues;}
    public List<String> getFormalFlowOutputs(){return formalFlowOutputs;}
}
