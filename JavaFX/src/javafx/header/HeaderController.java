package javafx.header;

import exceptions.*;
import javafx.Controller;
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

import javax.xml.bind.JAXBException;
import java.io.*;


public class HeaderController {
    private Controller mainController;
    @FXML
    private Button chooseXMLFileButton;
    @FXML
    private TextArea chosenXmlFilePath;
    @FXML
    private Label errorMessageLabel;

    private String filePath;

    private boolean isErrorMessageShown = false;

    @FXML
    private void initialize() {
        chosenXmlFilePath.setEditable(false);
    }

 @FXML
    void clickToChooseXMLFileButton(ActionEvent event) {
        mainController.initDataInFlowExecutionTab();
        mainController.initInputsInFlowExecutionTab();

        mainController.setFlowDetailsTree();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose XML File");

        Stage stage = (Stage) chooseXMLFileButton.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            filePath = selectedFile.getAbsolutePath();
            viewChosenXmlFilePath(event);
            try {
                isErrorMessageShown = false;
                mainController.getSystemEngineInterface().cratingFlowFromXml(filePath);
                hideError();
                mainController.showFlowsTree();
                mainController.getFlowsTree().setVisible(true);
            } catch (DuplicateFlowsNames | UnExistsStep | OutputsWithSameName | MandatoryInputsIsntUserFriendly |
                     UnExistsData | SourceStepBeforeTargetStep | TheSameDD | UnExistsOutput |
                     FreeInputsWithSameNameAndDifferentType | InitialInputIsNotExist | FileNotFoundException |
                     JAXBException | UnExistsFlow | UnExistsDataInTargetFlow | FileNotExistsException | FileIsNotXmlTypeException e) {
                showError(e.getMessage());
                mainController.getFlowsTree().setVisible(false);
            }
        } else {
            try {
                isErrorMessageShown = false;
                mainController.getSystemEngineInterface().cratingFlowFromXml(filePath);
                hideError();
                mainController.showFlowsTree();
                mainController.getFlowsTree().setVisible(true);
            } catch (DuplicateFlowsNames | UnExistsStep | OutputsWithSameName | MandatoryInputsIsntUserFriendly |
                     UnExistsData | SourceStepBeforeTargetStep | TheSameDD | UnExistsOutput |
                     FreeInputsWithSameNameAndDifferentType | InitialInputIsNotExist | FileNotFoundException |
                     JAXBException | UnExistsFlow | UnExistsDataInTargetFlow | FileNotExistsException | FileIsNotXmlTypeException e) {
                showError(e.getMessage());
                mainController.getFlowsTree().setVisible(false);
            }
        }
 }

    @FXML
    void viewChosenXmlFilePath(ActionEvent event) {
        chosenXmlFilePath.setText(filePath.toString());
    }
    public void setMainController(Controller mainController) {
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


/*
    @FXML
    void clickToChooseXMLFileButton(ActionEvent event) throws IOException {
        mainController.initDataInFlowExecutionTab();
        mainController.initInputsInFlowExecutionTab();

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
                            //open user Management tab
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
            try (InputStream inputStream = new FileInputStream(selectedFile)) {
                HttpURLConnection connection = (HttpURLConnection) new URL(UPLOAD_FILE).openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);

                // Set request headers
                connection.setRequestProperty("Content-Type", contentType);
                connection.setRequestProperty("Content-Disposition", "attachment; filename=\"" + filename + "\"");

                // Write file content to the request body
                try (OutputStream outputStream = connection.getOutputStream()) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;

                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }

                int responseCode = connection.getResponseCode();
                String responseMessage = connection.getResponseMessage();
                mainController.getSystemEngineInterface().cratingFlowFromXml(filePath);
                hideError();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    System.out.println("File uploaded successfully: " + responseMessage);
                } else {
                    System.out.println("Server returned the following response code: " + responseCode + " " + responseMessage);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }  catch (DuplicateFlowsNames | UnExistsStep | OutputsWithSameName | MandatoryInputsIsntUserFriendly |
                      UnExistsData | SourceStepBeforeTargetStep | TheSameDD | UnExistsOutput |
                      FreeInputsWithSameNameAndDifferentType | InitialInputIsNotExist |
                      JAXBException | UnExistsFlow | UnExistsDataInTargetFlow |FileNotExistsException | FileIsNotXmlTypeException e) {
                showError(e.getMessage());
            }

            viewChosenXmlFilePath(event);
    }
*/
