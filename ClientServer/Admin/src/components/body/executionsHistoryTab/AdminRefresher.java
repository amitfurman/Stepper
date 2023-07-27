package components.body.executionsHistoryTab;

import com.google.gson.reflect.TypeToken;
import dto.DTOFlowExeInfo;
import okhttp3.*;
import util.Constants;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

import static util.Constants.GSON_INSTANCE;

public class AdminRefresher extends TimerTask {
    private final Consumer<List<DTOFlowExeInfo>> flowExeListConsumer;


    public AdminRefresher(Consumer<List<DTOFlowExeInfo>> flowExeListConsumer){
        this.flowExeListConsumer = flowExeListConsumer;
    }

    @Override
    public void run() {
            HttpUrl.Builder urlBuilder = HttpUrl.parse(Constants.GET_FLOW_EXECUTION_LIST_SERVLET).newBuilder();
            String finalUrl = urlBuilder.build().toString();

            Request request = new Request.Builder()
                    .url(finalUrl)
                    .build();

            OkHttpClient HTTP_CLIENT = new OkHttpClient();
            Call call = HTTP_CLIENT.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println("on failure");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String jsonArrayOfUsersRoles = response.body().string();
                    LinkedList<DTOFlowExeInfo> flowExeList = GSON_INSTANCE.fromJson(jsonArrayOfUsersRoles, new TypeToken<LinkedList<DTOFlowExeInfo>>() {}.getType());
                    flowExeListConsumer.accept(flowExeList != null ? new LinkedList<>(flowExeList) : Collections.emptyList());


               /*     if (flowExeList != null) {
                    }*/

                }
            });

    }

}
