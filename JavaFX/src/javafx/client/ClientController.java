package javafx.client;

import api.HttpStatusUpdate;
import javafx.Controller;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.executionsHistoryTab.ExecutionsHistoryTabController;
import javafx.flowDefinitionTab.FlowDefinitionTabController;
import javafx.flowExecutionTab.FlowExecutionTabController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.header.HeaderController;
import javafx.login.LoginController;
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
import javafx.status.StatusController;

import java.io.IOException;
import java.net.URL;
import java.util.SplittableRandom;

import static login.util.Constants.JHON_DOE;
import static login.util.Constants.LOGIN_PAGE_FXML_RESOURCE_LOCATION;

public class ClientController{
    @FXML
    private Parent httpStatusComponent;
    @FXML private StatusController httpStatusComponentController;

    private GridPane loginComponent;
    private LoginController logicController;
    @FXML private Label userGreetingLabel;
    @FXML private AnchorPane mainPanel;
    private final StringProperty currentUserName;
    private ScrollPane mainComponent;
    private Stage primaryStage;

    public ClientController() {
        currentUserName = new SimpleStringProperty(JHON_DOE);
    }

    public void init() throws IOException {
       // userGreetingLabel.textProperty().bind(Bindings.concat("Hello ", currentUserName));
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/javafx/header/header.fxml");
        fxmlLoader.setLocation(url);
        GridPane headerComponent = fxmlLoader.load(url.openStream());
        HeaderController headerController = fxmlLoader.getController();

        fxmlLoader = new FXMLLoader();
        url = getClass().getResource("/javafx/source.fxml");
        fxmlLoader.setLocation(url);
        mainComponent = fxmlLoader.load(url.openStream());
        Controller mainController = fxmlLoader.getController();

        BorderPane borderPane = (BorderPane)mainComponent.getContent();
        borderPane.setTop(headerComponent);

        fxmlLoader = new FXMLLoader();
        url = getClass().getResource("/javafx/flowDefinitionTab/flowDefinitionTab.fxml");
        fxmlLoader.setLocation(url);
        Tab flowDefinitionTabComponent = fxmlLoader.load(url.openStream());
        FlowDefinitionTabController flowDefinitionTabController =  fxmlLoader.getController();


        fxmlLoader = new FXMLLoader();
        url = getClass().getResource("/javafx/flowExecutionTab/flowExecutionTab.fxml");
        fxmlLoader.setLocation(url);
        Tab flowExecutionTabComponent = fxmlLoader.load(url.openStream());
        FlowExecutionTabController flowExecutionTabController =  fxmlLoader.getController();

        fxmlLoader = new FXMLLoader();
        url = getClass().getResource("/javafx/executionsHistoryTab/executionsHistoryTab.fxml");
        fxmlLoader.setLocation(url);
        Tab executionsHistoryTabComponent = fxmlLoader.load(url.openStream());
        ExecutionsHistoryTabController executionsHistoryTabController =  fxmlLoader.getController();

        TabPane tabPane = (TabPane) borderPane.getCenter();
        tabPane.getTabs().addAll(flowDefinitionTabComponent,flowExecutionTabComponent,executionsHistoryTabComponent);

        mainController.setHeaderComponentController(headerController);
        mainController.setFlowDefinitionTabController(flowDefinitionTabController);
        mainController.setFlowExecutionTabController(flowExecutionTabController);
        mainController.setExecutionsHistoryTabController(executionsHistoryTabController);
/*        Scene scene = new Scene(mainComponent, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();*/

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
        mainPanel.getChildren().clear();
        mainPanel.getChildren().add(pane);
        AnchorPane.setBottomAnchor(pane, 1.0);
        AnchorPane.setTopAnchor(pane, 1.0);
        AnchorPane.setLeftAnchor(pane, 1.0);
        AnchorPane.setRightAnchor(pane, 1.0);
    }

/*    @Override
    public void close() throws IOException {
        chatRoomComponentController.close();
    }*/

/*
    private void loadLoginPage() {
        URL loginPageUrl = getClass().getResource(LOGIN_PAGE_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageUrl);
            loginComponent = fxmlLoader.load();
            logicController = fxmlLoader.getController();
            logicController.setClientMainController(this);
            setMainPanelTo(loginComponent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
*/

/*    private void loadChatRoomPage() {
        URL loginPageUrl = getClass().getResource(CHAT_ROOM_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageUrl);
            chatRoomComponent = fxmlLoader.load();
            chatRoomComponentController = fxmlLoader.getController();
            chatRoomComponentController.setChatAppMainController(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

/*    @Override
    public void updateHttpLine(String line) {
        httpStatusComponentController.addHttpStatusLine(line);
    }*/

/*    public void switchToChatRoom() {
        setMainPanelTo(chatRoomComponent);
        chatRoomComponentController.setActive();
    }*/
/*

    public void switchToLogin() {
        Platform.runLater(() -> {
            currentUserName.set(JHON_DOE);
            chatRoomComponentController.setInActive();
            setMainPanelTo(loginComponent);
        });
    }
*/


}
