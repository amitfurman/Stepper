package flow.api;

import datadefinition.api.DataDefinitions;
import flow.mapping.FlowCustomMapping;
import steps.api.DataDefinitionDeclaration;
import flow.mapping.FlowAutomaticMapping;
import flow.api.FlowIO.IO;
import flow.api.FlowIO.SingleFlowIOData;

import java.util.*;
import java.util.stream.Collectors;

public class FlowDefinitionImpl implements FlowDefinition {

    private final String name;
    private final String description;
    private final List<String> flowOutputs;
    private final List<StepUsageDeclaration> steps;
    private final Map<String, DataDefinitions> name2DataDefinition;
    //private final Map<String, Map<String, String>> MapIOName2Alias;

    private final Map<String, String> InputName2Alias;
    private final Map<String, String> OutputName2Alias;
    private final Map<String, String> MapAlias2StepName;
    private final List<SingleFlowIOData> IOlist;
    private final List<CustomMapping> customMapping;

    public FlowDefinitionImpl(String name, String description) {
        this.name = name;
        this.description = description;
        flowOutputs = new ArrayList<>();
        steps = new LinkedList<>();
        name2DataDefinition = new HashMap<>();
        IOlist = new ArrayList<>();
        // MapIOName2Alias = new HashMap<>();
        MapAlias2StepName = new HashMap<>();
        InputName2Alias = new HashMap<>();
        OutputName2Alias = new HashMap<>();
        customMapping = new ArrayList<>();
    }

    @Override
    public void addStepToFlow(StepUsageDeclaration stepUsageDeclaration) {
        steps.add(stepUsageDeclaration);
    }

    public void addFlowOutput(String outputName) {
        flowOutputs.add(outputName);
    }

