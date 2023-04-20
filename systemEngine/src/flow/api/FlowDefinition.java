package flow.api;

import DataDefinition.api.DataDefinitions;
import Steps.api.DataDefinitionDeclaration;
import flow.api.FlowIO.SingleFlowIOData;

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

    void validateFlowStructure();
    void addStepToFlow(StepUsageDeclaration stepUsageDeclaration);
    void addToName2DDMap(String Name, DataDefinitions DD);
    void addToName2AliasMap(String Name, String alias);
    void addElementToIoList(SingleFlowIOData IOElement);


    }