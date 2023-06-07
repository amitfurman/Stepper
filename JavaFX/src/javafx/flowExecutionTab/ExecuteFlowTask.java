package javafx.flowExecutionTab;

import dto.DTOFlowExecution;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.flowExecutionTab.MasterDetail.MasterDetailController;
import systemengine.systemengine;
import systemengine.systemengineImpl;

import java.util.UUID;

public class ExecuteFlowTask extends Task<Boolean> {
    private final systemengine engineManager = systemengineImpl.getInstance();
    private final MasterDetailController masterDetailController;
    private final UUID flowId;
    private final SimpleStringProperty currentFlowId;
    private final SimpleBooleanProperty isTaskFinished;

    public ExecuteFlowTask(UUID flowId, MasterDetailController masterDetailController, SimpleStringProperty currentFlowId, SimpleBooleanProperty isTaskFinished) {
        this.flowId = flowId;
        this.masterDetailController = masterDetailController;
        this.currentFlowId = currentFlowId;
        this.isTaskFinished = isTaskFinished;
    }

    protected Boolean call() {
        int SLEEP_TIME = 700;
        DTOFlowExecution executedData = engineManager.getDTOFlowExecution(this.flowId);

        masterDetailController.initMasterDetailPaneController(executedData);
        DTOFlowExecution finalExecutedData2 = executedData;
        Platform.runLater(() -> masterDetailController.updateFlowLabel(finalExecutedData2));

        while (executedData.getFlowExecutionResult() == null) {
            executedData = engineManager.getDTOFlowExecution(this.flowId);
            if (currentFlowId.getValue().equals(flowId.toString())) {
                DTOFlowExecution finalExecutedData = executedData;
                Platform.runLater(() -> masterDetailController.addStepsToMasterDetail(finalExecutedData));
            }
            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException ignored) {}
        }
        executedData = engineManager.getDTOFlowExecution(UUID.fromString(currentFlowId.getValue()));
        DTOFlowExecution finalExecutedData1 = executedData;
        Platform.runLater(() -> masterDetailController.updateFlowLabel(finalExecutedData1));

        masterDetailController.getFlowExecutionTabController().getMainController().goToStatisticsTab();
        return Boolean.TRUE;
    }
}

