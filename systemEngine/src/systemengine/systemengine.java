package systemengine;

import dto.*;
import exceptions.DuplicateFlowsNames;
import exceptions.UnExistsStep;
import flow.api.FlowDefinition;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

public interface systemengine {
    public void cratingFlowFromXml(String filePath) throws DuplicateFlowsNames, JAXBException, UnExistsStep, FileNotFoundException;
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
