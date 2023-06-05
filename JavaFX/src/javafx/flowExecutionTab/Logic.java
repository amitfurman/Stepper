package javafx.flowExecutionTab;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.flowExecutionTab.MasterDetail.MasterDetailController;

import java.util.UUID;

public class Logic {

    private Task<Boolean> currentRunningTask;

    private MasterDetailController controller;

    public Logic(MasterDetailController controller) {
        this.controller = controller;
    }

    public void fetchData(UUID flowId, MasterDetailController masterDetail, String currentFlowId, SimpleBooleanProperty isTaskFinished) {
        currentRunningTask = new ExecuteFlowTask(flowId, masterDetail, currentFlowId, isTaskFinished);

        System.out.println("creating new thread");
        new Thread(currentRunningTask).start();
    }
}
