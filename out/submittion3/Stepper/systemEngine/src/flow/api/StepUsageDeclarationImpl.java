package flow.api;

import steps.api.StepDefinition;

import java.io.Serializable;

public class StepUsageDeclarationImpl implements StepUsageDeclaration, Serializable {
    private final StepDefinition stepDefinition;
    private final boolean skipIfFail;
    private final String stepName;

    public StepUsageDeclarationImpl(StepDefinition stepDefinition) {
        this(stepDefinition, false, stepDefinition.name());
    }

    public StepUsageDeclarationImpl(StepDefinition stepDefinition, String name) {
        this(stepDefinition, false, name);
    }

    public StepUsageDeclarationImpl(StepDefinition stepDefinition, boolean skipIfFail, String stepName) {
        this.stepDefinition = stepDefinition;
        this.skipIfFail = skipIfFail;
        this.stepName = stepName;
    }

    @Override
    public String getFinalStepName() {
        return stepName;
    }
    @Override
    public StepDefinition getStepDefinition() {
        return stepDefinition;
    }
    @Override
    public boolean skipIfFail() {
        return skipIfFail;
    }
}