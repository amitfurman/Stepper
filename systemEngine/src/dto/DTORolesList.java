package dto;

import flow.api.FlowDefinition;
import roles.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DTORolesList {
    private List<DTORole> rolesList;

    public DTORolesList(List<Role> roles) {
        rolesList = new ArrayList<>();
        for (Role role : roles) {
            rolesList.add(new DTORole(role.getName(), role.getDescription(),role.getFlowsInRole()));
        }
    }
    public DTORolesList() {
        rolesList = new ArrayList<>();
    }

    public List<DTORole> getRoles() {
        return rolesList;
    }
}
