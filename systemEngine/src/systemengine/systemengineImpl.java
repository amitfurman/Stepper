package systemengine;

import dto.*;
import exceptions.*;
import flow.api.FlowDefinition;
import flow.api.FlowIO.SingleFlowIOData;
import flow.execution.FlowExecution;
import flow.execution.runner.FlowExecutor;
import flow.impl.FlowsManager;
import flow.mapping.FlowContinuationMapping;
import jaxb.schema.SchemaBasedJAXBMain;
import statistic.FlowAndStepStatisticData;
import steps.api.DataNecessity;
import xml.XmlValidator;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class systemengineImpl implements systemengine {
    private static systemengineImpl instance;
    public LinkedList<FlowDefinition> flowDefinitionList;
    public LinkedList<FlowExecution> flowExecutionList;
    public FlowAndStepStatisticData statisticData;
    public ExecutorService threadPool;
    public int numberOfThreads;
    public LinkedList<FlowContinuationMapping> allContinuationMappings;

    public systemengineImpl() {
        this.flowDefinitionList = new LinkedList<>();
        this.flowExecutionList = new LinkedList<>();
        this.statisticData = new FlowAndStepStatisticData();
        this.instance = this;
    }

    static public systemengine getInstance() {
        return instance;
    }


    @Override
    public Boolean isCurrFlowExecutionDone(String currFlowName) {
        FlowExecution currFlow = flowExecutionList.stream().filter(flow -> flow.getFlowName().equals(currFlowName)).findFirst().get();
        return currFlow.isComplete();
    }

    @Override
    public DTOFlowExecution getFlowExecutionStatus(UUID flowSessionId) {
        FlowExecution flowExecution = flowExecutionList.stream().filter(flow -> flow.getUniqueId().equals(flowSessionId)).findFirst().get();
        return new DTOFlowExecution(flowExecution);
    }

    @Override
    synchronized public DTOFlowExecution activateFlowByName(String flowName, DTOFreeInputsFromUser freeInputs) {
        FlowDefinition currFlow = flowDefinitionList.stream().filter(flow -> flow.getName().equals(flowName)).findFirst().get();
        FlowExecution flowExecution = new FlowExecution(currFlow);
        flowExecution.setFreeInputsValues(freeInputs.getFreeInputMap());
        flowExecutionList.addFirst(flowExecution);

        threadPool.execute(new FlowExecutor(flowExecution, freeInputs, currFlow.getInitialInputMap(), statisticData));
        return new DTOFlowExecution(flowExecution);
    }

    @Override
    public DTOFlowExecution activateFlow(int flowChoice, DTOFreeInputsFromUser freeInputs) {
        FlowDefinition currFlow = flowDefinitionList.get(flowChoice - 1);
        FlowExecution flowExecution = new FlowExecution(currFlow);
        flowExecution.setFreeInputsValues(freeInputs.getFreeInputMap());

        flowExecutionList.addFirst(flowExecution);
        return new DTOFlowExecution(flowExecution);
    }

    @Override
    public void cratingFlowFromXml(String filePath) throws DuplicateFlowsNames, JAXBException, UnExistsStep, FileNotFoundException, OutputsWithSameName, MandatoryInputsIsntUserFriendly, UnExistsData, SourceStepBeforeTargetStep, TheSameDD,
            UnExistsOutput, FreeInputsWithSameNameAndDifferentType, InitialInputIsNotExist, UnExistsFlow, UnExistsDataInTargetFlow, FileNotExistsException, FileIsNotXmlTypeException {
        XmlValidator validator = new XmlValidator();
        validator.isXmlFileValid(filePath);
        SchemaBasedJAXBMain schema = new SchemaBasedJAXBMain();
        FlowsManager flows = schema.schemaBasedJAXB(filePath);
        flowDefinitionList = flows.getAllFlows();
        numberOfThreads = flows.getNumberOfThreads();
        allContinuationMappings = new LinkedList<>(flows.getAllContinuationMappings());
        threadPool = Executors.newFixedThreadPool(numberOfThreads);
    }

    @Override
    public LinkedList<FlowContinuationMapping> getAllContinuationMappingsWithSameSourceFlow(String currFlowName) {
        LinkedList<FlowContinuationMapping> sortedContinuationMappings = new LinkedList<>();
        for (FlowContinuationMapping mapping : allContinuationMappings) {
            if (currFlowName.equals(mapping.getSourceFlow())) {
                sortedContinuationMappings.add(mapping);
            }
        }
        return sortedContinuationMappings;
    }

    @Override
    public DTOAllStepperFlows getAllFlows() {
        return new DTOAllStepperFlows(flowDefinitionList);
    }

    @Override
    public DTOFlowsNames printFlowsName() {
        int index = 1;
        StringBuilder flowData = new StringBuilder();
        flowData.append("Flows Names: " + '\n');
        for (FlowDefinition flow : flowDefinitionList) {
            flowData.append(index + ". " + flow.getName() + '\n');
            index++;
        }
        return new DTOFlowsNames(flowData);
    }

    @Override
    public List<FlowDefinition> getFlowDefinitionList() {
        return flowDefinitionList;
    }

    @Override
    public DTOFlowDefinition IntroduceTheChosenFlow(int flowNumber) {
        FlowDefinition flow = flowDefinitionList.get(flowNumber - 1);
        return new DTOFlowDefinition(flow);
    }

    @Override
    public boolean hasAllMandatoryInputs(int flowChoice, Map<String, Object> freeInputMap) {
        for (SingleFlowIOData input : flowDefinitionList.get(flowChoice - 1).getFlowFreeInputs()) {
            boolean found = freeInputMap.keySet().stream().anyMatch(key -> key.equals(input.getStepName() + "." + input.getOriginalName()));
            if (!found && input.getNecessity().equals(DataNecessity.MANDATORY)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public DTOFreeInputsByUserString printFreeInputsByUserString(int choice) {
        AtomicInteger freeInputsIndex = new AtomicInteger(1);
        StringBuilder freeInputsData = new StringBuilder();
        freeInputsData.append("*The free inputs in the current flow: *\n");
        FlowDefinition currFlow = flowDefinitionList.get(choice - 1);
        currFlow
                .getFlowFreeInputs()
                .stream()
                .filter(node -> currFlow.getInitialInputMap().keySet().stream().noneMatch(name -> (name.equals(node.getFinalName()))))
                .forEach(node -> {
                    freeInputsData.append("Free Input " + freeInputsIndex.getAndIncrement() + ": ");
                    freeInputsData.append(String.format("Input Name: %s(%s)", node.getUserString(), node.getFinalName()));
                    freeInputsData.append("\tMandatory/Optional: " + node.getNecessity() + "\n");
                });
        return new DTOFreeInputsByUserString(freeInputsData, flowDefinitionList.get(choice - 1).getFlowFreeInputs().size());
    }

    @Override
    public DTOSingleFlowIOData getSpecificFreeInput(int flowChoice, int freeInputChoice) {
        return new DTOSingleFlowIOData(flowDefinitionList.get(flowChoice - 1).getFlowFreeInputs().get(freeInputChoice - 1));
    }

    @Override
    public DTOFlowsExecutionList getFlowsExecutionList() {
        return new DTOFlowsExecutionList(flowExecutionList);
    }

    @Override
    public DTOFlowExecution getFlowExecutionDetails(int flowExecutionChoice) {
        return new DTOFlowExecution(flowExecutionList.get(flowExecutionChoice - 1));
    }

    @Override
    public DTOFlowAndStepStatisticData getStatisticData() {
        return new DTOFlowAndStepStatisticData(statisticData);
    }

    @Override
    public void saveToFile(String path) {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(
                             Files.newOutputStream(Paths.get(path)))) {
            out.writeObject(flowDefinitionList);
            out.writeObject(flowExecutionList);
            out.writeObject(statisticData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void loadFromFile(String path) {
        try (ObjectInputStream in =
                     new ObjectInputStream(
                             Files.newInputStream(Paths.get(path)))) {
            flowDefinitionList = (LinkedList<FlowDefinition>) in.readObject();
            flowExecutionList = (LinkedList<FlowExecution>) in.readObject();
            statisticData = (FlowAndStepStatisticData) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DTOFlowExecution getDTOFlowExecutionById(UUID flowId) {
        FlowExecution executedFlow = flowExecutionList.stream().filter(flow -> flow.getUniqueId().equals(flowId)).findFirst().get();
        return new DTOFlowExecution(executedFlow);
    }

    @Override
    public DTOFlowExecution getDTOFlowExecutionByName(String flowName) {
        FlowExecution executedFlow = flowExecutionList.stream().filter(flow -> flow.getFlowName().equals(flowName)).findFirst().get();
        return new DTOFlowExecution(executedFlow);
    }

    @Override
    public List<Input> getFreeInputsFromCurrFlow(String flowName) {
        List<Input> freeInputsList = new ArrayList<Input>();

        FlowExecution executedFlow = flowExecutionList.stream().filter(flow -> flow.getFlowName().equals(flowName)).findFirst().get();
        executedFlow.getFreeInputsList().forEach(io -> {
            Input input = new Input();
            input.setFinalName(io.getFinalName());
            input.setOriginalName(io.getOriginalName());
            input.setStepName(io.getStepName());
            input.setMandatory(io.getNecessity().toString());
            input.setType(io.getDD());

            executedFlow.getFreeInputsValues().forEach((key, value) -> {
                if (key.equals(io.getStepName() + "." + io.getOriginalName())) {
                    input.setValue(value);
                }
            });

            freeInputsList.add(input);

        });

        return freeInputsList;
    }

    @Override
    public  Map<String, Object> continuationFlowExecution(String sourceFlowName, String targetFlowName) {
        FlowExecution sourceFlowExecution = flowExecutionList.stream().filter(flow -> flow.getFlowName().equals(sourceFlowName)).findFirst().get();
        FlowContinuationMapping currContinuation = allContinuationMappings.stream().filter(mapping -> mapping.getSourceFlow().equals(sourceFlowName) && mapping.getTargetFlow().equals(targetFlowName)).findFirst().get();

        Map<String, String> source2targetDataMapping = currContinuation.getSource2targetDataMapping();
        Map<String, Object> continuationDataMap = new HashMap<>();

        for (Map.Entry<String, String> entry : source2targetDataMapping.entrySet()) {
            String sourceDataName = entry.getKey();
            Object sourceDataValue = sourceFlowExecution.getDataValues().get(sourceDataName);

            continuationDataMap.put(entry.getValue(), sourceDataValue);
        }

        return continuationDataMap;
    }

    @Override
    public  List<Input> getValuesListFromContinuationMap (String sourceFlowName, String targetFlowName){
        FlowDefinition targetFlow = flowDefinitionList.stream().filter(flow -> flow.getName().equals(targetFlowName)).findFirst().get();
        Map<String, Object> valuesMap = continuationFlowExecution(sourceFlowName, targetFlowName);
        List<Input> valuesList = new ArrayList<>();

        for (Map.Entry<String, Object> entry : valuesMap.entrySet()){
            Input input = new Input();
            input.setFinalName(entry.getKey());
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
            input.setValue(entry.getValue());
            SingleFlowIOData IO = targetFlow.getIOlist().stream().filter(io->io.getFinalName().equals(entry.getKey())).findFirst().get();
            input.setOriginalName(IO.getOriginalName());
            input.setStepName(IO.getStepName());
            input.setType(IO.getDD());
            input.setMandatory(IO.getNecessity().toString());

            System.out.println(input.getStepName());
            System.out.println(input.getOriginalName());
            System.out.println(input.getFinalName());
            System.out.println(input.getValue());

            valuesList.add(input);
        }

            return valuesList;
    }

}
