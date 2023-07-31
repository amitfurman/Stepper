package dto;

import java.util.ArrayList;
import java.util.List;

public class DTOFreeInputs {
    String name;
    String type;
    List<String> connectedSteps;
    String necessity;
    Object value;

    public DTOFreeInputs(String name, String type, List<String> connectedSteps, String necessity) {
        this.name = name;
        this.type = type;
        this.connectedSteps = connectedSteps;
        this.necessity = necessity;
    }
    public DTOFreeInputs(String name, String type, String necessity, Object value) {
        this.name = name;
        this.type = type;
        this.connectedSteps = new ArrayList<>();
        this.necessity = necessity;
        this.value = value;
    }


    public String getName() {return name;}
    public String getType() {return type;}
    public List<String> getConnectedSteps() {return connectedSteps;}
    public String getNecessity() {return necessity;}
    public Object getValue() {return value;}

}
