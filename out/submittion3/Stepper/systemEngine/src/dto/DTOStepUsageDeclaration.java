package dto;

import flow.api.StepUsageDeclaration;

public class DTOStepUsageDeclaration{
    private final String originalName;
    private final String finalName;
    private final boolean isReadOnly;

    public DTOStepUsageDeclaration(StepUsageDeclaration step) {
        this.originalName = step.getStepDefinition().name();
        this.finalName = step.getFinalStepName();
        this.isReadOnly = step.getStepDefinition().isReadonly();
    }
    public DTOStepUsageDeclaration(String OriginalName,String FinalName,boolean IsReadOnly) {
        this.originalName = OriginalName;
        this.finalName = FinalName;
        this.isReadOnly = IsReadOnly;
    }
    public String getOriginalStepName() {
        return originalName;
    }
    public String getFinalStepName() {
        return finalName;
    }
    public boolean getIsReadOnly() {
        return isReadOnly;
    }
}