package systemengine;

import dto.*;
import flow.api.FlowDefinition;

import java.util.List;
import java.util.Map;

public interface systemengine {
    public void cratingFlowFromXml(String filePath);
    DTOFlowsNames printFlowsName();
    List<FlowDefinition> getFlowDefinitionList();
    DTOFlowDefinition IntroduceTheChosenFlow(int flowNumber);
    boolean hasAllMandatoryInputs(int flowChoice, Map<String, Object> freeInputMap);
    DTOFlowExecution activateFlow(int flowChoice, DTOFreeInputsFromUser freeInputs);
    DTOFreeInputsByUserString printFreeInputsByUserString(int choice);
    DTOSingleFlowIOData getSpecificFreeInput(int flowChoice, int freeInputChoice);
    DTOFlowsExecutionList getFlowsExecutionList();
    DTOFlowExecution getFlowExecutionDetails(int flowExecutionChoice);
    DTOFlowAndStepStatisticData getStatisticData();

}
