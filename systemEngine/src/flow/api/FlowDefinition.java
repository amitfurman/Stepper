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
    DataDefinitions getDDFromMap(String InputName);
    Map<String, DataDefinitions> getName2DDMap();
    Map<String, String> getName2aliasMap();
    List<SingleFlowIOData> getIOlist();

    String getAliasFromMap(String originalName);
    void addStepToFlow(StepUsageDeclaration stepUsageDeclaration);
    void addToName2DDMap(String Name, DataDefinitions DD);
    void addToName2AliasMap(String name, String alias);
    void addElementToIoList(SingleFlowIOData IOElement);
    boolean stepExist(String stepName);
    boolean dataExist(String stepName, String dataName);
    boolean isFlowOutputsValid(List<String> outputsNamesList);
    void validateFlowStructure();
    void validateIfOutputsHaveSameName();
    void mandatoryInputsIsNotUserFriendly();
    void flowOutputsIsNotExists();
    void mandatoryInputsWithSameNameAndDifferentType();
    }