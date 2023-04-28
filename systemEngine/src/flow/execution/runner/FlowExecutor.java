package flow.execution.runner;


import dto.DTOFreeInputsFromUser;
import flow.execution.FlowExecutionResult;
import statistic.FlowAndStepStatisticData;
import statistic.StatisticData;
import steps.api.StepResult;
import flow.api.StepUsageDeclaration;
import flow.execution.FlowExecution;
import flow.execution.context.StepExecutionContext;
import flow.execution.context.StepExecutionContextImpl;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

public class FlowExecutor {

    public void executeFlow(FlowExecution flowExecution , DTOFreeInputsFromUser freeInputs , FlowAndStepStatisticData flowStatisticData) {
        System.out.println("Starting execution of flow " + flowExecution.getFlowDefinition().getName() + " [ID: " + flowExecution.getUniqueId() + "]");
        Instant startTime = Instant.now();
        flowExecution.setStartTime(startTime);

        StepExecutionContext context = new StepExecutionContextImpl
                (flowExecution.getFlowDefinition().getName2DDMap(),flowExecution.getFlowDefinition().getOutputName2aliasMap(),
                        flowExecution.getFlowDefinition().getAlias2StepNameMap());

        ////??????
        for (Map.Entry<String, Object> entry : freeInputs.getFreeInputMap().entrySet()) {
            context.storeDataValue(entry.getKey(), entry.getValue());
        }

        // start actual execution
        for (int i = 0; i < flowExecution.getFlowDefinition().getFlowSteps().size(); i++) {
            context.setCurrInvokingStep(flowExecution.getFlowDefinition().getFlowSteps().get(i).getFinalStepName());
            StepUsageDeclaration stepUsageDeclaration = flowExecution.getFlowDefinition().getFlowSteps().get(i);
            StepResult stepResult = stepUsageDeclaration.getStepDefinition().invoke(context);

            if(stepResult.equals(StepResult.FAILURE)) {
                flowExecution.setFlowExecutionResult(FlowExecutionResult.FAILURE);
                if(!(stepUsageDeclaration.skipIfFail())) {break;}
            }
            else if (stepResult.equals(StepResult.WARNING)) {
                flowExecution.setFlowExecutionResult(FlowExecutionResult.WARNING);
            }
            else {
                flowExecution.setFlowExecutionResult(FlowExecutionResult.SUCCESS);
                StatisticData stepStatistic = new StatisticData(flowExecution.getFlowDefinition().getFlowSteps().get(i).getStepDefinition().name());
                stepStatistic.incrementTimesRun();
                stepStatistic.addToTotalTime(context.getCurrInvokingStep().getTotalStepTime());
                if(!flowStatisticData.getStepsStatisticData().contains(stepStatistic)) {
                    flowStatisticData.addStepToStatistic(stepStatistic);
                }
            }
            context.addCurrInvokingStepToLogsList();
        }

        ////statistics
        Instant endTime = Instant.now();
        flowExecution.setEndTime(endTime);
        flowExecution.setTotalTime(Duration.between(startTime, endTime));

        StatisticData currFlowStatistic = new StatisticData(flowExecution.getFlowDefinition().getName());
        flowStatisticData.addFlowToStatistic(currFlowStatistic);
        currFlowStatistic.incrementTimesRun();
        currFlowStatistic.addToTotalTime(Duration.between(startTime, endTime));

        flowExecution.setDataValues(context.getDataValues());
    }
}