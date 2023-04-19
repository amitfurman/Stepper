package flow.impl;

import Steps.StepDefinitionRegistry;
import Steps.api.DataDefinitionDeclaration;
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
        for (int i=0 ; i< numberOfFlows; i++) {
            STFlow currFlow = stepper.getSTFlows().getSTFlow().get(i);
            FlowDefinition flow = new FlowDefinitionImpl(currFlow.getName(), currFlow.getSTFlowDescription());

            //add output to flow
            String output = currFlow.getSTFlowOutput();
            List<String> names = Arrays.stream(output.split(","))
                    .map(String::trim)
                    .collect(Collectors.toList());
            flow.getFlowFormalOutputs().addAll(names);

            //add steps to flow (we need to change and add more ctor)
            for(StepUsageDeclaration step : flow.getFlowSteps() ) {//should be currFlow?
                StepDefinitionRegistry myEnum = StepDefinitionRegistry.valueOf(step.getFinalStepName().toUpperCase().replace(" ", "_"));
                flow.addStepToFlow(new StepUsageDeclarationImpl(myEnum.getStepDefinition()));

                //add all inputs
                List<DataDefinitionDeclaration> flowFreeInputs = new ArrayList<>();
                for(DataDefinitionDeclaration input :  step.getStepDefinition().inputs()) {
                    flowFreeInputs.add(input);
                }
            }

            //delete the inputs in custom mappings from flowFreeInputs list
            for(STCustomMapping customMapping : currFlow.getSTCustomMappings().getSTCustomMapping() ) {

            }


            //add flow level aliasing to flow
            flow.getFlowSteps();



            flow.validateFlowStructure();
        }


     //list of flows

/*
        FlowExecutor fLowExecutor = new FlowExecutor();

        FlowExecution flow2Execution1 = new FlowExecution("1", flow2);
        // collect all user inputs and store them on the flow execution object
        fLowExecutor.executeFlow(flow2Execution1);

        FlowExecution flow2Execution2 = new FlowExecution("2", flow2);
        // collect all user inputs and store them on the flow execution object
        fLowExecutor.executeFlow(flow2Execution1);

 */
    }
}
