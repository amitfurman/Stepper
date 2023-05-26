package javafx;

import javafx.application.Application;
import javafx.flowDefinitionTab.FlowDefinitionTabController;
import javafx.flowExecutionTab.FlowExecutionTabController;
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


public class desktopUI extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Stepper");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/javafx/heaart.png")));

        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("header/header.fxml");
        fxmlLoader.setLocation(url);
        GridPane headerComponent = fxmlLoader.load(url.openStream());
        HeaderController headerController = fxmlLoader.getController();

        fxmlLoader = new FXMLLoader();
        url = getClass().getResource("source.fxml");
        fxmlLoader.setLocation(url);
        ScrollPane mainComponent = fxmlLoader.load(url.openStream());
        Controller mainController = fxmlLoader.getController();

        BorderPane borderPane = (BorderPane)mainComponent.getContent();
        borderPane.setTop(headerComponent);

        fxmlLoader = new FXMLLoader();
        url = getClass().getResource("flowDefinitionTab/flowDefinitionTab.fxml");
        fxmlLoader.setLocation(url);
        Tab flowDefinitionTabComponent = fxmlLoader.load(url.openStream());
        FlowDefinitionTabController flowDefinitionTabController =  fxmlLoader.getController();


        fxmlLoader = new FXMLLoader();
        url = getClass().getResource("flowExecutionTab/flowExecutionTab.fxml");
        fxmlLoader.setLocation(url);
        Tab flowExecutionTabComponent = fxmlLoader.load(url.openStream());
        FlowExecutionTabController flowExecutionTabController =  fxmlLoader.getController();

        TabPane tabPane = (TabPane) borderPane.getCenter();
        tabPane.getTabs().addAll(flowDefinitionTabComponent,flowExecutionTabComponent);


        mainController.setHeaderComponentController(headerController);
        mainController.setFlowDefinitionTabController(flowDefinitionTabController);
        mainController.setFlowExecutionTabController(flowExecutionTabController);
        Scene scene = new Scene(mainComponent, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
