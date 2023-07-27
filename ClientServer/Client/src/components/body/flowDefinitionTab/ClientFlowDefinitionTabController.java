package components.body.flowDefinitionTab;
import com.google.gson.Gson;
import commonComponents.CommonController;
import dto.*;
import flow.api.FlowIO.IO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.paint.Color;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static util.Constants.GSON_INSTANCE;

public class ClientFlowDefinitionTabController {
    private CommonController mainController;
    @FXML
    private TreeView<String> flowsTree;
    @FXML
    private TreeView<String> flowDetailsTree;
    @FXML
    private Button ExecuteFlowButton;
    private String chosenFlowName;
    private TreeItem<String> rootItem;
    private Set<String> allFlows = new HashSet<>();
    private Set<String> currSelectedFlows = new HashSet<>();

    @FXML
    public void initialize() {
        rootItem = new TreeItem<>("Flows");
        ExecuteFlowButton.setDisable(true);
    }
    @FXML
    public void setPressOnExecuteFlowButton() {
        mainController.goToClientFlowExecutionTab(chosenFlowName);
        mainController.initDataInFlowExecutionTab();

    }
    public void setMainController(CommonController mainController) {
        this.mainController = mainController;
    }
    public TreeView<String> getFlowsTree(){
        return flowsTree;
    }
/*    public void showFlowsTree(List<String> roles) {
        if (roles.size() != 0) {
           getAllFlows(roles);
        }else{
            allFlows.clear();
            rootItem.getChildren().clear();
            rootItem.setExpanded(true);
            Platform.runLater(() -> {
                flowsTree.setRoot(rootItem);
            });
        }
    }*/
    public void showFlowsTree(List<String> roles) {
/*        if(roles.size()==0) {
            allFlows.clear();
            rootItem.getChildren().clear();
            rootItem.setExpanded(true);
            Platform.runLater(() -> {
                flowsTree.setRoot(rootItem);
            });
        }*/
        getAllFlows(roles);
    }
    public void getAllFlows(List<String> roles) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constants.FLOWS_IN_ROLES_SERVLET).newBuilder();
        System.out.println("before roles");
        if(roles.size()!=0) {
            String rolesString = roles.stream().collect(Collectors.joining(","));
            System.out.println("1after roles");
            urlBuilder.addQueryParameter("roles_list", rolesString);
        }else {
            urlBuilder.addQueryParameter("roles_list","" );
            System.out.println("2after roles");

        }
        urlBuilder.addQueryParameter("is_manager", mainController.getHeaderClientComponentController().getIsManager().toString());
        String finalUrl = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        OkHttpClient HTTP_CLIENT = new OkHttpClient();
        Call call = HTTP_CLIENT.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonResponse = response.body().string();
                Gson gson = new Gson();
                DTOFlowsDefinitionInRoles dtoFlowsDefinition = gson.fromJson(jsonResponse, DTOFlowsDefinitionInRoles.class);
                if (dtoFlowsDefinition!=null && dtoFlowsDefinition.getFlowsDefinitionInRoles()!=null) {
                    updateFlowsTree(dtoFlowsDefinition);
                    mainController.getClientExecutionsHistoryTabController().initExecutionHistoryTable();
                }
            }
        });
    }
    public void updateFlowsTree(DTOFlowsDefinitionInRoles dtoFlowsDefinition) {
        rootItem.setExpanded(true);
        for (DTOFlowDefinitionInRoles flowDefinition: dtoFlowsDefinition.getFlowsDefinitionInRoles()) {
            if (!(allFlows.contains(flowDefinition.getFlowName()))) {
                allFlows.add(flowDefinition.getFlowName());
                TreeItem<String> branchItem = createBranchItem(flowDefinition);
                rootItem.getChildren().add(branchItem);
            }
        }
        for (String flow : allFlows) {
            if (dtoFlowsDefinition.getFlowsDefinitionInRoles().stream().noneMatch(flowDefinition -> flowDefinition.getFlowName().equals(flow))) {
                allFlows.remove(flow);
                rootItem.getChildren().removeIf(item -> item.getValue().equals(flow));
            }
        }

        Platform.runLater(() -> {
            flowsTree.setRoot(rootItem);
        });
    }
    private void collectExistingItems(TreeItem<String> item, Map<String, TreeItem<String>> existingItems) {
        existingItems.put(item.getValue(), item);
        for (TreeItem<String> child : item.getChildren()) {
            collectExistingItems(child, existingItems);
        }
    }
    public TreeItem<String> createBranchItem(DTOFlowDefinitionInRoles flowDefinition) {
        TreeItem<String> branchItem = new TreeItem<>(flowDefinition.getFlowName());
        TreeItem<String> leafItem1 = new TreeItem<>("Description: " + flowDefinition.getDescription());
        TreeItem<String> leafItem2 = new TreeItem<>("Number of steps: " + Integer.toString(flowDefinition.getNumberOfSteps()));
        TreeItem<String> leafItem3 = new TreeItem<>("Number of free inputs: " + Integer.toString(flowDefinition.getNumberOfFreeInputs()));
        TreeItem<String> leafItem4 = new TreeItem<>("Number of continuations: " + Integer.toString(flowDefinition.getNumberOfContinuations()));


        Button pressToSeeFullDetailsButton = new Button("Press to see full details");
        pressToSeeFullDetailsButton.setId("pressToSeeFullDetailsButton");

        pressToSeeFullDetailsButton.setOnAction(event -> {
             showChosenFlow(flowDefinition);
        });
        TreeItem<String> buttonItem = new TreeItem<>(" ");
        buttonItem.setGraphic(pressToSeeFullDetailsButton);

        branchItem.getChildren().addAll(leafItem1, leafItem2, leafItem3,leafItem4, buttonItem);

        return branchItem;
    }
    public void showChosenFlow(DTOFlowDefinitionInRoles flowDefinition) {
        TreeItem<String> rootItem = new TreeItem<>("Chosen Flow Details - " + flowDefinition.getFlowName());
        rootItem.setExpanded(true); // Set the root item to be initially expanded

        TreeItem<String> branchName = new TreeItem<>("Flow Name");
        chosenFlowName = flowDefinition.getFlowName();
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
        TreeItem<String> readOnlyItem = new TreeItem<>(Boolean.toString(flowDefinition.getIsReadOnly()));
        branchReadOnly.getChildren().addAll(readOnlyItem);

        // Add the green "V" or red "X" graphic
        Label statusLabel = new Label(flowDefinition.getIsReadOnly() ? "V" : "X");
        statusLabel.setTextFill(flowDefinition.getIsReadOnly()? Color.GREEN : Color.RED);
        readOnlyItem.setGraphic(statusLabel);

       TreeItem<String> branchSteps = showStepsOfChosenFlow(flowDefinition);
        TreeItem<String> branchFreeInputs = showFreeInputsOfChosenFlow(flowDefinition);
        TreeItem<String> branchFlowOutputs = showFlowOutputsOfChosenFlow(flowDefinition);

        rootItem.getChildren().addAll(branchName, branchDescription, branchFormalOutputs, branchReadOnly,branchSteps, branchFreeInputs ,branchFlowOutputs);
        flowDetailsTree.setRoot(rootItem);

        boolean isEmptyFlowDetailsTree = (flowDetailsTree.getRoot() == null || flowDetailsTree.getRoot().getChildren().isEmpty());
        ExecuteFlowButton.setDisable(isEmptyFlowDetailsTree);
    }
    public TreeItem<String> showStepsOfChosenFlow(DTOFlowDefinitionInRoles flowDefinition){
        AtomicInteger stepIndex = new AtomicInteger(1);
        TreeItem<String> branchSteps = new TreeItem<>("Steps in the current flow");
        flowDefinition
                .getStepsInFlow()
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
    public TreeItem<String> showFreeInputsOfChosenFlow(DTOFlowDefinitionInRoles flowDefinition){
        AtomicInteger freeInputIndex = new AtomicInteger(1);
        TreeItem<String> branchFreeInputs = new TreeItem<>("Free inputs in the current flow");
        flowDefinition
                .getFreeInputs()
                .stream()
                .forEach(node -> {
                    TreeItem<String> branchFreeInput = new TreeItem<>("Free Input " + freeInputIndex.getAndIncrement());

                    TreeItem<String> FinalName = new TreeItem<>("Final Name");
                    TreeItem<String> FinalNameItem = new TreeItem<>(node.getName());
                    FinalName.getChildren().addAll(FinalNameItem);

                    TreeItem<String> Type = new TreeItem<>("Type");
                    TreeItem<String> TypeItem = new TreeItem<>(node.getType());
                    Type.getChildren().addAll(TypeItem);

                    TreeItem<String> ConnectedSteps = new TreeItem<>("Connected Steps");
                    node.getConnectedSteps().stream().forEach(step-> {
                        TreeItem<String> ConnectedStepsItem = new TreeItem<>(step);
                        ConnectedSteps.getChildren().add(ConnectedStepsItem);
                    });

                    TreeItem<String> Necessity = new TreeItem<>("Necessity");
                    TreeItem<String> NecessityItem = new TreeItem<>(node.getNecessity());
                    Necessity.getChildren().addAll(NecessityItem);

                    branchFreeInput.getChildren().addAll(FinalName, Type, ConnectedSteps, Necessity);
                    branchFreeInputs.getChildren().addAll(branchFreeInput);

                });
        return branchFreeInputs;
    }
    public TreeItem<String> showFlowOutputsOfChosenFlow(DTOFlowDefinitionInRoles flowDefinition){
        AtomicInteger flowOutputIndex = new AtomicInteger(1);
        TreeItem<String> branchFlowOutputs = new TreeItem<>("Flow Outputs");
        List<DTOFlowOutputs> outputs = flowDefinition.getAllOutputs();
        for(DTOFlowOutputs output: outputs) {
            TreeItem<String> branchFlowOutput = new TreeItem<>("Flow Output " + flowOutputIndex.getAndIncrement());

            TreeItem<String> FinalName = new TreeItem<>("Final Name");
            TreeItem<String> FinalNameItem = new TreeItem<>(output.getFinalName());
            FinalName.getChildren().addAll(FinalNameItem);

            TreeItem<String> Type = new TreeItem<>("Type");
            TreeItem<String> TypeItem = new TreeItem<>(output.getType());
            Type.getChildren().addAll(TypeItem);

            TreeItem<String> CreatedStep = new TreeItem<>("Creating Step");
            TreeItem<String> CreatedStepItem = new TreeItem<>(output.getCreatingStep());
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
