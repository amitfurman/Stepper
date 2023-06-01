package flow.execution;

import steps.api.Logger;
import steps.api.StepResult;

import java.io.Serializable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class StepExecutionData implements Serializable {
    private String finalName;
    private String originalName;
    private Duration totalStepTime;
    private StepResult result;
    private String summaryLine;
    private List<Logger> loggerList;
    private boolean isExecuted;

    public StepExecutionData(String finalName, String originalName, boolean executed){
        this.finalName = finalName;
        this.originalName = originalName;
        this.loggerList = new LinkedList<>();
        this.summaryLine= "";
        this.totalStepTime = Duration.ZERO;
        this.result = null;
        this.isExecuted = executed;
    }

    public String getFinalNameStep(){
        return finalName;
    }
    public String getOriginalName() { return originalName; }
    public Duration getTotalStepTime(){ return totalStepTime;}
    public StepResult getResult() { return result; }
    public String getSummaryLine(){return summaryLine;}
    public List<Logger> getLoggerList(){return loggerList;}
    public void addLogger(Logger log){ loggerList.add(log); }
    public void setOriginalName(String originalName) { this.originalName = originalName;}
    public void setTotalStepTime(Duration totalStepTime) { this.totalStepTime = totalStepTime;}
    public void setSummaryLine(String summaryLine) { this.summaryLine = summaryLine; }
    public void setTotalTime(Duration totalTime) { this.totalStepTime = totalTime;}
    public void setResult(StepResult result) { this.result = result; }
    public boolean isExecuted() {return isExecuted;}
    public void setExecuted(boolean executed) {isExecuted = executed;}
}

