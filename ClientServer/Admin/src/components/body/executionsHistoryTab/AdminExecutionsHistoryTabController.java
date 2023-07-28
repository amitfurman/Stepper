package components.body.executionsHistoryTab;

import com.google.gson.reflect.TypeToken;
import commonComponents.CommonController;
import components.body.executionsHistoryTab.MasterDetail.AdminMasterDetailController;

import dto.DTOFlowExeInfo;
import dto.DTOInput;
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
import okhttp3.*;
import org.controlsfx.control.MasterDetailPane;
import org.jetbrains.annotations.NotNull;
import systemengine.Input;
import util.Constants;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.stream.Collectors;

import static util.Constants.GSON_INSTANCE;
import static util.Constants.REFRESH_RATE;

public class AdminExecutionsHistoryTabController {
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
    private TableColumn userColumn;
    @FXML
    private TableColumn chooseOldFlowExecutions;
    @FXML
    private ComboBox resultFilterComboBox;
    @FXML
    private GridPane executionHistoryGrid;
    private ObservableList<AdminExecutionHistoryEntry> executionHistoryData;
    private AdminMasterDetailController masterDetailController;
    private MasterDetailPane masterDetailPane;
    private String currFlowName;
    private Timer timer;
    private AdminRefresher adminRefresher;


