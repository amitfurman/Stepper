package dto;

import flow.api.FlowIO.SingleFlowIOData;
import java.util.List;

public interface DTOFlowDefinition {
    String getName();
    String getDescription();
    List<String> getFlowFormalOutputs();
    boolean getFlowReadOnly();
    List<DTOStepUsageDeclaration> getFlowStepsData();
    List<SingleFlowIOData> getIOlist();
    List<String> getListOfStepsWithCurrInput(String inputName);
    List<DTOSingleFlowIOData> getFlowFreeInputs();
}
