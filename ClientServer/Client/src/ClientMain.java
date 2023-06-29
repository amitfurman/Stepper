import javafx.Controller;
import javafx.StatisticsTab.StatisticsTabController;
import javafx.application.Application;
import javafx.client.ClientController;
import javafx.executionsHistoryTab.ExecutionsHistoryTabController;
import javafx.flowDefinitionTab.FlowDefinitionTabController;
import javafx.flowExecutionTab.FlowExecutionTabController;
import javafx.fxml.FXMLLoader;
import javafx.header.HeaderController;
import javafx.login.LoginController;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

import static login.util.Constants.LOGIN_PAGE_FXML_RESOURCE_LOCATION;


public class ClientMain extends Application {
   private ClientController clientController;
   private LoginController loginController;
    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(600);
        primaryStage.setTitle("Stepper Client");

        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/javafx/heaart.png")));


        URL loginPage = getClass().getResource("/javafx/login/login.fxml");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPage);
            GridPane root = fxmlLoader.load(loginPage.openStream());
            loginController = fxmlLoader.getController();
            clientController = new ClientController();
            clientController.setStage(primaryStage);

            Scene scene = new Scene(root, 700, 600);
            primaryStage.setScene(scene);
            primaryStage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) {
        launch(args);
    }
}