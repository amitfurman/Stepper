package components.body.flowExecutionTab;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import components.body.flowExecutionTab.MasterDetail.ClientMasterDetailController;
import dto.DTOFlowExeInfo;
import dto.DTOFlowExecution;
import dto.DTOStepsInFlow;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import systemengine.systemengine;
import systemengine.systemengineImpl;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static util.Constants.ACTIVATE_FLOW;
import static util.Constants.GSON_INSTANCE;

public class ExecuteFlowTask extends Task<Boolean> {
    private final systemengine engineManager = systemengineImpl.getInstance();
    private final ClientMasterDetailController masterDetailController;
    private final UUID flowId;
    private final SimpleStringProperty currentFlowId;
    private final SimpleBooleanProperty isTaskFinished;
    private int SLEEP_TIME = 700;

    public ExecuteFlowTask(UUID flowId, ClientMasterDetailController masterDetailController, SimpleStringProperty currentFlowId, SimpleBooleanProperty isTaskFinished) {
        this.flowId = flowId;
        this.masterDetailController = masterDetailController;
        this.currentFlowId = currentFlowId;
        this.isTaskFinished = isTaskFinished;
    }

    protected Boolean call() throws IOException {
        System.out.println("ExecuteFlowTask.call");
/*      //  DTOFlowExecution executedData = engineManager.getDTOFlowExecutionById(this.flowId);
        masterDetailController.initMasterDetailPaneController(executedData);
        DTOFlowExecution finalExecutedData2 = executedData;
        Platform.runLater(() -> masterDetailController.updateFlowLabel(finalExecutedData2));*/

        getStepsFirstData(flowId);
/*        while (executedData.getFlowExecutionResult() == null) {
            executedData = engineManager.getDTOFlowExecutionById(this.flowId);
            if (currentFlowId.getValue().equals(flowId.toString())) {
                DTOFlowExecution finalExecutedData = executedData;
                Platform.runLater(() -> masterDetailController.addStepsToMasterDetail(finalExecutedData));
            }
            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException ignored) {}
        }

        executedData = engineManager.getDTOFlowExecutionById(UUID.fromString(currentFlowId.getValue()));
        DTOFlowExecution finalExecutedData1 = executedData;
        Platform.runLater(() -> masterDetailController.updateFlowLabel(finalExecutedData1));
*/
        return Boolean.TRUE;
    }

   /* public void getStepsFirstData(UUID flowId) throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constants.GET_DTO_FLOW_EXECUTION_SERVLET).newBuilder();
        urlBuilder.addQueryParameter("uuid", String.valueOf(flowId));
        String finalUrl = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(finalUrl)
                .build();
        HttpClientUtil.runAsyncPost(finalUrl, request.body(), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("onFailure");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                System.out.println("response.isSuccessful()");
                String rawBody = response.body().string();
                System.out.println("rawBody : " + rawBody);
                DTOFlowExeInfo dtoFlowExecution = GSON_INSTANCE.fromJson(rawBody, new TypeToken<DTOFlowExecution>() {
                }.getType());
                System.out.println("name : " + dtoFlowExecution.getFlowName());
                masterDetailController.initMasterDetailPaneController(dtoFlowExecution);
                Platform.runLater(() -> masterDetailController.updateFlowLabel(dtoFlowExecution));

                while (dtoFlowExecution.getResultExecute() == null) {
                    DTOFlowExeInfo executedData = makeSyncHttpRequest(flowId);
                    if (currentFlowId.getValue().equals(flowId.toString())) {
                        Platform.runLater(() -> masterDetailController.addStepsToMasterDetail(executedData));
                    }
                    try {
                        Thread.sleep(SLEEP_TIME);
                    } catch (InterruptedException ignored) {

                    }

                }
                // finalRequest();
            }
        });
    }*/

    public void getStepsFirstData(UUID flowId) throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constants.GET_DTO_FLOW_EXECUTION_SERVLET).newBuilder();
        urlBuilder.addQueryParameter("uuid", String.valueOf(flowId));
        String finalUrl = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        OkHttpClient HTTP_CLIENT = new OkHttpClient();

        Call call = HTTP_CLIENT.newCall(request);
        Response response = call.execute();

        if (response.isSuccessful()) {

            System.out.println("response.isSuccessful()");
            String rawBody = response.body().string();
            System.out.println("rawBody : " + rawBody);
            DTOFlowExeInfo dtoFlowExecution = GSON_INSTANCE.fromJson(rawBody, new TypeToken<DTOFlowExeInfo>() {}.getType());
            System.out.println("name : " + dtoFlowExecution.getFlowName());
            masterDetailController.initMasterDetailPaneController(dtoFlowExecution);
            Platform.runLater(() -> masterDetailController.updateFlowLabel(dtoFlowExecution));

            while (dtoFlowExecution.getResultExecute() == null) {
                DTOFlowExeInfo executedData =  makeSyncHttpRequest(this.flowId);
                if (currentFlowId.getValue().equals(flowId.toString())) {
                    Platform.runLater(() -> masterDetailController.addStepsToMasterDetail(executedData));
                }
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException ignored) {}
            }

            finalRequest();

        } else {
            System.err.println("Error: " + response.code() + " " + response.message());
        }
        response.close();
    }
    private static DTOFlowExeInfo makeSyncHttpRequest(UUID flowId) throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constants.GET_DTO_FLOW_EXECUTION_SERVLET).newBuilder();
        urlBuilder.addQueryParameter("uuid", String.valueOf(flowId));
        String finalUrl = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(finalUrl)
                .build();
        OkHttpClient HTTP_CLIENT = new OkHttpClient();

        Call call = HTTP_CLIENT.newCall(request);
        Response response = call.execute();

        if (response.isSuccessful()) {
            String rawBody = response.body().string();
            return GSON_INSTANCE.fromJson(rawBody, new TypeToken<DTOFlowExeInfo>() {}.getType());
        } else {
            System.err.println("Error: " + response.code() + " " + response.message());
            return null;
        }
    }
    private void finalRequest() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constants.GET_DTO_FLOW_EXECUTION_SERVLET).newBuilder();
        urlBuilder.addQueryParameter("uuid", String.valueOf(UUID.fromString(currentFlowId.getValue())));
        String finalUrl = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        OkHttpClient HTTP_CLIENT = new OkHttpClient();
        Call call = HTTP_CLIENT.newCall(request);
        Response response = null;
        try {
            response = call.execute();
            if (response.isSuccessful()) {
                String rawBody = response.body().string();
                DTOFlowExeInfo FlowExecution = GSON_INSTANCE.fromJson(rawBody, new TypeToken<DTOFlowExeInfo>() {}.getType());
                Platform.runLater(() -> masterDetailController.updateFlowLabel(FlowExecution));
                masterDetailController.getFlowExecutionTabController().backToFlowExecutionTabAfterExecution();

            } else {
                System.err.println("Error: " + response.code() + " " + response.message());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            response.close();
        }
    }
}




