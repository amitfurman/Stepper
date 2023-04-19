package flow.api;

import Steps.api.DataDefinitionDeclaration;
import exceptions.OutputsWithSameName;
import jaxb.schema.generated.STFlow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FlowDefinitionImpl implements FlowDefinition {

    private final String name;
    private final String description;
    private final List<String> flowOutputs;
    private final List<StepUsageDeclaration> steps;

    public FlowDefinitionImpl(String name, String description) {
        this.name = name;
        this.description = description;
        flowOutputs = new ArrayList<>();
        steps = new ArrayList<>();
    }

    @Override
    public void addStepToFlow(StepUsageDeclaration stepUsageDeclaration){
        steps.add(stepUsageDeclaration);
    }


    public void addFlowOutput(String outputName) {
        flowOutputs.add(outputName);
    }

    @Override
    public void validateFlowStructure() {
        validateIfOutputsHaveSameName();
        MandatoryInputsIsNotUserFriendly();

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

    public void validateIfOutputsHaveSameName() {
        boolean isPresent =
                flowOutputs
                        .stream()
                        .anyMatch(name -> Collections
                                .frequency(flowOutputs, name) > 1);

        if(isPresent){
            String log = "Invalid. There are 2 or more outputs with the same name.";
        }
    }

    public void MandatoryInputsIsNotUserFriendly(){


    }

}