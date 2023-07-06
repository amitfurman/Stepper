import components.login.LoginController;
import components.mainClient.ClientController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;


public class ClientMain extends Application {
   private ClientController clientController;
   private LoginController loginController;
    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(600);
        primaryStage.setTitle("Stepper Client");

        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/javafx/heaart.png")));

        URL loginPage = getClass().getResource("/components/login/login.fxml");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPage);
            GridPane root = fxmlLoader.load(loginPage.openStream());
            loginController = fxmlLoader.getController();
            clientController = new ClientController();
            clientController.setStage(primaryStage);
            loginController.setClientMainController(clientController);

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
