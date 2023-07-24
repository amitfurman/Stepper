package dto;

import flow.api.FlowDefinition;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DTORole {

        private String name;
        private String description;
        private List<String> users;
        private List<String> flowsInRole;

        public DTORole(String name, String description, List<String> flows, List<String> users) {
            this.name = name;
            this.description = description;
            this.flowsInRole = new ArrayList<>(flows);
            if (users == null)
                this.users = new ArrayList<>();
            else
                this.users = new ArrayList<>(users);
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

        public List<String> getUsers() {return users;}
}
