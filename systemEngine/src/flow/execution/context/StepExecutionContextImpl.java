package flow.execution.context;

import datadefinition.api.DataDefinitions;
import datadefinition.api.IO_NAMES;
import flow.api.FlowIO.IO;
import flow.api.FlowIO.SingleFlowIOData;
import flow.execution.StepExecutionData;
import steps.api.Logger;
import steps.api.StepResult;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class StepExecutionContextImpl implements StepExecutionContext {

    private final Map<String, Object> dataValues;
    private final Map<String, DataDefinitions> stepAndIOName2DD;
    private final Map<String, DataDefinitions> name2DD;
    private final Map<String, String> inputName2alias;
    private final Map<String, String> outputName2alias;
    private final Map<String, String> stepName2alias;//key - step name after alias, value - original step name
    private final List<StepExecutionData> StepExecutionList;
    private StepExecutionData currInvokingStep;
    private IO_NAMES ioName;
    private List<SingleFlowIOData> IOlist;
    private final Map<String, String> name2Alias;



    public StepExecutionContextImpl(Map<String, DataDefinitions> originalDDMap, Map<String,String> originalOutputAliasMap, Map<String,String> originalInputAliasMap, Map<String, String> originalStepName2alias,
                                    List<SingleFlowIOData> originalIOlist,Map<String, DataDefinitions>  originalname2DD,Map<String, String> originalname2Alias) {
        dataValues = new HashMap<>();
        stepAndIOName2DD = new HashMap<>(originalDDMap);
        name2DD = new HashMap<>(originalname2DD);
        outputName2alias = new HashMap<>(originalOutputAliasMap);
        inputName2alias = new HashMap<>(originalInputAliasMap);
        stepName2alias = new HashMap<>(originalStepName2alias);
        StepExecutionList = new LinkedList<>();
        ioName = new IO_NAMES();
        IOlist = new ArrayList<>(originalIOlist);
        name2Alias = new HashMap<>(originalname2Alias);

    }
    @Override
    public void setCurrInvokingStep(String finalStepName, String originalStepName) {
        this.currInvokingStep = new StepExecutionData(finalStepName, originalStepName, false);
    }

    @Override
    public void setFinishToExecutionStep() {
        this.currInvokingStep.setExecuted(true);
    }
    @Override
    public StepExecutionData getCurrInvokingStep(){ return this.currInvokingStep; }

    @Override
    public <T> T getDataValue(String dataName, Class<T> expectedDataType) {
        String ioAlias;
        DataDefinitions theExpectedDataDefinition;
        if(currInvokingStep != null){
            ioAlias = inputName2alias.get(currInvokingStep.getFinalNameStep()+"."+dataName);
            if (ioAlias == null) {
                ioAlias = outputName2alias.get(currInvokingStep.getFinalNameStep()+"."+dataName);
            }
            theExpectedDataDefinition = stepAndIOName2DD.get(currInvokingStep.getFinalNameStep()+"."+ioAlias);
        }
        else {
            ioAlias = inputName2alias.get(dataName);
            if (ioAlias == null) {
                ioAlias = outputName2alias.get(dataName);
            }
            theExpectedDataDefinition = stepAndIOName2DD.get(dataName);
        }
/*
        IOlist.stream().forEach(data->{
            if(data.getType().equals(IO.INPUT)) {
                System.out.println("name: " + data.getFinalName() + " outputs into inputs: " + data.getOptionalOutput());
            }
            if(data.getType().equals(IO.OUTPUT)) {
                System.out.println("name: " + data.getFinalName() + " inputs into outputs: " + data.getOptionalInputs() );
            }
        });
*/
        if (expectedDataType.isAssignableFrom(theExpectedDataDefinition.getType())) {
            Object aValue = dataValues.get(ioAlias);
            if (aValue != null) {
                return expectedDataType.cast(aValue);
            }else if(!(IOlist.stream().filter(io -> io.getOriginalName().equals(dataName)).findFirst().get().getOptionalOutput().isEmpty())){
                aValue = dataValues.get(IOlist.stream().filter(io -> io.getOriginalName().equals(dataName)).findFirst().get().getOptionalOutput().get(0).getFinalName());
                return expectedDataType.cast(aValue);
            } else {
                return null; //for the optional inputs that are not provided
            }
        }
        else {
            throw new IllegalArgumentException("Data definition for " + dataName + " is not found or expected data type is not compatible.");
        }
    }
/*    @Override
    public <T> T getDataValue(String dataName, Class<T> expectedDataType) {
        DataDefinitions theExpectedDataDefinition =IO_NAMES.getDataDefinition(dataName);
        }
        DataDefinitions theExpectedDataDefinition =name2DD.get(dataName);
        String nameAfterAlias= name2Alias.get(dataName);

        if (expectedDataType.isAssignableFrom(theExpectedDataDefinition.getType())) {
           Object aValue = dataValues.get(nameAfterAlias);
            if (aValue != null) {
                return expectedDataType.cast(aValue);
            }else if(!(IOlist.stream().filter(io -> io.getName().equals(dataName)).findFirst().get().getOptionalOutput().isEmpty())){
                aValue = dataValues.get(IOlist.stream().filter(io -> io.getName().equals(dataName)).findFirst().get().getOptionalOutput().get(0).getFinalName());
                return expectedDataType.cast(aValue);
            } else {
                return null; //for the optional inputs that are not provided
            }
        }
        else {
            throw new IllegalArgumentException("Data definition for " + dataName + " is not found or expected data type is not compatible.");
        }

    }*/

    @Override
    public boolean storeDataValue(String dataName, Object value) {
        String ioAlias;
        DataDefinitions theData;
        if(currInvokingStep != null){
            ioAlias = inputName2alias.get(currInvokingStep.getFinalNameStep()+"."+dataName);
            if (ioAlias == null) {
                ioAlias = outputName2alias.get(currInvokingStep.getFinalNameStep()+"."+dataName);
            }
            theData = stepAndIOName2DD.get(currInvokingStep.getFinalNameStep()+"."+ioAlias);
        }
        else {
            ioAlias = inputName2alias.get(dataName);
            if (ioAlias == null) {
                ioAlias = outputName2alias.get(dataName);
            }
            theData = stepAndIOName2DD.get(dataName);
        }

        if (theData.getType().isAssignableFrom(value.getClass())) {
            dataValues.put(ioAlias, value);
        } else if (theData.getType().getSimpleName().equals("ZipEnumeratorData") && value.getClass().getSimpleName().equals("String")) { /////no neede
            dataValues.put(ioAlias, value);
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