package javafx.executionsHistoryTab;

import dto.DTOFlowExecution;
import dto.DTOFlowsExecutionList;
import javafx.Controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

public class ExecutionsHistoryTabController {
    private Controller mainController;
    public void setMainController(Controller mainController) {
        this.mainController = mainController;
    }
    @FXML
    private TableView executionHistoryTable;

    @FXML
    private TableColumn flowNameColumn;

    @FXML
    private TableColumn startDateColumn;

    @FXML
    private TableColumn resultColumn;

    private ObservableList<ExecutionHistoryEntry> executionHistoryData;

    @FXML
    public void initialize() {

    }

    public void initExecutionHistoryTable() {
        initializeExecutionHistoryTable();
        //addSortingFunctionality();
        //addFilteringFunctionality();
    }

    public void initExecutionHistoryDataList() {
        executionHistoryData = FXCollections.observableArrayList(); // Initialize the executionHistoryData list

        DTOFlowsExecutionList flowsExecutionList = mainController.getSystemEngineInterface().getFlowsExecutionList();

        // Iterate over the flows execution list and create ExecutionHistoryEntry objects
        for (DTOFlowExecution flowExecution : flowsExecutionList.getFlowsExecutionNamesList()) {
            // Extract the required information from DTOFlowExecution
            String flowName = flowExecution.getFlowName();
            String startDate = flowExecution.getStartTimeFormatted();
            String runResult = flowExecution.getFlowExecutionResult().toString();

            // Create a new ExecutionHistoryEntry and add it to the executionHistoryData list
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

    }
    private void initializeExecutionHistoryTable() {
        initExecutionHistoryDataList();

        flowNameColumn.setCellValueFactory(new PropertyValueFactory<>("flowName"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        resultColumn.setCellValueFactory(new PropertyValueFactory<>("runResult"));
        executionHistoryTable.setItems(executionHistoryData);
        executionHistoryTable.getStyleClass().add("execution-history-table");
    }

/*    private void addSortingFunctionality() {
        flowNameColumn.setSortType(TableColumn.SortType.ASCENDING);
        startDateColumn.setSortType(TableColumn.SortType.ASCENDING);
        resultColumn.setSortType(TableColumn.SortType.ASCENDING);
        executionHistoryTable.getSortOrder().addAll(flowNameColumn, startDateColumn, resultColumn);
        executionHistoryTable.setSortPolicy(table -> {
            Comparator<ExecutionHistoryEntry> comparator = (entry1, entry2) -> {
                // Customize the sorting logic based on your requirements
                // Compare entry1 and entry2 based on the selected column
                // Return -1, 0, or 1 depending on the comparison result
            };

            FXCollections.sort(executionHistoryData, comparator);
            return true;
        });
    }

    private void addFilteringFunctionality() {
        // Add UI controls for filtering, e.g., choice boxes or checkboxes

        // Add event handlers to the UI controls to apply filters
        // Customize the filtering logic based on your requirements
        // Update the executionHistoryData list with the filtered entries
        // Call executionHistoryTable.setItems(executionHistoryData) to refresh the table
    }*/

    // Other necessary methods and event handlers

}
