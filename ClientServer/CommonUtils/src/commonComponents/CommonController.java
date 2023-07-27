package commonComponents;

import components.body.RolesManagementTab.RolesManagementController;
import components.body.StatisticsTab.AdminStatisticsTabController;
import components.body.UsersManagementTab.UsersManagementTabController;
import components.body.executionsHistoryTab.AdminExecutionsHistoryTabController;
import components.body.executionsHistoryTab.ClientExecutionsHistoryTabController;
import components.body.flowDefinitionTab.ClientFlowDefinitionTabController;
import components.body.flowExecutionTab.ClientFlowExecutionTabController;
import components.header.AdminHeaderController;
import components.headerClient.HeaderClientController;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
public class CommonController {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private AdminHeaderController headerAdminComponentController;
    @FXML
    private UsersManagementTabController usersManagementTabController;
    @FXML
    private RolesManagementController rolesManagementTabController;
    @FXML
    private AdminExecutionsHistoryTabController adminExecutionsHistoryTabController;
    @FXML
    private AdminStatisticsTabController adminStatisticsTabController;

    @FXML
    private HeaderClientController headerClientComponentController;
    @FXML
    private ClientFlowDefinitionTabController clientFlowDefinitionTabController;
    @FXML
    private ClientFlowExecutionTabController clientFlowExecutionTabController;
    @FXML
    private ClientExecutionsHistoryTabController clientExecutionsHistoryTabController;

    @FXML
    private TabPane tabPane;
    String flowName;

    @FXML
    public void initialize() {
        if (headerAdminComponentController != null) {
            headerAdminComponentController.setMainController(this);
        }
        if (usersManagementTabController != null) {
            usersManagementTabController.setMainController(this);
        }
        if (rolesManagementTabController != null) {
            rolesManagementTabController.setMainController(this);
        }
        if (adminExecutionsHistoryTabController != null) {
            adminExecutionsHistoryTabController.setMainController(this);
        }
        if (adminStatisticsTabController != null) {
            adminStatisticsTabController.setMainController(this);
        }
        if (headerClientComponentController != null) {
            headerClientComponentController.setMainController(this);
        }

        if (clientFlowDefinitionTabController != null) {
            clientFlowDefinitionTabController.setMainController(this);
        }
        if (clientFlowExecutionTabController != null) {
            clientFlowExecutionTabController.setMainController(this);
        }
        if (clientExecutionsHistoryTabController != null) {
            clientExecutionsHistoryTabController.setMainController(this);
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
    public CommonController() {
    }
   /* public void showFlowsTree() {
        if (flowDefinitionTabController != null)
          flowDefinitionTabController.showFlowsTree();
    }*/
   /*
    public TreeView<String> getFlowsTree() {
        if (flowDefinitionTabController!=null)
            return flowDefinitionTabController.getFlowsTree();
        else
            return null;
    }*/
    public void setAdminHeaderComponentController(AdminHeaderController headerComponentController) {
        this.headerAdminComponentController = headerComponentController;
        headerAdminComponentController.setMainController(this);
    }
    public void setHeaderComponentController(HeaderClientController headerClientComponentController) {
        this.headerClientComponentController = headerClientComponentController;
        headerClientComponentController.setMainController(this);
    }
    public void setClientFlowDefinitionTabController(ClientFlowDefinitionTabController flowDefinitionTabComponentController) {
        this.clientFlowDefinitionTabController = flowDefinitionTabComponentController;
        clientFlowDefinitionTabController.setMainController(this);
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
    public void setAdminExecutionsHistoryTabController(AdminExecutionsHistoryTabController executionsHistoryTabController) {
        this.adminExecutionsHistoryTabController = executionsHistoryTabController;
        adminExecutionsHistoryTabController.setMainController(this);
    }
    public void setAdminStatisticsTabController(AdminStatisticsTabController statisticsTabComponentController) {
        this.adminStatisticsTabController = statisticsTabComponentController;
        adminStatisticsTabController.setMainController(this);
    }

    public void initDataInFlowExecutionTab() {if (clientFlowExecutionTabController!=null) clientFlowExecutionTabController.initDataInFlowExecutionTab();}

    /* public void goToStatisticsTab() {

        DTOFlowAndStepStatisticData statisticData = systemEngineInterface.getStatisticData();

        statisticsTabController.initCharts(statisticData);
    }*/
    public String getFlowName() { return flowName; }
/*
    public void initExecutionHistoryTableInExecutionsHistoryTab() { executionsHistoryTabController.initExecutionHistoryTable();}
*/

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

