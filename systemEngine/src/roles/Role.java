package roles;

import flow.api.FlowDefinition;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Role {
    private String name;
    private String description;
    private List<String> flowsInRole;
    private Set<String> users;

    public Role(String name, String description, List<String> flowsInRole) {
        this.name = name;
        this.description = description;
        this.flowsInRole = flowsInRole;
        this.users = new HashSet<>();
    }

    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public List<String> getFlowsInRole() {
        return flowsInRole;
    }
    public Set<String> getUsersInRole() { return users;}
    public void addUserToRole(String userName) {this.users.add(userName);}
    public void setFlowsInRole(List<String> flowsInRole) {this.flowsInRole = flowsInRole;}
    public void setUsersInRole(List<String> users) {this.users = new HashSet<>(users);}
}
