package dto;

import datadefinition.api.DataDefinitions;
import datadefinition.impl.mapping.MappingData;
import datadefinition.impl.mapping.MappingDataDefinition;
import datadefinition.impl.relation.RelationData;

public class DTOFlowOutputExecution {
    private String name;
    private String userString;
    private DataDefinitions type;
    private Object value;
    // private Object userPresentation;

    public DTOFlowOutputExecution(String name, String userString, DataDefinitions type, Object value) {
        this.name = name;
        this.userString = userString;
        this.type = type;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getUserString() {
        return userString;
    }

    public DataDefinitions getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public String getUserPresentation() {

        return value.toString();
        /*
        switch (type.getName()) {
            case "String":
                return value;
            case "Number":
                return value;
            case "Double":
                return value;
            case "File":
                return value.toString();
            // case "List":
             //   return value.toString();
            case "String List":
                return value.toString();
            case "File List":
                return value.toString();
           // case "Mapping":
              //  return value.toString();
            case "Numbers Mapping":
                return value.toString();
            case "Relation":
                return value.toString();
            default:
                return null;
        }
        */
    }
}


