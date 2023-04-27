package flow.execution.context;

import flow.execution.StepExecutionData;
import steps.api.Logger;

import java.time.Instant;
import java.util.Map;

public interface StepExecutionContext {
    void setCurrInvokingStep(String stepName);
    StepExecutionData getCurrInvokingStep();
    <T> T getDataValue(String dataName, Class<T> expectedDataType);
    boolean storeDataValue(String dataName, Object value);
    void storeLogLine(String log);
    void storeSummaryLine(String summaryLine);
    void storeLogLineAndSummaryLine(String log);
    void storeStepTotalTime(Instant statTime);
    Map<String, Object> getDataValues();
    void addCurrInvokingStepToLogsList();
}