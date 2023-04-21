package flow.execution.context;

public interface StepExecutionContext {
    <T> T getDataValue(String dataName, Class<T> expectedDataType);
    public boolean storeDataValue(String originalStepName, String dataName, Object value);

    //void storeLogLine(String logLine);
    //void setSummaryLine(String summaryLine);

}