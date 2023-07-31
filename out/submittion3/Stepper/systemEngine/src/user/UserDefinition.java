package user;

import roles.Role;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserDefinition {
    String username;
    boolean isManager;
    Set<Role> roles;
    Set<String> executedFlows;

    Set<String> flows; //i add this set for when the user is manager and need all the flows

    public UserDefinition(String username, boolean isManager) {
        this.username = username;
        this.isManager = isManager;
        this.roles = new HashSet<>();
        this.executedFlows = new HashSet<>();
    }

    public String getUsername() {return username;}
    public boolean isManager() { return isManager;}
    public void setManager(boolean manager) {isManager = manager;}
    public void addRole(Role role) {roles.add(role);}
    public Set<Role> getRoles(){return roles;};
    public Set<String> getExecutedFlows() {return executedFlows;}
    public void addExecutedFlow(String flowName) {this.executedFlows.add(flowName);}

    public void setRoles(List<Role> rolesList) {
        this.roles = new HashSet<>(rolesList);
    }

    public void setFlows(Set<String> flows) {
        this.flows = flows;
    }

    public Set<String> getFlows() {
        return flows;
    }
}
