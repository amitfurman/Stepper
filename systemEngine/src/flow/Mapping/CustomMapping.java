package flow.Mapping;

import flow.api.FlowDefinition;
import flow.api.FlowIO.IO;
import flow.api.FlowIO.SingleFlowIOData;
import jaxb.schema.generated.STCustomMapping;
import jaxb.schema.generated.STCustomMappings;

import java.util.stream.Collectors;

public class CustomMapping {
    public CustomMapping(FlowDefinition flow){
        STCustomMappings customMapping = null;
        for (STCustomMapping currCustom: customMapping.getSTCustomMapping()) {
            for(SingleFlowIOData data : flow.getIOlist())
            {
                if(data.getFinalName() == currCustom.getSourceStep()) {
                    if (data.getType() == IO.OUTPUT && data.getOptionalInputs().size()==0) {
                       // data.addToOptionalInputs(currCustom.getTargetData());
                    }

                    for(SingleFlowIOData storeData : data.getOptionalInputs()) {
                        storeData.addToOptionalOutput(data);//nodes that can be input to current node.
                    }
                }
            }
            
        }

    }
}
