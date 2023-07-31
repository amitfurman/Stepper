package components.headerClient;

import com.google.gson.Gson;
import components.body.flowDefinitionTab.ClientFlowDefinitionTabController;
import components.commonComponents.ClientCommonController;
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
import javafx.scene.text.TextFlow;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import util.Constants;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static util.Constants.REFRESH_RATE;

public class HeaderClientController {
    private Timer timer;
    private TimerTask clientRefresher;
    private ClientCommonController mainController;
    private ClientFlowDefinitionTabController clientFlowDefinitionTabController;
    @FXML
    GridPane HeaderClientGridPane;
    private VBox componentContainer;
    private ClientController clientController;
    private Label nameLabel;
    private String currentUserName;
    private Label rolesLabel;
    private Text textRolesLabel;
    private CheckBox managerCheckbox;

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
        managerCheckbox = new CheckBox();
        managerCheckbox.setSelected(false);
        managerCheckbox.setDisable(true);

        textRolesLabel = new Text("   Assigned Roles:");
        textRolesLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        textRolesLabel.setFill(Color.PINK);

        rolesLabel = new Label();
        rolesLabel.setGraphic(textRolesLabel);

        HBox managerBox = new HBox(managerLabel, managerCheckbox, rolesLabel);
        managerBox.setAlignment(Pos.CENTER_LEFT);
        managerBox.setSpacing(10);

        VBox root = new VBox(nameLabel, managerBox, componentContainer);
        HeaderClientGridPane.add(root, 0, 1);

    }

    public void setMainController(ClientCommonController mainController) {
        this.mainController = mainController;
    }

    public void setClientController(ClientController clientController) {
        this.clientController = clientController;
    }

    public void updateNameLabel() {
        currentUserName = clientController.getCurrentUserNameProperty().getValue();

        nameLabel.setText(currentUserName);
        nameLabel.setVisible(true);

        Font font = Font.font("Arial", FontWeight.NORMAL, 13);
        nameLabel.setFont(font);
        nameLabel.setStyle("-fx-text-fill: BLACK;");
    }
     public String getUserName(){
            return nameLabel.getText();
     }
     public Boolean getIsManager(){
            return managerCheckbox.isSelected();
     }
    public void updateManagerLabel(Boolean isManager, List<String> roles) {
        if (isManager != null) {
            Platform.runLater(() -> {
                if (isManager) {
                    managerCheckbox.setSelected(true);
                } else {
                    managerCheckbox.setSelected(false);
                }
                clientFlowDefinitionTabController.showFlowsTree(roles);
            });
        }
    }

    public void updateRolesLabel(List<String> roles) {
        Platform.runLater(() -> {
            if (roles.isEmpty()) {
                Text noRolesText = new Text(" You have no roles");
                noRolesText.setFill(Color.BLACK);
                noRolesText.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
                rolesLabel.setGraphic(new TextFlow(textRolesLabel, noRolesText));
            } else {
                StringBuilder rolesText = new StringBuilder(" ");
                for (String role : roles) {
                    rolesText.append(role).append(", ");
                }
                rolesText.delete(rolesText.length() - 2, rolesText.length());

                Text rolesTextFormatted = new Text(rolesText.toString());
                rolesTextFormatted.setFill(Color.BLACK);
                rolesTextFormatted.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
                rolesLabel.setGraphic(new TextFlow(textRolesLabel, rolesTextFormatted));
            }
        });
    }

    private void updateClientData(List<String> roles) {
        updateRolesLabel(roles);
        getIfManager(roles);
    }

    private void getIfManager(List<String> roles) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constants.IS_MANAGER_SERVLET).newBuilder();
        urlBuilder.addQueryParameter("userName", currentUserName);
        String finalUrl = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        OkHttpClient HTTP_CLIENT = new OkHttpClient();
        Call call = HTTP_CLIENT.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("Something went wrong.. " + e.getMessage() + " in getIfManager");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonResponse = response.body().string();
                Gson gson = new Gson();
                Boolean isManager = gson.fromJson(jsonResponse, Boolean.class);
                updateManagerLabel(isManager, roles);
            }
        });

      }

    public void startRolesListRefresher() {
        this.clientRefresher = new ClientRefresher(
                currentUserName , this::updateClientData);
        timer = new Timer();
        timer.schedule(clientRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    public void setFlowDefinitionTabController(ClientFlowDefinitionTabController flowDefinitionTabController) {
        this.clientFlowDefinitionTabController = flowDefinitionTabController;
    }
}
