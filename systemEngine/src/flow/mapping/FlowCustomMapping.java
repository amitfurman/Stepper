package flow.mapping;

import flow.api.CustomMapping;
import flow.api.FlowDefinition;
import flow.api.FlowIO.SingleFlowIOData;
import jaxb.schema.generated.STCustomMapping;
import jaxb.schema.generated.STCustomMappings;

public class FlowCustomMapping {
    public FlowCustomMapping(FlowDefinition flow){

        for (CustomMapping currCustom: flow.getCustomMappingList()) {
            if(flow.stepExist(currCustom.getTargetStep()) && flow.stepExist(currCustom.getSourceStep()) &&
                flow.dataExist(currCustom.getSourceStep(), currCustom.getSourceData()) &&
                    flow.dataExist(currCustom.getTargetStep(), currCustom.getTargetData()) &&
                       flow.doesSourceStepBeforeTargetStep(currCustom.getSourceStep() ,currCustom.getTargetStep()) &&
                          flow.isTheSameDD(currCustom.getSourceData(),currCustom.getTargetData())) {

                for (SingleFlowIOData data : flow.getIOlist()) {
                    if (data.getFinalName().equals(currCustom.getTargetData()) && data.getStepName().equals(currCustom.getTargetStep())) {
                        if (!data.getOptionalOutput().isEmpty()) {
                            data.getOptionalOutput().get(0).getOptionalInputs().remove(data);
                            data.getOptionalOutput().clear();
                        }
                        SingleFlowIOData sourceData = flow.getElementFromIOList(currCustom.getSourceStep(), currCustom.getSourceData());
                        data.addToOptionalOutput(sourceData);
                        sourceData.addToOptionalInputs(data);
                        break;
                    }
                }
            }
        }
    }
}


