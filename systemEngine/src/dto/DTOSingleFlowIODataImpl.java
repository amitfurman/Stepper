package dto;

import datadefinition.api.DataDefinitions;
import flow.api.FlowIO.SingleFlowIOData;
import steps.api.DataNecessity;

public class DTOSingleFlowIODataImpl implements DTOSingleFlowIOData{
    private String finalName;
    private DataDefinitions type;
    private String MyStep;
    private DataNecessity necessity;

    public DTOSingleFlowIODataImpl(SingleFlowIOData IOData)
    {
        this.finalName = IOData.getFinalName();
        this.type = IOData.getDD();
        this.MyStep = IOData.getStepName();
        this.necessity = IOData.getNecessity();
    }
    @Override
    public DataDefinitions getType() { return this.type; }
    @Override
    public String getFinalName() { return this.finalName; }
    @Override
    public String getStepName() {
        return this.MyStep;
    }
    @Override
    public DataNecessity getNecessity() { return necessity;}

}
