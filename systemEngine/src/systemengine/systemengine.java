package systemengine;

import dto.*;
import exceptions.*;
import flow.api.FlowDefinition;
import flow.mapping.FlowContinuationMapping;
import javafx.collections.ObservableList;
import user.UserManager;

import javax.xml.bind.JAXBException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

public interface systemengine {
    void cratingFlowFromXml(String filePath) throws DuplicateFlowsNames, JAXBException, UnExistsStep, FileNotFoundException, OutputsWithSameName, MandatoryInputsIsntUserFriendly, UnExistsData, SourceStepBeforeTargetStep, TheSameDD,
            UnExistsOutput, FreeInputsWithSameNameAndDifferentType, InitialInputIsNotExist, UnExistsFlow, UnExistsDataInTargetFlow, FileNotExistsException, FileIsNotXmlTypeException;
    void cratingFlowFromXml(InputStream inputStream) throws DuplicateFlowsNames, JAXBException, UnExistsStep, FileNotFoundException, OutputsWithSameName, MandatoryInputsIsntUserFriendly, UnExistsData, SourceStepBeforeTargetStep, TheSameDD,
            UnExistsOutput, FreeInputsWithSameNameAndDifferentType, InitialInputIsNotExist, UnExistsFlow, UnExistsDataInTargetFlow, FileNotExistsException, FileIsNotXmlTypeException;
    void initRoles();
    DTOAllStepperFlows getAllFlows();
    DTOFlowsNames printFlowsName();
    List<FlowDefinition> getFlowDefinitionList();
    DTOFlowDefinition IntroduceTheChosenFlow(int flowNumber);
    boolean hasAllMandatoryInputs(int flowChoice, Map<String, Object> freeInputMap);
    DTOFlowExecution activateFlow(int flowChoice, DTOFreeInputsFromUser freeInputs);
    //DTOFlowExecution activateFlowByName(String flowName, DTOFreeInputsFromUser freeInputs);
    DTOFlowID activateFlowByName(String userName, String flowName, DTOFreeInputsFromUser freeInputs);
    DTOSingleFlowIOData getSpecificFreeInput(int flowChoice, int freeInputChoice);
    DTOFlowsExecutionList getFlowsExecutionList();
    List<DTOFlowExeInfo> getDTOFlowsExecutionList();
    DTOFlowAndStepStatisticData getStatisticData();
    void saveToFile(String path);
    void loadFromFile(String path);
    Boolean isCurrFlowExecutionDone(String currFlowName);
    DTOFlowExecution getFlowExecutionStatus(UUID flowSessionId);
    DTOFreeInputsByUserString printFreeInputsByUserString(int choice);

    DTOFlowExecution getFlowExecutionDetails(int flowExecutionChoice);
    DTOFlowExecution getDTOFlowExecutionById(UUID flowId);
    DTOFlowExecution getDTOFlowExecutionByName(String flowName);

    LinkedList<FlowContinuationMapping> getAllContinuationMappingsWithSameSourceFlow(String currFlowName);
    LinkedList<DTOContinuationMapping> getDTOAllContinuationMappingsWithSameSourceFlow(String currFlowName);
    List<Input> getFreeInputsFromCurrFlow (String flowName);
    Map<String , Object> continuationFlowExecution(String sourceFlow, String targetFlow);
    LinkedList<DTOInput> getDTOValuesListFromContinuationMap(String sourceFlowName, String targetFlowName);
    List<Input> getValuesListFromContinuationMap(String sourceFlowName, String targetFlowName);
    UserManager getUserMangerObject();
    DTORolesList getDTORolesList();
    void addNewRole(DTORole dtoRole);
    void updateFlowsInRole(DTORole dtoRole);
    DTORolesList getDTORolesListPerUser(String userName);
    DTOFlowsDefinitionInRoles getDtoFlowsDefinition(Boolean isManager, List<String> rolesNames);
    List<DTOStepUsageDeclaration> createDTOListStep(FlowDefinition flow);
    List<DTOFlowOutputs> createDTOListFlowOutputs(FlowDefinition flow);
    List<DTOFreeInputs> createDTOListFreeInputs(FlowDefinition flow);
    List<DTOFlowFreeInputs> getDTOFlowFreeInputs(String flowName);

    DTOAllFlowsNames getAllFlowsList();
    DTOFlowExeInfo getAllFlowExecutionData(UUID flowId);

    void updateUser(String userName, Set<String> checkedItems, Boolean isManager);
    DTOFlowExeInfo getAllFlowExecutionDataNyName(String flowName);
    Set<String> getUsersOfRoles(String roleName);

    List<DTOInput> getDTOFlowFreeInputsToSrevlet(String flowName);
    DTOFlowsDefinitionInRoles getFlowsToUserFromRole(List<String> rolesNames);
    DTOFlowsDefinitionInRoles  getFlowsToManager();
}
