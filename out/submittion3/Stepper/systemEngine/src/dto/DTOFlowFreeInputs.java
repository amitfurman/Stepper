package dto;

import java.util.List;

public class DTOFlowFreeInputs {
    String finalName;
    String originalName;
    String type;
    String stepName;
    String necessity;

    public DTOFlowFreeInputs(String finalName, String originalName, String type, String stepName, String necessity) {
        this.finalName = finalName;
        this.originalName = originalName;
        this.type = type;
        this.stepName = stepName;
        this.necessity = necessity;
    }

    public String getFinalName() {return finalName;}
    public String getOriginalName() {return originalName;}
    public String getType() {return type;}
    public String getStepName() {return stepName;}
    public String getNecessity() {return necessity;}
}
