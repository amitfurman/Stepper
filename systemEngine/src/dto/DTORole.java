package dto;

import flow.api.FlowDefinition;

import java.util.ArrayList;
import java.util.List;

public class DTORole {

        private String name;
        private String description;
/*
    private List<DTOFlowName> flowsInRole;
*/
        private List<String> flowsInRole;

        public DTORole(String name, String description, List<String> flows) {
            this.name = name;
            this.description = description;
            flowsInRole = new ArrayList<>();
            flowsInRole.addAll(flows);
  /*          for (String flow : flows) {
                this.flowsInRole.add(new DTOFlowName(flow));
            }*/
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
}
