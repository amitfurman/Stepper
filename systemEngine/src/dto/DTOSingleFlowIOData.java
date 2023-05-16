package dto;

import datadefinition.api.DataDefinitions;
import flow.api.FlowIO.IO;
import flow.api.FlowIO.SingleFlowIOData;
import steps.api.DataNecessity;

public class DTOSingleFlowIOData{
    private IO IOType;
    private String originalName;
    private String finalName;
    private DataDefinitions type;
    private String MyStep;
    private DataNecessity necessity;
    private String userString;
    private Object value;

    public DTOSingleFlowIOData(SingleFlowIOData IOData)
    {
        this.IOType = IOData.getIOType();
        this.originalName = IOData.getOriginalName();
        this.finalName = IOData.getFinalName();
        this.type = IOData.getDD();
        this.MyStep = IOData.getStepName();
        this.necessity = IOData.getNecessity();
        this.userString = IOData.getUserString();
    }

    public DTOSingleFlowIOData(SingleFlowIOData IOData, Object value)
    {
        this.IOType = IOData.getIOType();
        this.finalName = IOData.getFinalName();
        this.type = IOData.getDD();
        this.MyStep = IOData.getStepName();
        this.necessity = IOData.getNecessity();
        this.userString = IOData.getUserString();
        this.value = value;
    }

    public DataDefinitions getType() { return this.type; }
    public String getFinalName() { return this.finalName; }
    public String getOriginalName() { return this.originalName; }
    public String getStepName() { return this.MyStep; }
    public DataNecessity getNecessity() { return necessity;}
    public String getUserString() { return userString; }
    public Object getValue() { return value; }
    public IO getIOType() { return IOType; }
}
