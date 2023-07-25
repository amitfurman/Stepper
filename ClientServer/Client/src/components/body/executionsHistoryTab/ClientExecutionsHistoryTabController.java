package components.body.executionsHistoryTab;

import commonComponents.CommonController;
import components.body.flowExecutionTab.MasterDetail.ClientMasterDetailController;
import dto.DTOFlowExecution;
import dto.DTOFlowsExecutionList;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import org.controlsfx.control.MasterDetailPane;
import systemengine.Input;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

public class ClientExecutionsHistoryTabController {
    private CommonController mainController;
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
    @FXML
    private ComboBox resultFilterComboBox;
    @FXML
    private GridPane executionHistoryGrid;
    @FXML
    private Button RerunFlow;
    private ObservableList<ExecutionHistoryEntry> executionHistoryData;
    private ClientMasterDetailController masterDetailController;
    private MasterDetailPane masterDetailPane;
    private String currFlowName;

    @FXML
    void rerunCurrentFlow(ActionEvent event) {
        RerunFlow.setOnMouseClicked(e -> {
/*
            mainController.getFlowExecutionTabController().initDataInFlowExecutionTab();
            mainController.goToFlowExecutionTab(currFlowName);
            List<Input> inputsValues = mainController.getSystemEngineInterface().getFreeInputsFromCurrFlow(currFlowName);
            mainController.getFlowExecutionTabController().setInputValuesFromContinuationMap(inputsValues);
*/
        });
    }
    public void setMainController(CommonController mainController) {this.mainController = mainController;}
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
        URL url = getClass().getResource("/components/body/flowExecutionTab/MasterDetail/ClientMasterDetails.fxml");
        fxmlLoader.setLocation(url);
        MasterDetailPane MasterDetailComponent = fxmlLoader.load(url.openStream());
        ClientMasterDetailController masterDetailController = fxmlLoader.getController();
        this.setMasterDetailsController(masterDetailController);
        this.masterDetailPane = MasterDetailComponent;
        if (masterDetailController != null) {
            masterDetailController.setExecutionsHistoryTabController(this);
        }
        masterDetailPane.getStylesheets().add("/components/body/flowExecutionTab/flowExecutionTab.css");
        executionHistoryGrid.add(masterDetailPane, 0 , 2);
    }
    private void setMasterDetailsController(ClientMasterDetailController masterDetailController) {this.masterDetailController = masterDetailController;}
    public void initExecutionHistoryTable() {
        initializeExecutionHistoryTable();
        addFilteringFunctionality();
    }
   /* public void initExecutionHistoryDataList() {
        executionHistoryData = FXCollections.observableArrayList();
        DTOFlowsExecutionList flowsExecutionList = mainController.getSystemEngineInterface().getFlowsExecutionList();

        List<DTOFlowExecution> executedFlows = flowsExecutionList.getFlowsExecutionNamesList().stream().filter(flow-> flow.getFlowExecutionResult() != null).collect(Collectors.toList());
        executedFlows.stream().forEach(
                    flow -> {
                    String flowName = flow.getFlowName();
                    String startDate = flow.getStartTimeFormatted();
                    String runResult = flow.getFlowExecutionResult().toString();
                    ExecutionHistoryEntry historyEntry = new ExecutionHistoryEntry(flowName, startDate, runResult);
                    executionHistoryData.add(historyEntry);
                }
        );
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
                    switch (item) {
                        case "SUCCESS":
                            getStyleClass().add("success-result");
                            break;
                        case "WARNING":
                            getStyleClass().add("warning-result");
                            break;
                        case "FAILURE":
                            getStyleClass().add("failure-result");
                            break;
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
    }*/
    private void initializeExecutionHistoryTable() {
       // initExecutionHistoryDataList();
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
        /*DTOFlowExecution executedData = mainController.getSystemEngineInterface().getDTOFlowExecutionByName(flowName);
        masterDetailController.initMasterDetailPaneController(executedData);
        masterDetailController.updateFlowLabel(executedData);
       masterDetailController.addStepsToMasterDetail(executedData);*/
    }
}
