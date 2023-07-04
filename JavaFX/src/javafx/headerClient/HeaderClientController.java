package javafx.headerClient;

import javafx.Controller;
import javafx.client.ClientController;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class HeaderClientController {
    private Controller mainController;
    @FXML
    GridPane HeaderClientGridPane;
    private VBox componentContainer;
    private ClientController clientController;
    private Label nameLabel;

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

/*

        Label nameLabel = new Label();
        nameLabel.textProperty().bind(nameField.textProperty());
        nameLabel.setVisible(false);
*/

/*        nameField.setOnAction(event -> {
            nameLabel.setVisible(true);
            managerCheckbox.setVisible(true);

            componentContainer.getChildren().addAll(nameLabel, managerCheckbox);

            nameField.clear();
        });*/

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
        nameLabel.setText(clientController.getCurrentUserNameProperty().getValue());
        nameLabel.setVisible(true);
        nameLabel.setStyle("-fx-text-fill: BLACK; -fx-font-size: 13"); // Apply CSS styling

    }

}
