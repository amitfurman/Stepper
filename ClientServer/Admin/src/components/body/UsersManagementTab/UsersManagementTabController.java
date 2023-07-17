package components.body.UsersManagementTab;

import dto.DTOAllStepperFlows;
import dto.DTOFlowDefinition;
import dto.DTOSingleFlowIOData;
import flow.api.FlowIO.IO;
import javafx.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import user.UserManager;
import users.UserListRefresher;
import users.UsersListController;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class UsersManagementTabController {
    private Controller mainController;
    @FXML
    private TreeView<String> usersTree;
    @FXML
    private TreeView<String> userDetailsTree;
    @FXML
    private Button SaveButton;

    private String chosenUserName;

    private UsersListController usersListComponentController;


    @FXML
    public void initialize() {
        SaveButton.setDisable(true);
        usersListComponentController = new UsersListController(this);
        usersListComponentController.startListRefresher();
    }

    @FXML
    public void setPressOnSaveButton() {
       // mainController.goToFlowExecutionTab(chosenUserName);
        mainController.initDataInFlowExecutionTab();
    }
    public void setMainController(Controller mainController) {
        this.mainController = mainController;
    }
    public TreeView<String> getUsersTree(){
        return usersTree;
    }
    public void showUsersTree(List<String> usersList) {
        TreeItem<String> rootItem = new TreeItem<>("Users");
        rootItem.setExpanded(true);

        for (String userName : usersList) {
            Button pressToSeeFullDetailsButton = new Button("Press to see full details");
            pressToSeeFullDetailsButton.setId("pressToSeeFullDetailsButton");

            TreeItem<String> branchItem = new TreeItem<>("");
            branchItem.setGraphic(createTreeCellGraphic(userName, pressToSeeFullDetailsButton));


            pressToSeeFullDetailsButton.setOnAction(event -> {
                showChosenFlow();
            });
            rootItem.getChildren().add(branchItem);
        }
        usersTree.setRoot(rootItem);
    }

    private HBox createTreeCellGraphic(String nodeName, Button button) {
        Label nameLabel = new Label(nodeName);
        HBox graphicContainer = new HBox(nameLabel, button);
        graphicContainer.setSpacing(5);
        return graphicContainer;
    }

    public void showChosenFlow() {
        TreeItem<String> rootItem = new TreeItem<>("Chosen Flow Details - ");
        userDetailsTree.setRoot(rootItem);

/*
        rootItem.setExpanded(true); // Set the root item to be initially expanded

        TreeItem<String> branchName = new TreeItem<>("Flow Name");
        chosenUserName = flowDefinition.getName();
        TreeItem<String> nameItem = new TreeItem<>(chosenUserName);
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
        statusLabel.setTextFill(flowDefinition.getFlowReadOnly() ? Color.GREEN : Color.RED);
        readOnlyItem.setGraphic(statusLabel);

        TreeItem<String> branchSteps = showStepsOfChosenFlow(flowDefinition);
        TreeItem<String> branchFreeInputs = showFreeInputsOfChosenFlow(flowDefinition);
        TreeItem<String> branchFlowOutputs = showFlowOutputsOfChosenFlow(flowDefinition);

        rootItem.getChildren().addAll(branchName, branchDescription, branchFormalOutputs, branchReadOnly, branchSteps, branchFreeInputs, branchFlowOutputs);*/



        boolean isEmptyFlowDetailsTree = (userDetailsTree.getRoot() == null || userDetailsTree.getRoot().getChildren().isEmpty());
        SaveButton.setDisable(isEmptyFlowDetailsTree);
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
                    status.setTextFill(node.getIsReadOnly() ? Color.GREEN : Color.RED);
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
        userDetailsTree.setRoot(rootItem);
        SaveButton.setDisable(true);
    }

}
