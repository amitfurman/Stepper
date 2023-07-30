package users;

import components.body.UsersManagementTab.UsersManagementTabController;
import dto.DTORole;
import javafx.beans.property.BooleanProperty;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

import static util.Constants.GSON_INSTANCE;

import java.util.Collections;

public class UserListRefresher extends TimerTask {
    private final Consumer<List<String>> usersListConsumer;
    private int requestNumber;

    public UserListRefresher(Consumer<List<String>> usersListConsumer) {
        this.usersListConsumer = usersListConsumer;
        requestNumber = 0;
    }

    @Override
    public void run() {
        final int finalRequestNumber = ++requestNumber;
        HttpClientUtil.runAsync(Constants.USERS_LIST, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("Something went wrong.. " + e.getMessage() + " in UserListRefresher");

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonArrayOfUsersNames = response.body().string();
               String[] usersNames = GSON_INSTANCE.fromJson(jsonArrayOfUsersNames, String[].class);
                usersListConsumer.accept(usersNames != null ? Arrays.asList(usersNames) : Collections.emptyList());
            }
        });

    }
}
