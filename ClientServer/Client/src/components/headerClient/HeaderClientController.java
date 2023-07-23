package components.headerClient;

import components.body.flowDefinitionTab.ClientFlowDefinitionTabController;
import dto.DTORole;
import javafx.Controller;
import components.mainClient.ClientController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static util.Constants.REFRESH_RATE;

public class HeaderClientController {
    private Timer timer;
    private TimerTask clientRefresher;
    private Controller mainController;
    private ClientFlowDefinitionTabController clientFlowDefinitionTabController;
    @FXML
    GridPane HeaderClientGridPane;
    private VBox componentContainer;
    private ClientController clientController;
    private Label nameLabel;
    private String currentUserName;

    @FXML
    private void initialize() {
        componentContainer = new VBox();
        componentContainer.setAlignment(Pos.TOP_LEFT);
        componentContainer.setSpacing(10);
        componentContainer.setPadding(new Insets(10));

        Text textNameLabel = new Text("   Name: ");
        textNameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        textNameLabel.setFill(Color.PINK);
        nameLabel = new Label();
        nameLabel.setGraphic(textNameLabel);
        nameLabel.setVisible(false);


        Text textManagerLabel = new Text("   Is Manager:");
        textManagerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        textManagerLabel.setFill(Color.PINK);

        Label managerLabel = new Label();
        managerLabel.setGraphic(textManagerLabel);
        CheckBox managerCheckbox = new CheckBox();
        managerCheckbox.setSelected(false);
        managerCheckbox.setDisable(true);

        Text textRolesLabel = new Text("   Assigned Roles:");
        textRolesLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        textRolesLabel.setFill(Color.PINK);

        Label rolesLabel = new Label();
        rolesLabel.setGraphic(textRolesLabel);

        HBox managerBox = new HBox(managerLabel, managerCheckbox, rolesLabel);
        managerBox.setAlignment(Pos.CENTER_LEFT);
        managerBox.setSpacing(10);

        VBox root = new VBox(nameLabel, managerBox, componentContainer);
        HeaderClientGridPane.add(root, 0, 1);

    }

    public void setMainController(Controller mainController) {
        this.mainController = mainController;
    }

    public void setClientController(ClientController clientController) {
        this.clientController = clientController;
    }

    public void updateNameLabel() {
        currentUserName = clientController.getCurrentUserNameProperty().getValue();
        System.out.println("updateNameLabel" + currentUserName);
        nameLabel.setText(currentUserName);
        nameLabel.setVisible(true);
        nameLabel.setStyle("-fx-text-fill: BLACK; -fx-font-size: 13"); // Apply CSS styling

    }

    public void updateManagerLabel() {
/*        Platform.runLater(() -> {
            if (clientController.isManager()) {
                componentContainer.getChildren().add(new Label("   You are a manager"));
            } else {
                componentContainer.getChildren().add(new Label("   You are not a manager"));
            }
        });*/
    }

    public void updateRolesLabel(List<String> roles) {
        Platform.runLater(() -> {
            componentContainer.getChildren().clear();
            if (roles.isEmpty()) {
                componentContainer.getChildren().add(new Label("   You have no roles"));
            } else {
                for (String role : roles) {
                    componentContainer.getChildren().add(new Label("   " + role));
                }
            }
        });
    }

    private void updateClientData(List<String> roles) {
        updateManagerLabel();
        System.out.println("updateClientData" + roles);
        if(roles != null)
            updateRolesLabel(roles);
       // clientFlowDefinitionTabController.showFlowsTree(roles);
    }

    public void startRolesListRefresher() {
        System.out.println("startRolesListRefresher" + currentUserName);
        this.clientRefresher = new ClientRefresher(
                currentUserName , this::updateClientData);
        timer = new Timer();
        timer.schedule(clientRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    public void setFlowDefinitionTabController(ClientFlowDefinitionTabController flowDefinitionTabController) {
        this.clientFlowDefinitionTabController = flowDefinitionTabController;
    }
}
