package javafx.header;

import exceptions.*;
import javafx.Controller;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import systemengine.systemengine;
import systemengine.systemengineImpl;


import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileNotFoundException;

public class HeaderController {

    private Controller mainController;
    // private final systemengine systemEngineInterface;
    private String filePath;
    private boolean isErrorMessageShown = false;
    @FXML
    private Button chooseXMLFileButton;
    @FXML
    private TextArea chosenXmlFilePath;
    @FXML
    private Label errorMessageLabel;


/*
    public HeaderController() {
        this.systemEngineInterface = new systemengineImpl();
    }
*/

    @FXML
    private void initialize() {
        chosenXmlFilePath.setEditable(false);

       // errorMessageLabel.setVisible(false);
    }

    public void setMainController(Controller mainController) {
        this.mainController = mainController;
    }

    @FXML
    void clickToChooseXMLFileButton(ActionEvent event) {
        mainController.setFlowDetailsTree();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose XML File");

        Stage stage = (Stage) chooseXMLFileButton.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            filePath = selectedFile.getAbsolutePath();
            viewChosenXmlFilePath(event);
        }
        try {
            isErrorMessageShown = false;
            mainController.getSystemEngineInterface().cratingFlowFromXml(filePath);
            hideError();
            mainController.showFlowsTree();
            mainController.getFlowsTree().setVisible(true);
        } catch (DuplicateFlowsNames | UnExistsStep | OutputsWithSameName | MandatoryInputsIsntUserFriendly |
                 UnExistsData | SourceStepBeforeTargetStep | TheSameDD | UnExistsOutput |
                 FreeInputsWithSameNameAndDifferentType | InitialInputIsNotFreeInput | FileNotFoundException |
                 JAXBException e) {
            showError(e.getMessage());
            mainController.getFlowsTree().setVisible(false);
        }
    }
    @FXML
    void viewChosenXmlFilePath(ActionEvent event) {
        chosenXmlFilePath.setText(filePath.toString());
    }

    private void showError(String message) {
        if (!isErrorMessageShown) {
            isErrorMessageShown = true;
            errorMessageLabel.setText(message);
            errorMessageLabel.getStyleClass().add("errors"); // Apply the CSS class to the label
            FadeTransition animation = new FadeTransition();
            animation.setNode(errorMessageLabel);
            animation.setDuration(Duration.seconds(0.5));
            animation.setFromValue(0.0);
            animation.setToValue(1.0);
            animation.play();
        }
        //set activeFlow - false
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

        //set activeFlow - false
    }
}
