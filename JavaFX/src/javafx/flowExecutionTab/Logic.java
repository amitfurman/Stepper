package javafx.flowExecutionTab;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.flowExecutionTab.MasterDetail.MasterDetailController;

import java.util.UUID;

public class Logic {

    private Task<Boolean> currentRunningTask;

    public void fetchData(UUID flowId, MasterDetailController masterDetail, SimpleStringProperty currentFlowId, SimpleBooleanProperty isTaskFinished) {
        currentRunningTask = new ExecuteFlowTask(flowId, masterDetail, currentFlowId, isTaskFinished);

        new Thread(currentRunningTask).start();
    }
}
