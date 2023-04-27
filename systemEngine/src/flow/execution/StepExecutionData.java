package flow.execution;

import steps.api.Logger;
import steps.api.StepResult;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class StepExecutionData {
    private String name;
    private Duration totalStepTime;
    private StepResult result;
    private String summaryLine;
    private List<Logger> loggerList;

    public StepExecutionData(String name){
        this.name = name;
        this.loggerList = new LinkedList<>();
        this.summaryLine= "";
        this.totalStepTime = Duration.ZERO;
        this.result = null;
    }

    public String getNameStep(){
        return name;
    }
    public Duration getTotalStepTime(){
        return totalStepTime;
    }

    public void addLogger(Logger log){
        loggerList.add(log);
    }

    public void setTotalStepTime(Duration totalStepTime) {
        this.totalStepTime = totalStepTime;
    }
    public void setSummaryLine(String summaryLine) {
        this.summaryLine = summaryLine;
    }

    public void setTotalTime(Duration totalTime) {
        this.totalStepTime = totalTime;
    }
}
