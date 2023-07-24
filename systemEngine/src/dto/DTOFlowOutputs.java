package dto;

public class DTOFlowOutputs {
    String finalName;
    String type;
    String creatingStep;

    public DTOFlowOutputs(String finalName, String type, String creatingStep) {
        this.finalName = finalName;
        this.type = type;
        this.creatingStep = creatingStep;
    }

    public String getFinalName() {return finalName;}
    public String getType() {return type;}
    public String getCreatingStep() {return creatingStep;}



}
