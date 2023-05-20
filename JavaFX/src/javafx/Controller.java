package javafx;

import dto.DTOAllStepperFlows;
import dto.DTOFlowDefinition;
import dto.DTOSingleFlowIOData;
import exceptions.*;
import flow.api.FlowIO.IO;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import systemengine.systemengine;
import systemengine.systemengineImpl;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Controller {
    private final systemengine systemEngineInterface ;

    private String filePath;
    @FXML
    private Button chooseXMLFileButton;
    @FXML
    private TextField chosenXmlFilePath;
    @FXML
    private Label errorMessageLabel;
    @FXML
    private TreeView<String> flowsTree;
    @FXML
    private TextArea flowDetails;
    @FXML
    private Label stepperTitle;

    private boolean isErrorMessageShown = false;

    public Controller() {
        this.systemEngineInterface = new systemengineImpl();
    }

    @FXML
    public void initialize() {
//        Font.loadFont(getClass().getResourceAsStream("baguet_script.ttf"), 12);
//        stepperTitle.setFont(Font.font("Baguet Script", 24));
        chosenXmlFilePath.setEditable(false);
        chosenXmlFilePath.setMouseTransparent(true);
        flowDetails.setEditable(false);
        flowDetails.setMouseTransparent(true);
        flowDetails.setScrollTop(ScrollPane.ScrollBarPolicy.ALWAYS.ordinal());


    }

    @FXML
    void clickToChooseXMLFileButton(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose XML File");

        Stage stage = (Stage) chooseXMLFileButton.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            filePath = selectedFile.getAbsolutePath();
            viewChosenXmlFilePath(event);
        }
        try {
            isErrorMessageShown = false;
            systemEngineInterface.cratingFlowFromXml(filePath);
            hideError();
            if (!isErrorMessageShown) {
                showFlowsTree();
            }
        } catch (DuplicateFlowsNames | UnExistsStep | OutputsWithSameName | MandatoryInputsIsntUserFriendly |
                 UnExistsData | SourceStepBeforeTargetStep | TheSameDD | UnExistsOutput |
                 FreeInputsWithSameNameAndDifferentType | FileNotFoundException | JAXBException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    void viewChosenXmlFilePath(ActionEvent event) {
        chosenXmlFilePath.setText(filePath.toString());
    }

    private void showError(String message) {
        if (!isErrorMessageShown) {
            isErrorMessageShown = true;
            errorMessageLabel.setText(message);
            errorMessageLabel.getStyleClass().add("errors"); // Apply the CSS class to the label
            FadeTransition animation = new FadeTransition();
            animation.setNode(errorMessageLabel);
            animation.setDuration(Duration.seconds(0.5));
            animation.setFromValue(0.0);
            animation.setToValue(1.0);
            animation.play();
        }
        //set activeFlow - false
    }

    private void hideError() {
        FadeTransition animation = new FadeTransition();
        animation.setNode(errorMessageLabel);
        animation.setDuration(Duration.seconds(0.5));
        animation.setFromValue(1.0);
        animation.setToValue(0.0);
        animation.play();

        isErrorMessageShown = false;
        errorMessageLabel.textProperty().setValue("");

        //set activeFlow - false
    }

    private void showFlowsTree() {
         TreeItem<String> rootItem = new TreeItem<>("Flows");

        DTOAllStepperFlows allStepperFlows = systemEngineInterface.getAllFlows();

        for (int i = 0; i < allStepperFlows.getNumberOfFlows(); i++) {
            TreeItem<String> branchItem = new TreeItem<>( allStepperFlows.getFlow(i).getName());
            TreeItem<String> leafItem1 = new TreeItem<>("Description: " +allStepperFlows.getFlow(i).getDescription());
            TreeItem<String> leafItem2 = new TreeItem<>("Number of steps: " + Integer.toString(allStepperFlows.getFlow(i).getNumberOfSteps()));
            TreeItem<String> leafItem3 = new TreeItem<>("Number of free inputs: "+ Integer.toString(allStepperFlows.getFlow(i).getNumberOfFreeInputs()));
            //TreeItem<String> leafItem4 = new TreeItem<>("video2");
            branchItem.getChildren().addAll(leafItem1, leafItem2, leafItem3);
            rootItem.getChildren().add(branchItem);
            flowsTree.setRoot(rootItem);

            showChosenFlow(allStepperFlows);
        }

        //treeView.setShowRoot(false);
    }
    public void showChosenFlow(DTOAllStepperFlows allStepperFlows){
     flowsTree.setOnMouseClicked(event -> {
         TreeItem<String> selectedItem = flowsTree.getSelectionModel().getSelectedItem();

         if (selectedItem != null && selectedItem.getParent() != null) {
             if (event.getClickCount() == 2 && !selectedItem.isLeaf() ) {
                 int index = IntStream.range(0, allStepperFlows.getAllFlows().size())
                         .filter(ind -> allStepperFlows.getAllFlows().get(ind).getName().equals(selectedItem.getValue()))
                         .findFirst()
                         .orElse(-1);
                 //String flowName =   selectedItem.getValue();
                 DTOFlowDefinition currFlowDefinition= systemEngineInterface.IntroduceTheChosenFlow(index+1);
                 StringBuilder flowData = new StringBuilder();
                 flowData.append("Flow Name: " + currFlowDefinition.getName() + "\n");
                 flowData.append("Flow Description: " + currFlowDefinition.getDescription() + '\n');
                 flowData.append("Flow Formal Outputs: " + String.join(", ", currFlowDefinition.getFlowFormalOutputs()) + '\n');
                 flowData.append("Is The Flow Read Only? " + currFlowDefinition.getFlowReadOnly() + "\n\n");
                 flowData.append(printStepsInfo(currFlowDefinition));
                 flowData.append(printFreeInputsInfo(currFlowDefinition));
                 flowData.append(printFlowOutputs(currFlowDefinition));
                 flowDetails.setText(flowData.toString());
             }
         }
     });
    }
    public StringBuilder printStepsInfo(DTOFlowDefinition flow) {
        AtomicInteger stepIndex = new AtomicInteger(1);
        StringBuilder stepData = new StringBuilder();
        stepData.append("*The information about the steps in the current flow: *\n");
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
        List<DTOSingleFlowIOData> outputs = flow.getIOlist().stream().filter(io -> io.getIOType().equals(IO.OUTPUT)).collect(Collectors.toList());
        for(DTOSingleFlowIOData output: outputs) {
            outputData.append("Flow Outputs " + flowOutputIndex.getAndIncrement() + ": \n");
            outputData.append("\tFinal Name: " + output.getFinalName() + '\n');
            outputData.append("\tType: " + output.getType().getName() + '\n');
            outputData.append("\tCreating Step: " + output.getStepName() + "\n");

        }
        return outputData;
    }


/*
    @Override
    public TreeCell<TreeData> call(TreeView<TreeData> param) {
        // Handle click events on the cell
        cell.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                TreeData selectedData = cell.getItem();
                System.out.println("Arrow Info: " + selectedData.getArrowInfo());
            } else if (event.getClickCount() == 2) {
                TreeData selectedData = cell.getItem();
                System.out.println("Text Info: " + selectedData.getTextInfo());
            }
        });
    }

 */

/*
    public void selectItem() {

        TreeItem<String> item = flowsTree.getSelectionModel().getSelectedItem();

        if(item != null) {
  //          System.out.println(item.getValue());
        }
    }
*/


}
