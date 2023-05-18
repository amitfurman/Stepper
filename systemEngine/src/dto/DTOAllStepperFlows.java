package dto;

import flow.api.FlowDefinition;

import java.util.LinkedList;

public class DTOAllStepperFlows {

    private LinkedList<DTOFlowDefinition> allFlowsList ;

    public DTOAllStepperFlows(LinkedList<FlowDefinition> allFlows) {
        this.allFlowsList = new LinkedList<>();
        for (FlowDefinition flow: allFlows) {
            this.allFlowsList.add(new DTOFlowDefinition(flow));
        }
    }

    public LinkedList<DTOFlowDefinition> getAllFlows() {
        return allFlowsList;
    }

    public int getNumberOfFlows(){
        return allFlowsList.size();
    }

    public DTOFlowDefinition getFlow(int index){
        return allFlowsList.get(index);
    }

}
