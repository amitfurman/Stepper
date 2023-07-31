package dto;

import java.util.Set;

public class DTOUserInfo{
    String userName;
    Set<DTORole> roles;
    Set<String> allFlows;
    Set<String> executedFlows;

    public DTOUserInfo(String userName, Set<DTORole> roles, Set<String> executedFlows) {
        this.userName = userName;
        this.roles = roles;
        this.allFlows = roles.stream()
                .map(DTORole::getFlowsInRole)
                .flatMap(Set::stream)
                .collect(java.util.stream.Collectors.toSet());
        this.executedFlows = executedFlows;
    }
    public String getUserName() {return userName;}
    public Set<DTORole> getRoles() {return roles;}
    public Set<String> getAllFlows() {return allFlows;}
    public Set<String> getExecutedFlows() {return executedFlows;}
}
