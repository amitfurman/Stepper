package systemengine;

import datadefinition.impl.list.FileListData;
import datadefinition.impl.mapping.NumberMappingData;
import datadefinition.impl.relation.RelationData;
import dto.*;
import exceptions.*;
import flow.api.FlowDefinition;
import flow.api.FlowIO.IO;
import flow.api.FlowIO.SingleFlowIOData;
import flow.execution.FlowExecution;
import flow.execution.runner.FlowExecutor;
import flow.impl.FlowsManager;
import flow.mapping.FlowContinuationMapping;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import jaxb.schema.SchemaBasedJAXBMain;
import roles.Role;
import statistic.FlowAndStepStatisticData;
import steps.api.DataNecessity;
import user.UserDefinition;
import user.UserManager;
import xml.XmlValidator;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.lang.System.*;

public class systemengineImpl implements systemengine {
    private static systemengineImpl instance;
    public LinkedList<FlowDefinition> flowDefinitionList;
    public LinkedList<FlowExecution> flowExecutionList;
    public FlowAndStepStatisticData statisticData;
    public ExecutorService threadPool;
    public int numberOfThreads;
    public LinkedList<FlowContinuationMapping> allContinuationMappings;
    public UserManager userManagerObject;

    public List<Role> roles;

    public systemengineImpl() {
        numberOfThreads =0;
        this.flowDefinitionList = new LinkedList<>();
        this.flowExecutionList = new LinkedList<>();
        this.statisticData = new FlowAndStepStatisticData();
        this.instance = this;

        this.userManagerObject = new UserManager();
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

   /* @Override
    synchronized public DTOFlowExecution activateFlowByName(String flowName, DTOFreeInputsFromUser freeInputs) {
        System.out.println("flowName: " + flowName);
        System.out.println("flowDL: " + flowDefinitionList);

        FlowDefinition currFlow = flowDefinitionList.stream().filter(flow -> flow.getName().equals(flowName)).findFirst().get();
        System.out.println("currFlow: " + currFlow);
        FlowExecution flowExecution = new FlowExecution(currFlow);
        System.out.println("MAP");
        freeInputs.getFreeInputMap().forEach((key, value) -> System.out.println(value));
        flowExecution.setFreeInputsValues(freeInputs.getFreeInputMap());
        flowExecutionList.addFirst(flowExecution);

        threadPool.execute(new FlowExecutor(flowExecution, freeInputs, currFlow.getInitialInputMap(), statisticData));
       return new DTOFlowExecution(flowExecution);
    }*/

    @Override
    synchronized public DTOFlowID activateFlowByName(String userName, String flowName, DTOFreeInputsFromUser freeInputs) {
        FlowDefinition currFlow = flowDefinitionList.stream().filter(flow -> flow.getName().equals(flowName)).findFirst().get();
        out.println(currFlow.getName());
        freeInputs.getFreeInputMap().forEach((key, value) -> out.println(value));
        FlowExecution flowExecution = new FlowExecution(userName, currFlow);
        flowExecution.setFreeInputsValues(freeInputs.getFreeInputMap());
        flowExecutionList.addFirst(flowExecution);

    //add the number of Executed Flows to the user
        UserDefinition user = getUserMangerObject().getUsers().stream().filter(i->i.getUsername().equals(userName)).findFirst().get();
        user.getExecutedFlows().add(flowName);

        threadPool.execute(new FlowExecutor(flowExecution, freeInputs, currFlow.getInitialInputMap(), statisticData));
        //return new DTOFlowExecution(flowExecution);

        return new DTOFlowID(flowExecution.getUniqueIdByUUID());
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
        if(numberOfThreads ==0){
        numberOfThreads = flows.getNumberOfThreads();}
        allContinuationMappings = new LinkedList<>(flows.getAllContinuationMappings());
        threadPool = Executors.newFixedThreadPool(numberOfThreads);
    }

    @Override
    public void cratingFlowFromXml(InputStream inputStream) throws DuplicateFlowsNames, JAXBException, UnExistsStep, FileNotFoundException, OutputsWithSameName, MandatoryInputsIsntUserFriendly, UnExistsData, SourceStepBeforeTargetStep, TheSameDD,
            UnExistsOutput, FreeInputsWithSameNameAndDifferentType, InitialInputIsNotExist, UnExistsFlow, UnExistsDataInTargetFlow, FileNotExistsException, FileIsNotXmlTypeException {
        SchemaBasedJAXBMain schema = new SchemaBasedJAXBMain();
        FlowsManager flows = schema.schemaBasedJAXB(inputStream);
        flowDefinitionList.addAll(flows.getAllFlows());
      //  flowDefinitionList = flows.getAllFlows();
        if(numberOfThreads==0) {
            numberOfThreads = flows.getNumberOfThreads();
        }
        allContinuationMappings = new LinkedList<>(flows.getAllContinuationMappings());

        threadPool = Executors.newFixedThreadPool(numberOfThreads);

        initRoles();
    }

    @Override
    public void initRoles() {
        roles = new ArrayList<>();

        roles.add(new Role("All Flows", "all flows", flowDefinitionList.stream().map(FlowDefinition::getName).collect(Collectors.toSet())));
        roles.add(new Role("Read Only Flows", "all flows that are read only", flowDefinitionList.stream().filter(flow -> flow.checkIfFlowIsReadOnly()).map(FlowDefinition::getName).collect(Collectors.toSet())));
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
    public LinkedList<DTOContinuationMapping> getDTOAllContinuationMappingsWithSameSourceFlow(String currFlowName) {
        LinkedList<DTOContinuationMapping> dtoSortedContinuationMappings = new LinkedList<>();
        for (FlowContinuationMapping mapping : allContinuationMappings) {
            if (currFlowName.equals(mapping.getSourceFlow())) {
                dtoSortedContinuationMappings.add(new DTOContinuationMapping(mapping.getSource2targetDataMapping(), mapping.getSourceFlow(), mapping.getTargetFlow()));
            }
        }
        return dtoSortedContinuationMappings;
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
    public List<DTOFlowExeInfo>getDTOFlowsExecutionList() {
        List<DTOFlowExeInfo> dtoFlowExeInfoList = new LinkedList<>();
        for (FlowExecution flowExecution : flowExecutionList) {
            dtoFlowExeInfoList.add(new DTOFlowExeInfo(flowExecution.getUserName(),flowExecution.getFlowName() , flowExecution.getStartTime().toString() , flowExecution.getFlowExecutionResult(), getStatisticData()));
        }
        return dtoFlowExeInfoList;
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
    public DTOFlowExeInfo getAllFlowExecutionData(UUID flowId) {
        FlowExecution executedFlow = flowExecutionList.stream().filter(flow -> flow.getUniqueId().equals(flowId)).findFirst().get();
        List<DTOFreeInputs> freeInputs = new ArrayList<>();
        Map<String, Object > freeInputsValues = executedFlow.getFreeInputsValues();

        ////check if it's for outputs
        List<DTOSingleFlowIOData> IOlist = new LinkedList<>();
        for (SingleFlowIOData io :executedFlow.getIOlist()) {
            if(executedFlow.getDataValues().get(io.getFinalName()) == null && io.getType().equals(IO.INPUT) && !(io.getOptionalOutput().isEmpty())) {
                IOlist.add(new DTOSingleFlowIOData(io, executedFlow.getDataValues().get(io.getOptionalOutput().get(0).getFinalName())));
            }else{
                IOlist.add(new DTOSingleFlowIOData(io, executedFlow.getDataValues().get(io.getFinalName())));
            }
        }

        executedFlow.getFreeInputsList().forEach(io -> {
            DTOFreeInputs input = new DTOFreeInputs(io.getFinalName(),io.getDD().getType().getSimpleName(), io.getNecessity().toString(), freeInputsValues.get(io.getStepName() + "." + io.getOriginalName()));
            freeInputs.add(input);
        });

        List<DTOStepsInFlow> steps = new ArrayList<>();
        executedFlow.getStepExecutionDataList().forEach(io -> {

           List<DTOInput> inputs = new ArrayList<>();
            executedFlow.getIOlist().stream().filter(io1 -> io1.getStepName().equals(io.getFinalNameStep()))
                    .forEach(ioInput -> {
                                if (ioInput.getIOType().equals(IO.INPUT)) {
                                    Object inputValue = IOlist.stream().filter(i-> (i.getFinalName().equals(ioInput.getFinalName()) && i.getStepName().equals(ioInput.getStepName()))).findFirst().get().getValue();
                                   inputs.add(new DTOInput(ioInput.getOriginalName(),ioInput.getFinalName(),inputValue,ioInput.getStepName(), ioInput.getDD().toString()));
                                }
                            });

            List<DTOOutput> outputs = new ArrayList<>();
            executedFlow.getIOlist().stream().filter(io1 -> io1.getStepName().equals(io.getFinalNameStep()))
                    .forEach(ioOutput -> {
                        if (ioOutput.getIOType().equals(IO.OUTPUT)) {
                            Object outputValue = IOlist.stream().filter(i-> (i.getFinalName().equals(ioOutput.getFinalName()) && i.getStepName().equals(ioOutput.getStepName()))).findFirst().get().getValue();
                            outputs.add(new DTOOutput(ioOutput.getOriginalName(),ioOutput.getFinalName(), outputValue ,ioOutput.getStepName(), ioOutput.getDD().toString()));
                        }
                    });

            List <DTOLogger> loggers = new ArrayList<>();
            io.getLoggerList().stream().forEach(logger -> {
                loggers.add(new DTOLogger(logger));
            });

            steps.add(new DTOStepsInFlow(io.getOriginalName(),io.getFinalNameStep(),io.getResult(),io.getTotalStepTime(),inputs, outputs , loggers , io.getSummaryLine()));
        });

        List<DTOOutput> outputsOfFlow = new ArrayList<>();
        executedFlow.getIOlist().stream()
                .forEach(io -> {
                    if (io.getIOType().equals(IO.OUTPUT)) {
                        Object outputValue = IOlist.stream().filter(i-> (i.getFinalName().equals(io.getFinalName()))).findFirst().get().getValue();
                        outputsOfFlow.add(new DTOOutput(io.getOriginalName(),io.getFinalName(), outputValue , io.getStepName(), io.getDD().toString()));
                    }
                });


        DTOFlowExeInfo dto= new DTOFlowExeInfo(executedFlow.getUserName(),executedFlow.getFlowName(), executedFlow.getUniqueId().toString(),getStartTimeFormatted(executedFlow.getStartTime()),
                executedFlow.getTotalTime(), executedFlow.getFlowExecutionResult(), freeInputs, steps , outputsOfFlow );
        return dto;
    }
    @Override
    public DTOFlowExeInfo getAllFlowExecutionDataNyName(String flowName) {
        out.println("getAllFlowExecutionDataNyNameCCcff");
        FlowExecution executedFlow = flowExecutionList.stream().filter(flow -> flow.getFlowName().equals(flowName)).findFirst().get();
        out.println(executedFlow.getFlowName());
        List<DTOFreeInputs> freeInputs = new ArrayList<>();
        Map<String, Object > freeInputsValues = executedFlow.getFreeInputsValues();

        ////check if it's for outputs
        List<DTOSingleFlowIOData> IOlist = new LinkedList<>();
        for (SingleFlowIOData io :executedFlow.getIOlist()) {
            if(executedFlow.getDataValues().get(io.getFinalName()) == null && io.getType().equals(IO.INPUT) && !(io.getOptionalOutput().isEmpty())) {
                IOlist.add(new DTOSingleFlowIOData(io, executedFlow.getDataValues().get(io.getOptionalOutput().get(0).getFinalName())));
            }else{
                IOlist.add(new DTOSingleFlowIOData(io, executedFlow.getDataValues().get(io.getFinalName())));
            }
        }

        executedFlow.getFreeInputsList().forEach(io -> {
            DTOFreeInputs input = new DTOFreeInputs(io.getFinalName(),io.getDD().getType().getSimpleName(), io.getNecessity().toString(), freeInputsValues.get(io.getStepName() + "." + io.getOriginalName()));
            freeInputs.add(input);
        });
        List<DTOStepsInFlow> steps = new ArrayList<>();
        executedFlow.getStepExecutionDataList().forEach(io -> {

           List<DTOInput> inputs = new ArrayList<>();
            executedFlow.getIOlist().stream().filter(io1 -> io1.getStepName().equals(io.getFinalNameStep()))
                    .forEach(ioInput -> {
                                if (ioInput.getIOType().equals(IO.INPUT)) {
                                    Object inputValue = IOlist.stream().filter(i-> (i.getFinalName().equals(ioInput.getFinalName()) && i.getStepName().equals(ioInput.getStepName()))).findFirst().get().getValue();
                                    inputs.add(new DTOInput(ioInput.getOriginalName(),ioInput.getFinalName(),inputValue,ioInput.getStepName(), ioInput.getDD().toString()));
                                }
                            });
            List<DTOOutput> outputs = new ArrayList<>();
            executedFlow.getIOlist().stream().filter(io1 -> io1.getStepName().equals(io.getFinalNameStep()))
                    .forEach(ioOutput -> {
                        if (ioOutput.getIOType().equals(IO.OUTPUT)) {
                            Object outputValue = IOlist.stream().filter(i-> (i.getFinalName().equals(ioOutput.getFinalName()) && i.getStepName().equals(ioOutput.getStepName()))).findFirst().get().getValue();
                            outputs.add(new DTOOutput(ioOutput.getOriginalName(),ioOutput.getFinalName(), outputValue ,ioOutput.getStepName(), ioOutput.getDD().toString()));
                        }
                    });
            List <DTOLogger> loggers = new ArrayList<>();
            io.getLoggerList().stream().forEach(logger -> {
                loggers.add(new DTOLogger(logger));
            });
            steps.add(new DTOStepsInFlow(io.getOriginalName(),io.getFinalNameStep(),io.getResult(),io.getTotalStepTime(),inputs, outputs , loggers , io.getSummaryLine()));
        });
        List<DTOOutput> outputsOfFlow = new ArrayList<>();
        executedFlow.getIOlist().stream()
                .forEach(io -> {
                    if (io.getIOType().equals(IO.OUTPUT)) {
                        Object outputValue = IOlist.stream().filter(i-> (i.getFinalName().equals(io.getFinalName()))).findFirst().get().getValue();
                        outputsOfFlow.add(new DTOOutput(io.getOriginalName(),io.getFinalName(), outputValue , io.getStepName(), io.getDD().toString()));
                    }
                });


        return new DTOFlowExeInfo(executedFlow.getUserName(),executedFlow.getFlowName(), executedFlow.getUniqueId().toString(),getStartTimeFormatted(executedFlow.getStartTime()),
                executedFlow.getTotalTime(), executedFlow.getFlowExecutionResult(), freeInputs, steps , outputsOfFlow );
    }

    public String getStartTimeFormatted(Instant startTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.ofInstant(startTime, ZoneId.systemDefault());

        return localDateTime.format(formatter);
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
    public Map<String, Object> continuationFlowExecution(String sourceFlowName, String targetFlowName) {
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
    public List<Input> getValuesListFromContinuationMap(String sourceFlowName, String targetFlowName) {
        FlowDefinition targetFlow = flowDefinitionList.stream().filter(flow -> flow.getName().equals(targetFlowName)).findFirst().get();
        Map<String, Object> valuesMap = continuationFlowExecution(sourceFlowName, targetFlowName);
        List<Input> valuesList = new ArrayList<>();

        for (Map.Entry<String, Object> entry : valuesMap.entrySet()) {
            Input input = new Input();
            input.setFinalName(entry.getKey());
            input.setValue(entry.getValue());
            SingleFlowIOData IO = targetFlow.getIOlist().stream().filter(io -> io.getFinalName().equals(entry.getKey())).findFirst().get();
            input.setOriginalName(IO.getOriginalName());
            input.setStepName(IO.getStepName());
            input.setType(IO.getDD());
            input.setMandatory(IO.getNecessity().toString());
            valuesList.add(input);
        }

        return valuesList;
    }
    @Override
    public LinkedList<DTOInput> getDTOValuesListFromContinuationMap(String sourceFlowName, String targetFlowName) {
        FlowDefinition targetFlow = flowDefinitionList.stream().filter(flow -> flow.getName().equals(targetFlowName)).findFirst().get();
        Map<String, Object> valuesMap = continuationFlowExecution(sourceFlowName, targetFlowName);
        LinkedList<DTOInput> valuesList = new LinkedList<>();

        for (Map.Entry<String, Object> entry : valuesMap.entrySet()) {
            Input input = new Input();
            input.setFinalName(entry.getKey());
            input.setValue(entry.getValue());
            SingleFlowIOData IO = targetFlow.getIOlist().stream().filter(io -> io.getFinalName().equals(entry.getKey())).findFirst().get();
            input.setOriginalName(IO.getOriginalName());
            input.setStepName(IO.getStepName());
            input.setType(IO.getDD());
            input.setMandatory(IO.getNecessity().toString());
            valuesList.add(new DTOInput(input.getOriginalName(), input.getFinalName(), input.getValue(),input.getStepName() ,input.getType().toString()));
        }

        return valuesList;
    }
    @Override
    public UserManager getUserMangerObject() {
        return userManagerObject;
    }
    @Override
    public DTORolesList getDTORolesList() {return new DTORolesList(roles.stream().collect(Collectors.toSet()));}
    @Override
    public void addNewRole(DTORole role) {
        this.roles.add(new Role(role.getName(), role.getDescription(), role.getFlowsInRole()));
    }
    @Override
    public void updateFlowsInRole(DTORole dtoRole) {
        Role role = roles.stream().filter(r -> r.getName().equals(dtoRole.getName())).findFirst().get();
        role.setFlowsInRole(dtoRole.getFlowsInRole());
        role.setUsersInRole(dtoRole.getUsers());

        dtoRole.getUsers().forEach(user -> {
            UserDefinition user1 = userManagerObject.getUsers().stream().filter(u -> u.getUsername().equals(user)).findFirst().get();
            user1.addRole(role);
        });

        userManagerObject.getUsers().stream().forEach(user ->
        {
            boolean roleFound = user.getRoles().stream().anyMatch(rol -> rol.equals(dtoRole.getName()));
            boolean userFound = (role.getUsersInRole().stream().anyMatch(us -> us.equals(user.getUsername())));

            if (roleFound && !userFound) {
                user.getRoles().remove(dtoRole.getName());
            }
        });

    }
    @Override
    public DTORolesList getDTORolesListPerUser(String userName) {
        UserDefinition user = userManagerObject.getUsers().stream().filter(u -> u.getUsername().equals(userName)).findFirst().get();
        return new DTORolesList(user.getRoles());

    }
    @Override
    public DTOFlowsDefinitionInRoles getDtoFlowsDefinition(Boolean isManager ,List<String> rolesNames) {
        if(!isManager) {
            return getFlowsToUserFromRole(rolesNames);
        }
        return getFlowsToManager();
    }
    @Override
    public DTOFlowsDefinitionInRoles getFlowsToUserFromRole(List<String> rolesNames){
        List<Role> filteredRoles = new ArrayList<>();

        filteredRoles = roles.stream()
                .filter(role -> rolesNames.contains(role.getName()))
                .collect(Collectors.toList());


        List<FlowDefinition> flowDefinitionList = new ArrayList<>();

        filteredRoles.forEach(role -> {
            role.getFlowsInRole().forEach(flowName -> {
                FlowDefinition flowDefinition = this.flowDefinitionList.stream()
                        .filter(flowD -> flowD.getName().equals(flowName))
                        .findFirst()
                        .orElse(null); // Handle the case when the flow with the given name is not found
                if (flowDefinition != null) {
                    flowDefinitionList.add(flowDefinition);
                }
            });
        });

        Set<DTOFlowDefinitionInRoles> flowsInRoles = new HashSet<>();
        flowDefinitionList.stream().forEach(flow -> {

            DTOFlowDefinitionInRoles flows = new DTOFlowDefinitionInRoles(flow.getName(), flow.getDescription(), flow.getFlowSteps().size(), flow.getFlowFreeInputs().size(), flow.getNumOfContinuation(),
                    flow.getFlowReadOnly(), flow.getFlowFormalOutputs(), createDTOListStep(flow), createDTOListFlowOutputs(flow), createDTOListFreeInputs(flow));
            flowsInRoles.add(flows);
        });

        return new DTOFlowsDefinitionInRoles(flowsInRoles);
    }
    @Override
    public DTOFlowsDefinitionInRoles  getFlowsToManager(){
        Set<DTOFlowDefinitionInRoles> flowsInRoles = new HashSet<>();
        flowDefinitionList.stream().forEach(flow -> {
            DTOFlowDefinitionInRoles flows = new DTOFlowDefinitionInRoles(flow.getName(), flow.getDescription(), flow.getFlowSteps().size(), flow.getFlowFreeInputs().size(), flow.getNumOfContinuation(),
                    flow.getFlowReadOnly(), flow.getFlowFormalOutputs(), createDTOListStep(flow), createDTOListFlowOutputs(flow), createDTOListFreeInputs(flow));
            flowsInRoles.add(flows);
        });

        return new DTOFlowsDefinitionInRoles(flowsInRoles);
    }
    @Override
    public List<DTOStepUsageDeclaration> createDTOListStep(FlowDefinition flow) {
        List<DTOStepUsageDeclaration> stepUsageDeclarationList = new ArrayList<>();
        flow.getFlowSteps().forEach(step -> {
            DTOStepUsageDeclaration stepUsageDeclaration = new DTOStepUsageDeclaration(step.getStepDefinition().name(), step.getFinalStepName(), step.getStepDefinition().isReadonly());
            stepUsageDeclarationList.add(stepUsageDeclaration);
        });

        return stepUsageDeclarationList;
    }
    @Override
    public List<DTOFlowOutputs> createDTOListFlowOutputs(FlowDefinition flow) {
        List<DTOFlowOutputs> flowOutputsList = new ArrayList<>();
        flow.getIOlist().stream().filter(io -> io.getIOType().equals(IO.OUTPUT)).forEach(output -> {
            DTOFlowOutputs flowOutputs = new DTOFlowOutputs(output.getFinalName(), output.getDD().toString(), output.getStepName());
            flowOutputsList.add(flowOutputs);
        });

        return flowOutputsList;
    }
    @Override
    public List<DTOFreeInputs> createDTOListFreeInputs(FlowDefinition flow) {

        List<DTOFreeInputs> freeInputsList = new ArrayList<>();
        flow.getFlowFreeInputs().stream().forEach(node -> {
            List<String> stepOfInput = new ArrayList<>();
            flow.getListOfStepsWithCurrInput(node.getFinalName()).stream().forEach(step -> {
                stepOfInput.add(step);
            });

            DTOFreeInputs freeInputs = new DTOFreeInputs(node.getFinalName(), node.getDD().toString(), stepOfInput, node.getNecessity().toString());
            freeInputsList.add(freeInputs);
        });

        return freeInputsList;
    }
    @Override
    public List<DTOFlowFreeInputs> getDTOFlowFreeInputs(String flowName) {
        FlowDefinition flow = flowDefinitionList.stream().filter(flowD -> flowD.getName().equals(flowName)).findFirst().get();
        List<DTOFlowFreeInputs> freeInputsList = new ArrayList<>();
        flow.getFlowFreeInputs().stream().forEach(node -> {
            DTOFlowFreeInputs freeInputs = new DTOFlowFreeInputs(node.getFinalName(), node.getOriginalName(), node.getDD().toString(), node.getStepName(), node.getNecessity().toString());
            freeInputsList.add(freeInputs);
        });

        List<DTOFlowFreeInputs> sortedList = freeInputsList.stream()
                .sorted(Comparator.comparing(obj -> obj.getNecessity().equals(DataNecessity.MANDATORY.toString()) ? 0 : 1))
                .collect(Collectors.toList());
        return sortedList;
    }
    @Override
    public List<DTOInput> getDTOFlowFreeInputsToSrevlet(String flowName){
        FlowDefinition flow = flowDefinitionList.stream().filter(flowD -> flowD.getName().equals(flowName)).findFirst().get();
        List<DTOInput> freeInputsList = new ArrayList<>();
        FlowExecution executedFlow = flowExecutionList.stream().filter(f -> f.getFlowName().equals(flowName)).findFirst().get();
        Map<String, Object > freeInputsValues = executedFlow.getFreeInputsValues();

        flow.getFlowFreeInputs().stream().forEach(node -> {
            DTOInput freeInputs = new DTOInput(node.getOriginalName(),node.getFinalName(),freeInputsValues.get(node.getStepName() + "." + node.getOriginalName()) ,node.getStepName(),node.getDD().toString(), node.getNecessity().toString());
            freeInputsList.add(freeInputs);
        });

        List<DTOInput> sortedList = freeInputsList.stream()
                .sorted(Comparator.comparing(obj -> obj.getNecessity().equals(DataNecessity.MANDATORY.toString()) ? 0 : 1))
                .collect(Collectors.toList());
        return sortedList;
    }
    @Override
    public DTOAllFlowsNames getAllFlowsList() {
        Set<String> flowsList = new HashSet<>();
        flowDefinitionList.stream().forEach(flow -> {
            flowsList.add(flow.getName());
        });
        return new DTOAllFlowsNames(flowsList);
    }
    @Override
    public void updateUser(String userName, Set<String> checkedItems, Boolean isManager) {
        UserDefinition user = userManagerObject.getUsers().stream().filter(u -> u.getUsername().equals(userName)).findFirst().get();
        user.setManager(isManager);
        List<String> rolesNames = new ArrayList<>();
        checkedItems.stream().forEach(role -> {
            rolesNames.add(role);
        });

        List<Role> rolesList = new ArrayList<>();
            //create new roles list to user
            rolesNames.stream().forEach(role -> {
                Role role1 = roles.stream().filter(r -> r.getName().equals(role)).findFirst().get();
                rolesList.add(role1);

                //add user to new roles
                role1.getUsersInRole().add(userName);

            });

        //remove user from roles
        user.getRoles().stream().forEach(role ->
        {
            boolean roleFound = checkedItems.stream().anyMatch(newRole -> newRole.equals(role.getName()));
            if (!roleFound) {
                role.getUsersInRole().remove(userName);
            }
        });
        user.setRoles(rolesList);
    }
    @Override
    public Set<String> getUsersOfRoles(String roleName) {
        Role role = roles.stream().filter(r -> r.getName().equals(roleName)).findFirst().get();
        return role.getUsersInRole();
    }

}
