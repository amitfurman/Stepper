package systemengine;

import dto.DTOFlowExecution;
import dto.DTOFlowExecutionImpl;
import dto.DTOFreeInputs;
import flow.api.FlowDefinition;
import flow.api.FlowDefinitionImpl;
import flow.execution.FlowExecution;
import flow.execution.runner.FlowExecutor;
import flow.impl.Stepper2Flows;

public class systemengineImpl implements systemengine{

    //public FlowDefinitionImpl currFlow;
    //public FlowExecution FlowExecutionList;

    //public StaticsData staticsData;

   // public FlowExecution optionalFlowExecution;

    public systemengineImpl() {
        //currFlow = new FlowDefinitionImpl();
    }

    @Override
    public DTOFlowExecution activateFlow(FlowDefinition currFlow, DTOFreeInputs freeInputs) {
        FlowExecutor flowExecutor = new FlowExecutor();
        FlowExecution flowExecution = new FlowExecution(currFlow);
        flowExecutor.executeFlow(flowExecution, freeInputs);

        return null;
    }
}
