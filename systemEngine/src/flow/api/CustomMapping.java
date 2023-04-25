package flow.api;

public class CustomMapping {
    protected String targetStep;
    protected String targetData;
    protected String sourceStep;
    protected String sourceData;

    public CustomMapping(String sourceStep, String sourceData, String targetStep, String targetData){
        this.sourceStep = sourceStep;
        this.sourceData = sourceData;
        this.targetStep = targetStep;
        this.targetData = targetData;
    }

    public String getTargetStep() {
        return targetStep;
    }

    public void setTargetStep(String value) {
        this.targetStep = value;
    }

    public String getTargetData() {
        return targetData;
    }

    public void setTargetData(String value) {
        this.targetData = value;
    }

    public String getSourceStep() {
        return sourceStep;
    }

    public void setSourceStep(String value) {
        this.sourceStep = value;
    }

    public String getSourceData() {
        return sourceData;
    }

    public void setSourceData(String value) {
        this.sourceData = value;
    }
}
