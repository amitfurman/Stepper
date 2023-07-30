package components.mainClient;

import commonComponents.CommonController;
import components.body.executionsHistoryTab.ClientExecutionsHistoryTabController;
import components.body.flowDefinitionTab.ClientFlowDefinitionTabController;
import components.body.flowExecutionTab.ClientFlowExecutionTabController;
import components.headerClient.HeaderClientController;
import components.login.LoginController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

import static util.Constants.LOGIN_PAGE_FXML_RESOURCE_LOCATION;

public class ClientController {
    private GridPane loginComponent;
    private LoginController logicController;
    private final StringProperty currentUserName;
    private ScrollPane mainComponent;
    private Stage primaryStage;

    public ClientController() {
        currentUserName = new SimpleStringProperty();
    }

    public void init() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/components/headerClient/headerClient.fxml");
        fxmlLoader.setLocation(url);
        GridPane headerComponent = fxmlLoader.load(url.openStream());
        HeaderClientController headerController = fxmlLoader.getController();
        headerController.setClientController(this);
        headerController.updateNameLabel();
        headerController.startRolesListRefresher();

        fxmlLoader = new FXMLLoader();
        url = getClass().getResource("/commonComponents/commonSource.fxml");
        fxmlLoader.setLocation(url);
        mainComponent = fxmlLoader.load(url.openStream());
        CommonController mainController = fxmlLoader.getController();

        BorderPane borderPane = (BorderPane) mainComponent.getContent();
        borderPane.setTop(headerComponent);

        fxmlLoader = new FXMLLoader();
        url = getClass().getResource("/components/body/flowDefinitionTab/flowDefinitionTab.fxml");
        fxmlLoader.setLocation(url);
        Tab flowDefinitionTabComponent = fxmlLoader.load(url.openStream());
        ClientFlowDefinitionTabController flowDefinitionTabController = fxmlLoader.getController();
        headerController.setFlowDefinitionTabController(flowDefinitionTabController);

        fxmlLoader = new FXMLLoader();
        url = getClass().getResource("/components/body/flowExecutionTab/flowExecutionTab.fxml");
        fxmlLoader.setLocation(url);
        Tab flowExecutionTabComponent = fxmlLoader.load(url.openStream());
        ClientFlowExecutionTabController flowExecutionTabController = fxmlLoader.getController();

        fxmlLoader = new FXMLLoader();
        url = getClass().getResource("/components/body/executionsHistoryTab/executionsHistoryTab.fxml");
        fxmlLoader.setLocation(url);
        Tab executionsHistoryTabComponent = fxmlLoader.load(url.openStream());
        ClientExecutionsHistoryTabController executionsHistoryTabController = fxmlLoader.getController();

        TabPane tabPane = (TabPane) borderPane.getCenter();
        tabPane.getTabs().addAll(flowDefinitionTabComponent, flowExecutionTabComponent, executionsHistoryTabComponent);

        mainController.setHeaderComponentController(headerController);
        mainController.setClientFlowDefinitionTabController(flowDefinitionTabController);
        mainController.setClientFlowExecutionTabController(flowExecutionTabController);
        mainController.setClientExecutionsHistoryTabController(executionsHistoryTabController);

    }

    public void setStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void switchToMainClient() {
        try {
            init();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene newScene = new Scene(mainComponent);
        this.primaryStage.setScene(newScene);
    }

    public void updateUserName(String userName) {
        currentUserName.set(userName);
    }

    private void setMainPanelTo(Parent pane) {
        AnchorPane.setBottomAnchor(pane, 1.0);
        AnchorPane.setTopAnchor(pane, 1.0);
        AnchorPane.setLeftAnchor(pane, 1.0);
        AnchorPane.setRightAnchor(pane, 1.0);
    }

    public StringProperty getCurrentUserNameProperty() {
        return currentUserName;
    }
}