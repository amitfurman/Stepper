package systemengine;

import dto.*;
import exceptions.*;
import flow.api.FlowDefinition;
import flow.api.FlowIO.IO;
import flow.api.FlowIO.SingleFlowIOData;
import flow.execution.FlowExecution;
import flow.execution.runner.FlowExecutor;
import flow.impl.FlowsManager;
import flow.mapping.FlowContinuationMapping;
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
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class systemengineImpl implements systemengine {
    private static systemengineImpl instance;
    public LinkedList<FlowDefinition> flowDefinitionList;
    public LinkedList<FlowExecution> flowExecutionList;
    public FlowAndStepStatisticData statisticData;
    public ExecutorService threadPool;
    public int numberOfThreads;
    public LinkedList<FlowContinuationMapping> allContinuationMappings;
    public UserManager userManagerObject ;
    public List<Role> roles;

    public systemengineImpl() {
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
    public void cratingFlowFromXml(InputStream inputStream) throws DuplicateFlowsNames, JAXBException, UnExistsStep, FileNotFoundException, OutputsWithSameName, MandatoryInputsIsntUserFriendly, UnExistsData, SourceStepBeforeTargetStep, TheSameDD,
            UnExistsOutput, FreeInputsWithSameNameAndDifferentType, InitialInputIsNotExist, UnExistsFlow, UnExistsDataInTargetFlow, FileNotExistsException, FileIsNotXmlTypeException {
        SchemaBasedJAXBMain schema = new SchemaBasedJAXBMain();
        FlowsManager flows = schema.schemaBasedJAXB(inputStream);
        //flowDefinitionList.addAll(flows.getAllFlows());
        flowDefinitionList = flows.getAllFlows();
        numberOfThreads = flows.getNumberOfThreads();
        allContinuationMappings = new LinkedList<>(flows.getAllContinuationMappings());
        threadPool = Executors.newFixedThreadPool(numberOfThreads);
        initRoles();
    }
    @Override
    public void initRoles() {
        roles = new ArrayList<>();

        roles.add(new Role("All Flows", "all flows", flowDefinitionList.stream().map(FlowDefinition::getName).collect(Collectors.toSet())));
        roles.add(new Role("Read Only Flows", "all flows that are read only",flowDefinitionList.stream().filter(flow -> flow.checkIfFlowIsReadOnly()).map(FlowDefinition::getName).collect(Collectors.toSet())));
        System.out.println("Roles initialized");
        System.out.println("All Flows: " + roles.get(1).getFlowsInRole());
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
            input.setValue(entry.getValue());
            SingleFlowIOData IO = targetFlow.getIOlist().stream().filter(io->io.getFinalName().equals(entry.getKey())).findFirst().get();
            input.setOriginalName(IO.getOriginalName());
            input.setStepName(IO.getStepName());
            input.setType(IO.getDD());
            input.setMandatory(IO.getNecessity().toString());
            valuesList.add(input);
        }

            return valuesList;
    }
    @Override
    public UserManager getUserMangerObject(){
        return userManagerObject;
    }
    @Override
    public DTORolesList getDTORolesList(){
        return new DTORolesList(roles);
    }
    @Override
    public void addNewRole(DTORole role){
        this.roles.add(new Role(role.getName(), role.getDescription(), role.getFlowsInRole()));
    }
    @Override
    public void updateFlowsInRole(DTORole dtoRole) {
        Role role = roles.stream().filter(r -> r.getName().equals(dtoRole.getName())).findFirst().get();
        System.out.println("1" + dtoRole.getName());
        System.out.println("2" + dtoRole.getFlowsInRole());
        System.out.println("3" +role.getName());
        System.out.println("4" +role.getFlowsInRole());
        role.setFlowsInRole(dtoRole.getFlowsInRole());
        role.setUsersInRole(dtoRole.getUsers());

        dtoRole.getUsers().forEach(user -> {
            UserDefinition user1 = userManagerObject.getUsers().stream().filter(u -> u.getUsername().equals(user)).findFirst().get();
            user1.addRole(role.getName());
            System.out.println(user1.getUsername());
            System.out.println(user1.getRoles());
        });


      userManagerObject.getUsers().stream().forEach(user ->
        {
            //if the curr user have dtoRole
            boolean roleFound = user.getRoles().stream().anyMatch(rol-> rol.equals(dtoRole.getName()));
            //if the update role dont have the curr user
            boolean userFound =  (role.getUsersInRole().stream().anyMatch(us-> us.equals(user.getUsername())));

            if(roleFound && !userFound){
                user.getRoles().remove(dtoRole.getName());
            }

        });

    }
    @Override
    public DTORolesList getDTORolesListPerUser(String userName){
        UserDefinition user = userManagerObject.getUsers().stream().filter(u -> u.getUsername().equals(userName)).findFirst().get();
        List<Role> rolesList = new ArrayList<>();
        user.getRoles().forEach(role -> {
            Role role1 = roles.stream().filter(r -> r.getName().equals(role)).findFirst().get();
            rolesList.add(role1);
        });
        System.out.println(user.getUsername() + " " + user.getRoles());
        return new DTORolesList(rolesList);
    }
    @Override
    public DTOFlowsDefinitionInRoles getDtoFlowsDefinition(List<String> rolesNames) {
        List<Role> filteredRoles = roles.stream()
                .filter(role -> rolesNames.contains(role.getName()))
                .collect(Collectors.toList());

        System.out.println("filteredRoles: " + filteredRoles);
        System.out.println(filteredRoles.get(0).getFlowsInRole());
        List<FlowDefinition> flowDefinitionList = new ArrayList<>();

        System.out.println("roles in system: " + roles);


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
                    flow.getFlowReadOnly(),flow.getFlowFormalOutputs(),createDTOListStep(flow),createDTOListFlowOutputs(flow), createDTOListFreeInputs(flow));
            flowsInRoles.add(flows);
        });

        System.out.println("1" + flowsInRoles);
        return new DTOFlowsDefinitionInRoles(flowsInRoles);
    }
    @Override
    public List<DTOStepUsageDeclaration> createDTOListStep(FlowDefinition flow){
        List<DTOStepUsageDeclaration> stepUsageDeclarationList = new ArrayList<>();
        flow.getFlowSteps().forEach(step -> {
            DTOStepUsageDeclaration stepUsageDeclaration = new DTOStepUsageDeclaration(step.getStepDefinition().name(),step.getFinalStepName(), step.getStepDefinition().isReadonly());
            stepUsageDeclarationList.add(stepUsageDeclaration);
        });

        return stepUsageDeclarationList;
    }
    @Override
    public List<DTOFlowOutputs> createDTOListFlowOutputs(FlowDefinition flow){
        List<DTOFlowOutputs> flowOutputsList = new ArrayList<>();
        flow.getIOlist().stream().filter(io-> io.getIOType().equals(IO.OUTPUT)).forEach(output -> {
            DTOFlowOutputs flowOutputs = new DTOFlowOutputs(output.getFinalName(),output.getDD().toString(),output.getStepName());
            flowOutputsList.add(flowOutputs);
        });

        return flowOutputsList;
    }

    @Override
    public  List<DTOFreeInputs> createDTOListFreeInputs(FlowDefinition flow){

        List<DTOFreeInputs> freeInputsList = new ArrayList<>();
        flow.getFlowFreeInputs().stream().forEach(node -> {
            List<String> stepOfInput = new ArrayList<>();
            flow.getListOfStepsWithCurrInput(node.getFinalName()).stream().forEach(step -> {
                stepOfInput.add(step);
            });

            DTOFreeInputs freeInputs = new DTOFreeInputs(node.getFinalName(),node.getDD().toString(),stepOfInput, node.getNecessity().toString());
            freeInputsList.add(freeInputs);
        });

        return freeInputsList;
    }

    @Override
    public List<DTOFlowFreeInputs> getDTOFlowFreeInputs(String flowName) {
        FlowDefinition flow = flowDefinitionList.stream().filter(flowD -> flowD.getName().equals(flowName)).findFirst().get();
        List<DTOFlowFreeInputs> freeInputsList = new ArrayList<>();
        flow.getFlowFreeInputs().stream().forEach(node -> {
            DTOFlowFreeInputs freeInputs = new DTOFlowFreeInputs(node.getFinalName(), node.getOriginalName(), node.getDD().toString(),node.getStepName(), node.getNecessity().toString());
            freeInputsList.add(freeInputs);
        });

        List<DTOFlowFreeInputs> sortedList = freeInputsList.stream()
                .sorted(Comparator.comparing(obj -> obj.getNecessity().equals(DataNecessity.MANDATORY) ? 0 : 1))
                .collect(Collectors.toList());

        return sortedList;
    }

    @Override
    public DTOAllFlowsNames getAllFlowsList() {
        System.out.println("in system");
        Set<String> flowsList = new HashSet<>();
        System.out.println(flowDefinitionList);
        flowDefinitionList.stream().forEach(flow -> {
            flowsList.add(flow.getName());
        });
        System.out.println(flowsList);
        return new DTOAllFlowsNames(flowsList);
    }

/*    @Override
    public DTOFlowsDefinitionInRoles getDtoFlowsDefinition(List<String> rolesNames){

        List<FlowDefinition> flowDefinitionList = new ArrayList<>();

        roles.stream().forEach(role->rolesNames.stream().anyMatch());


        roles.stream().filter(role->role.getName().equals).forEach(role -> {
            role.forEach(flowName -> {
                FlowDefinition flowDefinition = this.flowDefinitionList.stream().filter(flow -> flow.getName().equals(flowName)).findFirst().get();
                flowDefinitionList.add(flowDefinition);
            });
        });
        Set<DTOFlowDefinitionInRoles> flowsInRoles = new HashSet<>();
        flowDefinitionList.stream().forEach(flow -> {
            DTOFlowDefinitionInRoles flows = new DTOFlowDefinitionInRoles(flow.getName(), flow.getDescription(), flow.getFlowSteps().size(), flow.getFlowFreeInputs().size(), flow.getNumOfContinuation());
            flowsInRoles.add(flows);
            });

        System.out.println(flowsInRoles);
        return new DTOFlowsDefinitionInRoles(flowsInRoles);
    }*/
}
