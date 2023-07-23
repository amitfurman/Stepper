package dto;

import java.util.Set;

public class DTOFlowsDefinitionInRoles {
    public Set<DTOFlowDefinitionInRoles> flowsDefinitionInRoles;

    public DTOFlowsDefinitionInRoles(Set<DTOFlowDefinitionInRoles> flowsDefinitionInRoles) {
        this.flowsDefinitionInRoles = flowsDefinitionInRoles;
    }

    public Set<DTOFlowDefinitionInRoles> getFlowsDefinitionInRoles() {return flowsDefinitionInRoles;}
    public int getNumberOfFlowsDefinitionInRoles() {return flowsDefinitionInRoles.size();}
}
