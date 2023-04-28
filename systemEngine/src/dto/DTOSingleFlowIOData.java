package dto;

import datadefinition.api.DataDefinitions;
import flow.api.FlowIO.SingleFlowIOData;
import steps.api.DataNecessity;

public class DTOSingleFlowIOData{
    private String finalName;
    private DataDefinitions type;
    private String MyStep;
    private DataNecessity necessity;
    private String userString;

    public DTOSingleFlowIOData(SingleFlowIOData IOData)
    {
        this.finalName = IOData.getFinalName();
        this.type = IOData.getDD();
        this.MyStep = IOData.getStepName();
        this.necessity = IOData.getNecessity();
        this.userString = IOData.getUserString();
    }

    public DataDefinitions getType() { return this.type; }

    public String getFinalName() { return this.finalName; }

    public String getStepName() {
        return this.MyStep;
    }

    public DataNecessity getNecessity() { return necessity;}

    public String getUserString() { return userString; }

}
