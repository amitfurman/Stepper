package roles;

import flow.api.FlowDefinition;

import java.util.List;

public class Role {
    private String name;
    private String description;
    private List<FlowDefinition> flowsInRole;

    public Role(String name, String description, List<FlowDefinition> flowsInRole) {
        this.name = name;
        this.description = description;
        this.flowsInRole = flowsInRole;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<FlowDefinition> getFlowsInRole() {
        return flowsInRole;
    }
}
