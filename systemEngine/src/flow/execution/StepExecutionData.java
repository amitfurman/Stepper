package flow.execution;

import steps.api.Logger;
import steps.api.StepResult;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class StepExecutionData {
    private String name;
    private List<Logger> loggerList;
    private String summaryLine;
    private Duration totalStepTime;
    private StepResult result;

    public StepExecutionData(String name){
        this.name = name;
        this.loggerList = new ArrayList<>();
        this.summaryLine= "";
        this.totalStepTime = Duration.ofNanos(0);
        this.result = null;
    }
}
