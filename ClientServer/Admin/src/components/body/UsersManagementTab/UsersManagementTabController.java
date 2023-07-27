package components.body.UsersManagementTab;

import com.google.gson.Gson;
import commonComponents.CommonController;
import dto.*;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import okhttp3.*;
import org.controlsfx.control.CheckListView;
import org.jetbrains.annotations.NotNull;
import users.UsersListController;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static util.Constants.*;

public class UsersManagementTabController {
    private CommonController mainController;
    @FXML
    private TreeView<String> usersTree;
    @FXML
    private TreeView<String> userDetailsTree;
    @FXML
    private Button SaveButton;
    private UsersListController usersListComponentController;
    private Set<String> users;
    private CheckListView rolesCheckList = new CheckListView();
    @FXML private GridPane GridPaneUsers;
    @FXML private CheckBox isManagerCheckBox;
    private Set<String> usersList;
    private Set<String> allRoles;
    private String UserName;


    @FXML
    public void initialize() {
        usersListComponentController = new UsersListController(this);
        usersListComponentController.startListRefresher();
        GridPaneUsers.add(rolesCheckList,0,1);

    }

    public Set<String> getUsers() {
        return users;
    }

    @FXML
    public void setPressOnSaveButton() {
        ObservableList<String> checkedItems = rolesCheckList.getCheckModel().getCheckedItems();
        Set<String> checkedRoles = checkedItems.stream().collect(Collectors.toSet());
        Boolean isManager = isManagerCheckBox.isSelected();
        updateUser(checkedRoles, isManager);
    }
    public void updateUser(Set<String> checkedItems, Boolean isManager) {
        String jsonPayload = new Gson().toJson(checkedItems);
        RequestBody body = RequestBody.create(jsonPayload, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(UPDATE_USER)
                .post(body)
                .build();


        String finalUrl = HttpUrl
                .parse(UPDATE_USER)
                .newBuilder()
                .addQueryParameter("userName", UserName)
                .addQueryParameter("isManager", isManager.toString())
                .toString();

        HttpClientUtil.runAsyncPost(finalUrl, request.body(), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                    System.out.println("Something went wrong: " + e.getMessage());
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
    public void setMainController(CommonController mainController) {
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
                getAllUserInfo(userName);
                getAllRoles(userName);
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
    public void getAllUserInfo(String userName){
        UserName = userName;
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constants.USER_INFO_SERVLET).newBuilder();
        urlBuilder.addQueryParameter("userName", UserName);
        String finalUrl = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        OkHttpClient HTTP_CLIENT = new OkHttpClient();
        Call call = HTTP_CLIENT.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonResponse = response.body().string();
                Gson gson = new Gson();
                DTOUserInfo dtoUserInfo = gson.fromJson(jsonResponse, DTOUserInfo.class);
                if (dtoUserInfo!=null) {
                    showDetailsInTree(dtoUserInfo);
                }

            }
        });

    }
    public void getAllRoles(String userName) {
        HttpClientUtil.runAsync(Constants.ROLES_LIST, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonArrayOfRoles = response.body().string();
                DTORolesList returnedRolesList = GSON_INSTANCE.fromJson(jsonArrayOfRoles, DTORolesList.class);
                Platform.runLater(() -> {
                    showRolesToEachRole(returnedRolesList);
                });
            }
        });


    }
    public void showDetailsInTree(DTOUserInfo dtoUserInfo) {
        TreeItem<String> rootItem = new TreeItem<>("Selected User Information - " + dtoUserInfo.getUserName());
        rootItem.setExpanded(true); // Set the root item to be initially expanded

        TreeItem<String> branchName = new TreeItem<>("User Name");
        TreeItem<String> nameItem = new TreeItem<>(dtoUserInfo.getUserName());
        branchName.getChildren().addAll(nameItem);

        TreeItem<String> branchRoles = new TreeItem<>("Roles: ");
         for (DTORole role :dtoUserInfo.getRoles()) {
             TreeItem<String> formalRole = new TreeItem<>(role.getName());
             branchRoles.getChildren().add(formalRole);
         }

         TreeItem<String> branchFlows = new TreeItem<>("Flows: ");
         for (String flow :dtoUserInfo.getAllFlows()) {
             TreeItem<String> formalFlow = new TreeItem<>(flow);
             branchFlows.getChildren().add(formalFlow);
         }

         TreeItem<String> branchExecutedFlows = new TreeItem<>("Executed Flows By User: ");
         for (String ExeFlow :dtoUserInfo.getExecutedFlows()) {
             TreeItem<String> formalExecutedFlow = new TreeItem<>(ExeFlow);
             branchExecutedFlows.getChildren().add(formalExecutedFlow);
         }

        rootItem.getChildren().addAll(branchName, branchRoles,branchFlows,branchExecutedFlows);
         Platform.runLater(() -> {
             userDetailsTree.setRoot(rootItem);
         });
    }
    public void showRolesToEachRole(DTORolesList returnedRolesList){
        rolesCheckList = new CheckListView();
        if(returnedRolesList!=null) {
            if (returnedRolesList.getRoles().size() != 0) {
                for (DTORole role : returnedRolesList.getRoles()) {
                    rolesCheckList.getItems().add(role.getName());
                }
            }
            for (DTORole role : returnedRolesList.getRoles()) {
                if (role.getUsers().contains(UserName)) {
                    rolesCheckList.getCheckModel().check(role.getName());
                }
            }
            GridPaneUsers.add(rolesCheckList, 0, 1);
        }
    }

    /*
    public void setFlowDetailsTree() {
        TreeItem<String> rootItem = new TreeItem<>();
        userDetailsTree.setRoot(rootItem);
        SaveButton.setDisable(true);
    }*/
}
