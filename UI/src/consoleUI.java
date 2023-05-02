import dto.*;
import exceptions.*;
import flow.api.FlowIO.IO;
import steps.api.DataNecessity;
import systemengine.systemengine;
import systemengine.systemengineImpl;
import xml.XmlValidator;

import javax.xml.bind.JAXBException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class consoleUI {
    private final systemengine systemEngineInterface ;

    public consoleUI() {
        this.systemEngineInterface = new systemengineImpl();
    }
    public static void main(String[] args) {
        consoleUI console = new consoleUI();
        console.menu();
    }
    public void menu() {
        System.out.println(" _       __     __                             ______         _____ __                            \n" +
                "| |     / /__  / /________  ____ ___  ___     /_  __/___     / ___// /____  ____  ____  ___  _____\n" +
                "| | /| / / _ \\/ / ___/ __ \\/ __ `__ \\/ _ \\     / / / __ \\    \\__ \\/ __/ _ \\/ __ \\/ __ \\/ _ \\/ ___/\n" +
                "| |/ |/ /  __/ / /__/ /_/ / / / / / /  __/    / / / /_/ /   ___/ / /_/  __/ /_/ / /_/ /  __/ /    \n" +
                "|__/|__/\\___/_/\\___/\\____/_/ /_/ /_/\\___/    /_/  \\____/   /____/\\__/\\___/ .___/ .___/\\___/_/     \n" +
                "                                                                        /_/   /_/               ");
        int choice = 0;
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.println("\nPlease choose an option:");
            System.out.println("1. Reading the system information file");
            System.out.println("2. Introducing the Flow definition");
            System.out.println("3. Flow activation (Execution)");
            System.out.println("4. Displaying full details of past activation");
            System.out.println("5. Statistics");
            System.out.println("6. Saving the system information to a file");
            System.out.println("7. Loading the system information from a file");
            System.out.println("8. Exiting the system");
            choice = readChoice(scanner,8);

            if (choice != 1 && choice != 7 && choice != 8 && systemEngineInterface.getFlowDefinitionList().isEmpty()) {
                System.out.println("You can't choose this option because there isn't activation flow in the system.");
            } else {
                switch (choice) {
                    case 1:
                        readingTheSystemInformationFile(scanner);
                        break;
                    case 2:
                        introduceFlows(scanner);
                        break;
                    case 3:
                        executeFlow(scanner);
                        break;
                    case 4:
                        displayPastFlowActivationDetails(scanner);
                        break;
                    case 5:
                        displayStatistics();
                        break;
                    case 6:
                        saveToFile(scanner);
                        break;
                    case 7:
                        loadFromFile(scanner);
                        break;
                    case 8:
                        exitProgram();
                        break;
                    default:
                        System.out.println("Invalid choice, please choose again");
                        break;
                }
            }
        } while (choice != 8) ;
    }


    public int readChoice(Scanner scanner, int maxChoice) {
        boolean validInput = false;
        int choice = 0;
        do {
            if (scanner.hasNextInt()) {
                choice = Integer.parseInt(scanner.nextLine());

                if (choice >= 0 && choice <= maxChoice) {
                    validInput = true;
                } else if(choice < 0){
                    System.out.println("Invalid choice, please enter a positive number");
                } else {
                    System.out.println("Invalid choice, please enter a number between 1 to " + maxChoice);
                }
            } else {
                System.out.println("Error: You must enter a number, please try again");
                scanner.nextLine();
            }
        } while (!validInput);

        return choice;
    }
    public String readYesNoInput(Scanner scanner) {
        boolean validInput = false;
        String input = null;
        do {
            input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("no")) {
                validInput = true;
            } else if (input.matches("-?\\d+(\\.\\d+)?")) {
                System.out.println("Error: You must enter a string and not a number, please try again");
            } else {
                System.out.println("Error: You must enter a 'yes' or 'no' string, please try again");
            }
        } while (!validInput);
        return input;
    }
    public void readingTheSystemInformationFile(Scanner scanner) {
        XmlValidator validator = new XmlValidator();
        boolean isFileValid = false;
        String filePath, validFilePath = null, userInput;

        do {
            isFileValid = false;
            System.out.println("Please enter the path to the XML file:");
            filePath = scanner.nextLine();

            try {
                validator.isXmlFileValid(filePath);
                isFileValid = true;
                System.out.println();
                System.out.println("The path of the file is valid");
            } catch (FileNotExistsException e) {
                System.out.println(e.getMessage() + "Please provide a valid XML file.");
            } catch (FileIsNotXmlTypeException e) {
                System.out.println(e.getMessage() + "Please provide a valid XML file.");
            }

            if (isFileValid) {
                validFilePath = filePath;
            }

            if (validFilePath != null) {
                try {
                    systemEngineInterface.cratingFlowFromXml(validFilePath);
                } catch (JAXBException | FileNotFoundException e) {
                    e.printStackTrace();
                } catch (DuplicateFlowsNames e) {
                    System.out.println(e.getMessage());
                } catch (UnExistsStep e) {
                    System.out.println(e.getMessage());
                } catch (OutputsWithSameName e){
                    System.out.println(e.getMessage());
                } catch (MandatoryInputsIsntUserFriendly e){
                    System.out.println(e.getMessage());
                } catch (UnExistsData e) {
                    System.out.println(e.getMessage());
                } catch (SourceStepBeforeTargetStep e) {
                    System.out.println(e.getMessage());
                } catch (TheSameDD e) {
                    System.out.println(e.getMessage());
                } catch (UnExistsOutput e) {
                    System.out.println(e.getMessage());
                } catch (FreeInputsWithSameNameAndDifferentType e) {
                    System.out.println(e.getMessage());
                }

            }

            System.out.println("Do you want to load another file? (yes/no)");
            userInput = readYesNoInput(scanner);
        } while (userInput.equalsIgnoreCase("yes"));


    }
    public void introduceFlows(Scanner scanner) {
        int flowNumber = choosingWhichFlowToIntroduce(scanner);
        if(flowNumber == 0){
            return;
        }
        IntroduceTheChosenFlow(flowNumber);
    }
    public int choosingWhichFlowToIntroduce(Scanner scanner) {
        DTOFlowsNames names = systemEngineInterface.printFlowsName();
        int flowNumber;
        System.out.println(names.getFlowName());
        return flowNumber = getFlowChoice(scanner,systemEngineInterface.getFlowDefinitionList().size());

    }
    public void IntroduceTheChosenFlow(int flowNumber) {
        DTOFlowDefinition currFlowDefinition= systemEngineInterface.IntroduceTheChosenFlow(flowNumber);
        StringBuilder flowData = new StringBuilder();
        flowData.append("Flow Name: " + currFlowDefinition.getName() + "\n");
        flowData.append("Flow Description: " + currFlowDefinition.getDescription() + '\n');
        flowData.append("Flow Formal Outputs: " + String.join(", ", currFlowDefinition.getFlowFormalOutputs()) + '\n');
        flowData.append("Is The Flow Read Only? " + currFlowDefinition.getFlowReadOnly() + "\n\n");
        flowData.append(printStepsInfo(currFlowDefinition));
        flowData.append(printFreeInputsInfo(currFlowDefinition));
        flowData.append(printFlowOutputs(currFlowDefinition));

        System.out.println(flowData);
    }
    public int getFlowChoice(Scanner scanner, int maxChoice) {
            System.out.println("Please select the flow number you want us to display its data: (press 0 to return to the main menu)");
            return readChoice(scanner,maxChoice);
    }
    public StringBuilder printStepsInfo(DTOFlowDefinition flow) {
            AtomicInteger stepIndex = new AtomicInteger(1);
            StringBuilder stepData = new StringBuilder();
            stepData.append("*The information for the steps in the current flow: *\n");
            flow
                    .getFlowStepsData()
                    .stream()
                    .forEach(node -> {
                        stepData.append("Step " + stepIndex.getAndIncrement() + ": \n");
                        stepData.append("\tOriginal Name: " + node.getOriginalStepName() + '\n');
                        if (!(node.getFinalStepName().equals(node.getOriginalStepName()))) {
                            stepData.append("\tFinal Name: " + Objects.toString(node.getFinalStepName(), "") + "\n");
                    }
                    stepData.append("\tIs The Step Read Only? " + node.getIsReadOnly() + "\n\n");
                    });
        return stepData;
    }
    public StringBuilder printFreeInputsInfo(DTOFlowDefinition flow) {
        AtomicInteger freeInputIndex = new AtomicInteger(1);
        StringBuilder inputData = new StringBuilder();
        inputData.append("*The information about free inputs in the current flow: *\n");
        flow
                .getFlowFreeInputs()
                .stream()
                .forEach(node -> {
                    inputData.append("Free Input " + freeInputIndex.getAndIncrement() + ": \n");
                    inputData.append("\tFinal Name: " + node.getFinalName() + '\n');
                    inputData.append("\tType: " + node.getType().getName() + '\n');
                    inputData.append("\tConnected Steps: " + String.join(", ", flow.getListOfStepsWithCurrInput(node.getFinalName())) + '\n');
                    inputData.append("\tMandatory / Optional: " + node.getNecessity() + "\n\n");
                });
        return inputData;
    }
    public StringBuilder printFlowOutputs(DTOFlowDefinition flow) {
        AtomicInteger flowOutputIndex = new AtomicInteger(1);
        StringBuilder outputData = new StringBuilder();
        outputData.append("*The information about the flow outputs: *\n");
        flow.getIOlist()
                .stream().forEach(node -> {
                    if (flow.getFlowFormalOutputs().stream().anyMatch(output -> output.equals(node.getFinalName()))) {
                        outputData.append("Flow Outputs " + flowOutputIndex.getAndIncrement() + ": \n");
                        outputData.append("\tFinal Name: " + node.getFinalName() + '\n');
                        outputData.append("\tType: " + node.getType().getName() + '\n');
                        outputData.append("\tCreating Step: " + node.getStepName() + "\n\n");
                    }
                });

        return outputData;
    }
    public void executeFlow(Scanner scanner) {
        Map<String, Object> freeInputMap = new HashMap<>();
        String userContinue;
        int flowNumber = choosingWhichFlowToIntroduce(scanner);
        if(flowNumber == 0){
            return;
        }
        boolean isActivateFlow = false;

        while (!isActivateFlow) {
            // check if all mandatory inputs are received
            boolean hasAllMandatoryInputs = systemEngineInterface.hasAllMandatoryInputs(flowNumber, freeInputMap);

            // if all mandatory inputs are received, offer options to the user
            if (hasAllMandatoryInputs) {
                System.out.println("All mandatory inputs have been received.");
                System.out.println("1. Continue to activate the flow.");
                System.out.println("2. Update an input.");
                System.out.println("3. Return to the main menu.");
                int option = readChoice(scanner, 3);
                switch (option) {
                    case 1:
                        DTOFreeInputsFromUser freeInputs = new DTOFreeInputsFromUser(freeInputMap);
                        DTOFlowExecution flowExecution = systemEngineInterface.activateFlow(flowNumber, freeInputs);
                        printInformationAboutFlowActivation(flowExecution);
                        isActivateFlow = true;
                        return;
                    case 2:
                        askTheUserForInputValue(flowNumber, freeInputMap, scanner);
                        break;
                    case 3:
                        return;
                }
            }
            else {
                   System.out.println("Not all mandatory inputs have been received.");
                do {
                    if(askTheUserForInputValue(flowNumber, freeInputMap, scanner)==0){
                        return;
                    }
                    System.out.println("Do you want to continue updating inputs? (yes/no)");
                    userContinue = readYesNoInput(scanner);
                } while (userContinue.equals("yes"));
            }
        }
    }
    public int askTheUserForInputValue(int flowNumber, Map<String, Object> freeInputsMap, Scanner scanner) {
        DTOFreeInputsByUserString freeInputsByUserString = systemEngineInterface.printFreeInputsByUserString(flowNumber);
        System.out.println(freeInputsByUserString.getFreeInputsByUserString());
        System.out.println("Please select the input number you want to update: (press 0 to return to the main menu)");
        int userFreeInputChoice = readChoice(scanner,freeInputsByUserString.getNumberOfFreeInputs());
        if(userFreeInputChoice == 0){
            return userFreeInputChoice;
        }
        DTOSingleFlowIOData chosenInput = systemEngineInterface.getSpecificFreeInput(flowNumber, userFreeInputChoice);
        System.out.println("Please enter the new value for the input: " + chosenInput.getUserString());
        boolean isSameType = true;
        Object newValue;
        do{
           newValue = readObject(scanner);
           isSameType = validFreeInputValue(newValue, chosenInput);
       }while(!isSameType);
        freeInputsMap.put(chosenInput.getFinalName(), newValue);
        System.out.println("The new value for the input: " + chosenInput.getUserString() + " is: " + newValue);
        return userFreeInputChoice;
    }
    public boolean validFreeInputValue(Object newValue,DTOSingleFlowIOData chosenInput) {

        if(!chosenInput.getType().getType().getSimpleName().equals(newValue.getClass().getSimpleName())) {
            System.out.println("The value you entered is not valid for the input: " + chosenInput.getUserString());
            System.out.println("The input type is: " + chosenInput.getType().getName());
            System.out.println("Please enter a new value: ");
            return false;
        }
        return true;
    }
    public void printInformationAboutFlowActivation(DTOFlowExecution flowExecution) {
       AtomicInteger flowIndex = new AtomicInteger(1);
        System.out.println("The flow has been activated successfully.");
        System.out.println("The flow outputs are: ");
        System.out.println("Flow ID: " + flowExecution.getUniqueId());
        System.out.println("Flow Name: " + flowExecution.getFlowName());
        System.out.println("Flow Result: " + flowExecution.getFlowExecutionResult());
        System.out.println("Flow Outputs: \n");
        flowExecution.getFlowOutputExecutionList().forEach(output -> {
            System.out.println("Output " + flowIndex.getAndIncrement() + ":");
            System.out.println("\t" + output.getUserString());
            if(output.getValue() != null){
                System.out.println("\tValue: " +  output.getValue().toString());
            }
            else {
                System.out.println("\tValue: Not created due to failure in flow");
            }
        });
    }
    public Object readObject(Scanner scanner) {
        String input = scanner.nextLine();

        try {
            return Integer.parseInt(input); // try to parse input as an integer
        } catch (NumberFormatException e1) {
            try {
                return Double.parseDouble(input); // try to parse input as a double
            } catch (NumberFormatException e2) {
                // if parsing as integer or double fails, return input as a string
                return input;
            }
        }
    }
    public void displayPastFlowActivationDetails(Scanner scanner) {
        DTOFlowsExecutionList flowsExecutionList = systemEngineInterface.getFlowsExecutionList();
        AtomicInteger flowIndex = new AtomicInteger(1);
        System.out.println("Flows executed:");
        for (DTOFlowExecution flow: flowsExecutionList.getFlowsExecutionNamesList()) {
            System.out.println("Flow " + flowIndex.getAndIncrement() + ": ");
            System.out.println("Flow Name: " + flow.getFlowName());
            System.out.println("Flow ID: " + flow.getUniqueId());
            System.out.println("Flow Start Time: " + flow.getStartTimeFormatted());
            System.out.println("\n");
        }
        int flowNumber = getFlowChoice(scanner,flowsExecutionList.getFlowsExecutionNamesList().size());
        if(flowNumber == 0){
            return;
        }
        DTOFlowExecution flowExecution = systemEngineInterface.getFlowExecutionDetails(flowNumber);
        printFlowExecutionDetails(flowExecution);
    }
    public void printFlowExecutionDetails(DTOFlowExecution flowExecution) {
        printFlowInfo(flowExecution);
        printFreeInputData(flowExecution);
        printStepsOutputs(flowExecution);
        printStepsData(flowExecution);
    }
    public void printFlowInfo(DTOFlowExecution flowExecution) {
        System.out.println("Flow ID: " + flowExecution.getUniqueId());
        System.out.println("Flow Name: " + flowExecution.getFlowName());
        System.out.println("Flow Result: " + flowExecution.getFlowExecutionResult());
        System.out.println(String.format("Total Running Time: %d ms", flowExecution.getTotalTime().toMillis()));
        System.out.print("\n");
    }
    public void printFreeInputData(DTOFlowExecution flowExecution){
        AtomicInteger freeInputIndex = new AtomicInteger(1);
        System.out.println("Flow's Free Inputs: ");
        List<DTOSingleFlowIOData> sortedList = flowExecution.getFreeInputsList().stream()
                .sorted(Comparator.comparing(obj -> obj.getNecessity().equals(DataNecessity.MANDATORY) ? 0 : 1))
                .collect(Collectors.toList());

        sortedList.stream().forEach(input -> {
            System.out.println("Free Input " + freeInputIndex.getAndIncrement() + ":" );
            System.out.println("\tFinal Name:" + input.getFinalName());
            System.out.println("\tType:" + input.getType());
            if (input.getValue() != null) {
                System.out.println("\tValue: " + input.getValue().toString());
            } else {
                System.out.println("\tValue: N/A");
            }
            System.out.println("\tIs Mandatory / Optional: " + input.getNecessity());
        });
        System.out.print("\n");
    }
    public void printStepsOutputs(DTOFlowExecution flowExecution){
        System.out.println("Flow's Outputs: ");
        AtomicInteger outputIndex = new AtomicInteger(1);
        List<DTOSingleFlowIOData> outputs = flowExecution.getIOlist().stream().filter(io -> io.getIOType().equals(IO.OUTPUT)).collect(Collectors.toList());
        for(DTOSingleFlowIOData output: outputs) {
            System.out.println("Output " + outputIndex.getAndIncrement() + ":");
            System.out.println("\tFinal Name:" + output.getFinalName());
            System.out.println("\tType:" + output.getType());
            if (output.getValue() != null) {
                System.out.println("\tValue: " + output.getValue().toString());
            } else {
                System.out.println("\tValue: Not created due to failure in flow");
            }
        }
        System.out.print("\n");
    }
    public void printStepsData(DTOFlowExecution flowExecution){
        AtomicInteger stepIndex = new AtomicInteger(1);
        System.out.println("Flow's Steps: ");
        flowExecution.getStepExecutionDataList().stream().forEach(step -> {
            System.out.println("Step Number " + stepIndex.getAndIncrement()+ ":");
            if(step.getFinalNameStep().equals(step.getOriginalName())){
                System.out.println("\tStep Name:" + step.getOriginalName());
            }
            else {
                System.out.println("\tStep Name:" + step.getOriginalName() + " (renamed to " + step.getFinalNameStep() + ")");
            }
            System.out.println("\tTotal Running Time: " +  step.getTotalStepTime().toMillis() + " ms");
            System.out.println("\tStep Result: " +  step.getResult());
            System.out.println("\tStep Summery Line: " +  step.getSummaryLine());
            System.out.println("\tStep's Logs:");
            AtomicInteger logIndex = new AtomicInteger(1);
            step.getLoggerList().forEach(log -> {
                System.out.println("\tLog " + logIndex.getAndIncrement() + ":");
                System.out.println("\t\tLog Time: " + log.getLogTime());
                System.out.println("\t\tLog Message: " + log.getLog());
            });
        });
    }
    public void displayStatistics(){
        DTOFlowAndStepStatisticData statisticData = systemEngineInterface.getStatisticData();
        AtomicInteger flowIndex = new AtomicInteger(1);
        System.out.println("Flows Statistics:");
        for (DTOStatisticData flow: statisticData.getFlowsStatisticData()) {
            System.out.println("Flow " + flowIndex.getAndIncrement() + ": ");
            System.out.println("\tNumber Of Run Times: " + flow.getTimesRun());
            System.out.println("\tAverage Run Time: " + flow.getAverageTime());
        }

        AtomicInteger stepIndex = new AtomicInteger(1);
        System.out.println("Steps Statistics:");
        for (DTOStatisticData step: statisticData.getStepsStatisticData()) {
            System.out.println("Step " + stepIndex.getAndIncrement() + ": ");
            System.out.println("\tNumber Of Run Times: " + step.getTimesRun());
            System.out.println("\tAverage Run Time: " + step.getAverageTime());
        }
    }

    public void loadFromFile(Scanner scanner)  {
        System.out.println("Please enter the path of the file you want to load from:");
        String path = scanner.nextLine();
        FileInputStream file = null;
        try {
            file = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found at " + path);
            return;
        }
        systemEngineInterface.loadFromFile(path);
    }

    public void saveToFile(Scanner scanner) {
        System.out.println("Please enter the path of the file you want to save to: (including the name of the file but without the extension ");
        String path = scanner.nextLine();
        systemEngineInterface.saveToFile(path);
    }

    public void exitProgram() {
        System.out.println("Thank you for using our system. See you later (:");
        System.exit(0);
    }
    
}



