package flow.mapping;

import steps.api.DataDefinitionDeclaration;
import flow.api.FlowDefinition;
import flow.api.FlowIO.IO;
import flow.api.FlowIO.SingleFlowIOData;
import flow.api.StepUsageDeclaration;
import java.util.stream.Collectors;

public class FlowAutomaticMapping {
    public FlowAutomaticMapping(FlowDefinition flow){

        for (StepUsageDeclaration step : flow.getFlowSteps()) {
            for (DataDefinitionDeclaration input : step.getStepDefinition().inputs()) {
                flow.addElementToIoList(new SingleFlowIOData(IO.INPUT, input.getName(),
                        flow.getInputAliasFromMap(step.getFinalStepName(), input.getName()),flow.getDDFromMap(step.getFinalStepName(), input.getName()),input.userString(), input.necessity(), step));
            }
            for (DataDefinitionDeclaration output : step.getStepDefinition().outputs()) {
                flow.addElementToIoList(new SingleFlowIOData(IO.OUTPUT, output.getName(),
                        flow.getOutputAliasFromMap(step.getFinalStepName() ,output.getName()),flow.getDDFromMap(step.getFinalStepName(),output.getName()),output.userString(), output.necessity(), step));
            }
        }

        for(SingleFlowIOData data : flow.getIOlist())
        {
            if(data.getType() == IO.OUTPUT) {
                //nodes that current node can be input for them.
                data.setOptionalInputs(flow.getIOlist().stream().
                        skip(flow.getIOlist().indexOf(data)).
                        filter(io -> io.getType() != data.getType()).
                        filter(io -> io.getDD().equals(data.getDD())).//filter the outputs to outputs with the DD
                        filter(io -> io.getFinalName().equals(data.getFinalName())).//filter the outputs to outputs with the same name
                        collect(Collectors.toList()));

                for(SingleFlowIOData storeData : data.getOptionalInputs()) {
                    storeData.addToOptionalOutput(data);//nodes that can be input to current node.
                }
            }
        }
    }
}
