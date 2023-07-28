package components.header;

import commonComponents.CommonController;
import components.body.RolesManagementTab.RolesManagementController;
import dto.DTOAllFlowsNames;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;

import static util.Constants.GSON_INSTANCE;
import static util.Constants.UPLOAD_FILE;

public class AdminHeaderController {
    private CommonController mainController;
    @FXML
    private Button chooseXMLFileButton;
    @FXML
    private TextArea chosenXmlFilePath;
    @FXML
    private Label errorMessageLabel;

    private String filePath;

    private boolean isErrorMessageShown = false;
    private RolesManagementController rolesManagementController;

    @FXML
    private void initialize() {
        chosenXmlFilePath.setEditable(false);
    }

    @FXML
    void clickToChooseXMLFileButton(ActionEvent event) throws IOException {
/*        mainController.initDataInFlowExecutionTab();
        mainController.initInputsInFlowExecutionTab();*/

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose XML File");

        Stage stage = (Stage) chooseXMLFileButton.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        filePath = selectedFile.getAbsolutePath();

        if (selectedFile != null) {
            RequestBody body =
                    new MultipartBody.Builder()
                            .addFormDataPart("xmlFile",selectedFile.getName(),RequestBody.create(selectedFile, MediaType.parse("application/xml")))
                            .build();

            Request request = new Request.Builder()
                    .url(UPLOAD_FILE)
                    .post(body)
                    .build();

            String finalUrl = HttpUrl
                    .parse(UPLOAD_FILE)
                    .newBuilder()
                    .build()
                    .toString();

            HttpClientUtil.runAsyncPost(finalUrl, request.body(), new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Platform.runLater(() -> {
                        System.out.println("Something went wrong: " + e.getMessage());
                        showError(e.getMessage());
                    });
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        Platform.runLater(() -> {
                            hideError();
                            System.out.println("File uploaded successfully: " + response.message());
                            rolesManagementController = mainController.getRolesManagementController();
                            rolesManagementController.getAllFlows();
                            rolesManagementController.initDataInRolesManagementTab();
                        });
                    } else {
                        String errorMessage = response.body().string();

                        Platform.runLater(() -> {
                            showError(errorMessage);
                            System.out.println("Received message from server: " + errorMessage);

                        });
                    }

                }
            });

        }
            viewChosenXmlFilePath(event);


    }

    @FXML
    void viewChosenXmlFilePath(ActionEvent event) {
        chosenXmlFilePath.setText(filePath.toString());
    }
    public void setMainController(CommonController mainController) {
        this.mainController = mainController;
    }
    private void showError(String message) {
        if (!isErrorMessageShown) {
            isErrorMessageShown = true;
            errorMessageLabel.setText(message);
            errorMessageLabel.getStyleClass().add("errors");
            FadeTransition animation = new FadeTransition();
            animation.setNode(errorMessageLabel);
            animation.setDuration(Duration.seconds(0.5));
            animation.setFromValue(0.0);
            animation.setToValue(1.0);
            animation.play();
        }
    }
    private void hideError() {
        FadeTransition animation = new FadeTransition();
        animation.setNode(errorMessageLabel);
        animation.setDuration(Duration.seconds(0.5));
        animation.setFromValue(1.0);
        animation.setToValue(0.0);
        animation.play();

        isErrorMessageShown = false;
        errorMessageLabel.textProperty().setValue("");
    }
}