    @Override
    public List<DataDefinitionDeclaration> getFlowFreeInputs() {
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public List<StepUsageDeclaration> getFlowSteps() {
        return steps;
    }

    @Override
    public List<String> getFlowFormalOutputs() {
        return flowOutputs;
    }

    @Override
    public void addToName2DDMap(String name, DataDefinitions DD) {
        name2DataDefinition.put(name, DD);
    }

 /*   @Override
    public void addToIOName2AliasMap(String stepName, String IOName, String alias) {
        Map<String,String> newElement = new HashMap<>();
        newElement.put(IOName,alias);
        MapIOName2Alias.put(stepName ,newElement);
    }*/

    @Override
    public void addToInputName2AliasMap(String stepName, String inputName, String alias) {
        String result = stepName + "." + inputName;
        InputName2Alias.put(result, alias);
    }

    @Override
    public void addToOutputName2AliasMap(String stepName, String outputName, String alias) {
        String result = stepName + "." + outputName;
        OutputName2Alias.put(result, alias);
    }


    @Override
    public void addToAlias2StepNameMap(String stepName, String alias) {
        MapAlias2StepName.put(stepName, alias);
    }

    @Override
    public DataDefinitions getDDFromMap(String InputName) {

        return name2DataDefinition.get(InputName);
    }

    /* @Override
    public String getIOAliasFromMap(String stepName ,String originalName) {
        return MapIOName2Alias.get(stepName).get(originalName);
     }*/
    @Override
    public String getInputAliasFromMap(String stepName, String originalInputName) {
        return InputName2Alias.get(stepName + "." + originalInputName);
    }

    @Override
    public String getOutputAliasFromMap(String stepName, String originalOutputName) {
        return OutputName2Alias.get(stepName + "." + originalOutputName);
    }

    @Override
    public Map<String, DataDefinitions> getName2DDMap() {
        return name2DataDefinition;
    }

    @Override
    public Map<String, String> getAlias2StepNameMap() {
        return MapAlias2StepName;
    }

    /* @Override
     public Map<String,Map<String, String>> getIOName2aliasMap() {
         return MapIOName2Alias;
     }
 */
    @Override
    public Map<String, String> getInputName2aliasMap() {
        return InputName2Alias;
    }

    @Override
    public Map<String, String> getOutputName2aliasMap() {
        return OutputName2Alias;
    }

    @Override
    public List<SingleFlowIOData> getIOlist() {
        return IOlist;
    }

    @Override
    public void addElementToIoList(SingleFlowIOData IOElement) {
        IOlist.add(IOElement);
    }

    @Override
    public boolean stepExist(String stepName) {
        boolean isPresent =
                getFlowSteps()
                        .stream()
                        .anyMatch(name -> name.getFinalStepName().equals(stepName));

//the warning is species to aliasing flow def - need to change
        if (!isPresent) {
            String warning = "The step" + stepName +"does not exists in the current flow.";
            return false;
        }
        return true;
    }

    @Override
    public boolean dataExist(String stepName, String dataName) {

        Map<String, String> inputs = InputName2Alias.keySet().stream()
                .filter(key -> key.startsWith(stepName))
                .collect(Collectors.toMap(key -> key, key -> InputName2Alias.get(key)));
        boolean isPresentInInput = inputs.values().stream().anyMatch(data -> data.equals(dataName));


        Map<String, String> outputs = OutputName2Alias.keySet().stream()
                .filter(key -> key.startsWith(stepName))
                .collect(Collectors.toMap(key -> key, key -> OutputName2Alias.get(key)));
        boolean isPresentInOutput = outputs.values().stream().anyMatch(data -> data.equals(dataName));

        if (!isPresentInInput && !isPresentInOutput) {
            String warning = "The data" + dataName + "does not exists in the current flow.";
            return false;
        }

        return true;
    }

    @Override
    public boolean isFlowOutputsValid(List<String> outputsNamesList) {

        boolean isPresent = true;
        for (String outputName : outputsNamesList) {
            isPresent =
                    getOutputName2aliasMap() ////
                            .values()
                            .stream()
                            .anyMatch(output -> output.equals(outputName));
        }
//the warning is species to aliasing flow def - need to change
        if (!isPresent) {
            return false;
        }
        return true;
    }

    @Override
    public void validateFlowStructure() {
        validateIfOutputsHaveSameName();
        FlowAutomaticMapping automaticMapping = new FlowAutomaticMapping(this);
        FlowCustomMapping customMapping = new FlowCustomMapping(this);
        flowOutputsIsNotExists();
        mandatoryInputsWithSameNameAndDifferentType();
        mandatoryInputsIsUserFriendly();

    }

    @Override
    public void validateIfOutputsHaveSameName() {
        boolean isPresent =
                flowOutputs
                        .stream()
                        .anyMatch(name -> Collections
                                .frequency(flowOutputs, name) > 1);

        if (isPresent) {
            String exception = "Invalid. There are 2 or more outputs with the same name.";
        }
    }

    @Override
    public void mandatoryInputsIsUserFriendly() {
        boolean isPresent =
                getIOlist()
                        .stream()
                        .filter(data -> data.getType().equals(IO.INPUT))
                        .filter(data -> data.getOptionalOutput().isEmpty())
                        .anyMatch(data -> !data.getDD().isUserFriendly());

        if (isPresent) {
            String exception = "Invalid. There are mandatory inputs that is not user friendly.";
        }
    }

    @Override
    public void flowOutputsIsNotExists() {
        for (String output : getFlowFormalOutputs()) {
            boolean isPresent = getIOlist()
                    .stream()
                    .anyMatch(data -> data.getFinalName().equals(output));

            if (!isPresent) {
                String exception = "Invalid. There is at least one flow output that is not exists.";
            }
        }
    }

    @Override
    public void mandatoryInputsWithSameNameAndDifferentType() {
        for (SingleFlowIOData currData : getIOlist()) {
            boolean isPresent =
                    getIOlist()
                            .stream()
                            .filter(data -> data.getType() == IO.INPUT)
                            .filter(data -> data.getOptionalInputs().isEmpty())
                            .filter(data -> data.getName().equals(currData.getName()))
                            .anyMatch(data -> !data.getType().equals(currData.getType()));

            if (isPresent) {
                String exception = "Invalid. There are mandatory inputs with the same name but different type.";
            }
        }
    }

    @Override
    public SingleFlowIOData getElementFromIOList(String stepName, String dataName) {
        for (SingleFlowIOData obj : IOlist) {
            if (obj.getStepName().equals(stepName) && obj.getFinalName().equals(dataName)) {
                return obj;
            }
        }
        return null;
    }

    @Override
    public boolean doesSourceStepBeforeTargetStep(String sourceStepName, String targetStepName){
        int sourceIndex = steps.indexOf(sourceStepName);
        int targetIndex = steps.indexOf(targetStepName);
        if (sourceIndex > targetIndex) {
            String log = "Warning! the target step is before the source step.";
            return false;
        }
        return true;
    }

    @Override
    public boolean isTheSameDD (String sourceDataName, String targetDataName) {
       if(!name2DataDefinition.get(sourceDataName).equals(name2DataDefinition.get(targetDataName))){
           String log = "Warning! the source data is not the same data definition as target data.";
           return false;
       }
       return true;
    }

    @Override
    public List<CustomMapping> getCustomMappingList(){
        return customMapping;
    }
    @Override
    public void addToCustomMapping(CustomMapping obj){
        customMapping.add(obj);
    }
}