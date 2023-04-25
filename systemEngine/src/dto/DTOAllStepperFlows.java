package dto;

import java.util.LinkedList;

public class DTOAllStepperFlows {

    private LinkedList<DTOFlowDefinitionImpl> allFlowsList ;

    public DTOAllStepperFlows(LinkedList<DTOFlowDefinitionImpl> allFlows) {
        this.allFlowsList = new LinkedList<>(allFlows);}

    public LinkedList<DTOFlowDefinitionImpl> getAllFlows() {
        return allFlowsList;
    }
}
