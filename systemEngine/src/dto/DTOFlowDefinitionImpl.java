package dto;
import flow.api.CustomMapping;
import flow.api.FlowDefinition;
import flow.api.FlowIO.SingleFlowIOData;
import flow.api.StepUsageDeclaration;
import java.util.*;

public class DTOFlowDefinitionImpl implements DTOFlowDefinition{
    private final String name;
    private final String description;
    private final List<String> flowOutputs;
    private boolean isFlowReadOnly;
    private final List<DTOStepUsageDeclaration> steps;
    private final List<SingleFlowIOData> freeInputs;

    private final List<SingleFlowIOData> IOlist;
    private final List<CustomMapping> customMapping;

    public DTOFlowDefinitionImpl(FlowDefinition flow) {
        this.name = flow.getName();
        this.description = flow.getDescription();
        this.flowOutputs = new ArrayList<>(flow.getFlowFormalOutputs());
        this.isFlowReadOnly = flow.getFlowReadOnly();
        this.steps = new LinkedList<>();
        for (StepUsageDeclaration step: flow.getFlowSteps()) {
            this.steps.add(new DTOStepUsageDeclarationImpl(step));
        }
        this.IOlist = new ArrayList<SingleFlowIOData>(flow.getIOlist());
        this.customMapping = new ArrayList<CustomMapping>(flow.getCustomMappingList());

        for (SingleFlowIOData input: flow.getMandatoryInputsList()) {
            this.mandatoryInputs.add(new DTOSingleFlowIODataImpl(input));
        }
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
    public List<String> getFlowFormalOutputs() {
        return flowOutputs;
    }

    @Override
    public boolean getFlowReadOnly() {
        return this.isFlowReadOnly;
    }

    @Override
    public List<DTOStepUsageDeclaration> getFlowStepsData() {
        return steps;
    }

/*
    @Override
    public List<DataDefinitionDeclaration> getFlowFreeInputs() {
        return new ArrayList<>();
    }




    @Override
    public List<StepUsageDeclaration> getFlowSteps() {
        return steps;
    }
*/


 /*   @Override
    public void addToIOName2AliasMap(String stepName, String IOName, String alias) {
        Map<String,String> newElement = new HashMap<>();
        newElement.put(IOName,alias);
        MapIOName2Alias.put(stepName ,newElement);
    }*/

    /*
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
    public List<SingleFlowIOData> getIOlist() {
        return IOlist;
    }*/

    /*
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
    public void validateFlowStructure() {
        validateIfOutputsHaveSameName();
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
    public SingleFlowIOData getElementFromIOList(String stepName, String dataName) {
        for (SingleFlowIOData obj : IOlist) {
            if (obj.getStepName().equals(stepName) && obj.getFinalName().equals(dataName)) {
                return obj;
            }
        }
        return null;
    }


    @Override
    public List<CustomMapping> getCustomMappingList(){
        return customMapping;
    }


    @Override
    public List<SingleFlowIOData> getMandatoryInputsList(){
        return mandatoryInputs;*/
}
