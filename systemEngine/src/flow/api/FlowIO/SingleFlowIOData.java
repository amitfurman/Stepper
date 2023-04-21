package flow.api.FlowIO;

import DataDefinition.api.DataDefinitions;
import Steps.api.StepDefinition;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SingleFlowIOData {

    private IO type;
    private String originalName;
    private String finalName;
    private DataDefinitions dataDefinition;
    private String userString;
    private StepDefinition MyStep;
    private List<SingleFlowIOData> optionalInputs;
    private List<SingleFlowIOData> optionalOutput;

    public SingleFlowIOData(IO type, String originalName, String finalName, DataDefinitions dataDefinition,
                            String userString, StepDefinition MyStep) {
        this.type = type;
        this.originalName = originalName;
        this.finalName = finalName;
        this.dataDefinition = dataDefinition;
        this.userString = userString;
        this.MyStep = MyStep;
        this.optionalInputs = new LinkedList<>();
        this.optionalOutput = new LinkedList<>();
    }

    public void setOptionalInputs(List<SingleFlowIOData> optionalInputs){
        this.optionalInputs = optionalInputs;
    }

    public void setOptionalOutput(List<SingleFlowIOData> optionalOutput){
        this.optionalOutput= optionalOutput;
    }
    public void addToOptionalInputs(SingleFlowIOData data){
        this.optionalInputs.add(data);
    }

    public void addToOptionalOutput(SingleFlowIOData data){
        this.optionalOutput.add(data);
    }

    public List<SingleFlowIOData> getOptionalInputs(){
        return this.optionalInputs;
    }
    public List<SingleFlowIOData> getOptionalOutput(){
        return this.optionalOutput;
    }

    public IO getType(){
        return this.type;
    }

    public DataDefinitions getDD()
    {
        return this.dataDefinition;
    }

    public String getName()
    {
        return this.originalName;
    }
    public String getFinalName()
    {
        return this.finalName;
    }




}
