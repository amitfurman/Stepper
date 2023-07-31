package components.commonComponents;

import components.body.executionsHistoryTab.ClientExecutionsHistoryTabController;
import components.body.flowDefinitionTab.ClientFlowDefinitionTabController;
import components.body.flowExecutionTab.ClientFlowExecutionTabController;
import components.headerClient.HeaderClientController;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
public class ClientCommonController {
    @FXML
    private ScrollPane scrollPane;

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
    public ClientCommonController() {
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
    public void setClientExecutionsHistoryTabController(ClientExecutionsHistoryTabController executionsHistoryTabController) {
        this.clientExecutionsHistoryTabController = executionsHistoryTabController;
        executionsHistoryTabController.setMainController(this);
    }

    public void initDataInFlowExecutionTab() {if (clientFlowExecutionTabController!=null) clientFlowExecutionTabController.initDataInFlowExecutionTab();}
    public String getFlowName() { return flowName; }
    public void initExecutionHistoryTableInExecutionsHistoryTab() { clientExecutionsHistoryTabController.initExecutionHistoryTable();}

    public void goToClientFlowExecutionTab(String chosenFlowName) {
        flowName = chosenFlowName;
        Tab ClientFlowExecutionTab = tabPane.getTabs().stream()
                .filter(tab -> tab.getId().equals("flowExecutionTab"))
                .findFirst()
                .orElse(null);

        if (ClientFlowExecutionTab != null) {
            tabPane.getSelectionModel().select(ClientFlowExecutionTab);
        }
        clientFlowExecutionTabController.getFreeInputs(flowName);
    }
    public ClientFlowExecutionTabController getClientFlowExecutionTabController(){ return clientFlowExecutionTabController;};
    public HeaderClientController getHeaderClientComponentController() {return headerClientComponentController;}

    public ClientExecutionsHistoryTabController getClientExecutionsHistoryTabController() {return clientExecutionsHistoryTabController;}

}

