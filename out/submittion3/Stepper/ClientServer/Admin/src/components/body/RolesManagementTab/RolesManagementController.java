package components.body.RolesManagementTab;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import components.commonComponents.AdminCommonController;
import dto.*;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import okhttp3.*;
import org.controlsfx.control.CheckListView;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.*;

import static util.Constants.*;

public class RolesManagementController {
    private AdminCommonController mainController;
    @FXML private Button SaveButton;
    @FXML private TreeView<String> rolesTree;
    @FXML private TreeView<String> roleDetailsTree;
    @FXML private Button newButton;
    @FXML private GridPane checkBoxGridPane;
    private CheckListView flowsCheckList = new CheckListView();
    private ListView<String> usersListView = new ListView<>();
    private DTORolesList returnedRolesList;
    private String currRole;
    private Set<String> usersList;
    Set<String> allFlows;
    private VBox usersListvbox;
    private Label titleLabel;

    public void setMainController(AdminCommonController mainController) {
        this.mainController = mainController;
    }
    @FXML
    public void initialize() {
        checkBoxGridPane.add(flowsCheckList,0,1);

        titleLabel = new Label("Users List:");
        titleLabel.setTextFill(Color.web("#ffa6c5"));
        titleLabel.setStyle("-fx-font-weight: bold");
        titleLabel.setAlignment(Pos.CENTER);
        usersListvbox = new VBox(titleLabel, usersListView);
        checkBoxGridPane.add(usersListvbox, 0, 2);

    }
    public Integer getNumberOfAllFlows(){
        return allFlows.size();
    }

    public void setAllFlows(Set<String> flows){
        allFlows = flows;
    }


