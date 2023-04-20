package flow.impl;

import DataDefinition.api.DataDefinitions;
import Steps.StepDefinitionRegistry;
import Steps.api.DataDefinitionDeclaration;
import flow.Mapping.AutomaticMapping;
import flow.api.FlowDefinition;
import flow.api.FlowDefinitionImpl;
import flow.api.StepUsageDeclaration;
import flow.api.StepUsageDeclarationImpl;
import flow.execution.FlowExecution;
import flow.execution.runner.FlowExecutor;
import jaxb.schema.generated.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Stepper2Flows {

    public Stepper2Flows(STStepper stepper)
    {
        int numberOfFlows = stepper.getSTFlows().getSTFlow().size();
        List<FlowDefinition> allFlows = new ArrayList<>();
        FlowDefinition flow;
      ////////each flow///////////
        for (int i=0 ; i< numberOfFlows; i++) {
            STFlow currFlow = stepper.getSTFlows().getSTFlow().get(i);
            flow = new FlowDefinitionImpl(currFlow.getName(), currFlow.getSTFlowDescription());

            //add output to flow
            String outputsName = currFlow.getSTFlowOutput();
            List<String> names = Arrays.stream(outputsName.split(","))
                    .map(String::trim)
                    .collect(Collectors.toList());
            flow.getFlowFormalOutputs().addAll(names);

            /////////each step///////////
            //add steps to flow (we need to change and add more ctor)
            for(STStepInFlow step :  currFlow.getSTStepsInFlow().getSTStepInFlow() ) {
                StepDefinitionRegistry myEnum = StepDefinitionRegistry.valueOf(step.getName().toUpperCase().replace(" ", "_"));
                if(step.getAlias() != null && step.isContinueIfFailing()) {
                    flow.addStepToFlow(new StepUsageDeclarationImpl(myEnum.getStepDefinition(),step.isContinueIfFailing() ,step.getAlias()));
                }
                else if(step.getAlias() != null){
                    flow.addStepToFlow(new StepUsageDeclarationImpl(myEnum.getStepDefinition(),step.getAlias()));
                }
                else{
                    flow.addStepToFlow(new StepUsageDeclarationImpl(myEnum.getStepDefinition()));
                }

                List<DataDefinitionDeclaration> stepInputs = myEnum.getStepDefinition().inputs();
                for (DataDefinitionDeclaration input: stepInputs) {

                    flow.addToName2DDMap(input.getName(),input.dataDefinition());
                    flow.addToName2AliasMap(input.getName(), input.getName());
                }
                List<DataDefinitionDeclaration> stepOutputs = myEnum.getStepDefinition().outputs();
                for (DataDefinitionDeclaration output: stepOutputs) {
                    flow.addToName2DDMap(output.getName(),output.dataDefinition());
                    flow.addToName2AliasMap(output.getName(),output.getName());
                }
            }
            ////////FlowLevelAliasing/////////
           for( STFlowLevelAlias flowLevelAlias: currFlow.getSTFlowLevelAliasing().getSTFlowLevelAlias()){
               DataDefinitions data = flow.getDDFromMap(flowLevelAlias.getSourceDataName());
               flow.addToName2DDMap(flowLevelAlias.getAlias(),data);
               flow.addToName2AliasMap(flowLevelAlias.getSourceDataName(),flowLevelAlias.getAlias());
           }

            flow.validateFlowStructure();

            AutomaticMapping automaticMapping = new AutomaticMapping(flow);

            FlowExecutor fLowExecutor = new FlowExecutor();

            //add flow to flows list
            allFlows.add(flow);
        }


     //list of flows

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
