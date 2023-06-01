package dto;

import flow.api.FlowDefinition;
import flow.api.FlowIO.SingleFlowIOData;
import flow.api.StepUsageDeclaration;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DTOFlowDefinition {
    private final String name;
    private final String description;
    private boolean isFlowReadOnly;
    private final List<String> flowOutputs;
    private final List<DTOStepUsageDeclaration> steps;
    private final List<DTOSingleFlowIOData> IOlist;
    private final List<DTOSingleFlowIOData> freeInputs;

    public DTOFlowDefinition(FlowDefinition flow) {
        this.name = flow.getName();

        this.description = flow.getDescription();
        this.isFlowReadOnly = flow.getFlowReadOnly();
        this.flowOutputs = new ArrayList<>(flow.getFlowFormalOutputs());
        this.steps = new LinkedList<>();
        for (StepUsageDeclaration step: flow.getFlowSteps()) {
            this.steps.add(new DTOStepUsageDeclaration(step));
        }
        this.IOlist = new ArrayList<>();
        for (SingleFlowIOData input: flow.getIOlist()) {
            this.IOlist.add(new DTOSingleFlowIOData(input));
        }
        this.freeInputs = new LinkedList<>();
        for (SingleFlowIOData input: flow.getMandatoryInputsList()) {
            this.freeInputs.add(new DTOSingleFlowIOData(input));
        }
    }

    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public List<String> getFlowFormalOutputs() {
        return flowOutputs;
    }
    public boolean getFlowReadOnly() {
        return this.isFlowReadOnly;
    }
    public List<DTOStepUsageDeclaration> getFlowStepsData() {
        return steps;
    }
    public List<DTOSingleFlowIOData> getIOlist() {
        return IOlist;
    }
    public List<String> getListOfStepsWithCurrInput(String inputName){

        List<String> stepsWithCurrInput = new LinkedList<>();

        for(DTOSingleFlowIOData input : freeInputs){
            if(input.getFinalName().equals(inputName)){
                stepsWithCurrInput.add(input.getStepName());
            }
        }
        return stepsWithCurrInput;
    }
    public List<DTOSingleFlowIOData> getFlowFreeInputs() {
        return this.freeInputs;
    }

    public int getNumberOfSteps() {
        return this.steps.size();
    }

    public int getNumberOfFreeInputs() {
        return this.freeInputs.size();
    }
}

