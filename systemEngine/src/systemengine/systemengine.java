package systemengine;

import dto.*;
import exceptions.*;
import flow.api.FlowDefinition;
import flow.mapping.FlowContinuationMapping;

import javax.xml.bind.JAXBException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface systemengine {

    public void cratingFlowFromXml(String filePath) throws DuplicateFlowsNames, JAXBException, UnExistsStep, FileNotFoundException, OutputsWithSameName, MandatoryInputsIsntUserFriendly, UnExistsData, SourceStepBeforeTargetStep, TheSameDD,
            UnExistsOutput, FreeInputsWithSameNameAndDifferentType, InitialInputIsNotExist, UnExistsFlow, UnExistsDataInTargetFlow, FileNotExistsException, FileIsNotXmlTypeException;

    DTOAllStepperFlows getAllFlows();

    DTOFlowsNames printFlowsName();

    List<FlowDefinition> getFlowDefinitionList();

    DTOFlowDefinition IntroduceTheChosenFlow(int flowNumber);

    boolean hasAllMandatoryInputs(int flowChoice, Map<String, Object> freeInputMap);
    //boolean hasAllMandatoryInputsByFlowName(int flowChoice, Map<String, Object> freeInputMap);

    DTOFlowExecution activateFlow(int flowChoice, DTOFreeInputsFromUser freeInputs);

    DTOFlowExecution activateFlowByName(String flowName, DTOFreeInputsFromUser freeInputs);

    DTOFreeInputsByUserString printFreeInputsByUserString(int choice);

    DTOSingleFlowIOData getSpecificFreeInput(int flowChoice, int freeInputChoice);

    DTOFlowsExecutionList getFlowsExecutionList();

    DTOFlowExecution getFlowExecutionDetails(int flowExecutionChoice);

    DTOFlowAndStepStatisticData getStatisticData();



    void saveToFile(String path);

    void loadFromFile(String path);



    Boolean isCurrFlowExecutionDone(String currFlowName);
    DTOFlowExecution getFlowExecutionStatus(UUID flowSessionId);
    DTOFlowExecution getDTOFlowExecution(UUID flowId);
    LinkedList<FlowContinuationMapping> getAllContinuationMappingsWithSameSourceFlow(String currFlowName);

}
