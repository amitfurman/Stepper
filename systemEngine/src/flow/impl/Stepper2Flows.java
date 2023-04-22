package flow.impl;

import datadefinition.api.DataDefinitions;
import flow.api.CustomMapping;
import steps.StepDefinitionRegistry;
import steps.api.DataDefinitionDeclaration;
import flow.api.FlowDefinition;
import flow.api.FlowDefinitionImpl;
import flow.api.StepUsageDeclarationImpl;
import flow.execution.runner.FlowExecutor;
import jaxb.schema.generated.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Stepper2Flows {

    public Stepper2Flows(STStepper stepper) {
        int numberOfFlows = stepper.getSTFlows().getSTFlow().size();
        LinkedList<FlowDefinition> allFlows = new LinkedList<>();
        FlowDefinition flow;
        //each flow
        for (int i = 0; i < numberOfFlows; i++) {
            STFlow currFlow = stepper.getSTFlows().getSTFlow().get(i);
            flow = new FlowDefinitionImpl(currFlow.getName(), currFlow.getSTFlowDescription());

            //each step
            //add steps to flow (we need to change and add more ctor)
            for (STStepInFlow step : currFlow.getSTStepsInFlow().getSTStepInFlow()) {
                StepDefinitionRegistry myEnum = StepDefinitionRegistry.valueOf(step.getName().toUpperCase().replace(" ", "_"));
                if (step.getAlias() != null && step.isContinueIfFailing()) {
                    flow.addStepToFlow(new StepUsageDeclarationImpl(myEnum.getStepDefinition(), step.isContinueIfFailing(), step.getAlias()));
                    flow.addToAlias2StepNameMap(step.getAlias(),step.getName());
                } else if (step.getAlias() != null) {
                    flow.addStepToFlow(new StepUsageDeclarationImpl(myEnum.getStepDefinition(), step.getAlias()));
                    flow.addToAlias2StepNameMap(step.getAlias(),step.getName());
                } else {
                    flow.addStepToFlow(new StepUsageDeclarationImpl(myEnum.getStepDefinition()));
                    flow.addToAlias2StepNameMap(step.getName(),step.getName());
                }

                List<DataDefinitionDeclaration> stepInputs = myEnum.getStepDefinition().inputs();
                for (DataDefinitionDeclaration input : stepInputs) {
                    flow.addToName2DDMap(input.getName(), input.dataDefinition());
                    if(step.getAlias()!=null){
                        flow.addToInputName2AliasMap(step.getAlias(),input.getName(), input.getName());
                    }
                    else{
                        flow.addToInputName2AliasMap(step.getName(),input.getName(), input.getName());
                    }
                }
                List<DataDefinitionDeclaration> stepOutputs = myEnum.getStepDefinition().outputs();
                for (DataDefinitionDeclaration output : stepOutputs) {
                    flow.addToName2DDMap(output.getName(), output.dataDefinition());
                    if(step.getAlias()!=null){
                        flow.addToOutputName2AliasMap(step.getAlias(),output.getName(), output.getName());}
                    else {
                        flow.addToOutputName2AliasMap(step.getName(),output.getName(), output.getName());}

                }

            }

            //FlowLevelAliasing
            for (STFlowLevelAlias flowLevelAlias : currFlow.getSTFlowLevelAliasing().getSTFlowLevelAlias()) {
                if(flow.stepExist(flowLevelAlias.getStep()) && flow.dataExist(flowLevelAlias.getStep(), flowLevelAlias.getSourceDataName())) {
                    DataDefinitions data = flow.getDDFromMap(flowLevelAlias.getSourceDataName());
                    flow.addToName2DDMap(flowLevelAlias.getAlias(), data);
                    if ((flow.getInputName2aliasMap().get(flowLevelAlias.getStep() + "." + flowLevelAlias.getSourceDataName())) != null) {
                        flow.addToInputName2AliasMap(flowLevelAlias.getStep(), flowLevelAlias.getSourceDataName(), flowLevelAlias.getAlias());
                    } else {
                        flow.addToOutputName2AliasMap(flowLevelAlias.getStep(), flowLevelAlias.getSourceDataName(), flowLevelAlias.getAlias());

                    }
                }
            }

            /////Custom Mapping
            for(STCustomMapping  customMapping : currFlow.getSTCustomMappings().getSTCustomMapping()){
                flow.addToCustomMapping(new CustomMapping(customMapping.getSourceStep(), customMapping.getSourceData(), customMapping.getTargetStep(), customMapping.getTargetData()));
            }

            //add output to flow
            String outputsName = currFlow.getSTFlowOutput();
            List<String> names = Arrays.stream(outputsName.split(","))
                    .map(String::trim)
                    .collect(Collectors.toList());
            flow.getFlowFormalOutputs().addAll(names);

            flow.validateFlowStructure();

                //I put Automatic&Custom Mapping in validateFlowStructure() FOR NOW because there are methods that need to be after mapping.
           /*  AutomaticMapping automaticMapping = new AutomaticMapping(flow);
            CustomMapping customMapping = new CustomMapping(flow);
           */

                FlowExecutor fLowExecutor = new FlowExecutor();

                 allFlows.addFirst(flow);
            }


/*

        FlowExecution flow2Execution1 = new FlowExecution("1", flow2);
        // collect all user inputs and store them on the flow execution object
        fLowExecutor.executeFlow(flow2Execution1);

        FlowExecution flow2Execution2 = new FlowExecution("2", flow2);
        // collect all user inputs and store them on the flow execution object
        fLowExecutor.executeFlow(flow2Execution1);

 */
    }
}