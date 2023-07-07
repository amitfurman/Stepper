import components.body.StatisticsTab.AdminStatisticsTabController;
import components.body.UsersManagementTab.UsersManagementTabController;
import components.header.AdminHeaderController;
import javafx.Controller;
import javafx.StatisticsTab.StatisticsTabController;
import javafx.application.Application;
import javafx.executionsHistoryTab.ExecutionsHistoryTabController;
import javafx.fxml.FXMLLoader;
import javafx.header.HeaderController;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Stepper");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/javafx/heaart.png")));

        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/components/header/Adminheader.fxml");
        fxmlLoader.setLocation(url);
        GridPane headerComponent = fxmlLoader.load(url.openStream());
        AdminHeaderController headerController = fxmlLoader.getController();

        fxmlLoader = new FXMLLoader();
        url = getClass().getResource("/javafx/source.fxml");
        fxmlLoader.setLocation(url);
        ScrollPane mainComponent = fxmlLoader.load(url.openStream());
        Controller mainController = fxmlLoader.getController();

        BorderPane borderPane = (BorderPane)mainComponent.getContent();
        borderPane.setTop(headerComponent);

        fxmlLoader = new FXMLLoader();
        url = getClass().getResource("/components/body/UsersManagementTab/usersManagementTab.fxml");
        fxmlLoader.setLocation(url);
        Tab usersManagementTabComponent = fxmlLoader.load(url.openStream());
        UsersManagementTabController usersManagementTabController =  fxmlLoader.getController();

        fxmlLoader = new FXMLLoader();
        url = getClass().getResource("/javafx/executionsHistoryTab/executionsHistoryTab.fxml");
        fxmlLoader.setLocation(url);
        Tab executionsHistoryTabComponent = fxmlLoader.load(url.openStream());
        ExecutionsHistoryTabController executionsHistoryTabController =  fxmlLoader.getController();

        fxmlLoader = new FXMLLoader();
        url = getClass().getResource("/components/body/StatisticsTab/StatisticsTab.fxml");
        fxmlLoader.setLocation(url);
        Tab StatisticsTabComponent = fxmlLoader.load(url.openStream());
        AdminStatisticsTabController StatisticsTabController =  fxmlLoader.getController();

        TabPane tabPane = (TabPane) borderPane.getCenter();
        tabPane.getTabs().addAll(usersManagementTabComponent,executionsHistoryTabComponent, StatisticsTabComponent);

        mainController.setAdminHeaderComponentController(headerController);
        mainController.setUsersManagementTabController(usersManagementTabController);
        mainController.setExecutionsHistoryTabController(executionsHistoryTabController);
        mainController.setAdminStatisticsTabController(StatisticsTabController);
        Scene scene = new Scene(mainComponent, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }}