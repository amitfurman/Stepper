package javafx.flowDefinitionTab;

import dto.DTOAllStepperFlows;
import dto.DTOFlowDefinition;
import dto.DTOSingleFlowIOData;
import flow.api.FlowIO.IO;
import javafx.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class FlowDefinitionTabController {

    private Controller mainController;
    @FXML
    private TreeView<String> flowsTree;
    @FXML
    private TreeView<String> flowDetailsTree;
    @FXML
    private Button ExecuteFlowButton;

    private String chosenFlowName;


    @FXML
    public void initialize() {
        ExecuteFlowButton.setDisable(true);
    }
    @FXML
    public void setPressOnExecuteFlowButton() {
        mainController.goToFlowExecutionTab(chosenFlowName);
        mainController.initDataInFlowExecutionTab();

    }
    public void setMainController(Controller mainController) {
        this.mainController = mainController;
    }
    public TreeView<String> getFlowsTree(){
        return flowsTree;
    }
    public void showFlowsTree() {
        TreeItem<String> rootItem = new TreeItem<>("Flows");
        rootItem.setExpanded(true); // Set the root item to be initially expanded
        DTOAllStepperFlows allStepperFlows =  mainController.getSystemEngineInterface().getAllFlows();

        for (int i = 0; i < allStepperFlows.getNumberOfFlows(); i++) {
            DTOFlowDefinition flowDefinition = allStepperFlows.getFlow(i);
            TreeItem<String> branchItem = createBranchItem(flowDefinition);
            rootItem.getChildren().add(branchItem);
        }

        flowsTree.setRoot(rootItem);
    }
    public TreeItem<String> createBranchItem(DTOFlowDefinition flowDefinition) {
        TreeItem<String> branchItem = new TreeItem<>(flowDefinition.getName());
        TreeItem<String> leafItem1 = new TreeItem<>("Description: " + flowDefinition.getDescription());
        TreeItem<String> leafItem2 = new TreeItem<>("Number of steps: " + Integer.toString(flowDefinition.getNumberOfSteps()));
        TreeItem<String> leafItem3 = new TreeItem<>("Number of free inputs: " + Integer.toString(flowDefinition.getNumberOfFreeInputs()));

        Button pressToSeeFullDetailsButton = new Button("Press to see full details");
        pressToSeeFullDetailsButton.setId("pressToSeeFullDetailsButton");

        pressToSeeFullDetailsButton.setOnAction(event -> {
            showChosenFlow(flowDefinition);
        });
        TreeItem<String> buttonItem = new TreeItem<>(" ");
        buttonItem.setGraphic(pressToSeeFullDetailsButton);

        branchItem.getChildren().addAll(leafItem1, leafItem2, leafItem3, buttonItem);

        return branchItem;
    }
    public void showChosenFlow(DTOFlowDefinition flowDefinition) {
        TreeItem<String> rootItem = new TreeItem<>("Chosen Flow Details - " + flowDefinition.getName());
        rootItem.setExpanded(true); // Set the root item to be initially expanded
        DTOAllStepperFlows allStepperFlows =  mainController.getSystemEngineInterface().getAllFlows();

        TreeItem<String> branchName = new TreeItem<>("Flow Name");
        chosenFlowName = flowDefinition.getName();
        TreeItem<String> nameItem = new TreeItem<>(chosenFlowName);
        branchName.getChildren().addAll(nameItem);

        TreeItem<String> branchDescription = new TreeItem<>("Flow Description");
        TreeItem<String> descriptionItem = new TreeItem<>(flowDefinition.getDescription());
        branchDescription.getChildren().addAll(descriptionItem);

        TreeItem<String> branchFormalOutputs = new TreeItem<>("Flow Formal Outputs");
        for (int i = 0; i < flowDefinition.getFlowFormalOutputs().size(); i++) {
            TreeItem<String> formalOutputItem = new TreeItem<>(flowDefinition.getFlowFormalOutputs().get(i));
            branchFormalOutputs.getChildren().add(formalOutputItem);
        }

        TreeItem<String> branchReadOnly = new TreeItem<>("Is The Flow Read Only?");
        TreeItem<String> readOnlyItem = new TreeItem<>(Boolean.toString(flowDefinition.getFlowReadOnly()));
        branchReadOnly.getChildren().addAll(readOnlyItem);

        // Add the green "V" or red "X" graphic
        Label statusLabel = new Label(flowDefinition.getFlowReadOnly() ? "V" : "X");
        statusLabel.setTextFill(flowDefinition.getFlowReadOnly() ? javafx.scene.paint.Color.GREEN : Color.RED);
        readOnlyItem.setGraphic(statusLabel);

        TreeItem<String> branchSteps = showStepsOfChosenFlow(flowDefinition);
        TreeItem<String> branchFreeInputs = showFreeInputsOfChosenFlow(flowDefinition);
        TreeItem<String> branchFlowOutputs = showFlowOutputsOfChosenFlow(flowDefinition);

        rootItem.getChildren().addAll(branchName, branchDescription, branchFormalOutputs, branchReadOnly, branchSteps, branchFreeInputs, branchFlowOutputs);
        flowDetailsTree.setRoot(rootItem);

        boolean isEmptyFlowDetailsTree = (flowDetailsTree.getRoot() == null || flowDetailsTree.getRoot().getChildren().isEmpty());
        ExecuteFlowButton.setDisable(isEmptyFlowDetailsTree);
    }
    public TreeItem<String> showStepsOfChosenFlow(DTOFlowDefinition flowDefinition){
        AtomicInteger stepIndex = new AtomicInteger(1);
        TreeItem<String> branchSteps = new TreeItem<>("Steps in the current flow");
        flowDefinition
                .getFlowStepsData()
                .stream()
                .forEach(node -> {
                    TreeItem<String> branchStep = new TreeItem<>("Step " + stepIndex.getAndIncrement() + " - " + node.getFinalStepName());
                    TreeItem<String> OriginalName = new TreeItem<>("Original Name");
                    TreeItem<String> OriginalNameItem = new TreeItem<>(node.getOriginalStepName());
                    OriginalName.getChildren().addAll(OriginalNameItem);
                    TreeItem<String> FinalName = null;
                    if (!(node.getFinalStepName().equals(node.getOriginalStepName()))) {
                        FinalName = new TreeItem<>("Final Name");
                        TreeItem<String> FinalNameItem = new TreeItem<>(Objects.toString(node.getFinalStepName()));
                        FinalName.getChildren().addAll(FinalNameItem);
                    }
                    TreeItem<String> StepReadOnly = new TreeItem<>("Is The Step Read Only?");
                    TreeItem<String> StepReadOnlyItem = new TreeItem<>(Boolean.toString(node.getIsReadOnly()));
                    StepReadOnly.getChildren().addAll(StepReadOnlyItem);

                    // Add the green "V" or red "X" graphic
                    Label status = new Label(node.getIsReadOnly() ? "V" : "X");
                    status.setTextFill(node.getIsReadOnly() ? javafx.scene.paint.Color.GREEN : Color.RED);
                    StepReadOnlyItem.setGraphic(status);

                    branchStep.getChildren().addAll(OriginalName);
                    if (!(node.getFinalStepName().equals(node.getOriginalStepName()))) {
                        branchStep.getChildren().addAll(FinalName);
                    }
                    branchStep.getChildren().addAll(StepReadOnly);
                    branchSteps.getChildren().addAll(branchStep);
                });
        return branchSteps;

    }
    public TreeItem<String> showFreeInputsOfChosenFlow(DTOFlowDefinition flowDefinition){
        AtomicInteger freeInputIndex = new AtomicInteger(1);
        TreeItem<String> branchFreeInputs = new TreeItem<>("Free inputs in the current flow");
        flowDefinition
                .getFlowFreeInputs()
                .stream()
                .forEach(node -> {
                    TreeItem<String> branchFreeInput = new TreeItem<>("Free Input " + freeInputIndex.getAndIncrement());

                    TreeItem<String> FinalName = new TreeItem<>("Final Name");
                    TreeItem<String> FinalNameItem = new TreeItem<>(node.getFinalName());
                    FinalName.getChildren().addAll(FinalNameItem);

                    TreeItem<String> Type = new TreeItem<>("Type");
                    TreeItem<String> TypeItem = new TreeItem<>(node.getType().getName());
                    Type.getChildren().addAll(TypeItem);

                    TreeItem<String> ConnectedSteps = new TreeItem<>("Connected Steps");
                    flowDefinition.getListOfStepsWithCurrInput(node.getFinalName()).stream().forEach(step -> {
                        TreeItem<String> ConnectedStepsItem = new TreeItem<>(step);
                        ConnectedSteps.getChildren().addAll(ConnectedStepsItem);
                    });

                    TreeItem<String> Necessity = new TreeItem<>("Necessity");
                    TreeItem<String> NecessityItem = new TreeItem<>(node.getNecessity().toString());
                    Necessity.getChildren().addAll(NecessityItem);

                    branchFreeInput.getChildren().addAll(FinalName, Type, ConnectedSteps, Necessity);
                    branchFreeInputs.getChildren().addAll(branchFreeInput);
                });
        return branchFreeInputs;
    }
    public TreeItem<String> showFlowOutputsOfChosenFlow(DTOFlowDefinition flowDefinition){
        AtomicInteger flowOutputIndex = new AtomicInteger(1);
        TreeItem<String> branchFlowOutputs = new TreeItem<>("Flow Outputs");
        List<DTOSingleFlowIOData> outputs = flowDefinition.getIOlist().stream().filter(io -> io.getIOType().equals(IO.OUTPUT)).collect(Collectors.toList());
        for(DTOSingleFlowIOData output: outputs) {
            TreeItem<String> branchFlowOutput = new TreeItem<>("Flow Output " + flowOutputIndex.getAndIncrement());

            TreeItem<String> FinalName = new TreeItem<>("Final Name");
            TreeItem<String> FinalNameItem = new TreeItem<>(output.getFinalName());
            FinalName.getChildren().addAll(FinalNameItem);

            TreeItem<String> Type = new TreeItem<>("Type");
            TreeItem<String> TypeItem = new TreeItem<>(output.getType().getName());
            Type.getChildren().addAll(TypeItem);

            TreeItem<String> CreatedStep = new TreeItem<>("Creating Step");
            TreeItem<String> CreatedStepItem = new TreeItem<>(output.getStepName());
            CreatedStep.getChildren().addAll(CreatedStepItem);

            branchFlowOutput.getChildren().addAll(FinalName, Type, CreatedStep);
            branchFlowOutputs.getChildren().addAll(branchFlowOutput);
        }
        return branchFlowOutputs;
    }
    public void setFlowDetailsTree(){
        TreeItem<String> rootItem = new TreeItem<>();
        flowDetailsTree.setRoot(rootItem);
        ExecuteFlowButton.setDisable(true);

    }

}