    public void getAllFlows() {
        HttpClientUtil.runAsync(Constants.ALL_FLOWS_SERVLET, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("Failed in getAllFlows");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonArrayOfRoles = response.body().string();
                DTOAllFlowsNames allFlowsNames = GSON_INSTANCE.fromJson(jsonArrayOfRoles, DTOAllFlowsNames.class);
                allFlows = allFlowsNames.getAllFlowsNames();
            }
        });
    }
    @FXML
    public void setPressOnSave() {
        ObservableList<String> checkedItems = flowsCheckList.getCheckModel().getCheckedItems();
        //ObservableList<String> checkedUsersItems = usersCheckList.getCheckModel().getCheckedItems();
        //updateRoles(checkedItems,checkedUsersItems);

    }
    public void updateRoles(ObservableList<String> checkedItems, ObservableList<String> checkedUsersItems) {
       DTORole currentRole =  returnedRolesList.getRoles().stream().filter(role-> role.getName().equals(currRole)).findFirst().get();
        currentRole.getFlowsInRole().clear();
        currentRole.getFlowsInRole().addAll(checkedItems);
        currentRole.getUsers().clear();
        currentRole.getUsers().addAll(checkedUsersItems);
        updateRole(currentRole);
    }
    @FXML
    public void setPressOnNewButton(ActionEvent actionEvent) {
        Stage popupWindow = new Stage();
        popupWindow.initModality(Modality.APPLICATION_MODAL);
        popupWindow.setTitle("Enter new role details");
        VBox layout = new VBox(10);

        HBox labelHbox = new HBox();
        Label label = new Label("New Role Details");
        label.getStyleClass().add("popup-label");
        labelHbox.setAlignment(Pos.CENTER);
        labelHbox.getChildren().add(label);

        HBox nameHbox = new HBox();
        Label nameLabel = new Label("Role Name: ");
        TextField nameTextField = new TextField();
        nameTextField.getStyleClass().add("text-field");
        nameLabel.getStyleClass().add("data-label");
        nameHbox.getChildren().addAll(nameLabel, nameTextField);

        HBox descriptionHbox = new HBox();
        Label descriptionLabel = new Label("Description: ");
        descriptionLabel.getStyleClass().add("data-label");
        TextField descriptionTextField = new TextField();
        descriptionTextField.getStyleClass().add("text-field");
        descriptionHbox.getChildren().addAll(descriptionLabel, descriptionTextField);

        VBox flowVBox = new VBox();
        CheckListView<String> flowsForNewRoleListView = new CheckListView<>();
        Label flowsLabel = new Label("Choose flows: ");
        flowsLabel.getStyleClass().add("data-label");
        returnedRolesList.getRoles().stream().filter(role->role.getName().equals("All Flows")).findFirst().get().getFlowsInRole().forEach(flow -> flowsForNewRoleListView.getItems().add(flow));

        flowsForNewRoleListView.setMaxHeight(100);
        flowsForNewRoleListView.setPrefWidth(200);
        flowVBox.getChildren().addAll(flowsLabel, flowsForNewRoleListView);

        ObservableList<String> checkedItems = flowsForNewRoleListView.getCheckModel().getCheckedItems();

        Button saveButton = new Button("Press to create a new role");
        saveButton.getStyleClass().add("role-button");
        Label errorLabel = new Label();
        errorLabel.getStyleClass().add("data-label");
        saveButton.setOnAction(e -> {
            errorLabel.setText("");
            String name = nameTextField.getText();
            String description = descriptionTextField.getText();
            List<String> chosenItems = new ArrayList<>(checkedItems);
            Set<String> flowsInRole = new HashSet<>(chosenItems);

            String finalName = name;
            if(returnedRolesList.getRoles().stream().anyMatch(r->r.getName().equals(finalName))) {
                errorLabel.setText("There is already role with this name. Please choose a new one.");
            }else {
                returnedRolesList.getRoles().add(new DTORole(name, description, flowsInRole, null));

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("name", name);
                jsonObject.addProperty("description", description);
                jsonObject.addProperty("chosenItems", new Gson().toJson(chosenItems));
                String jsonPayload = jsonObject.toString();

                createNewRole(jsonPayload);
                initRolesTree();

                // Close the popup window
                popupWindow.close();
            }

        });


        layout.getChildren().addAll(labelHbox,nameHbox, descriptionHbox, flowVBox, saveButton, errorLabel);

        layout.setAlignment(Pos.CENTER);
        Scene scene1 = new Scene(layout, 700, 400);
        scene1.getStylesheets().add(getClass().getResource("rolesManagementTab.css").toExternalForm());
        popupWindow.setScene(scene1);
        popupWindow.showAndWait();
    }
    public void createNewRole(String jsonPayload){
        RequestBody body = RequestBody.create(jsonPayload, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(NEW_ROLE)
                .post(body)
                .build();

        String finalUrl = HttpUrl
                .parse(NEW_ROLE)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runAsyncPost(finalUrl, request.body(), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                    System.out.println("Something went wrong: " + e.getMessage()+ " in createNewRole");
                });
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    Platform.runLater(() -> {


                    });
                } else {
                    String errorMessage = response.body().string();

                    Platform.runLater(() -> {
                        System.out.println("Received message from server: " + errorMessage);

                    });
                }

            }
        });
    }
    public void updateRole(DTORole currentRole){
        String jsonPayload = new Gson().toJson(currentRole);
        RequestBody body = RequestBody.create(jsonPayload, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(UPDATE_ROLE)
                .post(body)
                .build();

        String finalUrl = HttpUrl
                .parse(UPDATE_ROLE)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runAsyncPost(finalUrl, request.body(), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                    System.out.println("Something went wrong: " + e.getMessage() + "in updateRole");
                });
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    Platform.runLater(() -> {


                    });
                } else {
                    String errorMessage = response.body().string();

                    Platform.runLater(() -> {
                        System.out.println("Received message from server: " + errorMessage);

                    });
                }

            }
        });
    }
    public void initDataInRolesManagementTab() {
        HttpClientUtil.runAsync(Constants.ROLES_LIST, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("onFailure in initDataInRolesManagementTab");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonArrayOfRoles = response.body().string();
                returnedRolesList = GSON_INSTANCE.fromJson(jsonArrayOfRoles, DTORolesList.class);
                Platform.runLater(() -> {
                    initRolesTree();
                });
            }
        });
    }
    public void initRolesTree(){
        showRolesTree();
    }
    public void showRolesTree() {
        TreeItem<String> rootItem = new TreeItem<>("Roles");
        rootItem.setExpanded(true);

        for (DTORole role : returnedRolesList.getRoles()) {
            Button pressToSeeFullDetailsButton = new Button("Press to see full details");
            pressToSeeFullDetailsButton.setId("pressToSeeFullDetailsButton");
            pressToSeeFullDetailsButton.getStyleClass().add("pressToSeeFullDetailsButton");

            TreeItem<String> branchItem = new TreeItem<>("");
            branchItem.setGraphic(createTreeCellGraphic(role.getName(), pressToSeeFullDetailsButton));


            pressToSeeFullDetailsButton.setOnAction(event -> {

                currRole = role.getName();
                showChosenRole(role);
                flowsCheckList.getItems().clear();
                usersListvbox.getChildren().clear();
                usersListView.getItems().clear();
                showFlowsToEachRole(role);
                showUsersToEachRole(role);
            });

            rootItem.getChildren().add(branchItem);
        }
        Platform.runLater(() -> {
            rolesTree.setRoot(rootItem);
        });
    }
    private HBox createTreeCellGraphic(String nodeName, Button button) {
        Label nameLabel = new Label(nodeName);
        HBox graphicContainer = new HBox(nameLabel, button);
        graphicContainer.setSpacing(5);
        return graphicContainer;
    }
    public void showChosenRole(DTORole role) {
        TreeItem<String> rootItem = new TreeItem<>("Chosen Role Details - " + role.getName());
        roleDetailsTree.setRoot(rootItem);
        rootItem.setExpanded(true); // Set the root item to be initially expanded

        TreeItem<String> branchName = new TreeItem<>("Role Details");
        TreeItem<String> nameItem = new TreeItem<>("Name: " + role.getName());
        TreeItem<String> descriptionItem = new TreeItem<>("Description: " + role.getDescription());
        TreeItem<String> flowsName = new TreeItem<>("Flows");
        for(String flow : role.getFlowsInRole()){
            TreeItem<String> flowName = new TreeItem<>(flow);
            flowsName.getChildren().add(flowName);
        }
        branchName.getChildren().addAll(nameItem, descriptionItem,flowsName);
        rootItem.getChildren().add(branchName);

        boolean isEmptyFlowDetailsTree = (roleDetailsTree.getRoot() == null || roleDetailsTree.getRoot().getChildren().isEmpty());
        SaveButton.setDisable(isEmptyFlowDetailsTree);
    }
    public void showFlowsToEachRole(DTORole role){
        flowsCheckList = new CheckListView();

        for (String currFlow : allFlows) {
            flowsCheckList.getItems().add(currFlow);
        }

        for (String currFlow : allFlows) {
            if (role.getFlowsInRole().contains(currFlow)) {

                flowsCheckList.getCheckModel().check(currFlow);
            }
        }
        checkBoxGridPane.add(flowsCheckList, 0, 1);
    }


    public void showUsersToEachRole(DTORole role){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constants.GET_USERS_PER_ROLE).newBuilder();
        urlBuilder.addQueryParameter("roleName", role.getName());
        String finalUrl = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        OkHttpClient HTTP_CLIENT = new OkHttpClient();
        Call call = HTTP_CLIENT.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println(e.getMessage() + " in showUsersToEachRole");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonResponse = response.body().string();
                Gson gson = new Gson();
                List<String> users = GSON_INSTANCE.fromJson(jsonResponse, new TypeToken<List<String>>(){}.getType());
                if (users !=null) {
                    updateUserList(users);
                }

            }
        });

    }

    public void updateUserList(List<String> users) {
       Platform.runLater(() -> {
            usersListView = new ListView<>();

            for (String user : users) {
                usersListView.getItems().add(user);
            }

            usersListvbox = new VBox(titleLabel, usersListView);
            checkBoxGridPane.add(usersListvbox, 0, 2);
       });
    }

}
