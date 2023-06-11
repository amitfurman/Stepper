package javafx.executionsHistoryTab;

import dto.DTOFlowExecution;
import dto.DTOFlowsExecutionList;
import javafx.Controller;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.flowExecutionTab.MasterDetail.MasterDetailController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import org.controlsfx.control.MasterDetailPane;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ExecutionsHistoryTabController {
    private Controller mainController;
    @FXML
    private TableView executionHistoryTable;
    @FXML
    private TableColumn flowNameColumn;
    @FXML
    private TableColumn startDateColumn;
    @FXML
    private TableColumn resultColumn;
    @FXML
    private TableColumn chooseOldFlowExecutions;

    private ObservableList<ExecutionHistoryEntry> executionHistoryData;
    @FXML
    private ComboBox resultFilterComboBox;
    @FXML
    private GridPane executionHistoryGrid;

    private MasterDetailController masterDetailController;

    private MasterDetailPane masterDetailPane;

    @FXML
    private Button RerunFlow;

    private String currFlowName;

    @FXML
    void rerunCurrentFlow(ActionEvent event) {
        RerunFlow.setOnMouseClicked(e -> {
            mainController.getFlowExecutionTabController().initDataInFlowExecutionTab();
            mainController.getFlowExecutionTabController().initFlowContinuationTableView(mainController.getSystemEngineInterface().getAllContinuationMappingsWithSameSourceFlow(currFlowName));
            mainController.goToFlowExecutionTab(currFlowName);
            Map<String,Object> inputsValues = mainController.getSystemEngineInterface().getFreeInputsFromCurrFlow(currFlowName);
            mainController.getFlowExecutionTabController().setInputValuesFromContinuationMap(inputsValues);
        });
    }

    public void setMainController(Controller mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() throws IOException {
        RerunFlow.setDisable(true);
        executionHistoryTable.getStyleClass().add("execution-history-table");
        double tableWidth = executionHistoryTable.getWidth();
        double columnWidth = tableWidth / 4;
        flowNameColumn.setPrefWidth(columnWidth);
        startDateColumn.setPrefWidth(columnWidth);
        resultColumn.setPrefWidth(columnWidth);
        chooseOldFlowExecutions.setPrefWidth(columnWidth);

        executionHistoryTable.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            double newColumnWidth = newWidth.doubleValue() / 4;
            flowNameColumn.setPrefWidth(newColumnWidth);
            startDateColumn.setPrefWidth(newColumnWidth);
            resultColumn.setPrefWidth(newColumnWidth);
            chooseOldFlowExecutions.setPrefWidth(newColumnWidth);
        });
        executionHistoryTable.getSortOrder().add(flowNameColumn);
        executionHistoryTable.getSortOrder().add(startDateColumn);
        executionHistoryTable.getSortOrder().add(resultColumn);
        executionHistoryTable.getSortOrder().add(chooseOldFlowExecutions);

        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/javafx/flowExecutionTab/MasterDetail/masterDetails.fxml");
        fxmlLoader.setLocation(url);
        MasterDetailPane MasterDetailComponent = fxmlLoader.load(url.openStream());
        MasterDetailController masterDetailController = fxmlLoader.getController();
        this.setMasterDetailsController(masterDetailController);
        this.masterDetailPane = MasterDetailComponent;
        if (masterDetailController != null) {
            masterDetailController.setExecutionsHistoryTabController(this);
        }
       // masterDetailPane.getStylesheets().add("/javafx/flowExecutionTab/MasterDetail/masterDetails.css");
        masterDetailPane.getStylesheets().add("/javafx/flowExecutionTab/flowExecutionTab.css");

        executionHistoryGrid.add(masterDetailPane, 0 , 2);
    }

    private void setMasterDetailsController(MasterDetailController masterDetailController) {
        this.masterDetailController = masterDetailController;
    }

    public void initExecutionHistoryTable() {
        initializeExecutionHistoryTable();
        addFilteringFunctionality();
    }

    public void initExecutionHistoryDataList() {
        executionHistoryData = FXCollections.observableArrayList(); // Initialize the executionHistoryData list

        DTOFlowsExecutionList flowsExecutionList = mainController.getSystemEngineInterface().getFlowsExecutionList();
        for (DTOFlowExecution flowExecution : flowsExecutionList.getFlowsExecutionNamesList()) {
            String flowName = flowExecution.getFlowName();
            String startDate = flowExecution.getStartTimeFormatted();
            String runResult = flowExecution.getFlowExecutionResult().toString();
            ExecutionHistoryEntry historyEntry = new ExecutionHistoryEntry(flowName, startDate, runResult);
            executionHistoryData.add(historyEntry);
        }

        resultColumn.setCellFactory(column -> new TableCell<ExecutionHistoryEntry, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle(""); // Reset cell style
                } else {
                    setText(item);
                    getStyleClass().removeAll("success-result", "warning-result", "failure-result"); // Remove existing style classes
                    if (item.equals("SUCCESS")) {
                        getStyleClass().add("success-result");
                    } else if (item.equals("WARNING")) {
                        getStyleClass().add("warning-result");
                    } else if (item.equals("FAILURE")) {
                        getStyleClass().add("failure-result");
                    }
                }
            }
        });

        chooseOldFlowExecutions.setCellFactory(column -> new TableCell<ExecutionHistoryEntry, String>() {
            private final Button chooseButton = new Button("Choose This Flow");
            {
                chooseButton.getStyleClass().add("choose-flow-button");
                chooseButton.setOnAction(event -> {
                    ExecutionHistoryEntry entry = (ExecutionHistoryEntry) getTableRow().getItem();
                    if (entry != null) {
                        currFlowName = entry.getFlowName();
                        initMasterDetails(currFlowName);
                        RerunFlow.setDisable(false);
                    }
                });
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(chooseButton);
                }
            }
        });
    }

    private void initializeExecutionHistoryTable() {
        initExecutionHistoryDataList();

        Platform.runLater(() -> {
            flowNameColumn.setCellValueFactory(new PropertyValueFactory<>("flowName"));
            startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
            resultColumn.setCellValueFactory(new PropertyValueFactory<>("runResult"));
            chooseOldFlowExecutions.setCellValueFactory(new PropertyValueFactory<>(""));
            executionHistoryTable.setItems(executionHistoryData);
        });

    }

    private void addFilteringFunctionality() {
        resultFilterComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            filterExecutionHistoryTable((String) newValue);
        });
    }

    private void filterExecutionHistoryTable(String filterValue) {
        if (filterValue.equals("All")) {
            executionHistoryTable.setItems(executionHistoryData);
        } else {
            FilteredList<ExecutionHistoryEntry> filteredData = new FilteredList<>(executionHistoryData);
            filteredData.setPredicate(entry -> entry.getRunResult().equals(filterValue));
            executionHistoryTable.setItems(filteredData);
        }
    }

    public void initMasterDetails(String flowName) {
        DTOFlowExecution executedData = mainController.getSystemEngineInterface().getDTOFlowExecutionByName(flowName);
        masterDetailController.initMasterDetailPaneController(executedData);
        masterDetailController.updateFlowLabel(executedData);
        masterDetailController.addStepsToMasterDetail(executedData);

    }


}
