package javafx.flowExecutionTab.MasterDetail;
import datadefinition.impl.relation.RelationData;
import dto.DTOFlowExecution;
import dto.DTOSingleFlowIOData;
import dto.DTOStepExecutionData;
import flow.api.FlowIO.IO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.flowExecutionTab.FlowExecutionTabController;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.*;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import org.controlsfx.control.MasterDetailPane;
import steps.api.DataNecessity;
import steps.api.StepResult;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MasterDetailController {

    @FXML
    private MasterDetailPane FlowMasterDetails;

    FlowExecutionTabController flowExecutionTabController;

    DTOFlowExecution flowExecution;

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

    public void initMasterDetailComponent(DTOFlowExecution FlowExecution) {
        flowExecution = FlowExecution;
        VBox detailPane = new VBox();
        detailPane.setPadding(new Insets(10));
        detailPane.setSpacing(5);

        Label FlowdetailLabel = createDetailLabel(flowExecution.getFlowName(), FlowMasterDetails, true);
        detailPane.getChildren().add(FlowdetailLabel);

        int counter = 1;
        for (DTOStepExecutionData stepExecution : flowExecution.getStepExecutionDataList()) {
            Label detailLabel = createDetailLabel("Step " + counter++ + ": " + stepExecution.getFinalNameStep(), FlowMasterDetails, false);
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
        }

        ScrollPane scrollPane = new ScrollPane(detailPane);
        scrollPane.setFitToWidth(true);
        FlowMasterDetails.setDetailNode(scrollPane);
        FlowMasterDetails.setDividerPosition(0.3);
    }

    private TextFlow createTextFlow(String label, String value) {
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
    }

    private Label createDetailLabel(String text, MasterDetailPane masterDetailPane, boolean isFirstLabel) {
        Label detailLabel = new Label(text);
        detailLabel.setOnMouseClicked(event -> {
            TextArea textArea = new TextArea();
            textArea.setWrapText(true);  // Enable text wrapping
            textArea.setEditable(false); // Make the text area read-only

            TextFlow textFlow = null;
            TreeView treeView = null;
            if (isFirstLabel) {
                treeView = cratingGeneralFlowExecutionDetail();
            } else {
                //steps method
            }
            StackPane stackPane = new StackPane();
/*            ScrollPane scrollPane = new ScrollPane(treeView);
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);*/

            stackPane.getChildren().addAll(textArea, treeView);


            FlowMasterDetails.setMasterNode(stackPane);
            masterDetailPane.setDividerPosition(0.3);
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


    TreeView<Object> cratingGeneralFlowExecutionDetail() {
        TreeItem<Object> rootItem = new TreeItem<>("Flow Details");
        rootItem.setExpanded(true);

        // Add Flow Name
        TreeItem<Object> flowNameItem = new TreeItem<>("Flow Name: " + flowExecution.getFlowName());
        rootItem.getChildren().add(flowNameItem);

        // Add Flow ID
        TreeItem<Object> flowIdItem = new TreeItem<>("Flow ID: " + flowExecution.getUniqueId());
        rootItem.getChildren().add(flowIdItem);

        // Add Flow Result
        TreeItem<Object> flowResultItem = new TreeItem<>("Flow Result: " + flowExecution.getFlowExecutionResult().toString());
        rootItem.getChildren().add(flowResultItem);

        // Add Start Time
        TreeItem<Object> startTimeItem = new TreeItem<>("Start Time: " + flowExecution.getStartTimeFormatted());
        rootItem.getChildren().add(startTimeItem);

        // Add Total Running Time
        TreeItem<Object> totalTimeItem = new TreeItem<>("Total Running Time: " + String.format("%d ms", flowExecution.getTotalTime().toMillis()));
        rootItem.getChildren().add(totalTimeItem);

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
                    new TreeItem<>(showOutputValue(output))
            );
        }

        TreeView<Object> treeView = new TreeView<>(rootItem);
        treeView.setShowRoot(false);

        return treeView;
    }


    public Hyperlink showOutputValue(DTOSingleFlowIOData output) {
        System.out.println("Output Type: " + output.getType().toString());
        if (output.getType().toString().equals("RELATION")) {
            Hyperlink viewDataLink = new Hyperlink("View Data");
            viewDataLink.setOnAction(event -> {

                Stage popupwindow = new Stage();
                popupwindow.initModality(Modality.APPLICATION_MODAL);
                popupwindow.setTitle("This is a pop up window");
                Label label1 = new Label("Pop up window now displayed");

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
                }

/*                for (String column :((RelationData)output.getValue()).getColumns()) {
                    TableColumn<String, String> tableColumn = new TableColumn<>(column);
                    tableColumn.setPrefWidth(100);
                    table.getColumns().add(tableColumn);
                }

                for (RelationData.SingleRow singleRow :((RelationData)output.getValue()).getRows()) {
                    TableRow<String> tableROW = new TableRow<>()
                    Map<String, String> row = singleRow.getRowData();

                }*/

               // table.getItems().addAll(((RelationData)output.getValue()).getRows());

                VBox layout = new VBox(10);
                layout.getChildren().addAll(label1, table);
                layout.setAlignment(Pos.CENTER);
                Scene scene1 = new Scene(layout, 300, 250);

                popupwindow.setScene(scene1);
                popupwindow.showAndWait();
            });

            return viewDataLink;
        }
        return null;
    }
}




