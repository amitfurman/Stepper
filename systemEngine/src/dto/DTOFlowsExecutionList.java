package dto;

import flow.execution.FlowExecution;

import java.util.LinkedList;
import java.util.List;

public class DTOFlowsExecutionList {
    private List<DTOFlowExecution> flowsExecutionNamesList;

    public DTOFlowsExecutionList(LinkedList<FlowExecution> flowsExecution) {
        this.flowsExecutionNamesList = new LinkedList<>();
        for (FlowExecution flowExecution : flowsExecution) {
            flowsExecutionNamesList.add(new DTOFlowExecution(flowExecution));
        }
    }
    public List<DTOFlowExecution> getFlowsExecutionNamesList() {
        return flowsExecutionNamesList;
    }
}
