package flow.api;

import datadefinition.api.DataDefinitions;
import exceptions.MandatoryInputsIsntUserFriendly;
import exceptions.OutputsWithSameName;
import flow.api.FlowIO.SingleFlowIOData;

import java.util.List;
import java.util.Map;

public interface FlowDefinition {
    String getName();
    String getDescription();
    List<StepUsageDeclaration> getFlowSteps();
    List<String> getFlowFormalOutputs();
    List<SingleFlowIOData> getFlowFreeInputs();
    List<SingleFlowIOData> getIOlist();
    DataDefinitions getDDFromMap(String InputName);
    String getInputAliasFromMap(String stepName ,String originalInputName);
    String getOutputAliasFromMap(String stepName ,String originalOutputName);
    Map<String, DataDefinitions> getName2DDMap();
    Map<String, String> getInputName2aliasMap();
    Map<String, String> getOutputName2aliasMap();
    Map<String,String> getAlias2StepNameMap();
    SingleFlowIOData getElementFromIOList(String stepName, String dataName );
    List<CustomMapping> getCustomMappingList();
    List<SingleFlowIOData> getMandatoryInputsList();
    boolean getFlowReadOnly();
    void addStepToFlow(StepUsageDeclaration stepUsageDeclaration);
    void addToName2DDMap(String Name, DataDefinitions DD);
    void addToInputName2AliasMap(String stepName, String inputName, String alias);
    void addToOutputName2AliasMap(String stepName, String outputName, String alias);
    void addElementToIoList(SingleFlowIOData IOElement);
    void addToAlias2StepNameMap(String stepName, String alias);
    void addToCustomMapping(CustomMapping obj);
    void addToMandatoryInputsList(SingleFlowIOData mandatoryInput);
    boolean stepExist(String stepName);
    boolean dataExist(String stepName, String dataName);
    void validateIfOutputsHaveSameName() throws OutputsWithSameName;
    void mandatoryInputsIsUserFriendly() throws MandatoryInputsIsntUserFriendly;
    void flowOutputsIsNotExists();
    void freeInputsWithSameNameAndDifferentType();
    boolean doesSourceStepBeforeTargetStep(String sourceStepName, String targetStepName);
    boolean isTheSameDD (String sourceName, String targetStepName);
    void initMandatoryInputsList();
    void setFlowReadOnly();
    boolean checkIfFlowIsReadOnly();
    List<String> getListOfStepsWithCurrInput(String inputName);
    void addFlowOutput(String outputName);
    }