package dto;

import datadefinition.api.DataDefinitions;

public class DTOFlowOutputExecution {
    private String name;
    private String userString;
    private DataDefinitions type;
    private Object value;

    public DTOFlowOutputExecution(String name, String userString, DataDefinitions type, Object value) {
        this.name = name;
        this.userString = userString;
        this.type = type;
        this.value = value;
    }
    public String getName() { return name;}
    public String getUserString() { return userString;}
    public DataDefinitions getType() { return type;}
    public Object getValue() { return value;}
}


