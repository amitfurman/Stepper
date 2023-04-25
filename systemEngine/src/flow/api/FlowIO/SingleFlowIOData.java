package flow.api.FlowIO;

import datadefinition.api.DataDefinitions;
import flow.api.StepUsageDeclaration;
import steps.api.StepDefinition;

import java.util.LinkedList;
import java.util.List;

public class SingleFlowIOData {

    private IO type;
    private String originalName;
    private String finalName;
    private DataDefinitions dataDefinition;
    private String userString;
    private StepUsageDeclaration MyStep;
    private List<SingleFlowIOData> inputsThatCanGetCurrOutput;
    private List<SingleFlowIOData> outputThatCanBringDataToCurrInput;

    public SingleFlowIOData(IO type, String originalName, String finalName, DataDefinitions dataDefinition,
                            String userString, StepUsageDeclaration MyStep) {
        this.type = type;
        this.originalName = originalName;
        this.finalName = finalName;
        this.dataDefinition = dataDefinition;
        this.userString = userString;
        this.MyStep = MyStep;
        this.inputsThatCanGetCurrOutput = new LinkedList<>();
        this.outputThatCanBringDataToCurrInput = new LinkedList<>();
    }

    public void setOptionalInputs(List<SingleFlowIOData> optionalInputs){
        this.inputsThatCanGetCurrOutput = optionalInputs;
    }

    public void setOptionalOutput(List<SingleFlowIOData> optionalOutput){
        this.outputThatCanBringDataToCurrInput= optionalOutput;
    }
    public void addToOptionalInputs(SingleFlowIOData data){
        this.inputsThatCanGetCurrOutput.add(data);
    }

    public void addToOptionalOutput(SingleFlowIOData data){
        this.outputThatCanBringDataToCurrInput.add(data);
    }

    public List<SingleFlowIOData> getOptionalInputs(){
        return this.inputsThatCanGetCurrOutput;
    }
    public List<SingleFlowIOData> getOptionalOutput(){
        return this.outputThatCanBringDataToCurrInput;
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

    public String getFinalName() {
        return this.finalName;
    }
    public String getStepName() {
        return this.MyStep.getFinalStepName();
    }


}
