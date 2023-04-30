package flow.execution.context;

import flow.execution.StepExecutionData;
import steps.api.Logger;
import steps.api.StepResult;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public interface StepExecutionContext {
    void setCurrInvokingStep(String finalStepName, String originalStepName);
    StepExecutionData getCurrInvokingStep();
    <T> T getDataValue(String dataName, Class<T> expectedDataType);
    boolean storeDataValue(String dataName, Object value);
    void storeLogLine(String log);
    void storeSummaryLine(String summaryLine);
    void storeLogLineAndSummaryLine(String log);
    void storeStepTotalTime(Instant statTime);
    Map<String, Object> getDataValues();
    void addCurrInvokingStepToStepExecutionList();
    void setStepResultToCurrInvokingStep(StepResult stepResult);
    StepResult getStepResultToCurrInvokingStep();
    List<StepExecutionData> getStepExecutionList();

}