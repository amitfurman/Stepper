package systemengine;

import dto.*;
import flow.api.FlowDefinition;
import flow.api.FlowIO.SingleFlowIOData;
import flow.execution.FlowExecution;
import flow.execution.runner.FlowExecutor;
import jaxb.schema.SchemaBasedJAXBMain;
import statistic.FlowAndStepStatisticData;
import steps.api.DataNecessity;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class systemengineImpl implements systemengine{

    public List<FlowDefinition> flowDefinitionList;

    public LinkedList<FlowExecution> FlowExecutionList;

    public FlowAndStepStatisticData statisticData;

   // public FlowExecution optionalFlowExecution;

    public systemengineImpl() {
        this.flowDefinitionList = new LinkedList<>();
        this.FlowExecutionList = new LinkedList<>();
        this.statisticData = new FlowAndStepStatisticData();
    }

    @Override
    public void cratingFlowFromXml(String filePath){
        SchemaBasedJAXBMain schema = new SchemaBasedJAXBMain();
        flowDefinitionList = schema.schemaBasedJAXB(filePath);
    }

    public DTOFlowsNames printFlowsName() {
        int index = 1;
        StringBuilder flowData = new StringBuilder();
        flowData.append("Flows Names: " + '\n');
        for (FlowDefinition flow : flowDefinitionList) {
            flowData.append(index + ". " + flow.getName() + '\n');
            index++;
        }
        return new DTOFlowsNames(flowData);
    }

    @Override
    public List<FlowDefinition> getFlowDefinitionList() {
        return flowDefinitionList;
    }

    public DTOFlowDefinition IntroduceTheChosenFlow(int flowNumber) {
        FlowDefinition flow = flowDefinitionList.get(flowNumber - 1);
        return new DTOFlowDefinitionImpl(flow);
    }

    @Override
    public boolean hasAllMandatoryInputs(int flowChoice, Map<String, Object> freeInputMap) {
        for (SingleFlowIOData input : flowDefinitionList.get(flowChoice-1).getFlowFreeInputs()) {
            boolean found = freeInputMap.keySet().stream().anyMatch(key -> key.equals(input.getFinalName()));
            if(!found && input.getNecessity().equals(DataNecessity.MANDATORY)){
                return false;
            }
        }
        return true;
    }

    @Override
    public DTOFlowExecution activateFlow(int flowChoice, DTOFreeInputsFromUser freeInputs) {
        FlowExecutor flowExecutor = new FlowExecutor();
        FlowDefinition currFlow = flowDefinitionList.get(flowChoice-1);

        FlowExecution flowExecution = new FlowExecution(currFlow);
        flowExecutor.executeFlow(flowExecution, freeInputs, statisticData);
        FlowExecutionList.addFirst(flowExecution);

        return new DTOFlowExecutionImpl(flowExecution);
    }
    @Override
    public DTOFreeInputsByUserString printFreeInputsByUserString(int choice) {
        AtomicInteger freeInputsIndex = new AtomicInteger(1);
        StringBuilder freeInputsData = new StringBuilder();
        freeInputsData.append("*The free inputs in the current flow: *\n");
        flowDefinitionList.get(choice-1)
                .getFlowFreeInputs()
                .stream()
                .forEach(node -> {
                    freeInputsData.append("Free Input " + freeInputsIndex.getAndIncrement() + ": ");
                    freeInputsData.append(String.format("Input Name: %s(%s)" ,node.getUserString(), node.getFinalName()));
                    freeInputsData.append("\tMandatory/Optional: " + node.getNecessity() + "\n");
                });
        return new DTOFreeInputsByUserString(freeInputsData);
    }

    @Override
    public DTOSingleFlowIOData getSpecificFreeInput(int flowChoice, int freeInputChoice) {
        return new DTOSingleFlowIODataImpl(flowDefinitionList.get(flowChoice-1).getFlowFreeInputs().get(freeInputChoice-1));
    }

}

///////
/*
    public static DTOFlowDefinitionData IntroduceTheChosenFlow(int flowNumber) {
        DTOFlowDefinition flow = allStepperFlows.getAllFlows().get(flowNumber - 1);

        StringBuilder flowData = new StringBuilder();
        flowData.append(printFlowsName(allStepperFlows));
        flowData.append("Flow Description: " + flow.getDescription() + '\n');
        flowData.append("Flow Formal Outputs: " + String.join(", ", flow.getFlowFormalOutputs()) + '\n');
        flowData.append("Is The Flow Read Only? " + flow.getFlowReadOnly() + "\n\n");
        flowData.append(printStepsInfo(flow));
        flowData.append(printFreeInputsInfo(flow));
        flowData.append(printFlowOutputs(flow));

        System.out.println(flowData);
    }
*/
