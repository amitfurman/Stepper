package flow.api;

import DataDefinition.api.DataDefinitions;
import Steps.api.DataDefinitionDeclaration;
import flow.api.FlowIO.SingleFlowIOData;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface FlowDefinition {
    String getName();
    String getDescription();
    List<StepUsageDeclaration> getFlowSteps();
    List<String> getFlowFormalOutputs();
    List<DataDefinitionDeclaration> getFlowFreeInputs();
    List<SingleFlowIOData> getIOlist();
    DataDefinitions getDDFromMap(String InputName);
    String getIOAliasFromMap(String stepName ,String originalName);

    Map<String, DataDefinitions> getName2DDMap();
    Map<String,Map<String, String>> getIOName2aliasMap();
    Map<String,String> getStepName2aliasMap();

    void addStepToFlow(StepUsageDeclaration stepUsageDeclaration);
    void addToName2DDMap(String Name, DataDefinitions DD);
    void addToIOName2AliasMap(String stepName, String IOName, String alias);
    void addElementToIoList(SingleFlowIOData IOElement);
    void addToStepName2AliasMap(String stepName, String alias);

    boolean stepExist(String stepName);
    boolean dataExist(String stepName, String dataName);
    boolean isFlowOutputsValid(List<String> outputsNamesList);
    void validateFlowStructure();
    void validateIfOutputsHaveSameName();
    void mandatoryInputsIsNotUserFriendly();
    void flowOutputsIsNotExists();
    void mandatoryInputsWithSameNameAndDifferentType();
    }