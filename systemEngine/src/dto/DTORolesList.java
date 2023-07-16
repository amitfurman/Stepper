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
            List<String> flowsNames = new ArrayList<>();
            for (FlowDefinition flow : role.getFlowsInRole()) {
                flowsNames.add(flow.getName());
            }
            rolesList.add(new DTORole(role.getName(), role.getDescription(),flowsNames));
        }
    }
    public DTORolesList() {
        rolesList = new ArrayList<>();
    }

    public List<DTORole> getRoles() {
        return rolesList;
    }
}
