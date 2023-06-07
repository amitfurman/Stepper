package javafx.flowExecutionTab.MasterDetail;

import datadefinition.impl.list.FileListData;
import datadefinition.impl.list.StringListData;
import datadefinition.impl.mapping.NumberMappingData;
import datadefinition.impl.relation.RelationData;
import dto.DTOFlowExecution;
import dto.DTOSingleFlowIOData;
import dto.DTOStepExecutionData;
import flow.api.FlowIO.IO;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.flowExecutionTab.FlowExecutionTabController;
import javafx.flowExecutionTab.Logic;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.MasterDetailPane;
import steps.api.DataNecessity;
import steps.api.StepResult;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MasterDetailController {

    @FXML
    private MasterDetailPane FlowMasterDetails;

    FlowExecutionTabController flowExecutionTabController;

    DTOFlowExecution flowExecution;

    private final Logic logic;

    private final SimpleBooleanProperty isTaskFinished;

    private boolean isActive;

    VBox detailPane;

    List<String> addedStepNames = new ArrayList<>();

    int stepCounter;

    public MasterDetailController() {
        logic = new Logic();
        this.isTaskFinished = new SimpleBooleanProperty(false);
    }

    public FlowExecutionTabController getFlowExecutionTabController() {
        return flowExecutionTabController;
    }

    public void clearStepsInMasterDetail() {
        detailPane.getChildren().removeIf(node -> node instanceof Label && !((Label) node).getStyleClass().contains("first-label"));
        addedStepNames.clear();
        stepCounter = 1;
    }

    @FXML
    public void initialize() {
        FlowMasterDetails.setDetailSide(Side.LEFT);
        FlowMasterDetails.setDividerPosition(0.3);

    }

    public void setFlowExecutionTabController(FlowExecutionTabController flowExecutionTabController) {
        this.flowExecutionTabController = flowExecutionTabController;
    }

    public MasterDetailPane getMasterDetailsComponent() {
        return FlowMasterDetails;
    }

    public void initMasterDetailPaneController(){
        ScrollPane scrollPane = new ScrollPane();
        FlowMasterDetails.setDetailNode(scrollPane);
        scrollPane.setFitToWidth(true);
        StackPane stackPane = new StackPane();
        FlowMasterDetails.setMasterNode(stackPane);
    }

    public void initMasterDetailPaneController(DTOFlowExecution flowExecution) {
        this.flowExecution = flowExecution;
        detailPane = new VBox();
        detailPane.setPadding(new Insets(10));
        detailPane.setSpacing(5);
        Label flowDetailLabel = createDetailLabel(flowExecution.getFlowName(), FlowMasterDetails, true, detailPane);
        detailPane.getChildren().add(flowDetailLabel);
        flowDetailLabel.getStyleClass().add("label-selected");
        stepCounter = 1;
    }

    public  void updateFlowLabel(DTOFlowExecution flowExecution) {
        this.flowExecution = flowExecution;
        TreeView treeView = null;
        treeView = cratingGeneralFlowExecutionDetail();
        if (treeView != null) {
            treeView.getStyleClass().add("tree-view-style");
            StackPane stackPane = new StackPane();

            stackPane.getChildren().addAll(treeView);
            FlowMasterDetails.setMasterNode(stackPane);
            FlowMasterDetails.setDividerPosition(0.3);

        }
    }
    public void initMasterDetailComponent(DTOFlowExecution flowExecution) {
/*
        this.flowExecution = flowExecution;

        detailPane = new VBox();
        detailPane.setPadding(new Insets(10));
        detailPane.setSpacing(5);

        Label flowDetailLabel = createDetailLabel(flowExecution.getFlowName(), FlowMasterDetails, true, detailPane);
        detailPane.getChildren().add(flowDetailLabel);
        System.out.println(flowExecution.getFlowName());
        System.out.println(flowExecution.isComplete());
        System.out.println(flowExecution.getStepExecutionDataList());
*/

      //  addStepsToMasterDetail();

            ScrollPane scrollPane = new ScrollPane(detailPane);
            scrollPane.setFitToWidth(true);
            FlowMasterDetails.setDetailNode(scrollPane);
            FlowMasterDetails.setDividerPosition(0.3);

          this.isTaskFinished.set(false);


    }


        ////UPDATE HISTORY



    public void addStepsToMasterDetail(DTOFlowExecution flowExecution)
    {
        this.flowExecution = flowExecution;
        for (DTOStepExecutionData stepExecution : flowExecution.getStepExecutionDataList()) {
            String stepName = stepExecution.getFinalNameStep();
            if (stepExecution.getResult() != null && addedStepNames.stream().noneMatch(name->name.equals(stepName))) {
                // Check if step is executed and if there is no existing label with the same step name
                Label detailLabel = createDetailLabel("Step " + stepCounter++ + ": " + stepName, FlowMasterDetails, false, detailPane);
                ImageView statusImage = new ImageView();
                if (stepExecution.getResult().equals(StepResult.FAILURE)) {
                    statusImage.setImage(new Image(getClass().getResource("icons8-close-16.png").toString()));
                    detailLabel.setGraphic(statusImage);
                } else if (stepExecution.getResult().equals(StepResult.SUCCESS)) {
                    statusImage.setImage(new Image(getClass().getResource("icons8-checkmark-16.png").toString()));
                    detailLabel.setGraphic(statusImage);
                } else {
                    statusImage.setImage(new Image(getClass().getResource("icons8-error-16.png").toString()));
                    detailLabel.setGraphic(statusImage);
                }
                detailPane.getChildren().add(detailLabel);
                addedStepNames.add(stepName); // Add the step name to the list
            }
        }

        ScrollPane scrollPane = new ScrollPane(detailPane);
        scrollPane.setFitToWidth(true);
        FlowMasterDetails.setDetailNode(scrollPane);
        FlowMasterDetails.setDividerPosition(0.3);
    }


/*
    private Label createDetailLabel(String text, MasterDetailPane masterDetailPane, boolean isFirstLabel, VBox detailPane) {
        Label detailLabel = new Label(text);
        */
/*TextArea textArea = new TextArea();
        textArea.setWrapText(true);  // Enable text wrapping
        textArea.setEditable(false);*//*
 // Make the text area read-only

        detailLabel.setOnMouseClicked(event -> {
            detailLabel.getStyleClass().add("label-selected");
            System.out.println("clicked on label"+ detailLabel.getText());

            // Remove highlight from previously selected labels
            for (Node child : detailPane.getChildren()) {
                if (child instanceof Label) {
                    Label label = (Label) child;
                    if (!label.equals(detailLabel)) {
                        label.getStyleClass().remove("label-selected");
                    }
                }
            }

            TreeView treeView = null;

             if (isFirstLabel) {
                 treeView = cratingGeneralFlowExecutionDetail();
             } else {
                 treeView = cratingStepsExecutionDetail(text);
             }

            TextArea textArea = new TextArea();
            textArea.setWrapText(true);  // Enable text wrapping
            textArea.setEditable(false);

            if (treeView != null) {
                treeView.getStyleClass().add("tree-view-style");
                StackPane stackPane = new StackPane();

                stackPane.getChildren().addAll(textArea, treeView);

                FlowMasterDetails.setMasterNode(stackPane);
                masterDetailPane.setDividerPosition(0.3);

            }
        });

        if (isFirstLabel) {
            detailLabel.getStyleClass().add("first-label");
        } else {
            detailLabel.getStyleClass().add("detail-label");
        }

        detailLabel.setCursor(Cursor.HAND);
        detailLabel.setOnMouseEntered(event -> {
            detailLabel.setUnderline(true);
        });
        detailLabel.setOnMouseExited(event -> {
            detailLabel.setUnderline(false);
        });

        return detailLabel;
    }
*/

    private Label createDetailLabel(String text, MasterDetailPane masterDetailPane, boolean isFirstLabel, VBox detailPane) {
        Label detailLabel = new Label(text);
        if (isFirstLabel) {
            detailLabel.getStyleClass().add("first-label");
        } else {
            detailLabel.getStyleClass().add("detail-label");
        }
        detailLabel.setCursor(Cursor.HAND);
        detailLabel.setOnMouseEntered(event -> {
            detailLabel.setUnderline(true);
        });
        detailLabel.setOnMouseExited(event -> {
            detailLabel.setUnderline(false);
        });

        detailLabel.setOnMouseClicked(event -> {
            showDetails(text, isFirstLabel);
        });

        return detailLabel;
    }

    private void showDetails(String text, boolean isFirstLabel) {
        // Remove highlight from previously selected labels
        for (Node child : detailPane.getChildren()) {
            if (child instanceof Label) {
                Label label = (Label) child;
                label.getStyleClass().remove("label-selected");
            }
        }

        Label selectedLabel = null;
        for (Node child : detailPane.getChildren()) {
            if (child instanceof Label && ((Label) child).getText().equals(text)) {
                selectedLabel = (Label) child;
                break;
            }
        }

        TreeView treeView = null;
        if (selectedLabel != null) {
            selectedLabel.getStyleClass().add("label-selected");
            if (isFirstLabel) {
                treeView = cratingGeneralFlowExecutionDetail();
            } else {
                treeView = cratingStepsExecutionDetail(text);
            }

            TextArea textArea = new TextArea();
            textArea.setWrapText(true);  // Enable text wrapping
            textArea.setEditable(false);

            if (treeView != null) {
                treeView.getStyleClass().add("tree-view-style");
                StackPane stackPane = new StackPane();

                stackPane.getChildren().addAll(textArea, treeView);

                FlowMasterDetails.setMasterNode(stackPane);
                FlowMasterDetails.setDividerPosition(0.3);

            }
        }
    }



    TreeView<Object> cratingStepsExecutionDetail(String dataName) {
        TreeItem<Object> rootItem = new TreeItem<>("Step Details");
        rootItem.setExpanded(true);

        int colonIndex = dataName.indexOf(":"); // Find the index of the colon
        String stepFinalName;
        if (colonIndex != -1) {
            stepFinalName= dataName.substring(colonIndex + 1).trim(); // Extract the substring after the colon and trim any leading/trailing spaces
        } else {
            stepFinalName = null;
        }
        DTOStepExecutionData step = flowExecution.getStepExecutionDataList().stream().filter(data -> data.getFinalNameStep().equals(stepFinalName)).findFirst().get();

        TreeItem<Object> flowNameItem;
        if (step.getFinalNameStep().equals(step.getOriginalName())) {
            flowNameItem = new TreeItem<>("Step Name: " + step.getFinalNameStep());
        } else {
            flowNameItem = new TreeItem<>("Step Name:" + step.getOriginalName() + " (renamed to " + step.getFinalNameStep() + ")");
        }

        rootItem.getChildren().add(flowNameItem);

        TreeItem<Object> totalTimeItem = new TreeItem<>("Total Running Time: " + step.getTotalStepTime().toMillis() + " ms");
        rootItem.getChildren().add(totalTimeItem);

        TreeItem<Object> resultItem = new TreeItem<>("Step Result: " + step.getResult());
        rootItem.getChildren().add(resultItem);

        TreeItem<Object> summaryItem = new TreeItem<>("Step Summery Line: " + step.getSummaryLine());
        rootItem.getChildren().add(summaryItem);

        TreeItem<Object> ioItem = new TreeItem<>("Step IO:");
        rootItem.getChildren().add(ioItem);
        ioItem.setExpanded(true);

        TreeItem<Object> inputItem = new TreeItem<>("Inputs:");
        ioItem.getChildren().add(inputItem);

        TreeItem<Object> outputItem = new TreeItem<>("Outputs:");
        ioItem.getChildren().add(outputItem);

        AtomicInteger inputIndex = new AtomicInteger(1);
        AtomicInteger outputIndex = new AtomicInteger(1);


        flowExecution.getIOlist().stream()
                .filter(io1 -> io1.getStepName().equals(step.getFinalNameStep()))
                .forEach(io -> {
                    if(io.getIOType().equals(IO.INPUT)) {
                        TreeItem<Object> input = new TreeItem<>("Input " + inputIndex.getAndIncrement());
                        inputItem.getChildren().add(input);
                        input.getChildren().add(new TreeItem<>("Final Name: " + io.getFinalName()));
                        if (io.getType().toString().equals("RELATION") || io.getType().toString().equals("STRING_LIST")
                                || io.getType().toString().equals("FILE_LIST") || io.getType().toString().equals("MAPPING2NUMBERS")) {
                            input.getChildren().add(new TreeItem<>(showOutputValue(io)));
                        } else {
                            if (io.getValue() != null) {
                                input.getChildren().add(new TreeItem<>("Value: " + io.getValue().toString()));
                            } else {
                                input.getChildren().add(new TreeItem<>("Value: N/A"));
                            }
                        }
                    }

                    if(io.getIOType().equals(IO.OUTPUT)) {
                        TreeItem<Object> output = new TreeItem<>("Output " + outputIndex.getAndIncrement());
                        outputItem.getChildren().add(output);

                        output.getChildren().add(new TreeItem<>("Final Name: " + io.getFinalName()));
                        if (io.getType().toString().equals("RELATION") || io.getType().toString().equals("STRING_LIST")
                                || io.getType().toString().equals("FILE_LIST") || io.getType().toString().equals("MAPPING2NUMBERS")) {
                            output.getChildren().add(new TreeItem<>(showOutputValue(io)));
                        } else {
                            if (io.getValue() != null) {
                                output.getChildren().add(new TreeItem<>("Value: " + io.getValue().toString()));
                            } else {
                                output.getChildren().add(new TreeItem<>("Value: Not created due to failure in flow"));
                            }
                        }
                    }
                });


        TreeItem<Object> logsItem = new TreeItem<>("Step's Logs:");
        rootItem.getChildren().add(logsItem);
        logsItem.setExpanded(true);

        AtomicInteger logsIndex = new AtomicInteger(1);
        step.getLoggerList().forEach(log -> {
            TreeItem<Object> logItem = new TreeItem<>("Log " + logsIndex.getAndIncrement());
            logsItem.getChildren().add(logItem);
            logItem.getChildren().addAll(
                    new TreeItem<>("Log Time: " + log.getLogTime()),
                    new TreeItem<>("Log Message: " + log.getLog())
                    );
        });

        TreeView<Object> treeView = new TreeView<>(rootItem);
        treeView.setShowRoot(false);
        treeView.getStyleClass().add("tree-view-style");


        return treeView;
    }

    TreeView<Object> cratingGeneralFlowExecutionDetail() {
        TreeItem<Object> rootItem = new TreeItem<>("Flow Details");
        rootItem.setExpanded(true);

        TreeItem<Object> flowNameItem = new TreeItem<>("Flow Name: " + flowExecution.getFlowName());
        rootItem.getChildren().add(flowNameItem);

        TreeItem<Object> flowIdItem = new TreeItem<>("Flow ID: " + flowExecution.getUniqueId());
        rootItem.getChildren().add(flowIdItem);

        TreeItem<Object> startTimeItem = new TreeItem<>("Start Time: " + flowExecution.getStartTimeFormatted());
        rootItem.getChildren().add(startTimeItem);

        // Add Flow's Free Inputs
        TreeItem<Object> freeInputsItem = new TreeItem<>("Flow's Free Inputs");
        rootItem.getChildren().add(freeInputsItem);
        freeInputsItem.setExpanded(true);

        AtomicInteger freeInputIndex = new AtomicInteger(1);
        List<DTOSingleFlowIOData> sortedList = flowExecution.getFreeInputsList().stream()
                .sorted(Comparator.comparing(obj -> obj.getNecessity().equals(DataNecessity.MANDATORY) ? 0 : 1))
                .collect(Collectors.toList());

        sortedList.forEach(input -> {
            TreeItem<Object> inputItem = new TreeItem<>("Free Input " + freeInputIndex.getAndIncrement());
            freeInputsItem.getChildren().add(inputItem);
            inputItem.getChildren().addAll(
                    new TreeItem<>("Final Name: " + input.getFinalName()),
                    new TreeItem<>("Type: " + input.getType().toString()),
                    input.getValue() != null ? new TreeItem<>("Value: " + input.getValue().toString()) :
                            new TreeItem<>("Value: N/A"),
                    new TreeItem<>("Is Mandatory / Optional: " + input.getNecessity().toString())
            );
        });

        if (flowExecution.getFlowExecutionResult() != null) {
            // Add Flow Result
            TreeItem<Object> flowResultItem = new TreeItem<>("Flow Result: " + flowExecution.getFlowExecutionResult().toString());
            rootItem.getChildren().add(flowResultItem);

        }
        if (flowExecution.getTotalTime() != null) {
            // Add Total Running Time
            TreeItem<Object> totalTimeItem = new TreeItem<>("Total Running Time: " + String.format("%d ms", flowExecution.getTotalTime().toMillis()));
            rootItem.getChildren().add(totalTimeItem);
        }
        if (flowExecution.getFlowExecutionResult() != null) {
            // Add Flow's Outputs
            TreeItem<Object> outputsItem = new TreeItem<>("Flow's Outputs");
            rootItem.getChildren().add(outputsItem);
            outputsItem.setExpanded(true);

            AtomicInteger outputIndex = new AtomicInteger(1);
            List<DTOSingleFlowIOData> outputs = flowExecution.getIOlist().stream().filter(io -> io.getIOType().equals(IO.OUTPUT)).collect(Collectors.toList());

            for (DTOSingleFlowIOData output : outputs) {
                TreeItem<Object> outputItem = new TreeItem<>("Output " + outputIndex.getAndIncrement());
                outputsItem.getChildren().add(outputItem);
                outputItem.getChildren().addAll(
                        new TreeItem<>("Final Name: " + output.getFinalName()),
                        new TreeItem<>("Type: " + output.getType().toString()),
                        output.getType().toString().equals("RELATION") || output.getType().toString().equals("STRING_LIST")
                                || output.getType().toString().equals("FILE_LIST") || output.getType().toString().equals("MAPPING2NUMBERS")
                                ? new TreeItem<>(showOutputValue(output)) : new TreeItem<>("Value: " + output.getValue().toString())
                );
            }
        }

        TreeView<Object> treeView = new TreeView<>(rootItem);
        treeView.setShowRoot(false);

        treeView.getStyleClass().add("tree-view-style");

        return treeView;
    }

    public Hyperlink showOutputValue(DTOSingleFlowIOData output) {

        Hyperlink viewDataLink = new Hyperlink("View Data");
        viewDataLink.setOnAction(event -> {
            Stage popupwindow = new Stage();

            popupwindow.initModality(Modality.APPLICATION_MODAL);
            popupwindow.setTitle("Data Values");
            Label label1 = new Label("Data Values");
            label1.getStyleClass().add("data-values-label");
            VBox layout = new VBox(10);
            
            if (output.getType().toString().equals("RELATION")) {
                TableView table = showRelationData(output);
                layout.getChildren().addAll(label1, table);
            }else if(output.getType().toString().equals("STRING_LIST") || output.getType().toString().equals("FILE_LIST")){
                ListView<String> list = showListData(output);
                layout.getChildren().addAll(label1, list);
            }else if(output.getType().toString().equals("MAPPING2NUMBERS")){
                TableView table = showMappingData(output);
                layout.getChildren().addAll(label1, table);
            }
            layout.setAlignment(Pos.CENTER);
            Scene scene1 = new Scene(layout, 700, 400);
            scene1.getStylesheets().add(getClass().getResource("MasterDetail.css").toExternalForm());

            popupwindow.setScene(scene1);
            popupwindow.showAndWait();
        });
        return  viewDataLink;
    }

    public TableView showMappingData(DTOSingleFlowIOData output) {
        TableView<Map.Entry<Number, Number>> table = new TableView<>();
        table.setEditable(false);

        TableColumn<Map.Entry<Number, Number>, String> keyCol = new TableColumn<>("Key");
        keyCol.setCellValueFactory(cellData -> {
            Number key = cellData.getValue().getKey();
            String displayValue = key.equals(0) ? "car" : "cdr";
            return new SimpleStringProperty(displayValue);
        });

        TableColumn<Map.Entry<Number, Number>, Number> valueCol = new TableColumn<>("Value");
        valueCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getValue()));

        double tableWidth = 1.0; // Total width of the table, set to 1.0 for simplicity
        double columnWidth = tableWidth / 2;

        keyCol.prefWidthProperty().bind(table.widthProperty().multiply(columnWidth));
        valueCol.prefWidthProperty().bind(table.widthProperty().multiply(columnWidth));

        table.getColumns().addAll(keyCol, valueCol);

        ObservableList<Map.Entry<Number, Number>> items = FXCollections.observableArrayList();
        items.addAll(((NumberMappingData) output.getValue()).getItems().entrySet());

        table.getStyleClass().add("table-view-style");

        table.setItems(items);
        return table;
    }


    public ListView<String> showListData(DTOSingleFlowIOData output){
        ListView<String> list = new ListView<>();

        ObservableList<String> items = FXCollections.observableArrayList();
        int index =1;
        if(output.getType().toString().equals("FILE_LIST")) {
            for (File value :((FileListData) output.getValue()).getItems()) {
                String name = index +". " + value.getAbsolutePath();
                index++;
                items.add(name);
            }
        }else {//////////////////check in step 3
            for (String value : ((StringListData) output.getValue()).getItems()) {
                String name = index +". " + value;
                index++;
                items.add(value);
            }
        }

        list.getStyleClass().add("list-view-style");

        list.setItems(items);

        return list;
    }

    public TableView showRelationData(DTOSingleFlowIOData output) {
        TableView<Map<String, String>> table = new TableView<>();
        table.setEditable(false);
        table.setSelectionModel(null);

        double tableWidth = 1.0; // Total width of the table, set to 1.0 for simplicity

        double firstColumnWidth = tableWidth * 0.15;
        double remainingColumnsWidth = (tableWidth - firstColumnWidth) / 2;

        for (String column : ((RelationData) output.getValue()).getColumns()) {
            TableColumn<Map<String, String>, String> tableColumn = new TableColumn<>(column);

            // Set the preferred width for the columns
            if (column.equals(((RelationData) output.getValue()).getColumns().get(0))) {
                tableColumn.prefWidthProperty().bind(table.widthProperty().multiply(firstColumnWidth));
            } else {
                tableColumn.prefWidthProperty().bind(table.widthProperty().multiply(remainingColumnsWidth));
            }

            tableColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(column)));
            table.getColumns().add(tableColumn);
        }

        ObservableList<Map<String, String>> data = FXCollections.observableArrayList();
        for (RelationData.SingleRow singleRow : ((RelationData) output.getValue()).getRows()) {
            Map<String, String> row = singleRow.getRowData();
            data.add(row);
            table.setItems(data);
        }
        table.getStyleClass().add("table-view-style");

        return table;
    }


