package components.body.flowExecutionTab;

import com.google.gson.reflect.TypeToken;
import components.body.flowExecutionTab.MasterDetail.ClientMasterDetailController;
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

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static util.Constants.GSON_INSTANCE;

public class ExecuteFlowTask extends Task<Boolean> {
    private final systemengine engineManager = systemengineImpl.getInstance();
    private final ClientMasterDetailController masterDetailController;
    private final UUID flowId;
    private final SimpleStringProperty currentFlowId;
    private final SimpleBooleanProperty isTaskFinished;

    public ExecuteFlowTask(UUID flowId, ClientMasterDetailController masterDetailController, SimpleStringProperty currentFlowId, SimpleBooleanProperty isTaskFinished) {
        this.flowId = flowId;
        this.masterDetailController = masterDetailController;
        this.currentFlowId = currentFlowId;
        this.isTaskFinished = isTaskFinished;
    }
    protected Boolean call() {
        int SLEEP_TIME = 700;
        DTOFlowExecution executedData = engineManager.getDTOFlowExecutionById(this.flowId);

        masterDetailController.initMasterDetailPaneController(executedData);
        DTOFlowExecution finalExecutedData2 = executedData;
        Platform.runLater(() -> masterDetailController.updateFlowLabel(finalExecutedData2));

        while (executedData.getFlowExecutionResult() == null) {
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

        masterDetailController.getFlowExecutionTabController().backToFlowExecutionTabAfterExecution();
        return Boolean.TRUE;
    }

    public void getStepsFirstData(UUID flowId){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constants.GET_DTO_FLOW_EXECUTION_SERVLET).newBuilder();
        urlBuilder.addQueryParameter("uuid", String.valueOf(flowId));
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
                List<DTOStepsInFlow> flowFreeInputs = GSON_INSTANCE.fromJson(jsonResponse, new TypeToken<List<DTOStepsInFlow>>(){}.getType());
                Platform.runLater(() -> {


                });
            }
        });
    }

}

