package dto;

import flow.execution.StepExecutionData;
import steps.api.Logger;
import steps.api.StepResult;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;

public class DTOStepExecutionData {
    private String finalName;
    private String originalName;
    private Duration totalStepTime;
    private StepResult result;
    private String summaryLine;
    private List<DTOLogger> loggerList;

    public DTOStepExecutionData(StepExecutionData stepExecutionData){
        this.finalName = stepExecutionData.getFinalNameStep();
        this.originalName = stepExecutionData.getOriginalName();
        this.totalStepTime = stepExecutionData.getTotalStepTime();
        this.result = stepExecutionData.getResult();
        this.summaryLine= stepExecutionData.getSummaryLine();
        this.loggerList = new LinkedList<>();
        for (Logger logger : stepExecutionData.getLoggerList()) {
            loggerList.add(new DTOLogger(logger));
        }
    }
    public String getFinalNameStep(){
        return finalName;
    }
    public String getOriginalName() { return originalName; }
    public Duration getTotalStepTime(){
        return totalStepTime;
    }
    public StepResult getResult() { return result; }
    public String getSummaryLine() { return summaryLine; }
    public List<DTOLogger> getLoggerList() { return loggerList; }
}
