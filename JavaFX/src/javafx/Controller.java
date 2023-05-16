package javafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class Controller {

    private String filePath;
    @FXML
    private Button chooseXMLFileButton;
    @FXML
    private TextField chosenXmlFilePath;

    @FXML
    void clickToChooseXMLFileButton(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose XML File");

        Stage stage = (Stage) chooseXMLFileButton.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            filePath = selectedFile.getAbsolutePath();
            viewChosenXmlFilePath(event);
        }

    }

    @FXML
    void viewChosenXmlFilePath(ActionEvent event) {

        chosenXmlFilePath.setText(filePath.toString());
    }

}
