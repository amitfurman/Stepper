package dto;

import flow.api.StepUsageDeclaration;

import java.util.List;

public interface DTOFlowDefinition {
    String getName();
    String getDescription();
    List<String> getFlowFormalOutputs();
    boolean getFlowReadOnly();
    List<DTOStepUsageDeclaration> getFlowStepsData();


}
