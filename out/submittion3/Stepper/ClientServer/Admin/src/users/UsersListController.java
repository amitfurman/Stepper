package users;

import components.api.HttpStatusUpdate;
import components.body.RolesManagementTab.RolesManagementController;
import components.body.UsersManagementTab.UsersManagementTabController;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.io.Closeable;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static util.Constants.REFRESH_RATE;

public class UsersListController implements Closeable {

    private Timer timer;
    private TimerTask listRefresher;
    private final IntegerProperty totalUsers;
    private UsersManagementTabController usersManagementTabController;
    @FXML private ListView<String> usersListView;
    @FXML private Label chatUsersLabel;

    public UsersListController(UsersManagementTabController usersManagementTabController) {
        totalUsers = new SimpleIntegerProperty();
        setUsersManagementController(usersManagementTabController);
    }

    @FXML
    public void initialize() {
        chatUsersLabel.textProperty().bind(Bindings.concat("Chat Users: (", totalUsers.asString(), ")"));
    }

    public void setUsersManagementController(UsersManagementTabController usersManagementTabController) {
        this.usersManagementTabController = usersManagementTabController;
    }

    private void updateUsersList(List<String> usersNames) {
        Platform.runLater(() -> {
            usersManagementTabController.showUsersTree(usersNames);
            totalUsers.set(usersNames.size());
        });
    }

    public void startListRefresher() {
        listRefresher = new UserListRefresher(
                this::updateUsersList);
        timer = new Timer();
        timer.schedule(listRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    @Override
    public void close() {
        usersListView.getItems().clear();
        totalUsers.set(0);
        if (listRefresher != null && timer != null) {
            listRefresher.cancel();
            timer.cancel();
        }
    }
}
