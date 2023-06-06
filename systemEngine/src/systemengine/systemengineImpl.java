package systemengine;

import dto.*;
import exceptions.*;
import flow.api.FlowDefinition;
import flow.api.FlowDefinitionImpl;
import flow.api.FlowIO.SingleFlowIOData;
import flow.execution.FlowExecution;
import flow.execution.runner.FlowExecutor;
import flow.impl.FlowsManager;
import jaxb.schema.SchemaBasedJAXBMain;
import statistic.FlowAndStepStatisticData;
import steps.api.DataNecessity;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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


    static public systemengine getInstance() {
        if (instance == null) {
            instance = new systemengineImpl();
        }
        return instance;
    }
    @Override
    public Boolean isCurrFlowExecutionDone(String currFlowName){
        FlowExecution currFlow = flowExecutionList.stream().filter(flow -> flow.getFlowName().equals(currFlowName)).findFirst().get();
        return currFlow.isComplete();
    }
    @Override
    public DTOFlowExecution getFlowExecutionStatus(UUID flowSessionId){
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

        DTOFlowExecution dtoFlowExecution = new DTOFlowExecution(flowExecution);

        return dtoFlowExecution;
    }

/////need synchronized?
    @Override
    public DTOFlowExecution activateFlow(int flowChoice, DTOFreeInputsFromUser freeInputs) {
        FlowDefinition currFlow = flowDefinitionList.get(flowChoice - 1);
        FlowExecution flowExecution = new FlowExecution(currFlow);
        flowExecution.setFreeInputsValues(freeInputs.getFreeInputMap());

        flowExecutionList.addFirst(flowExecution);
        threadPool.execute(new FlowExecutor(flowExecution, freeInputs, currFlow.getInitialInputMap(), statisticData));
        return new DTOFlowExecution(flowExecution);
    }

    public systemengineImpl() {
        this.flowDefinitionList = new LinkedList<>();
        this.flowExecutionList = new LinkedList<>();
        this.statisticData = new FlowAndStepStatisticData();
        this.instance = this;
    }

    @Override
    public void cratingFlowFromXml(String filePath) throws DuplicateFlowsNames, JAXBException, UnExistsStep, FileNotFoundException, OutputsWithSameName, MandatoryInputsIsntUserFriendly, UnExistsData, SourceStepBeforeTargetStep, TheSameDD, UnExistsOutput, FreeInputsWithSameNameAndDifferentType, InitialInputIsNotExist {
        SchemaBasedJAXBMain schema = new SchemaBasedJAXBMain();
        FlowsManager flows = schema.schemaBasedJAXB(filePath);
        flowDefinitionList = flows.getAllFlows();
        numberOfThreads = flows.getNumberOfThreads();
        this.threadPool = Executors.newFixedThreadPool(5);
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






    //////check!!!!!!!!!
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
    public DTOFlowExecution getDTOFlowExecution(UUID flowId) {
        FlowExecution executedFlow = flowExecutionList.stream().filter(flow -> flow.getUniqueId().equals(flowId)).findFirst().get();
        return new DTOFlowExecution(executedFlow);
    }

}

