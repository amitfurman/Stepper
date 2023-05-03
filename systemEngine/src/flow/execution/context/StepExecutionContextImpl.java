package flow.execution.context;

import datadefinition.api.DataDefinitions;
import datadefinition.api.IO_NAMES;
import flow.execution.StepExecutionData;
import steps.api.Logger;
import steps.api.StepResult;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class StepExecutionContextImpl implements StepExecutionContext {

    private final Map<String, Object> dataValues;
    private final Map<String, DataDefinitions> name2DD;
    private final Map<String, String> outputName2alias;
    private final Map<String, String> stepName2alias;//key - step name after alias, value - original step name
    private final List<StepExecutionData> StepExecutionList;
    private StepExecutionData currInvokingStep;
    private IO_NAMES ioName;


    public StepExecutionContextImpl(Map<String, DataDefinitions> originalDDMap, Map<String,String> originalOutputAliasMap, Map<String, String> originalStepName2alias) {
        dataValues = new HashMap<>();
        name2DD = new HashMap<>(originalDDMap);
        outputName2alias = new HashMap<>(originalOutputAliasMap);
        stepName2alias = new HashMap<>(originalStepName2alias);
        StepExecutionList = new LinkedList<>();
        ioName = new IO_NAMES();
    }
    @Override
    public void setCurrInvokingStep(String finalStepName, String originalStepName) {
        this.currInvokingStep = new StepExecutionData(finalStepName, originalStepName);
    }
    @Override
    public StepExecutionData getCurrInvokingStep(){ return this.currInvokingStep; }
    @Override
    public <T> T getDataValue(String dataName, Class<T> expectedDataType) {
        DataDefinitions theExpectedDataDefinition =IO_NAMES.getDataDefinition(dataName);

        if (expectedDataType.isAssignableFrom(theExpectedDataDefinition.getType())) {
            Object aValue = dataValues.get(dataName);
            if (aValue != null) {
                return expectedDataType.cast(aValue);
            } else {
                return null; //for the optional inputs that are not provided
            }
        }
        else {
            throw new IllegalArgumentException("Data definition for " + dataName + " is not found or expected data type is not compatible.");
        }
    }
    @Override
    public boolean storeDataValue(String dataName, Object value) {
        DataDefinitions theData =IO_NAMES.getDataDefinition(dataName);

        if (theData.getType().isAssignableFrom(value.getClass())) {
            String stepName,outputAlias = dataName;
            for (String key : outputName2alias.keySet()) {
                if (key.endsWith("." + dataName)) {
                    stepName = key.substring(0, key.lastIndexOf("."));
                    outputAlias = outputName2alias.get(key);
                    break;
                }
            }
            dataValues.put(outputAlias, value);
        } else {
            throw new IllegalArgumentException("Data definition for " + dataName + " is not found or expected data type is not compatible.");
        }

        return false;
    }
    @Override
    public void storeLogLine(String log) {
        currInvokingStep.addLogger(new Logger(log));
    }
    @Override
    public void storeSummaryLine(String summaryLine) {
        currInvokingStep.setSummaryLine(summaryLine);
    }
    @Override
    public void storeLogLineAndSummaryLine(String log) {
        currInvokingStep.addLogger(new Logger(log));
        currInvokingStep.setSummaryLine(log);
    }
    @Override
    public void storeStepTotalTime(Instant startTime) {
        Instant endTime = Instant.now();
        Duration totalTime = Duration.between(startTime, endTime);
        long totalTimeMillis = totalTime.toMillis();
        currInvokingStep.setTotalTime(Duration.ofMillis(totalTimeMillis));
    }
    @Override
    public Map<String, Object> getDataValues() {
        return dataValues;
    }
    @Override
    public void setStepResultToCurrInvokingStep(StepResult stepResult) {
        currInvokingStep.setResult(stepResult);
    }
    @Override
    public void addCurrInvokingStepToStepExecutionList() { StepExecutionList.add(currInvokingStep);}
    @Override
    public StepResult getStepResultToCurrInvokingStep() {return currInvokingStep.getResult();}
    @Override
    public List<StepExecutionData> getStepExecutionList() { return StepExecutionList;}
}