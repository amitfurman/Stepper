package flow.execution.context;

public interface StepExecutionContext {
    <T> T getDataValue(String dataName, Class<T> expectedDataType);
    boolean storeDataValue(String dataName, Object value);

    //void storeLogLine(String logLine);
    //void setSummaryLine(String summaryLine);

}