package flow.mapping;

import exceptions.SourceStepBeforeTargetStep;
import exceptions.TheSameDD;
import exceptions.UnExistsData;
import exceptions.UnExistsStep;
import flow.api.CustomMapping;
import flow.api.FlowDefinition;
import flow.api.FlowIO.SingleFlowIOData;

public class FlowCustomMapping {
    public FlowCustomMapping(FlowDefinition flow) throws UnExistsStep, UnExistsData, SourceStepBeforeTargetStep, TheSameDD {
        for (CustomMapping currCustom: flow.getCustomMappingList()) {
            if (!(flow.stepExist(currCustom.getTargetStep())) || !(flow.stepExist(currCustom.getSourceStep()))){
               throw new UnExistsStep();
            }else if(!(flow.dataExist(currCustom.getSourceStep(), currCustom.getSourceData())) || !(flow.dataExist(currCustom.getTargetStep(), currCustom.getTargetData()))) {
                throw new UnExistsData();
            }else if(!(flow.doesSourceStepBeforeTargetStep(currCustom.getSourceStep(), currCustom.getTargetStep()))){
                throw new SourceStepBeforeTargetStep();
            }else if(!(flow.isTheSameDD(currCustom.getSourceData(), currCustom.getTargetData()))) {
                throw new TheSameDD();
            }
            else {
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


