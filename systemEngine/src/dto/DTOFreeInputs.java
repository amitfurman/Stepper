package dto;

import java.util.List;

public class DTOFreeInputs {
    String name;
    String type;
    List<String> connectedSteps;
    String necessity;

    public DTOFreeInputs(String name, String type, List<String> connectedSteps, String necessity) {
        this.name = name;
        this.type = type;
        this.connectedSteps = connectedSteps;
        this.necessity = necessity;
    }

    public String getName() {return name;}
    public String getType() {return type;}
    public List<String> getConnectedSteps() {return connectedSteps;}
    public String getNecessity() {return necessity;}

}
