package flow.api.FlowIO;

import DataDefinition.api.DataDefinitions;
import Steps.api.StepDefinition;

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
    }





}
