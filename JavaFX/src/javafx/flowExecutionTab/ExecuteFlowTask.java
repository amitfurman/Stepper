package javafx.flowExecutionTab;

import dto.DTOFlowExecution;
import dto.DTOStepExecutionData;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.flowExecutionTab.MasterDetail.MasterDetailController;
import systemengine.systemengineImpl;
import systemengine.systemengine;

import java.util.UUID;

public class ExecuteFlowTask extends Task<Boolean> {
    private final systemengine engineManager = systemengineImpl.getInstance();
    private final MasterDetailController masterDetailController;
    private final UUID flowId;
    private final SimpleStringProperty currentFlowId;
    private final SimpleBooleanProperty isTaskFinished;
    private String lastTaskId = "";

    public ExecuteFlowTask(UUID flowId, MasterDetailController masterDetailController, SimpleStringProperty currentFlowId, SimpleBooleanProperty isTaskFinished) {
        this.flowId = flowId;
        this.masterDetailController = masterDetailController;
        this.currentFlowId = currentFlowId;
        this.isTaskFinished = isTaskFinished;
    }

/*    @Override
    protected Boolean call() {
        lastTaskId = currentFlowId;
        int SLEEP_TIME = 200;
        DTOFlowExecution executedData = engineManager.getDTOFlowExecution(this.flowId);
        masterDetailController.initMasterDetailPaneController(executedData);
        DTOFlowExecution finalExecutedData2 = executedData;
        Platform.runLater(() -> masterDetailController.updateFlowLabel(finalExecutedData2));

        while (executedData.getFlowExecutionResult() == null) {
            executedData = engineManager.getDTOFlowExecution(this.flowId);
            System.out.println(currentFlowId);
            System.out.println(flowId);
            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException ignored) {}


        }


            executedData = engineManager.getDTOFlowExecution(this.flowId);
            DTOFlowExecution finalExecutedData1 = executedData;
            Platform.runLater(() -> {
                //masterDetailController.clearStepsInMasterDetail(); // Clear existing steps in master details
               // masterDetailController.initMasterDetailPaneController(finalExecutedData1);
                masterDetailController.updateFlowLabel(finalExecutedData1);
               // masterDetailController.addStepsToMasterDetail(finalExecutedData1);
            });


        return Boolean.TRUE;
    }*/


    protected Boolean call() {
        int SLEEP_TIME = 500;
        //DTOFlowExecution executedData = engineManager.getDTOFlowExecution(UUID.fromString(currentFlowId.getValue()))
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
        System.out.println(finalExecutedData1.getFlowName());
        System.out.println(finalExecutedData1.getStepExecutionDataList().size());
        Platform.runLater(() -> masterDetailController.updateFlowLabel(finalExecutedData1));

        masterDetailController.getFlowExecutionTabController().getMainController().goToStatisticsTab();
        return Boolean.TRUE;
    }
}

