package flow.Mapping;

import Steps.api.DataDefinitionDeclaration;
import flow.api.FlowDefinition;
import flow.api.FlowIO.IO;
import flow.api.FlowIO.SingleFlowIOData;
import flow.api.StepUsageDeclaration;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class AutomaticMapping {
    public AutomaticMapping(FlowDefinition flow){

        //List<SingleFlowIOData> optionalOutput = new LinkedList<>();

        for (StepUsageDeclaration step : flow.getFlowSteps()) {
            for (DataDefinitionDeclaration input : step.getStepDefinition().inputs()) {
                flow.addElementToIoList(new SingleFlowIOData(IO.INPUT, input.getName(),
                        flow.getAliasFromMap(input.getName()),flow.getDDFromMap(input.getName()),input.userString(),
                        step.getStepDefinition()));
            }
            for (DataDefinitionDeclaration output : step.getStepDefinition().outputs()) {
                flow.addElementToIoList(new SingleFlowIOData(IO.OUTPUT, output.getName(),
                        flow.getAliasFromMap(output.getName()),flow.getDDFromMap(output.getName()),output.userString(),
                        step.getStepDefinition()));
            }
        }

        for(SingleFlowIOData data : flow.getIOlist())
        {
            if(data.getType() == IO.OUTPUT) {
                //nodes that current node can be input for them.
                data.setOptionalOutput(flow.getIOlist().stream().
                        skip(flow.getIOlist().indexOf(data)).
                        filter(io -> io.getType() != data.getType()).
                        filter(io -> io.getDD().equals(data.getDD())).//filter the outputs to outputs with the DD
                        filter(io -> io.getName().equals(data.getName())).//filter the outputs to outputs with the same name
                //CHECK IF ALIAS OR NAME
                        collect(Collectors.toList()));

                for(SingleFlowIOData storeData : data.getOptionalOutput()) {
                    storeData.addToOptionalInputs(storeData);//nodes that can be input to current node.
                }
            }
        }

    }
}
