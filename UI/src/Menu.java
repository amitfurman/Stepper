import dto.DTOAllStepperFlows;
import dto.DTOFlowDefinition;
import dto.DTOFlowDefinitionImpl;
import exceptions.FileIsNotXmlTypeException;
import exceptions.FileNotExistsException;
import jaxb.schema.SchemaBasedJAXBMain;
import xml.XmlValidator;

import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class Menu {
    public static void main(String[] args) {
        DTOAllStepperFlows allStepperFlows = null;
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
                    allStepperFlows = readingTheSystemInformationFile();
                    break;
                case 2:
                    int flowNumber = choosingWhichFlowToIntroduce(allStepperFlows);
                    IntroduceTheChosenFlow(flowNumber, allStepperFlows);
                    break;
                case 3:
                    // Code to activate flow
                    System.out.println("Flow activation (Execution)");
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
                    System.out.println("Exiting the system");
                    break;
                default:
                    System.out.println("Invalid choice, please choose again");
                    break;
            }
        } while (choice != 6);
    }

    public static int readChoice() {
        Scanner input = new Scanner(System.in);
        int choice = input.nextInt();
        return choice;
    }

    public static DTOAllStepperFlows readingTheSystemInformationFile() {
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
            SchemaBasedJAXBMain schema = new SchemaBasedJAXBMain();
            return schema.schemaBasedJAXB(validFilePath);
        }
        return null;
    }

    public static int choosingWhichFlowToIntroduce(DTOAllStepperFlows allStepperFlows) {
        printFlowsName(allStepperFlows);
        int flowNumber;
        boolean isValidChoice;
        do {
            flowNumber = getFlowChoice();
            isValidChoice = validFlowInputChoice(flowNumber, allStepperFlows.getAllFlows().size());
        } while (flowNumber != 0 && !isValidChoice);
        return flowNumber;
    }

    public static void printFlowsName(DTOAllStepperFlows allStepperFlows) {
        int index = 1;
        System.out.println("The flow options to choose are:");
        for (DTOFlowDefinition flow : allStepperFlows.getAllFlows()) {
            System.out.println(index + ". " + flow.getName());
            index++;
        }
    }

    public static int getFlowChoice() {
        System.out.println("Please select the flow number you want us to display its data: (press 0 to return to the main menu)");
        int userChoice = readChoice();

        return userChoice;
    }

    public static boolean validFlowInputChoice(int choice, int numberOfFlows) {
        if (choice > numberOfFlows) {
            System.out.println("Invalid input! The number chosen cannot be greater than the maximum flows that existing in the system." +
                    "You a number between 0 to " + numberOfFlows);
        } else if (choice < 0) {
            System.out.println("Invalid input! The choice flow number must be a positive number.");
        } else {
            return true;
        }
        return false;
    }

    /////////divide to small funcs
    public static void IntroduceTheChosenFlow(int flowNumber, DTOAllStepperFlows allStepperFlows) {
        DTOFlowDefinitionImpl flow = allStepperFlows.getAllFlows().get(flowNumber - 1);
        AtomicInteger stepesIndex= new AtomicInteger(1);
        AtomicInteger freeInputsIndex= new AtomicInteger(1);
        AtomicInteger flowOutputsIndex= new AtomicInteger(1);

        StringBuilder flowData = new StringBuilder();
        flowData.append("Flow Name: " + flow.getName() + '\n');
        flowData.append("Flow Description: " + flow.getDescription() + '\n');
        flowData.append("Flow Formal Outputs: " + String.join(", ", flow.getFlowFormalOutputs()) + '\n');
        flowData.append("Is The Flow Raed Only? " + flow.getFlowReadOnly() + '\n');
        flowData.append("The information for the steps in the current flow: \n");
        flow
                .getFlowStepsData()
                .stream()
                .forEach(node -> {
                    flowData.append("Step " + stepesIndex.getAndIncrement() + ": \n");
                    flowData.append("Original Name: " + node.getOriginalStepName() + '\n');
                    if (node.getFinalStepName() != node.getOriginalStepName()) {
                        flowData.append("Final Name: " + Objects.toString(node.getFinalStepName(), "") + "\n");
                    }
                    flowData.append("Is The Step Read Only? " + node.getIsReadOnly() +"\n\n");
                });
        flowData.append("The information about free inputs in the current flow: \n");

        flow
                .getFlowFreeInputs()
                .stream()
                .forEach(node -> {
                    flowData.append("Free Input " + freeInputsIndex.getAndIncrement() + ": \n");
                    flowData.append("Final Name: " + node.getFinalName() + '\n');
                    flowData.append("Type: " + node.getType().getName() + '\n');
                    flowData.append("Connected Steps: " + String.join(", ", flow.getListOfStepsWithCurrInput(node.getFinalName())) +'\n');
                    flowData.append("Mandatory / Optional: " + node.getNecessity() + "\n\n");
                });

        flow.getIOlist()
                .stream().forEach(node -> {
                    if(flow.getFlowFormalOutputs().stream().anyMatch(output->output.equals(node.getFinalName()))) {
                        flowData.append("Flow Outputs " + flowOutputsIndex.getAndIncrement() + ": \n");
                        flowData.append("Final Name: " + node.getFinalName() + '\n');
                        flowData.append("Type: " + node.getDD().getName() + '\n');
                        flowData.append("Creating Step: " + node.getStepName() + "\n\n");
                    }
                });

        System.out.println(flowData);
    }

    public String printStepsInfo(DTOAllStepperFlows allStepperFlows) {
        
    }

    public String printFreeInputsInfo() {

    }

    public String printFlowOutputs() {

    }
}


