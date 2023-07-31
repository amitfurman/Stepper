import components.body.RolesManagementTab.RolesManagementController;
import components.body.StatisticsTab.AdminStatisticsTabController;
import components.body.UsersManagementTab.UsersManagementTabController;
import components.body.executionsHistoryTab.AdminExecutionsHistoryTabController;
import components.commonComponents.AdminCommonController;
import components.header.AdminHeaderController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AdminMain extends Application {

    private boolean isFirstAdmin;

    @Override
    public void start(Stage primaryStage) throws Exception {
        if (isAnotherInstanceRunning()) {
            // Show an error message to the user if another admin instance is running
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Unauthorized Access");
            alert.setContentText("Another instance of the application is already running.");
            alert.showAndWait();

            // Close the application
            primaryStage.close();
        } else {
            // Create the lock file to mark an active admin instance
            createLockFile();

            // When closing the primaryStage, remember to delete the lock file
            primaryStage.setOnCloseRequest(e -> deleteLockFile());

            // The rest of your code...
            primaryStage.setTitle("Stepper");
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/components/commonComponents/heaart.png")));

            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource("/components/header/Adminheader.fxml");
            fxmlLoader.setLocation(url);
            GridPane headerComponent = fxmlLoader.load(url.openStream());
            AdminHeaderController headerController = fxmlLoader.getController();

            fxmlLoader = new FXMLLoader();
            url = getClass().getResource("/components/commonComponents/commonSource.fxml");
            fxmlLoader.setLocation(url);
            ScrollPane mainComponent = fxmlLoader.load(url.openStream());
            AdminCommonController mainController = fxmlLoader.getController();

            BorderPane borderPane = (BorderPane) mainComponent.getContent();
            borderPane.setTop(headerComponent);

            fxmlLoader = new FXMLLoader();
            url = getClass().getResource("/components/body/UsersManagementTab/usersManagementTab.fxml");
            fxmlLoader.setLocation(url);
            Tab usersManagementTabComponent = fxmlLoader.load(url.openStream());
            UsersManagementTabController usersManagementTabController = fxmlLoader.getController();

            fxmlLoader = new FXMLLoader();
            url = getClass().getResource("/components/body/RolesManagementTab/rolesManagementTab.fxml");
            fxmlLoader.setLocation(url);
            Tab rolesManagementTabComponent = fxmlLoader.load(url.openStream());
            RolesManagementController rolesManagementTabController = fxmlLoader.getController();

            fxmlLoader = new FXMLLoader();
            url = getClass().getResource("/components/body/executionsHistoryTab/executionsHistoryTab.fxml");
            fxmlLoader.setLocation(url);
            Tab executionsHistoryTabComponent = fxmlLoader.load(url.openStream());
            AdminExecutionsHistoryTabController executionsHistoryTabController = fxmlLoader.getController();

            fxmlLoader = new FXMLLoader();
            url = getClass().getResource("/components/body/StatisticsTab/StatisticsTab.fxml");
            fxmlLoader.setLocation(url);
            Tab StatisticsTabComponent = fxmlLoader.load(url.openStream());
            AdminStatisticsTabController StatisticsTabController = fxmlLoader.getController();

            TabPane tabPane = (TabPane) borderPane.getCenter();
            tabPane.getTabs().addAll(usersManagementTabComponent, rolesManagementTabComponent, executionsHistoryTabComponent, StatisticsTabComponent);


            mainController.setAdminHeaderComponentController(headerController);
            mainController.setUsersManagementTabController(usersManagementTabController);
            mainController.setRolesManagementTabController(rolesManagementTabController);
            mainController.setAdminExecutionsHistoryTabController(executionsHistoryTabController);
            mainController.setAdminStatisticsTabController(StatisticsTabController);
            Scene scene = new Scene(mainComponent, 600, 400);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
    }
    private boolean isAnotherInstanceRunning() {
        // Check for the lock file indicating an admin instance is already running
        File lockFile = new File("admin_lock.txt");
        return lockFile.exists();
    }

    private void createLockFile() {
        File lockFile = new File("admin_lock.txt");
        try {
            lockFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteLockFile() {
        try {
            Files.deleteIfExists(Paths.get("admin_lock.txt"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public static void main (String[]args){
        launch(args);
    }

}