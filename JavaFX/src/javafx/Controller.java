package javafx;

import components.body.RolesManagementTab.RolesManagementController;
import components.body.StatisticsTab.AdminStatisticsTabController;
import components.body.UsersManagementTab.UsersManagementTabController;
import components.body.executionsHistoryTab.AdminExecutionsHistoryTabController;
import components.body.executionsHistoryTab.ClientExecutionsHistoryTabController;
import components.body.flowDefinitionTab.ClientFlowDefinitionTabController;
import components.body.flowExecutionTab.ClientFlowExecutionTabController;
import components.header.AdminHeaderController;
import dto.DTOFlowAndStepStatisticData;
import dto.DTOSingleFlowIOData;
import javafx.StatisticsTab.StatisticsTabController;
import javafx.executionsHistoryTab.ExecutionsHistoryTabController;
import javafx.flowDefinitionTab.FlowDefinitionTabController;
import javafx.flowExecutionTab.FlowExecutionTabController;
import javafx.fxml.FXML;
import javafx.header.HeaderController;
import components.headerClient.HeaderClientController;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeView;
import steps.api.DataNecessity;
import systemengine.systemengine;
import systemengine.systemengineImpl;
import users.UsersListController;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Controller {
    private final systemengine systemEngineInterface;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private HeaderController headerComponentController;
    @FXML
    private UsersManagementTabController usersManagementTabController;
    @FXML
    private RolesManagementController rolesManagementTabController;
    @FXML
    private AdminHeaderController headerAdminComponentController;
    @FXML
    private HeaderClientController headerClientComponentController;
    @FXML
    private FlowDefinitionTabController flowDefinitionTabController;
    @FXML
    private ClientFlowDefinitionTabController clientFlowDefinitionTabController;
    @FXML
    private FlowExecutionTabController flowExecutionTabController;
    @FXML
    private ClientFlowExecutionTabController clientFlowExecutionTabController;
    @FXML
    private ClientExecutionsHistoryTabController clientExecutionsHistoryTabController;
   @FXML
    private AdminExecutionsHistoryTabController adminExecutionsHistoryTabController;
    @FXML
    private ExecutionsHistoryTabController executionsHistoryTabController;
    @FXML
    private StatisticsTabController statisticsTabController;
    @FXML
    private AdminStatisticsTabController adminStatisticsTabController;
    @FXML
    private TabPane tabPane;
    String flowName;


    @FXML
    public void initialize() {
        if (headerComponentController != null) {
            headerComponentController.setMainController(this);
        }
        if (flowDefinitionTabController != null) {
            flowDefinitionTabController.setMainController(this);
        }
        if (flowExecutionTabController != null) {
            flowExecutionTabController.setMainController(this);
        }
        if (executionsHistoryTabController != null) {
            executionsHistoryTabController.setMainController(this);
        }
        if (statisticsTabController != null) {
            statisticsTabController.setMainController(this);
        }
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
    public FlowExecutionTabController getFlowExecutionTabController(){ return flowExecutionTabController;};
    public Controller() {
        this.systemEngineInterface = new systemengineImpl();
    }
    public systemengine getSystemEngineInterface() {return systemEngineInterface;}
    public void showFlowsTree() {
        if (flowDefinitionTabController != null)
          flowDefinitionTabController.showFlowsTree();
    }
    public TreeView<String> getFlowsTree() {
        if (flowDefinitionTabController!=null)
            return flowDefinitionTabController.getFlowsTree();
        else
            return null;
    }
    public void setHeaderComponentController(HeaderController headerComponentController) {
        this.headerComponentController = headerComponentController;
        headerComponentController.setMainController(this);
    }
    public void setAdminHeaderComponentController(AdminHeaderController headerComponentController) {
        this.headerAdminComponentController = headerComponentController;
        headerAdminComponentController.setMainController(this);
    }
    public void setHeaderComponentController(HeaderClientController headerClientComponentController) {
        this.headerClientComponentController = headerClientComponentController;
        headerClientComponentController.setMainController(this);
    }
    public void setFlowDefinitionTabController(FlowDefinitionTabController flowDefinitionTabComponentController) {
        this.flowDefinitionTabController = flowDefinitionTabComponentController;
        flowDefinitionTabController.setMainController(this);
    }
    public void setClientFlowDefinitionTabController(ClientFlowDefinitionTabController flowDefinitionTabComponentController) {
        this.clientFlowDefinitionTabController = flowDefinitionTabComponentController;
        clientFlowDefinitionTabController.setMainController(this);
    }
    public void setFlowExecutionTabController(FlowExecutionTabController flowExecutionTabComponentController) {
        this.flowExecutionTabController = flowExecutionTabComponentController;
        flowExecutionTabController.setMainController(this);
    }
    public void setClientFlowExecutionTabController(ClientFlowExecutionTabController flowExecutionTabComponentController) {
        this.clientFlowExecutionTabController = flowExecutionTabComponentController;
        clientFlowExecutionTabController.setMainController(this);
    }
    public void setUsersManagementTabController(UsersManagementTabController usersManagementTabController) {
        this.usersManagementTabController = usersManagementTabController;
        usersManagementTabController.setMainController(this);
    }
    public void setRolesManagementTabController(RolesManagementController rolesManagementTabController) {
        this.rolesManagementTabController = rolesManagementTabController;
        rolesManagementTabController.setMainController(this);
    }
    public void setClientExecutionsHistoryTabController(ClientExecutionsHistoryTabController executionsHistoryTabController) {
        this.clientExecutionsHistoryTabController = executionsHistoryTabController;
        executionsHistoryTabController.setMainController(this);
    }
    public void setExecutionsHistoryTabController(ExecutionsHistoryTabController executionsHistoryTabController) {
        this.executionsHistoryTabController = executionsHistoryTabController;
        executionsHistoryTabController.setMainController(this);
    }
    public void setAdminExecutionsHistoryTabController(AdminExecutionsHistoryTabController executionsHistoryTabController) {
        this.adminExecutionsHistoryTabController = executionsHistoryTabController;
        adminExecutionsHistoryTabController.setMainController(this);
    }
    public void setStatisticsTabController(StatisticsTabController statisticsTabComponentController) {
        this.statisticsTabController = statisticsTabComponentController;
        statisticsTabController.setMainController(this);
    }
    public void setAdminStatisticsTabController(AdminStatisticsTabController statisticsTabComponentController) {
        this.adminStatisticsTabController = statisticsTabComponentController;
        adminStatisticsTabController.setMainController(this);
    }
    public void setFlowDetailsTree() {
        if (flowDefinitionTabController!=null)
            flowDefinitionTabController.setFlowDetailsTree();
    }
    public void goToFlowExecutionTab(String chosenFlowName) {
    flowName = chosenFlowName;

        Tab flowExecutionTab = tabPane.getTabs().stream()
                .filter(tab -> tab.getId().equals("flowExecutionTab"))
                .findFirst()
                .orElse(null);

        if (flowExecutionTab != null) {
            tabPane.getSelectionModel().select(flowExecutionTab);
        }

        List<DTOSingleFlowIOData> freeInputs = systemEngineInterface.getAllFlows().getFlowByName(chosenFlowName).getFlowFreeInputs();
        List<DTOSingleFlowIOData> sortedList = freeInputs.stream()
                .sorted(Comparator.comparing(obj -> obj.getNecessity().equals(DataNecessity.MANDATORY) ? 0 : 1))
                .collect(Collectors.toList());
        flowExecutionTabController.initInputsTable(sortedList);
    }
    public void goToStatisticsTab() {
        DTOFlowAndStepStatisticData statisticData = systemEngineInterface.getStatisticData();
        statisticsTabController.initCharts(statisticData);
    }
    public String getFlowName() { return flowName; }
    public void initDataInFlowExecutionTab() {if (flowExecutionTabController!=null) flowExecutionTabController.initDataInFlowExecutionTab();}
    public void initInputsInFlowExecutionTab() { if (flowExecutionTabController!=null) flowExecutionTabController.initInputsInFlowExecutionTab();}
    public void initExecutionHistoryTableInExecutionsHistoryTab() { executionsHistoryTabController.initExecutionHistoryTable();}

    public UsersManagementTabController getUsersManagementTabController() {
        return usersManagementTabController;
    }
    public RolesManagementController getRolesManagementController() {
        return rolesManagementTabController;
    }

    public void goToClientFlowExecutionTab(String chosenFlowName) {
        flowName = chosenFlowName;

        Tab ClientFlowExecutionTab = tabPane.getTabs().stream()
                .filter(tab -> tab.getId().equals("flowExecutionTab"))
                .findFirst()
                .orElse(null);

        if (ClientFlowExecutionTab != null) {
            tabPane.getSelectionModel().select(ClientFlowExecutionTab);
        }

        clientFlowExecutionTabController.getFreeInputs(flowName);}
}