/*    public TableView showRelationData(DTOSingleFlowIOData output) {
        TableView table = new TableView();
        table.setEditable(false);

        for (String column : ((RelationData) output.getValue()).getColumns()) {
            TableColumn<Map<String, String>, String> tableColumn = new TableColumn<>(column);
            tableColumn.setPrefWidth(100);
            tableColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(column)));
            table.getColumns().add(tableColumn);
        }

        ObservableList<Map<String, String>> data = FXCollections.observableArrayList();
        for (RelationData.SingleRow singleRow : ((RelationData) output.getValue()).getRows()) {
            Map<String, String> row = singleRow.getRowData();
            data.add(row);
            table.setItems(data);
        }

       return table;
    }*/

}

/* private TextFlow createTextFlow(String label, String value) {
        Text labelText = new Text(label);
        labelText.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.ITALIC, 12));

        Text valueText = new Text(value);
        valueText.setFont(Font.font("Arial", FontWeight.NORMAL, 12));

        TextFlow textFlow = new TextFlow(labelText, valueText);
        return textFlow;
    }

    private TextFlow createTextFlow(String label, Void value) {
        Text labelText = new Text(label);
        labelText.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.ITALIC, 12));

        Text valueText = new Text(value.toString());
        valueText.setFont(Font.font("Arial", FontWeight.NORMAL, 12));

        TextFlow textFlow = new TextFlow(labelText, valueText);
        return textFlow;
    }*/
