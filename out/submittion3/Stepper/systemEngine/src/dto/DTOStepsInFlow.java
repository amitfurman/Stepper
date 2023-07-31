package dto;

import steps.api.StepResult;

import java.time.Duration;
import java.util.List;

public class DTOStepsInFlow {
    private String originalName;
    private String finalName;
    private StepResult result;
    private Duration totalTime;
    private List<DTOInput> inputs;
    private List<DTOOutput> outputs;
    private List<DTOLogger> loggers;
    private String summaryLine;

    public DTOStepsInFlow(String originalName, String finalName, StepResult result, Duration totalTime, List<DTOInput> inputs,
                          List<DTOOutput> outputs, List<DTOLogger> loggers, String summaryLine){
        this.originalName = originalName;
        this.finalName = finalName;
        this.result = result;
        this.totalTime = totalTime;
        this.inputs = inputs;
        this.outputs = outputs;
        this.loggers = loggers;
        this.summaryLine = summaryLine;

    }
    public String getOriginalName() {return originalName;}
    public String getFinalName() {return finalName;}
    public StepResult getResult() {return result;}
    public Duration getTotalTime() {return totalTime;}
    public List<DTOInput> getInputs() {return inputs;}
    public List<DTOOutput> getOutputs() {return outputs;}
    public List<DTOLogger> getLoggers() {return loggers;}
    public String getSummaryLine() {return summaryLine;}



}
