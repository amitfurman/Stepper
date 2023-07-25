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
        System.out.println("roles: "+roles);
        rolesList = new HashSet<>();
        for (Role role : roles) {
            System.out.println("1.role name: " + role.getName());
            System.out.println("2.flow list: " + role.getFlowsInRole());
            System.out.println("3.user list: "+role.getUsersInRole().stream().collect(Collectors.toList()));
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
