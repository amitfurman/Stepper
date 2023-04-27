import dto.*;
import exceptions.FileIsNotXmlTypeException;
import exceptions.FileNotExistsException;
import systemengine.systemengine;
import systemengine.systemengineImpl;
import xml.XmlValidator;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class consoleUI {
    private systemengine systemEngineInterface ;

    public consoleUI() {
        this.systemEngineInterface = new systemengineImpl();
    }

    public static void main(String[] args) {
        consoleUI console = new consoleUI();
        //DTOAllStepperFlows allStepperFlows = null;
        console.menu();
    }
    public void menu(){
        int choice = 0;
        do {
            System.out.println("Please choose an option:");
            System.out.println("1. Reading the system information file");
            System.out.println("2. Introducing the Flow definition");
            System.out.println("3. Flow activation (Execution)");
            System.out.println("4. Displaying full details of past activation");
            System.out.println("5. Statistics");
            System.out.println("6. Exiting the system");
            choice = readChoice();

            switch (choice) {
                case 1:
                    readingTheSystemInformationFile();
                    break;
                case 2:
                    introduceFlows();
                    break;
                case 3:
                    executeFlow();
                    break;
                case 4:
                    // Code to display past activation details
                    System.out.println("Displaying full details of past activation");
                    break;
                case 5:
                    // Code to display statistics
                    System.out.println("Statistics");
                    break;
                case 6:
                    exitProgram();
                    break;
                default:
                    System.out.println("Invalid choice, please choose again");
                    break;
            }
        } while (choice != 6);
    }
    public int readChoice() {
        Scanner input = new Scanner(System.in);
        int choice = input.nextInt();
        return choice;
    }
    public void readingTheSystemInformationFile() {
        Scanner scanner = new Scanner(System.in);
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
                System.out.println("XML file is valid");
            } catch (FileNotExistsException e) {
                System.out.println(e.getMessage() + "Please provide a valid XML file.");
            } catch (FileIsNotXmlTypeException e) {
                System.out.println(e.getMessage() + "Please provide a valid XML file.");
            }

            if (isFileValid) {
                validFilePath = filePath;
            }

            System.out.println("Do you want to load another file? (yes/no)");
            userInput = scanner.nextLine();
        } while (userInput.equalsIgnoreCase("yes") || userInput.equalsIgnoreCase("y"));

        if (validFilePath != null) {
            systemEngineInterface.cratingFlowFromXml(validFilePath);
        }
    }
    public void introduceFlows() {
        int flowNumber = choosingWhichFlowToIntroduce();
        IntroduceTheChosenFlow(flowNumber);
    }
    public int choosingWhichFlowToIntroduce() {
        DTOFlowsNames names = systemEngineInterface.printFlowsName();
        int flowNumber;
        boolean isValidChoice;
        do {
            System.out.println(names.getFlowName());
            flowNumber = getFlowChoice();
            isValidChoice = validFlowInputChoice(flowNumber, systemEngineInterface.getFlowDefinitionList().size());
        } while (flowNumber != 0 && !isValidChoice);
        return flowNumber;
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
    public int getFlowChoice() {
            System.out.println("Please select the flow number you want us to display its data: (press 0 to return to the main menu)");
            int userChoice = readChoice();
            return userChoice;
    }
    public boolean validFlowInputChoice(int choice, int numberOfFlows) {
        if (choice > numberOfFlows) {
            System.out.println("Invalid input! \nThe number chosen cannot be greater than the maximum flows that existing in the system." +
                    " You have to choose a number between 0 to " + numberOfFlows + ".\n");
        } else if (choice < 0) {
            System.out.println("Invalid input! The choice flow number must be a positive number. \n");
        } else {
            return true;
        }
        return false;
    }
    public StringBuilder printStepsInfo(DTOFlowDefinition flow) {
            AtomicInteger stepsIndex = new AtomicInteger(1);
            StringBuilder stepData = new StringBuilder();
            stepData.append("*The information for the steps in the current flow: *\n");
            flow
                    .getFlowStepsData()
                    .stream()
                    .forEach(node -> {
                        stepData.append("Step " + stepsIndex.getAndIncrement() + ": \n");
                        stepData.append("Original Name: " + node.getOriginalStepName() + '\n');
                        if (node.getFinalStepName() != node.getOriginalStepName()) {
                            stepData.append("Final Name: " + Objects.toString(node.getFinalStepName(), "") + "\n");
                    }
                    stepData.append("Is The Step Read Only? " + node.getIsReadOnly() + "\n\n");
                    });
        return stepData;
    }
    public StringBuilder printFreeInputsInfo(DTOFlowDefinition flow) {
        AtomicInteger freeInputsIndex = new AtomicInteger(1);
        StringBuilder inputData = new StringBuilder();
        inputData.append("*The information about free inputs in the current flow: *\n");
        flow
                .getFlowFreeInputs()
                .stream()
                .forEach(node -> {
                    inputData.append("Free Input " + freeInputsIndex.getAndIncrement() + ": \n");
                    inputData.append("Final Name: " + node.getFinalName() + '\n');
                    inputData.append("Type: " + node.getType().getName() + '\n');
                    inputData.append("Connected Steps: " + String.join(", ", flow.getListOfStepsWithCurrInput(node.getFinalName())) + '\n');
                    inputData.append("Mandatory / Optional: " + node.getNecessity() + "\n\n");
                });
        return inputData;
    }
    public StringBuilder printFlowOutputs(DTOFlowDefinition flow) {
        AtomicInteger flowOutputsIndex = new AtomicInteger(1);
        StringBuilder outputData = new StringBuilder();
        outputData.append("*The information about the flow outputs: *\n");
        flow.getIOlist()
                .stream().forEach(node -> {
                    if (flow.getFlowFormalOutputs().stream().anyMatch(output -> output.equals(node.getFinalName()))) {
                        outputData.append("Flow Outputs " + flowOutputsIndex.getAndIncrement() + ": \n");
                        outputData.append("Final Name: " + node.getFinalName() + '\n');
                        outputData.append("Type: " + node.getType().getName() + '\n');
                        outputData.append("Creating Step: " + node.getStepName() + "\n\n");
                    }
                });

        return outputData;
    }

    public void executeFlow() {
        Map<String, Object> freeInputMap = new HashMap<>();
        String userContinue;
        int flowNumber = choosingWhichFlowToIntroduce();
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
                int option = readChoice();
                switch (option) {
                    case 1:
                        DTOFreeInputsFromUser freeInputs = new DTOFreeInputsFromUser(freeInputMap);
                        DTOFlowExecution flowExecution = systemEngineInterface.activateFlow(flowNumber, freeInputs);
                        printInformationAboutFlowActivation(flowExecution);
                        isActivateFlow = true;

                        /////delete the map
                        return;
                    case 2:
                        askTheUserForInputValue(flowNumber, freeInputMap);
                        break;
                    case 3:
                        return;
                }
            }
            // if mandatory inputs are not received, show available inputs to the user
            else {
                do {
                    if(askTheUserForInputValue(flowNumber, freeInputMap)==0){
                        return;
                    }
                    System.out.println("Do you want to continue updating inputs? (yes/no)");
                    userContinue = readString();
                } while (userContinue.equals("yes"));
            }
        }
    }

    public int askTheUserForInputValue(int flowNumber, Map<String, Object> freeInputsMap){
        DTOFreeInputsByUserString freeInputsByUserString = systemEngineInterface.printFreeInputsByUserString(flowNumber);
        System.out.println(freeInputsByUserString.getFreeInputsByUserString());
        System.out.println("Please select the input number you want to update: (press 0 to return to the main menu)");
        int userFreeInputChoice = readChoice();
        if(userFreeInputChoice == 0){
            return userFreeInputChoice;
        }
        DTOSingleFlowIOData chosenInput = systemEngineInterface.getSpecificFreeInput(flowNumber, userFreeInputChoice);
        System.out.println("Please enter the new value for the input: " + chosenInput.getUserString());
        Object newValue = readObject();
        freeInputsMap.put(chosenInput.getFinalName(), newValue);
        System.out.println("The new value for the input: " + chosenInput.getUserString() + " is: " + newValue);
        return userFreeInputChoice;
    }

    public void printInformationAboutFlowActivation(DTOFlowExecution flowExecution) {
       AtomicInteger counter = new AtomicInteger(1);
        System.out.println("The flow has been activated successfully.");
        System.out.println("The flow outputs are: ");
        System.out.println("Flow ID: " + flowExecution.getUniqueId());
        System.out.println("Flow Name: " + flowExecution.getFlowName());
        System.out.println("Flow Result: " + flowExecution.getFlowExecutionResult());
        System.out.println("Flow Outputs: ");
        flowExecution.getFormalFlowOutputs().forEach(output -> {
            System.out.println("Output " + counter.getAndIncrement() + ":");
            System.out.println("Output Name: " + output);
            System.out.println(flowExecution
            System.out.println("Output Value: " +  flowExecution.getDataValues().get(output) + "\n"); //?
        });
    }

    public String readString() {
        Scanner scanner = new Scanner(System.in);
        return scanner.next();
    }

    public Object readObject() {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.next();

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

    public void exitProgram() {
        System.out.println("Thank you for using our system. See you later (:");
        System.exit(0);
    }

}


/*    private boolean hasAllMandatoryInputs(DTOFlowDefinition flow, Map<String, Object> freeInputMap) {
        for (DTOSingleFlowIOData input : flow.getFlowFreeInputs()) {
            boolean found = freeInputMap.keySet().stream().anyMatch(key -> key.equals(input.getFinalName()));
            if(!found){
                return false;
            }
        }
        return true;
    }*/


