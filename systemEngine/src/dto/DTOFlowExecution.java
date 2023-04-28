package dto;

import flow.execution.FlowExecution;
import flow.execution.FlowExecutionResult;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DTOFlowExecution {
    private UUID uniqueId;
    private String flowName;
    private String userString;
    private FlowExecutionResult flowExecutionResult;
    private Map<String,Object> dataValues;
    private List<String> formalFlowOutputs;


    public DTOFlowExecution(FlowExecution flowExecution) {
        this.uniqueId = flowExecution.getUniqueId();
        this.flowName = flowExecution.getFlowDefinition().getName();
        //this.userString = flowExecution.getFlowDefinition().getFlowSteps();
        this.flowExecutionResult = flowExecution.getFlowExecutionResult();
        this.dataValues = flowExecution.getDataValues();
        this.formalFlowOutputs = flowExecution.getFlowDefinition().getFlowFormalOutputs();
    }
    public String getUniqueId() { return uniqueId.toString(); }
    public String getFlowName() { return flowName; }
    public String getUserString() { return userString; }
    public FlowExecutionResult getFlowExecutionResult() {return flowExecutionResult;}
    public Map<String,Object> getDataValues(){ return dataValues;}
    public List<String> getFormalFlowOutputs(){return formalFlowOutputs;}
}
