package javafx;

import dto.DTOAllStepperFlows;
import dto.DTOFlowDefinition;
import exceptions.*;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import systemengine.systemengine;
import systemengine.systemengineImpl;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileNotFoundException;

public class Controller {
    private final systemengine systemEngineInterface ;

    private String filePath;
    @FXML
    private Button chooseXMLFileButton;
    @FXML
    private TextField chosenXmlFilePath;
    @FXML
    private Label errorMessageLabel;
    @FXML
    private TreeView<String> flowsTree;

    private boolean isErrorMessageShown = false;

    public Controller() {
        this.systemEngineInterface = new systemengineImpl();
    }

    @FXML
    public void initialize() {
        chosenXmlFilePath.setEditable(false);
        chosenXmlFilePath.setMouseTransparent(true);

    }

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
        try {
            isErrorMessageShown = false;
            systemEngineInterface.cratingFlowFromXml(filePath);
            hideError();
            showFlowsTree();
        } catch (DuplicateFlowsNames | UnExistsStep | OutputsWithSameName | MandatoryInputsIsntUserFriendly |
                 UnExistsData | SourceStepBeforeTargetStep | TheSameDD | UnExistsOutput |
                 FreeInputsWithSameNameAndDifferentType | FileNotFoundException | JAXBException e) {
            showError(e.getMessage());
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
            FadeTransition animation = new FadeTransition();
            animation.setNode(errorMessageLabel);
            animation.setDuration(Duration.seconds(0.3));
            animation.setFromValue(0.0);
            animation.setToValue(1.0);
            animation.play();
        }
        //set activeFlow - false
    }

    private void hideError() {
        FadeTransition animation = new FadeTransition();
        animation.setNode(errorMessageLabel);
        animation.setDuration(Duration.seconds(0.3));
        animation.setFromValue(1.0);
        animation.setToValue(0.0);
        animation.play();

        isErrorMessageShown = false;
        errorMessageLabel.textProperty().setValue("");

        //set activeFlow - false
    }

    private void showFlowsTree() {
        TreeItem<String> rootItem = new TreeItem<>("Flows");
        //TreeItem<String> rootItem = new TreeItem<>("Files", new ImageView(new Image("Folder_Icon.png")));

        DTOAllStepperFlows allStepperFlows = systemEngineInterface.getAllFlows();

        for (int i = 0; i < allStepperFlows.getNumberOfFlows(); i++) {
            TreeItem<String> branchItem = new TreeItem<>( allStepperFlows.getFlow(i).getName());
            TreeItem<String> leafItem1 = new TreeItem<>("Description: " +allStepperFlows.getFlow(i).getDescription());
            TreeItem<String> leafItem2 = new TreeItem<>("Number of steps: " + Integer.toString(allStepperFlows.getFlow(i).getNumberOfSteps()));
            TreeItem<String> leafItem3 = new TreeItem<>("Number of free inputs: "+ Integer.toString(allStepperFlows.getFlow(i).getNumberOfFreeInputs()));
            //TreeItem<String> leafItem4 = new TreeItem<>("video2");
            branchItem.getChildren().addAll(leafItem1, leafItem2, leafItem3);
            rootItem.getChildren().add(branchItem);

            // Handle single click event on branchItem
            branchItem.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                if (event.getClickCount() == 1) {
                    // Show leaf items
                    branchItem.setExpanded(true);
                }
                else if (event.getClickCount() == 2) {
                    // Call another function
                    System.out.println("Double clicked");    }

            });



/*            // Handle double click event on branchItem
            branchItem.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            });*/
        }

        //treeView.setShowRoot(false);
        flowsTree.setRoot(rootItem);

        flowsTree.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                TreeItem<String> selectedItem = flowsTree.getSelectionModel().getSelectedItem();
                if (selectedItem != null && selectedItem.isExpanded()) {
                    System.out.println("hi");
                }
            }
        });

    }
/*
    @Override
    public TreeCell<TreeData> call(TreeView<TreeData> param) {
        // Handle click events on the cell
        cell.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                TreeData selectedData = cell.getItem();
                System.out.println("Arrow Info: " + selectedData.getArrowInfo());
            } else if (event.getClickCount() == 2) {
                TreeData selectedData = cell.getItem();
                System.out.println("Text Info: " + selectedData.getTextInfo());
            }
        });
    }

 */

/*
    public void selectItem() {

        TreeItem<String> item = flowsTree.getSelectionModel().getSelectedItem();

        if(item != null) {
  //          System.out.println(item.getValue());
        }
    }
*/


}