/*
    TextFlow cratingGeneralFlowExecutionDetail(){
        TextFlow textFlow = new TextFlow();

        textFlow.getChildren().addAll(
                createTextFlow("Flow Name: ", flowExecution.getFlowName()),
                new Text("\n"),
                createTextFlow("Flow ID: ", flowExecution.getUniqueId()),
                new Text("\n"),
                createTextFlow("Flow Result: ", flowExecution.getFlowExecutionResult().toString()),
                new Text("\n"),
                createTextFlow("Start Time: ", flowExecution.getStartTimeFormatted()),
                new Text("\n"),
                createTextFlow("Total Running Time: ", String.format("%d ms", flowExecution.getTotalTime().toMillis())),
                new Text("\n\n"),
                createTextFlow("Flow's Free Inputs: ", ""),
                new Text("\n")
        );

        AtomicInteger freeInputIndex = new AtomicInteger(1);
        List<DTOSingleFlowIOData> sortedList = flowExecution.getFreeInputsList().stream()
                .sorted(Comparator.comparing(obj -> obj.getNecessity().equals(DataNecessity.MANDATORY) ? 0 : 1))
                .collect(Collectors.toList());

        sortedList.forEach(input -> {
            textFlow.getChildren().addAll(
                    createTextFlow("Free Input " + freeInputIndex.getAndIncrement() + ":", ""),
                    new Text("\n"),
                    createTextFlow("\tFinal Name: ", input.getFinalName()),
                    new Text("\n"),
                    createTextFlow("\tType: ", input.getType().toString()),
                    new Text("\n"),
                    input.getValue() != null ? createTextFlow("\tValue: ", input.getValue().toString()) :
                            createTextFlow("\tValue: N/A", ""),
                    new Text("\n"),
                    createTextFlow("\tIs Mandatory / Optional: ", input.getNecessity().toString()),
                    new Text("\n")
            );
        });

        AtomicInteger outputIndex = new AtomicInteger(1);
        List<DTOSingleFlowIOData> outputs = flowExecution.getIOlist().stream().filter(io -> io.getIOType().equals(IO.OUTPUT)).collect(Collectors.toList());

        textFlow.getChildren().addAll(
                new Text("\n"),
                createTextFlow("Flow's Outputs ", ""),
                new Text("\n"));

        for(DTOSingleFlowIOData output: outputs) {
        textFlow.getChildren().addAll(
            createTextFlow("Output " ,outputIndex.getAndIncrement() + ":"),
            new Text("\n"),
            createTextFlow("\tFinal Name: " , output.getFinalName()),
            new Text("\n"),
            createTextFlow("\tType: " , output.getType().toString()),
            new Text("\n"),
            output.getValue() != null ? createTextFlow("\tValue: ", output.getValue().toString()) :  createTextFlow("\tValue: Not created due to failure in flow", ""),
            new Text("\n")
        );};
        return textFlow;

        //textFlow.getChildren().addAll( showOutputValue());
    }
*/








    // {
/*
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setWrapText(true);

        TextFlow textFlow = new TextFlow();
        textFlow.setLineSpacing(5);

        Text boldText = new Text("This is bold text");
        boldText.setFont(Font.font("Arial", FontWeight.BOLD, 12));

        Text regularText = new Text("This is regular text");

        textFlow.getChildren().addAll(boldText, regularText);

        textArea.setText(""); // Clear the existing text
        textArea.setPrefRowCount(10); // Set the desired number of rows
        textArea.setFont(Font.font("Arial", 12)); // Set the desired font

        // Add the TextFlow as the graphic of the TextArea
        textArea.setGraphic(textFlow);*/
/*

        TextFlow textFlow = new TextFlow();
        Text labelText = new Text("Flow Name: ");
        labelText.setFont(Font.font("Arial", FontWeight.BOLD, 12));

        Text flowNameText = new Text(flowExecution.getFlowName());
        flowNameText.setFont(Font.font("Arial", FontWeight.NORMAL, 10));
*/

/*// Add the texts to the TextFlow
        textFlow.getChildren().addAll(labelText, flowNameText);
        System.out.println("Flow ID: " + flowExecution.getUniqueId());
        System.out.println();
            System.out.println(String.format("Total Running Time: %d ms", flowExecution.getTotalTime().toMillis()));
            System.out.print("\n");
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
       // detailText.setFont(Font.font("Arial", bold ? FontWeight.BOLD : FontWeight.NORMAL, 12));
        textFlow.getChildren().add(detailText);*/

       // return textFlow;
    //}


/*
        TextFlow cratingFlowExecutionDetail(DTOFlowExecution flowExecution) {
        TextFlow textFlow = new TextFlow();
        Text detailText = new Text();
       // detailText.setFont(Font.font("Arial", bold ? FontWeight.BOLD : FontWeight.NORMAL, 12));
        textFlow.getChildren().add(detailText);

        return textFlow;
    }
*/


//}
