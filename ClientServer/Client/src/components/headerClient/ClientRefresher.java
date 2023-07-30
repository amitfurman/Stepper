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
        this.rolesListConsumer = rolesList;
    }

    @Override
    public void run() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constants.ROLES_LIST_PER_USER).newBuilder();
        urlBuilder.addQueryParameter("user", userName);
        String finalUrl = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        OkHttpClient HTTP_CLIENT = new OkHttpClient();
        Call call = HTTP_CLIENT.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("on failure in ClientRefresher");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String jsonArrayOfUsersRoles = response.body().string();
                Set<String> userRolesNames = GSON_INSTANCE.fromJson(jsonArrayOfUsersRoles, new TypeToken<Set<String>>(){}.getType());
                rolesListConsumer.accept(userRolesNames != null ? new ArrayList<>(userRolesNames) : Collections.emptyList());
            }
        });
    }
}
