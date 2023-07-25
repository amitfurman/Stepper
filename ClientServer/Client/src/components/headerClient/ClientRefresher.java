package components.headerClient;

import com.google.gson.reflect.TypeToken;
import dto.DTORole;
import dto.DTORolesList;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static util.Constants.GSON_INSTANCE;

public class ClientRefresher extends TimerTask {
    private final Consumer<List<String>> rolesListConsumer;
    private final String userName;

    public ClientRefresher(String userName, Consumer<List<String>> rolesList) {
        this.userName = userName;
        System.out.println("userName: " + userName);
        this.rolesListConsumer = rolesList;
    }

    @Override
    public void run() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constants.ROLES_LIST_PER_USER).newBuilder();
        urlBuilder.addQueryParameter("user", userName);
        String finalUrl = urlBuilder.build().toString();
        System.out.println("finalUrl: " + finalUrl);

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        OkHttpClient HTTP_CLIENT = new OkHttpClient();
        Call call = HTTP_CLIENT.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String jsonArrayOfUsersRoles = response.body().string();
                System.out.println("jsonArrayOfUsersRoles: " + jsonArrayOfUsersRoles);
                Set<String> userRolesNames = GSON_INSTANCE.fromJson(jsonArrayOfUsersRoles, new TypeToken<Set<String>>(){}.getType());
                System.out.println("userRolesNames: " + userRolesNames);
                rolesListConsumer.accept(userRolesNames != null ? new ArrayList<>(userRolesNames) : Collections.emptyList());

               /* String jsonArrayOfUsersRoles = response.body().string();
                System.out.println("jsonArrayOfUsersRoles: " + jsonArrayOfUsersRoles);
                String[] userRolesNames = GSON_INSTANCE.fromJson(jsonArrayOfUsersRoles, String[].class);
                System.out.println("userRolesNames: " + userRolesNames);
                rolesListConsumer.accept(userRolesNames != null ? Arrays.asList(userRolesNames) : Collections.emptyList());

                if (jsonArrayOfUsersRoles != null && !jsonArrayOfUsersRoles.isEmpty()) {
                    rolesListConsumer.accept(null);
                } else {
                    String[] userRolesNames = GSON_INSTANCE.fromJson(jsonArrayOfUsersRoles, String[].class);
                    rolesListConsumer.accept(userRolesNames != null ? Arrays.asList(userRolesNames) : Collections.emptyList());
                }*/
            }
        });
    }
}