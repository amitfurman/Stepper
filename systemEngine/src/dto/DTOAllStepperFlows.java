package dto;

import java.util.LinkedList;

public class DTOAllStepperFlows {

    private LinkedList<DTOFlowDefinition> allFlowsList ;
    public DTOAllStepperFlows(LinkedList<DTOFlowDefinition> allFlows) {
        this.allFlowsList = new LinkedList<>(allFlows);}
    public LinkedList<DTOFlowDefinition> getAllFlows() {
        return allFlowsList;
    }
}
