package components.body.executionsHistoryTab.MasterDetail;

import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import components.body.executionsHistoryTab.AdminExecutionsHistoryTabController;
import dto.*;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AdminMasterDetailController {
    @FXML
    private MasterDetailPane FlowMasterDetails;
    private AdminExecutionsHistoryTabController flowExecutionHistoryTabController;
    private DTOFlowExeInfo flowExecution;
    private VBox detailPane;
    private List<String> addedStepNames = new ArrayList<>();
    private int stepCounter;


    public MasterDetailPane getFlowMasterDetails() {return FlowMasterDetails;}
    @FXML
    public void initialize() {
        FlowMasterDetails.setDetailSide(Side.LEFT);
        FlowMasterDetails.setDividerPosition(0.3);
    }

    public void setExecutionsHistoryTabController(AdminExecutionsHistoryTabController flowExecutionHistoryTabController) {
        this.flowExecutionHistoryTabController = flowExecutionHistoryTabController;
    }
    public void initMasterDetailPaneController(){
        detailPane = new VBox();
        ScrollPane scrollPane = new ScrollPane(detailPane);
        scrollPane.setFitToWidth(true);
        FlowMasterDetails.setDetailNode(scrollPane);
        StackPane stackPane = new StackPane();
        FlowMasterDetails.setMasterNode(stackPane);
    }
    public void initMasterDetailPaneController(DTOFlowExeInfo flowExecution) {
        this.flowExecution = flowExecution;
        detailPane = new VBox();
        detailPane.setPadding(new Insets(10));
        detailPane.setSpacing(5);
        Label flowDetailLabel = createDetailLabel(flowExecution.getFlowName(), FlowMasterDetails, true, detailPane);
        detailPane.getChildren().add(flowDetailLabel);
        flowDetailLabel.getStyleClass().add("label-selected");
        addedStepNames = new ArrayList<>();
        stepCounter = 1;
    }
    public void updateFlowLabel(DTOFlowExeInfo flowExecution) {
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
    public void addStepsToMasterDetail(DTOFlowExeInfo flowExecution) {
        this.flowExecution = flowExecution;
        for (DTOStepsInFlow stepExecution : flowExecution.getSteps()) {
            String stepName = stepExecution.getFinalName();
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
        DTOStepsInFlow step = flowExecution.getSteps().stream().filter(data -> data.getFinalName().equals(stepFinalName)).findFirst().get();

        TreeItem<Object> flowNameItem;
        if (step.getFinalName().equals(step.getOriginalName())) {
            flowNameItem = new TreeItem<>("Step Name: " + step.getFinalName());
        } else {
            flowNameItem = new TreeItem<>("Step Name:" + step.getOriginalName() + " (renamed to " + step.getFinalName() + ")");
        }

        rootItem.getChildren().add(flowNameItem);

        TreeItem<Object> totalTimeItem = new TreeItem<>("Total Running Time: " + String.valueOf(step.getTotalTime().toMillis()) + " ms");
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

        step.getInputs().stream().forEach(ioInput-> {
            TreeItem<Object> input = new TreeItem<>("Input " + inputIndex.getAndIncrement());
            inputItem.getChildren().add(input);
            input.getChildren().add(new TreeItem<>("Final Name: " + ioInput.getFinalName()));
                    if (ioInput.getType().equals("RELATION") || ioInput.getType().equals("STRING_LIST")
                            || ioInput.getType().equals("FILE_LIST") || ioInput.getType().equals("MAPPING2NUMBERS")) {
                        input.getChildren().add(new TreeItem<>(showOutputValue(ioInput.getType(),ioInput.getValue())));
                    } else {
            if (ioInput.getValue() != null) {
                input.getChildren().add(new TreeItem<>("Value: " + ioInput.getValue().toString()));
            } else {
                input.getChildren().add(new TreeItem<>("Value: N/A"));
            }
            };
        });

        step.getOutputs().stream().forEach(ioOutput -> {
            TreeItem<Object> output = new TreeItem<>("Output " + outputIndex.getAndIncrement());
            outputItem.getChildren().add(output);
            output.getChildren().add(new TreeItem<>("Final Name: " + ioOutput.getFinalName()));
            if (ioOutput.getType().equals("RELATION") || ioOutput.getType().equals("STRING_LIST")
                    || ioOutput.getType().equals("FILE_LIST") || ioOutput.getType().equals("MAPPING2NUMBERS")) {
                output.getChildren().add(new TreeItem<>(showOutputValue(ioOutput.getType(),ioOutput.getValue())));
            } else {
            if (ioOutput.getValue() != null) {
                output.getChildren().add(new TreeItem<>("Value: " + ioOutput.getValue()));
            } else {
                output.getChildren().add(new TreeItem<>("Value: Not created due to failure in flow"));
            }
            }
        });

        TreeItem<Object> logsItem = new TreeItem<>("Step's Logs:");
        rootItem.getChildren().add(logsItem);
        logsItem.setExpanded(true);

        AtomicInteger logsIndex = new AtomicInteger(1);
        step.getLoggers().forEach(log -> {
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

        TreeItem<Object> flowIdItem = new TreeItem<>("Flow ID: " + flowExecution.getID());
        rootItem.getChildren().add(flowIdItem);

        TreeItem<Object> startTimeItem = new TreeItem<>("Start Time: " + flowExecution.getStartTime());
        rootItem.getChildren().add(startTimeItem);


        if (flowExecution.getResultExecute() != null) {
            TreeItem<Object> flowResultItem = new TreeItem<>("Flow Result: " + flowExecution.getResultExecute());
            rootItem.getChildren().add(flowResultItem);

        }
        if (flowExecution.getTotalTime() != null) {
            // Add Total Running Time
            TreeItem<Object> totalTimeItem = new TreeItem<>("Total Running Time: " + String.format("%d ms", flowExecution.getTotalTime().toMillis()));
            rootItem.getChildren().add(totalTimeItem);
        }

        // Add Flow's Free Inputs
        TreeItem<Object> freeInputsItem = new TreeItem<>("Flow's Free Inputs");
        rootItem.getChildren().add(freeInputsItem);
        freeInputsItem.setExpanded(true);

        AtomicInteger freeInputIndex = new AtomicInteger(1);
        List<DTOFreeInputs> sortedList = flowExecution.getFreeInputs().stream()
                .sorted(Comparator.comparing(obj -> obj.getNecessity().equals(DataNecessity.MANDATORY) ? 0 : 1))
                .collect(Collectors.toList());

        sortedList.forEach(input -> {
            TreeItem<Object> inputItem = new TreeItem<>("Free Input " + freeInputIndex.getAndIncrement());
            freeInputsItem.getChildren().add(inputItem);
            inputItem.getChildren().addAll(
                    new TreeItem<>("Final Name: " + input.getName()),
                    new TreeItem<>("Type: " + input.getType().toString()),
                    input.getValue() != null ? new TreeItem<>("Value: " + input.getValue().toString()) :
                            new TreeItem<>("Value: N/A"),
                    new TreeItem<>("Is Mandatory / Optional: " + input.getNecessity().toString())
            );
        });

        if (flowExecution.getResultExecute() != null) {
            // Add Flow's Outputs
            TreeItem<Object> outputsItem = new TreeItem<>("Flow's Outputs");
            rootItem.getChildren().add(outputsItem);
            outputsItem.setExpanded(true);

            AtomicInteger outputIndex = new AtomicInteger(1);
            List<DTOOutput>outputs = flowExecution.getOutputs();

            for (DTOOutput output : outputs) {
                TreeItem<Object> outputItem = new TreeItem<>("Output " + outputIndex.getAndIncrement());
                outputsItem.getChildren().add(outputItem);
                outputItem.getChildren().addAll(
                        new TreeItem<>("Final Name: " + output.getFinalName()),
                        new TreeItem<>("Type: " + output.getType().toString()),
                        output.getValue() == null ?  new TreeItem<>( "Value: N/A") :
                                output.getType().equals("RELATION") || output.getType().equals("STRING_LIST")
                                        || output.getType().equals("FILE_LIST") || output.getType().equals("MAPPING2NUMBERS")
                                        ? new TreeItem<>(showOutputValue(output.getType(), output.getValue())) : new TreeItem<>("Value: " + output.getValue().toString())
                );

            }
        }

        TreeView<Object> treeView = new TreeView<>(rootItem);
        treeView.setShowRoot(false);
        treeView.getStyleClass().add("tree-view-style");
        return treeView;
    }
    public Hyperlink showOutputValue(String type,Object io) {
        Hyperlink viewDataLink = new Hyperlink("View Data");
        viewDataLink.setOnAction(event -> {
            Stage popupWindow = new Stage();

            popupWindow.initModality(Modality.APPLICATION_MODAL);
            popupWindow.setTitle("Data Values");
            Label label1 = new Label("Data Values");
            label1.getStyleClass().add("data-values-label");
            VBox layout = new VBox(10);

            if (type.equals("RELATION")) {
                TableView table = showRelationData(io);
                layout.getChildren().addAll(label1, table);
            }else if(type.equals("STRING_LIST") || type.equals("FILE_LIST")){
                ListView<String> list = showListData(io , type);
                layout.getChildren().addAll(label1, list);
            }else if(type.equals("MAPPING2NUMBERS")){
                TableView table = showMappingData(io);
                layout.getChildren().addAll(label1, table);
            }
            layout.setAlignment(Pos.CENTER);
            Scene scene1 = new Scene(layout, 700, 400);
            scene1.getStylesheets().add(getClass().getResource("MasterDetail.css").toExternalForm());

            popupWindow.setScene(scene1);
            popupWindow.showAndWait();
        });
        return  viewDataLink;
    }

    public TableView showMappingData(Object io) {
        LinkedTreeMap<String, Object> linkedTreeMap = (LinkedTreeMap<String, Object>) io;
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


        Map<Integer, Double> resultMap = parseStringToMap(linkedTreeMap.get("map").toString());

        int value0 = 0;
        if (resultMap.containsKey(0)) {
            value0 = resultMap.get(0).intValue();
            items.add(new AbstractMap.SimpleEntry<>(0, value0));
        }

        int value1 = 0;
        if (resultMap.containsKey(1)) {
            value1 = resultMap.get(1).intValue();
            items.add(new AbstractMap.SimpleEntry<>(1, value1));
        }

        table.getStyleClass().add("table-view-style");

        table.setItems(items);
        return table;
    }

    public static Map<Integer, Double> parseStringToMap(String jsonString) {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<Integer, Double>>() {}.getType();
        return gson.fromJson(jsonString, type);
    }

    public ListView<String> showListData(Object io , String type) {
        ListView<String> list = new ListView<>();

        ObservableList<String> items = FXCollections.observableArrayList();
        int index = 1;
        LinkedTreeMap<String, Object> linkedTreeMap = (LinkedTreeMap<String, Object>) io;
        List<String> list1 = extractPathsFromList(linkedTreeMap.get("list").toString());

        for (String dictionary : list1) {
            String val = index + ". " + dictionary;
            index++;
            items.add(val);
        }

        list.getStyleClass().add("list-view-style");
        list.setItems(items);
        return list;
    }

    public List<String> extractPathsFromList(String listString) {
        List<String> pathsList = new ArrayList<>();

        // Regular expression pattern to match the paths
        Pattern pattern = Pattern.compile("path=([^,]+)");
        Matcher matcher = pattern.matcher(listString);

        while (matcher.find()) {
            String path = matcher.group(1);
            pathsList.add(path);
        }
        return pathsList;
    }

    public TableView showRelationData(Object io) {
        TableView<Map<String, String>> table = new TableView<>();
        table.setEditable(false);
        table.setSelectionModel(null);
        double tableWidth = 1.0; // Total width of the table, set to 1.0 for simplicity
        double firstColumnWidth = tableWidth * 0.15;
        double remainingColumnsWidth = (tableWidth - firstColumnWidth) / 2;

        List<String> columns = Arrays.asList("Serial Number", "Original file's name", "file's name after change");

        for (int i = 0; i < columns.size(); i++) {
            String column = columns.get(i);
            TableColumn<Map<String, String>, String> tableColumn = new TableColumn<>(column);

            if (i == 0) {
                tableColumn.prefWidthProperty().bind(table.widthProperty().multiply(firstColumnWidth));
            } else {
                tableColumn.prefWidthProperty().bind(table.widthProperty().multiply(remainingColumnsWidth));
            }

            tableColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(column)));
            table.getColumns().add(tableColumn);
        }

        LinkedTreeMap<String, Object> linkedTreeMap = (LinkedTreeMap<String, Object>) io;

        List<Map<String, String>> dataList = parseJsonStringToList(linkedTreeMap.get("rows").toString());

        // Now, you can work with the dataList to extract the relevant information.
        // For example, you can iterate over the dataList and extract each entry's data.
        for (Map<String, String> dataMap : dataList) {
            String fileNameAfterChange = dataMap.get("file's name after change");
            String originalFileName = dataMap.get("Original file's name");
            String serialNumber = dataMap.get("Serial Number");

            Map<String, String> rowData = new HashMap<>();
            rowData.put("Serial Number", serialNumber);
            rowData.put("Original file's name", originalFileName);
            rowData.put("file's name after change", fileNameAfterChange);

            table.getItems().add(rowData);
        }
        table.getStyleClass().add("table-view-style");


        return table;
    }

    public static List<Map<String, String>> parseJsonStringToList(String jsonString) {
        // Implement your custom parsing here
        // For example, split the string and extract the data accordingly
        // In this example, we are simply splitting the string by "}, {"
        jsonString = jsonString.replace("[", "").replace("]", "");

        String[] dataEntries = jsonString.split("\\}, \\{");

        List<Map<String, String>> dataList = new ArrayList<>();

        for (String dataEntry : dataEntries) {
            // Remove the curly braces and split each entry by comma
            String[] keyValuePairs = dataEntry.replace("{", "").replace("}", "").split(", ");

            Map<String, String> dataMap = new HashMap<>();

            for (String keyValuePair : keyValuePairs) {
                String[] parts = keyValuePair.split("=");
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    dataMap.put(key, value);
                }
                if (parts.length == 3) {
                    String key = parts[1].trim();
                    String value = parts[2].trim();
                    dataMap.put(key, value);
                }
            }

            dataList.add(dataMap);
        }

        return dataList;
    }
}
