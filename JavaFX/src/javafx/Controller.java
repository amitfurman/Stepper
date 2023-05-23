package javafx;

import dto.DTOAllStepperFlows;
import dto.DTOFlowDefinition;
import dto.DTOSingleFlowIOData;
import flow.api.FlowIO.IO;
import javafx.fxml.FXML;
import javafx.header.HeaderController;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import systemengine.systemengine;
import systemengine.systemengineImpl;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Controller {
    private final systemengine systemEngineInterface ;
    @FXML
    private TreeView<String> flowsTree;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TreeView<String> flowDetailsTree;

    @FXML private HeaderController headerComponentController;
    @FXML private GridPane headerComponent;

    public Controller() {
        this.systemEngineInterface = new systemengineImpl();
    }

    public TreeView<String> getFlowsTree(){
        return flowsTree;
    }

    @FXML
    public void initialize() {
        double threshold = 500; // Set your threshold value here

        scrollPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.doubleValue() <= threshold) {
                scrollPane.setFitToWidth(false);
            } else {
                scrollPane.setFitToWidth(true);
            }
        });

        scrollPane.heightProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.doubleValue() <= threshold) {
                scrollPane.setFitToHeight(false);
            } else {
                scrollPane.setFitToHeight(true);
            }
        });

    }

    public void showFlowsTree() {
        TreeItem<String> rootItem = new TreeItem<>("Flows");
        rootItem.setExpanded(true); // Set the root item to be initially expanded
        DTOAllStepperFlows allStepperFlows = systemEngineInterface.getAllFlows();

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
        DTOAllStepperFlows allStepperFlows = systemEngineInterface.getAllFlows();

        TreeItem<String> branchName = new TreeItem<>("Flow Name");
        TreeItem<String> nameItem = new TreeItem<>(flowDefinition.getName());
        branchName.getChildren().addAll(nameItem);

        TreeItem<String> branchDescription = new TreeItem<>("Flow Description");
        TreeItem<String> descriptionItem = new TreeItem<>(flowDefinition.getDescription());
        branchDescription.getChildren().addAll(descriptionItem);

        TreeItem<String> branchFormalOutputs = new TreeItem<>("Flow Formal Outputs");
        for (int i = 0; i < flowDefinition.getFlowFormalOutputs().size(); i++) {
            TreeItem<String> formalOutputItem = new TreeItem<>(flowDefinition.getFlowFormalOutputs().get(i));
            branchFormalOutputs.getChildren().add(formalOutputItem);
        }
/*
        TreeItem<String> branchReadOnly = new TreeItem<>("Is The Flow Read Only?");
        TreeItem<String> readOnlyItem = new TreeItem<>(Boolean.toString(flowDefinition.getFlowReadOnly()));
        branchReadOnly.getChildren().addAll(readOnlyItem);*/

        TreeItem<String> branchReadOnly = new TreeItem<>("Is The Flow Read Only?");
        TreeItem<String> readOnlyItem = new TreeItem<>(Boolean.toString(flowDefinition.getFlowReadOnly()));
        branchReadOnly.getChildren().addAll(readOnlyItem);

        // Add the green "V" or red "X" graphic
        Label statusLabel = new Label(flowDefinition.getFlowReadOnly() ? "V" : "X");
        statusLabel.setTextFill(flowDefinition.getFlowReadOnly() ? javafx.scene.paint.Color.GREEN : Color.RED);
        readOnlyItem.setGraphic(statusLabel);


        AtomicInteger stepIndex = new AtomicInteger(1);
        TreeItem<String> branchSteps = new TreeItem<>("Steps in the current flow");
        flowDefinition
                .getFlowStepsData()
                .stream()
                .forEach(node -> {
                    TreeItem<String> branchStep = new TreeItem<>("Step " + stepIndex.getAndIncrement());
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

        rootItem.getChildren().addAll(branchName, branchDescription, branchFormalOutputs, branchReadOnly, branchSteps, branchFreeInputs, branchFlowOutputs);



        flowDetailsTree.setRoot(rootItem);
    }

}