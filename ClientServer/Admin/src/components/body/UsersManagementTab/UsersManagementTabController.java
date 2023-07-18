package components.body.UsersManagementTab;

import dto.DTOAllStepperFlows;
import dto.DTOFlowDefinition;
import dto.DTOSingleFlowIOData;
import flow.api.FlowIO.IO;
import javafx.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import user.UserManager;
import users.UserListRefresher;
import users.UsersListController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class UsersManagementTabController {
    private Controller mainController;
    @FXML
    private TreeView<String> usersTree;
    @FXML
    private TreeView<String> userDetailsTree;
    @FXML
    private Button SaveButton;

    private String chosenUserName;

    private UsersListController usersListComponentController;

    private Set<String> users;


    @FXML
    public void initialize() {
        SaveButton.setDisable(true);
        usersListComponentController = new UsersListController(this);
        usersListComponentController.startListRefresher();
    }

    public Set<String> getUsers() {
        return users;
    }

    @FXML
    public void setPressOnSaveButton() {
       // mainController.goToFlowExecutionTab(chosenUserName);
        mainController.initDataInFlowExecutionTab();
    }
    public void setMainController(Controller mainController) {
        this.mainController = mainController;
    }
    public TreeView<String> getUsersTree(){
        return usersTree;
    }
    public void showUsersTree(List<String> usersList) {
        users = usersList.stream().collect(Collectors.toSet());
        TreeItem<String> rootItem = new TreeItem<>("Users");
        rootItem.setExpanded(true);

        for (String userName : usersList) {
            Button pressToSeeFullDetailsButton = new Button("Press to see full details");
            pressToSeeFullDetailsButton.setId("pressToSeeFullDetailsButton");

            TreeItem<String> branchItem = new TreeItem<>("");
            branchItem.setGraphic(createTreeCellGraphic(userName, pressToSeeFullDetailsButton));


            pressToSeeFullDetailsButton.setOnAction(event -> {
                showChosenFlow();
            });
            rootItem.getChildren().add(branchItem);
        }
        usersTree.setRoot(rootItem);
    }
    private HBox createTreeCellGraphic(String nodeName, Button button) {
        Label nameLabel = new Label(nodeName);
        HBox graphicContainer = new HBox(nameLabel, button);
        graphicContainer.setSpacing(5);
        return graphicContainer;
    }
    public void showChosenFlow() {
        TreeItem<String> rootItem = new TreeItem<>("Chosen Flow Details - ");
        userDetailsTree.setRoot(rootItem);
        boolean isEmptyFlowDetailsTree = (userDetailsTree.getRoot() == null || userDetailsTree.getRoot().getChildren().isEmpty());
        SaveButton.setDisable(isEmptyFlowDetailsTree);
    }
    public void setFlowDetailsTree(){
        TreeItem<String> rootItem = new TreeItem<>();
        userDetailsTree.setRoot(rootItem);
        SaveButton.setDisable(true);
    }

}
