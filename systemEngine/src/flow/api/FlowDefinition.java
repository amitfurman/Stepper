package flow.api;

import Steps.api.DataDefinitionDeclaration;

import java.util.List;

public interface FlowDefinition {
    String getName();
    String getDescription();
    List<StepUsageDeclaration> getFlowSteps();
    List<String> getFlowFormalOutputs();
    void validateFlowStructure();
    void addStepToFlow(StepUsageDeclaration stepUsageDeclaration);
    List<DataDefinitionDeclaration> getFlowFreeInputs();
}