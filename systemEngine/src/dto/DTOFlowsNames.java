package dto;

public class DTOFlowsNames {
    private StringBuilder flowsNames;

    public DTOFlowsNames(StringBuilder flowsNames) {
        this.flowsNames = flowsNames;
    }

    public StringBuilder getFlowName() {
        return flowsNames;
    }
}
