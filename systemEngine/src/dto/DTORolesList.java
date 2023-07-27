package dto;

import flow.api.FlowDefinition;
import roles.Role;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DTORolesList {
    private Set<DTORole> rolesList;

    public DTORolesList(Set<Role> roles) {
        rolesList = new HashSet<>();
        for (Role role : roles) {
            rolesList.add(new DTORole(role.getName(), role.getDescription(),role.getFlowsInRole(), role.getUsersInRole().stream().collect(Collectors.toList())));
        }
    }
    /*public DTORolesList() {
        rolesList = new ArrayList<>();
    }*/

    public Set<DTORole> getRoles() {
        return rolesList;
    }
}
