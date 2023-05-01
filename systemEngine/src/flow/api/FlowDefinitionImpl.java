package flow.api;

import datadefinition.api.DataDefinitions;
import flow.mapping.FlowCustomMapping;
import statistic.StatisticData;
import steps.api.DataDefinitionDeclaration;
import flow.mapping.FlowAutomaticMapping;
import flow.api.FlowIO.IO;
import flow.api.FlowIO.SingleFlowIOData;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class FlowDefinitionImpl implements FlowDefinition, Serializable {
    private final String name;
    private final String description;
    private final List<String> flowOutputs;
    private final List<StepUsageDeclaration> steps;
    private boolean isFlowReadOnly;
    private final Map<String, DataDefinitions> name2DataDefinition;
    private final Map<String, String> InputName2Alias;
    private final Map<String, String> OutputName2Alias;
    private final Map<String, String> MapAlias2StepName;
    private final List<SingleFlowIOData> IOlist;
    private final List<CustomMapping> customMapping;
    private final List<SingleFlowIOData> freeInputs;
    private final StatisticData flowStatisticData;

    public FlowDefinitionImpl(String name, String description) {
        this.name = name;
        this.description = description;
        this.flowOutputs = new ArrayList<>();
        this.isFlowReadOnly = true;
        this.steps = new LinkedList<>();
        this.name2DataDefinition = new HashMap<>();
        this.IOlist = new ArrayList<>();
        this.MapAlias2StepName = new HashMap<>();
        this.InputName2Alias = new HashMap<>();
        this.OutputName2Alias = new HashMap<>();
        this.customMapping = new ArrayList<>();
        this.freeInputs = new LinkedList<>();
        this.flowStatisticData = new StatisticData(name); ///maybe without name
    }

    @Override
    public String getName() { return name;}
    @Override
    public String getDescription() {
        return description;
    }
    @Override
    public List<String> getFlowFormalOutputs() {
        return flowOutputs;
    }
    @Override
    public List<StepUsageDeclaration> getFlowSteps() { return steps;}
    @Override
    public boolean getFlowReadOnly() {
        return this.isFlowReadOnly;
    }
    @Override
    public Map<String, DataDefinitions> getName2DDMap() { return name2DataDefinition;}
    @Override
    public String getInputAliasFromMap(String stepName, String originalInputName) {
        return InputName2Alias.get(stepName + "." + originalInputName);
    }
    @Override
    public String getOutputAliasFromMap(String stepName, String originalOutputName) {
        return OutputName2Alias.get(stepName + "." + originalOutputName);
    }
    @Override
    public DataDefinitions getDDFromMap(String InputName) { return name2DataDefinition.get(InputName);}
    @Override
    public Map<String, String> getAlias2StepNameMap() {
        return MapAlias2StepName;
    }
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
    public List<SingleFlowIOData> getFlowFreeInputs() {return this.freeInputs;}
    @Override
    public List<CustomMapping> getCustomMappingList(){
        return customMapping;
    }
    @Override
    public List<SingleFlowIOData> getMandatoryInputsList(){
        return freeInputs;
    }
    @Override
    public List<String> getListOfStepsWithCurrInput(String inputName){
        List<String> stepsWithCurrInput = new LinkedList<>();

        for(SingleFlowIOData input : freeInputs){
            if(input.getFinalName() == inputName){
                stepsWithCurrInput.add(input.getStepName());
            }
        }
        return stepsWithCurrInput;
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
    public void setFlowReadOnly() {
        this.isFlowReadOnly = checkIfFlowIsReadOnly();
    }
    @Override
    public void addStepToFlow(StepUsageDeclaration stepUsageDeclaration) {
        steps.add(stepUsageDeclaration);
    }
    @Override
     public void addFlowOutput(String outputName) {
        flowOutputs.add(outputName);
    }
    @Override
    public void addToName2DDMap(String name, DataDefinitions DD) {
        name2DataDefinition.put(name, DD);
    }
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
    public void addToCustomMapping(CustomMapping obj){
        customMapping.add(obj);
    }
    @Override
    public void addToMandatoryInputsList(SingleFlowIOData mandatoryInput){
        freeInputs.add(mandatoryInput);
    }
    @Override
    public boolean checkIfFlowIsReadOnly() {
        return steps.stream().anyMatch(step -> !(step.getStepDefinition().isReadonly()));
    }
    @Override
    public boolean stepExist(String stepName) {
        boolean isPresent =
                getFlowSteps()
                        .stream()
                        .anyMatch(name -> name.getFinalStepName().equals(stepName));

        if (!isPresent) {
            String warning = "The step" + stepName + "does not exists in the current flow.";
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
    public void freeInputsWithSameNameAndDifferentType() {
        for (SingleFlowIOData currData :  freeInputs) {
            boolean isPresent =
                    freeInputs
                            .stream()
                            .filter(data -> data.getFinalName().equals(currData.getFinalName()))
                            .anyMatch(data -> !data.getDD().equals(currData.getDD()));

            if (isPresent) {
                String exception = "Invalid. There are mandatory inputs with the same name but different type.";
            }
        }
    }

    @Override
    public void mandatoryInputsIsUserFriendly() {
        boolean isPresent =
                freeInputs
                        .stream()
                        .anyMatch(data -> !data.getDD().isUserFriendly());

        if (isPresent) {
            String exception = "Invalid. There are mandatory inputs that is not user friendly.";
        }
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
    public void initMandatoryInputsList(){
        freeInputs.addAll(getIOlist()
                .stream()
                .filter(data -> data.getType().equals(IO.INPUT))
                .filter(data -> data.getOptionalOutput().isEmpty()).collect(Collectors.toList()));
    }
}
