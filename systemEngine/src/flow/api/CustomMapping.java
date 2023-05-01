package flow.api;

import java.io.Serializable;

public class CustomMapping implements Serializable {
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
    public String getTargetData() {
        return targetData;
    }
    public String getSourceStep() {
        return sourceStep;
    }
    public String getSourceData() {
        return sourceData;
    }

    public void setTargetStep(String value) {
        this.targetStep = value;
    }
    public void setTargetData(String value) {
        this.targetData = value;
    }
    public void setSourceStep(String value) {
        this.sourceStep = value;
    }
    public void setSourceData(String value) {
        this.sourceData = value;
    }
}
