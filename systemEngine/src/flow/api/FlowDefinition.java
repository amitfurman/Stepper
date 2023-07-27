package flow.api;

import datadefinition.api.DataDefinitions;
import exceptions.*;
import flow.api.FlowIO.SingleFlowIOData;
import statistic.StatisticData;

import java.util.List;
import java.util.Map;

public interface FlowDefinition {
    String getName();
    String getDescription();
    List<StepUsageDeclaration> getFlowSteps();
    List<String> getFlowFormalOutputs();
    List<SingleFlowIOData> getFlowFreeInputs();
    List<SingleFlowIOData> getIOlist();
    DataDefinitions getDDFromMap(String stepName, String InputName);
    String getInputAliasFromMap(String stepName ,String originalInputName);
    String getOutputAliasFromMap(String stepName ,String originalOutputName);
    Map<String, DataDefinitions> getStepAndIOName2DDMap();
    Map<String, DataDefinitions> getName2DDMap();//newwwwwwwwwwwwww
    Map<String, String> getName2AliasMap();
    Map<String, String> getInputName2aliasMap();
    Map<String, String> getOutputName2aliasMap();
    Map<String,String> getAlias2StepNameMap();
    SingleFlowIOData getElementFromIOList(String stepName, String dataName );
    List<CustomMapping> getCustomMappingList();
    List<SingleFlowIOData> getMandatoryInputsList();
    Map<String, Object> getInitialInputMap();
    boolean getFlowReadOnly();
    void addStepToFlow(StepUsageDeclaration stepUsageDeclaration);
    void  addToStepAndIOName2DDMap(String stepName,String inputName ,DataDefinitions DD);
    void addToName2DDMap(String inputName ,DataDefinitions DD);//newwwwwwwwwwww
    void addToName2AliasMap(String inputName ,String alias);
    void addToInputName2AliasMap(String stepName, String inputName, String alias);
    void addToOutputName2AliasMap(String stepName, String outputName, String alias);
    void addElementToIoList(SingleFlowIOData IOElement);
    void addToAlias2StepNameMap(String stepName, String alias);
    void addToCustomMapping(CustomMapping obj);
    void addToMandatoryInputsList(SingleFlowIOData mandatoryInput);
    void addToInitialInputMap(String inputName, Object value);
    boolean stepExist(String stepName);
    boolean dataExist(String stepName, String dataName);
    void validateIfOutputsHaveSameName() throws OutputsWithSameName;
    void mandatoryInputsIsUserFriendly() throws MandatoryInputsIsntUserFriendly;
    void flowOutputsIsNotExists() throws UnExistsOutput;
    void freeInputsWithSameNameAndDifferentType()throws FreeInputsWithSameNameAndDifferentType;
    boolean doesSourceStepBeforeTargetStep(String sourceStepName, String targetStepName);
    boolean isTheSameDD (String sourceStepName, String sourceDataName,String targetStepName, String targetDataName);
    void initMandatoryInputsList();
    void setFlowReadOnly();
    boolean checkIfFlowIsReadOnly();
    // boolean checkIfInitialInputIsFreeInput(String inputName) throws InitialInputIsNotFreeInput;
    List<String> getListOfStepsWithCurrInput(String inputName);
    void addFlowOutput(String outputName);
    void removeOptionalOutputsFromInitialInputs();

    void setNumOfContinuation(int size);
    int getNumOfContinuation();
}