package flow.impl;

import datadefinition.api.DataDefinitions;
import exceptions.*;
import flow.api.*;
import flow.mapping.FlowAutomaticMapping;
import flow.mapping.FlowContinuationMapping;
import flow.mapping.FlowCustomMapping;
import steps.StepDefinitionRegistry;
import steps.api.DataDefinitionDeclaration;
import jaxb.schema.generated.*;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Stepper2Flows {
    private LinkedList<FlowDefinition> allFlows;
    private int numberOfThreads;
    private LinkedList<FlowContinuationMapping> allContinuationMappings;

    public Stepper2Flows(STStepper stepper) throws OutputsWithSameName, MandatoryInputsIsntUserFriendly, UnExistsStep, UnExistsData, SourceStepBeforeTargetStep, TheSameDD, UnExistsOutput, FreeInputsWithSameNameAndDifferentType,InitialInputIsNotExist, UnExistsFlow, UnExistsDataInTargetFlow {
         this.numberOfThreads = stepper.getSTThreadPool();

        allFlows = new LinkedList<>();
        allContinuationMappings = new LinkedList<>();
        int numberOfFlows = stepper.getSTFlows().getSTFlow().size();
        FlowDefinition flow;
        //each flow
        for (int i = 0; i < numberOfFlows; i++) {
            STFlow currFlow = stepper.getSTFlows().getSTFlow().get(i);
            flow = new FlowDefinitionImpl(currFlow.getName(), currFlow.getSTFlowDescription());

            //add steps to flow
            for (STStepInFlow step : currFlow.getSTStepsInFlow().getSTStepInFlow()) {;
                StepDefinitionRegistry myEnum = StepDefinitionRegistry.valueOf(step.getName().toUpperCase().replace(" ", "_"));
                if (step.getAlias() != null && step.isContinueIfFailing()!=null) {
                    flow.addStepToFlow(new StepUsageDeclarationImpl(myEnum.getStepDefinition(), step.isContinueIfFailing(), step.getAlias()));
                    flow.addToAlias2StepNameMap(step.getAlias(), step.getName());
                } else if (step.getAlias() != null) {
                    flow.addStepToFlow(new StepUsageDeclarationImpl(myEnum.getStepDefinition(), step.getAlias()));
                    flow.addToAlias2StepNameMap(step.getAlias(), step.getName());
                } else {
                    flow.addStepToFlow(new StepUsageDeclarationImpl(myEnum.getStepDefinition()));
                    flow.addToAlias2StepNameMap(step.getName(), step.getName());
                }

                List<DataDefinitionDeclaration> stepInputs = myEnum.getStepDefinition().inputs();
                for (DataDefinitionDeclaration input : stepInputs) {
                    flow.addToName2DDMap(input.getName(), input.dataDefinition());
                    flow.addToName2AliasMap(input.getName(), input.getName());
                    if (step.getAlias() != null) {
                        flow.addToStepAndIOName2DDMap(step.getAlias(), input.getName(), input.dataDefinition());
                        flow.addToInputName2AliasMap(step.getAlias(), input.getName(), input.getName());
                    } else {
                        flow.addToStepAndIOName2DDMap(step.getName(), input.getName(), input.dataDefinition());
                        flow.addToInputName2AliasMap(step.getName(), input.getName(), input.getName());
                    }
                }
                List<DataDefinitionDeclaration> stepOutputs = myEnum.getStepDefinition().outputs();
                for (DataDefinitionDeclaration output : stepOutputs) {
                    flow.addToName2DDMap(output.getName(), output.dataDefinition());
                    flow.addToName2AliasMap(output.getName(), output.getName());
                    if (step.getAlias() != null) {
                        flow.addToStepAndIOName2DDMap(step.getAlias(),output.getName(), output.dataDefinition());
                        flow.addToOutputName2AliasMap(step.getAlias(), output.getName(), output.getName());
                    } else {
                        flow.addToStepAndIOName2DDMap(step.getName(),output.getName(), output.dataDefinition());
                        flow.addToOutputName2AliasMap(step.getName(), output.getName(), output.getName());
                    }
                }
            }

            //check if flow is read only
            flow.setFlowReadOnly();

            //FlowLevelAliasing
            if(currFlow.getSTFlowLevelAliasing()!=null) {
                for (STFlowLevelAlias flowLevelAlias : currFlow.getSTFlowLevelAliasing().getSTFlowLevelAlias()) {
                    if (!flow.stepExist(flowLevelAlias.getStep())){
                        throw new UnExistsStep();
                    }
                    else if(!flow.dataExist(flowLevelAlias.getStep(), flowLevelAlias.getSourceDataName())){
                        throw new UnExistsData();
                    }else{
                        DataDefinitions data = flow.getDDFromMap(flowLevelAlias.getStep(),flowLevelAlias.getSourceDataName());
                        flow.addToStepAndIOName2DDMap(flowLevelAlias.getStep(),flowLevelAlias.getAlias(), data);
                        flow.addToName2DDMap(flowLevelAlias.getAlias(), data);
                        flow.addToName2AliasMap(flowLevelAlias.getSourceDataName() , flowLevelAlias.getAlias());
                        if ((flow.getInputName2aliasMap().get(flowLevelAlias.getStep() + "." + flowLevelAlias.getSourceDataName())) != null) {
                            flow.addToInputName2AliasMap(flowLevelAlias.getStep(), flowLevelAlias.getSourceDataName(), flowLevelAlias.getAlias());
                        } else {
                            flow.addToOutputName2AliasMap(flowLevelAlias.getStep(), flowLevelAlias.getSourceDataName(), flowLevelAlias.getAlias());
                        }
                    }
                }
            }

            //add output to flow
            String outputsName = currFlow.getSTFlowOutput();
            List<String> names = Arrays.stream(outputsName.split(","))
                    .map(String::trim)
                    .collect(Collectors.toList());
            flow.getFlowFormalOutputs().addAll(names);

            //Custom Mapping
            if(currFlow.getSTCustomMappings() != null) {
                for (STCustomMapping customMapping : currFlow.getSTCustomMappings().getSTCustomMapping()) {
                    flow.addToCustomMapping(new CustomMapping(customMapping.getSourceStep(), customMapping.getSourceData(), customMapping.getTargetStep(), customMapping.getTargetData()));
                }
            }

            FlowAutomaticMapping automaticMapping = new FlowAutomaticMapping(flow);
            FlowCustomMapping customMapping = new FlowCustomMapping(flow);

            //continuation
            if(currFlow.getSTContinuations() != null){
                FlowContinuationMapping flowContinuation;
                for(STContinuation continuation:  currFlow.getSTContinuations().getSTContinuation()){
                    flowContinuation = new FlowContinuationMapping(currFlow.getName(), continuation.getTargetFlow());
                    for (STContinuationMapping continuationMapping:continuation.getSTContinuationMapping()) {
                        flowContinuation.addToSource2targetDataMapping(continuationMapping.getSourceData(), continuationMapping.getTargetData());
                    }
                    allContinuationMappings.add(flowContinuation);
                }
            }

            //initial input
/*            if(currFlow.getSTInitialInputValues() != null) {
                for(STInitialInputValue initValue : currFlow.getSTInitialInputValues().getSTInitialInputValue()) {
                    if(!flow.getIOlist().stream().anyMatch(io -> io.getFinalName().equals(initValue.getInputName())))
                        throw new InitialInputIsNotExist();
                    flow.addToInitialInputMap(initValue.getInputName(), initValue.getInitialValue());
                }
                flow.removeOptionalOutputsFromInitialInputs();
            }*/

            if(currFlow.getSTInitialInputValues() != null) {
                for(STInitialInputValue initValue : currFlow.getSTInitialInputValues().getSTInitialInputValue()) {
                    if(!flow.getIOlist().stream().anyMatch(io -> io.getFinalName().equals(initValue.getInputName())))
                        throw new InitialInputIsNotExist();

                    List<String> stepsWithInitialInput = new ArrayList<>();
                    for (StepUsageDeclaration step : flow.getFlowSteps()) {
                        if(step.getStepDefinition().inputs().stream().anyMatch(input->input.getName().equals(initValue.getInputName()))){
                            stepsWithInitialInput.add(step.getFinalStepName());
                        }
                    }
                    for (String stepName : stepsWithInitialInput) {
                        flow.addToInitialInputMap(stepName + "." + initValue.getInputName(), initValue.getInitialValue());
                    }
                }
                flow.removeOptionalOutputsFromInitialInputs();
            }

            flow.validateIfOutputsHaveSameName();
            flow.flowOutputsIsNotExists();
            flow.initMandatoryInputsList();
            flow.freeInputsWithSameNameAndDifferentType();
            flow.mandatoryInputsIsUserFriendly();

            allFlows.add(flow);
        }

        //check if all continuations are valid
        for (FlowContinuationMapping flowContinuationMapping: allContinuationMappings) {
            allFlows.stream().filter(flowDefinition -> flowDefinition.getName().equals(flowContinuationMapping.getTargetFlow())).findFirst().orElseThrow(() -> new UnExistsFlow());
        }

        for (FlowContinuationMapping flowContinuationMapping: allContinuationMappings) {

            FlowDefinition sourceFlow = allFlows.stream().filter(flowDefinition -> flowDefinition.getName().equals(flowContinuationMapping.getSourceFlow())).findFirst().get();
            FlowDefinition targetFlow = allFlows.stream().filter(flowDefinition -> flowDefinition.getName().equals(flowContinuationMapping.getTargetFlow())).findFirst().get();

            for (Map.Entry<String, String> entry : flowContinuationMapping.getSource2targetDataMapping().entrySet()) {
                String sourceDataName = entry.getKey();
                if (!sourceFlow.getIOlist().stream().anyMatch(io -> io.getFinalName().equals(sourceDataName)))
                    throw new UnExistsData();

                String targetDataName = entry.getValue();
                if (!targetFlow.getIOlist().stream().anyMatch(io -> io.getFinalName().equals(targetDataName)))
                    throw new UnExistsDataInTargetFlow();

                DataDefinitions DDOfSourceData = sourceFlow.getIOlist().stream().filter(io -> io.getFinalName().equals(sourceDataName)).findFirst().get().getDD();
                DataDefinitions DDOfTargetData = targetFlow.getIOlist().stream().filter(io -> io.getFinalName().equals(targetDataName)).findFirst().get().getDD();

                if (!DDOfSourceData.getType().equals(DDOfTargetData.getType()))
                    throw new TheSameDD();
            }

        }

    }

    public LinkedList<FlowDefinition> getAllFlows() {
        return allFlows;
    }

    public LinkedList<FlowContinuationMapping> getAllContinuationMappings() {
        return allContinuationMappings;
    }

    public int getNumberOfThreads() {
        return numberOfThreads;
    }


}