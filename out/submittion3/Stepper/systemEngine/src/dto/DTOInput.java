package dto;

public class DTOInput {
    String originalName;
    String finalName;
    Object value;
    String stepName;
    String type;
    String necessity;

    public DTOInput(String originalName, String finalName,Object value,String stepName, String type , String necessity){
        this.originalName =originalName;
        this.finalName= finalName;
        this.stepName = stepName;
        this.value =value;
        this.type = type;
        this.necessity = necessity;
    }
    public DTOInput(String originalName, String finalName,Object value,String stepName, String type){
        this.originalName =originalName;
        this.finalName= finalName;
        this.value =value;
        this.stepName = stepName;
        this.type = type;
    }

    public String getOriginalName() {
        return originalName;
    }

    public Object getValue() {
        return value;
    }

    public String getFinalName() {
        return finalName;
    }
    public String getType() {return type;}

    public String getStepName() {return stepName;}
    public String getNecessity() {return necessity;}

}
