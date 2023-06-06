package javafx.flowExecutionTab;

import dto.DTOFlowExecution;
import dto.DTOStepExecutionData;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.flowExecutionTab.MasterDetail.MasterDetailController;
import systemengine.systemengineImpl;
import systemengine.systemengine;

import java.util.UUID;

public class ExecuteFlowTask extends Task<Boolean> {
    private final systemengine engineManager = systemengineImpl.getInstance();
    private final MasterDetailController masterDetailController;
    private final UUID flowId;
    private final String currentFlowId;
    private final SimpleBooleanProperty isTaskFinished;

    public ExecuteFlowTask(UUID flowId, MasterDetailController masterDetailController, String currentFlowId, SimpleBooleanProperty isTaskFinished) {
        this.flowId = flowId;
        this.masterDetailController = masterDetailController;
        this.currentFlowId = currentFlowId;
        this.isTaskFinished = isTaskFinished;
    }

    @Override
    protected Boolean call() {
        int SLEEP_TIME = 200;

        System.out.println("start call");
        DTOFlowExecution executedData = engineManager.getDTOFlowExecution(this.flowId);
        System.out.println("after one");
        masterDetailController.initMasterDetailComponent(executedData);

        while (executedData.isComplete() == null) {
            System.out.println("start while");
            System.out.println("Executing flow: " + flowId);
            executedData = engineManager.getDTOFlowExecution(this.flowId);
            if (currentFlowId.equals(flowId.toString())) {
                masterDetailController.initMasterDetailComponent(executedData);
            }
            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException ignored) {}
        }
        isTaskFinished.set(true);
        return Boolean.TRUE;
    }
}

