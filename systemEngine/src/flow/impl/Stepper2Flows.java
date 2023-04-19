package flow.impl;

import Steps.StepDefinitionRegistry;
import flow.api.FlowDefinition;
import flow.api.FlowDefinitionImpl;
import flow.api.StepUsageDeclaration;
import flow.api.StepUsageDeclarationImpl;
import flow.execution.FlowExecution;
import flow.execution.runner.FlowExecutor;
import jaxb.schema.generated.STFlow;
import jaxb.schema.generated.STFlows;
import jaxb.schema.generated.STStepper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Stepper2Flows {

    public Stepper2Flows(STStepper stepper)
    {
        int numberOfFlows = stepper.getSTFlows().getSTFlow().size();
        for (int i=0 ; i< numberOfFlows; i++) {
            STFlow flow = stepper.getSTFlows().getSTFlow().get(i);
            FlowDefinition flow1 = new FlowDefinitionImpl(flow.getName(), flow.getSTFlowDescription());
            for(StepUsageDeclaration step : flow1.getFlowSteps() ) {
                StepDefinitionRegistry myEnum = StepDefinitionRegistry.valueOf(step.getFinalStepName().toUpperCase().replace(" ", "_"));
                flow1.addStepToFlow(new StepUsageDeclarationImpl(myEnum.getStepDefinition()));
            }
            String output = flow.getSTFlowOutput();
            List<String> names = Arrays.stream(output.split(","))
                    .map(String::trim)
                    .collect(Collectors.toList());
            flow1.getFlowFormalOutputs().addAll(names);
            flow1.validateFlowStructure();
        }


            //list of flows


        FlowExecutor fLowExecutor = new FlowExecutor();

        FlowExecution flow2Execution1 = new FlowExecution("1", flow2);
        // collect all user inputs and store them on the flow execution object
        fLowExecutor.executeFlow(flow2Execution1);

        FlowExecution flow2Execution2 = new FlowExecution("2", flow2);
        // collect all user inputs and store them on the flow execution object
        fLowExecutor.executeFlow(flow2Execution1);
    }
}
