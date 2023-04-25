package dto;

import flow.api.StepUsageDeclaration;

public class DTOStepUsageDeclarationImpl implements DTOStepUsageDeclaration{
    private final String originalName;
    private final String finalName;
    private final boolean isReadOnly;

    public DTOStepUsageDeclarationImpl(StepUsageDeclaration step) {
        this.originalName = step.getStepDefinition().name();
        this.finalName = step.getFinalStepName();
        this.isReadOnly = step.getStepDefinition().isReadonly();
    }

    @Override
    public String getOriginalStepName() {
        return originalName;
    }

    @Override
    public String getFinalStepName() {
        return finalName;
    }

    @Override
    public boolean getIsReadOnly() {
        return isReadOnly;
    }

}