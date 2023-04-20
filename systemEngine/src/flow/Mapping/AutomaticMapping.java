package flow.Mapping;

import Steps.api.DataDefinitionDeclaration;
import flow.api.FlowDefinition;
import flow.api.FlowIO.IO;
import flow.api.FlowIO.SingleFlowIOData;
import flow.api.StepUsageDeclaration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AutomaticMapping {
    public AutomaticMapping(FlowDefinition flow){

        for (StepUsageDeclaration step : flow.getFlowSteps()) {
            for (DataDefinitionDeclaration input : step.getStepDefinition().inputs()) {
                List<SingleFlowIOData> optionalForInputs = new ArrayList<>();
                optionalForInputs =  flow.getFlowSteps().stream().skip(flow.getFlowSteps().indexOf(step)).collect(Collectors.toList());
                for (StepUsageDeclaration nextStep :flow.getFlowSteps()) {

                }
                flow.addElementToIoList(new SingleFlowIOData(IO.INPUT, input.getName(),
                        flow.getAliasFromMap(input.getName()),flow.getDDFromMap(input.getName()),input.userString(),
                        step.getStepDefinition()), );

            }
            for (DataDefinitionDeclaration output : step.getStepDefinition().outputs()) {


            }
        }

    }

}