    public void setMainController(CommonController mainController) {this.mainController = mainController;}
    @FXML
    public void initialize() throws IOException {
        executionHistoryTable.getStyleClass().add("execution-history-table");
        double tableWidth = executionHistoryTable.getWidth();
        double columnWidth = tableWidth /5;
        flowNameColumn.setPrefWidth(columnWidth);
        startDateColumn.setPrefWidth(columnWidth);
        resultColumn.setPrefWidth(columnWidth);
        userColumn.setPrefWidth(columnWidth);
        chooseOldFlowExecutions.setPrefWidth(columnWidth);

        executionHistoryTable.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            double newColumnWidth = newWidth.doubleValue() / 5;
            flowNameColumn.setPrefWidth(newColumnWidth);
            startDateColumn.setPrefWidth(newColumnWidth);
            resultColumn.setPrefWidth(newColumnWidth);
            userColumn.setPrefWidth(newColumnWidth);
            chooseOldFlowExecutions.setPrefWidth(newColumnWidth);
        });
        executionHistoryTable.getSortOrder().add(flowNameColumn);
        executionHistoryTable.getSortOrder().add(startDateColumn);
        executionHistoryTable.getSortOrder().add(resultColumn);
        executionHistoryTable.getSortOrder().add(userColumn);
        executionHistoryTable.getSortOrder().add(chooseOldFlowExecutions);

        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/components/body/executionsHistoryTab/MasterDetail/AdminMasterDetails.fxml");
        fxmlLoader.setLocation(url);
        MasterDetailPane MasterDetailComponent = fxmlLoader.load(url.openStream());
        AdminMasterDetailController masterDetailController = fxmlLoader.getController();
        this.setMasterDetailsController(masterDetailController);
        this.masterDetailPane = MasterDetailComponent;
        if (masterDetailController != null) {
            masterDetailController.setExecutionsHistoryTabController(this);
        }
        masterDetailPane.getStylesheets().add("/components/body/flowExecutionTab/flowExecutionTab.css");
        executionHistoryGrid.add(masterDetailPane, 0 , 2);
        startExecutionTableRefresher();
    }

    public void startExecutionTableRefresher() {
        this.adminRefresher = new AdminRefresher(this::updateClientData);
        timer = new Timer();
        timer.schedule(adminRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    private void updateClientData(List<DTOFlowExeInfo> flowExeList) {
        //initExecutionHistoryTable();
        initExecutionHistoryDataList(flowExeList);
        addFilteringFunctionality();
        if(flowExeList.size()!=0) {
            mainController.getAdminStatisticsTabController().initCharts(flowExeList.get(0).getStatisticData());
        }

    }
    private void setMasterDetailsController(AdminMasterDetailController masterDetailController) {this.masterDetailController = masterDetailController;}
/*    public void initExecutionHistoryTable() {
        System.out.println("initExecutionHistoryTable");
        initializeExecutionHistoryTable();
        addFilteringFunctionality();
    }*/
    public void initExecutionHistoryDataList(List<DTOFlowExeInfo> flowExeList ) {
        executionHistoryData = FXCollections.observableArrayList();
        List<DTOFlowExeInfo> executedFlows = flowExeList.stream().filter(flow-> flow.getResultExecute() != null).collect(Collectors.toList());
        executedFlows.stream().forEach(
                flow -> {
                    String flowName = flow.getFlowName();
                    String startDate = flow.getStartTime();
                    String runResult = flow.getResultExecute().toString();
                    String userName = flow.getUserName();
                    AdminExecutionHistoryEntry historyEntry = new AdminExecutionHistoryEntry(flowName, startDate, runResult, userName );
                    executionHistoryData.add(historyEntry);
                }
        );
        resultColumn.setCellFactory(column -> new TableCell<AdminExecutionHistoryEntry, String>(){
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

        chooseOldFlowExecutions.setCellFactory(column -> new TableCell<AdminExecutionHistoryEntry, String>() {
            private final Button chooseButton = new Button("Choose This Flow");
            {
                chooseButton.getStyleClass().add("choose-flow-button");
                chooseButton.setOnAction(event -> {
                    AdminExecutionHistoryEntry entry = (AdminExecutionHistoryEntry) getTableRow().getItem();
                    if (entry != null) {
                        currFlowName = entry.getFlowName();
                        System.out.println("currFlowName: " + currFlowName);
                        initMasterDetails(currFlowName);
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

        Platform.runLater(() -> {
            flowNameColumn.setCellValueFactory(new PropertyValueFactory<>("flowName"));
            startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
            resultColumn.setCellValueFactory(new PropertyValueFactory<>("runResult"));
            userColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
            chooseOldFlowExecutions.setCellValueFactory(new PropertyValueFactory<>(""));

            executionHistoryTable.setItems(executionHistoryData);
        });
    }
   /* private void initializeExecutionHistoryTable() {
        getFlowsExecutionList();
    }*/
    public void addFilteringFunctionality() {
        resultFilterComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            filterExecutionHistoryTable((String) newValue);
        });
    }
    public void filterExecutionHistoryTable(String filterValue) {
        if (filterValue.equals("All")) {
            executionHistoryTable.setItems(executionHistoryData);
        } else {
            FilteredList<AdminExecutionHistoryEntry> filteredData = new FilteredList<>(executionHistoryData);
            filteredData.setPredicate(entry -> entry.getRunResult().equals(filterValue));
            executionHistoryTable.setItems(filteredData);
        }
    }
    public void initMasterDetails(String flowName) {
        getDTOFlowExecutionByName(flowName);

    }
/*    public void getFlowsExecutionList() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constants.GET_FLOW_EXECUTION_LIST_SERVLET).newBuilder();
        String finalUrl = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        OkHttpClient HTTP_CLIENT = new OkHttpClient();
        Call call = HTTP_CLIENT.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("on failure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonArrayOfUsersRoles = response.body().string();
                LinkedList<DTOFlowExeInfo> flowExeList = GSON_INSTANCE.fromJson(jsonArrayOfUsersRoles, new TypeToken<LinkedList<DTOFlowExeInfo>>(){}.getType());
                if(flowExeList != null)
                    initExecutionHistoryDataList(flowExeList);

            }
        });
    }*/
    public void getDTOFlowExecutionByName(String currFlowName) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constants.GET_FLOW_EXECUTION_BY_NAME).newBuilder();
        urlBuilder.addQueryParameter("flowName", currFlowName);
        String finalUrl = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        OkHttpClient HTTP_CLIENT = new OkHttpClient();
        Call call = HTTP_CLIENT.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("on failure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String rawBody = response.body().string();
                DTOFlowExeInfo executedData = GSON_INSTANCE.fromJson(rawBody,DTOFlowExeInfo.class);
                Platform.runLater(() -> {
                    masterDetailController.initMasterDetailPaneController(executedData);
                    masterDetailController.updateFlowLabel(executedData);
                    masterDetailController.addStepsToMasterDetail(executedData);
                    addFilteringFunctionality();
                });
            }
        });
    }

}
