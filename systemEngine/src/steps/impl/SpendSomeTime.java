package steps.impl;

import datadefinition.DataDefinitionRegistry;
import datadefinition.api.IO_NAMES;
import steps.api.AbstractStepDefinition;
import steps.api.DataDefinitionDeclarationImpl;
import steps.api.DataNecessity;
import steps.api.StepResult;
import flow.execution.context.StepExecutionContext;

public class SpendSomeTime extends AbstractStepDefinition {

    public SpendSomeTime () {
        super("Spend Some Time", true);
        //step input
        addInput(new DataDefinitionDeclarationImpl("TIME_TO_TIME_TO_SPEND", DataNecessity.MANDATORY, "Total sleeping time (sec)", DataDefinitionRegistry.NUMBER));
    }

    @Override
    public StepResult invoke(StepExecutionContext context) {
        int timeToSpend = context.getDataValue(IO_NAMES.TIME_TO_SPEND, Integer.class);

        if (timeToSpend <= 0) {
            //add log and summaryLine
            context.storeLogLine("Step failed. The total sleeping time must be a positive number.");
            return StepResult.FAILURE;
        }

        // Log before sleeping
        context.storeLogLine("About to sleep for " + timeToSpend + " seconds...");

        try{ Thread.sleep(timeToSpend * 1000); }
        catch (InterruptedException e) { throw new RuntimeException(e); }

        // Log after sleeping
        context.storeLogLine("Done sleeping...");

        // Return result
        return StepResult.SUCCESS;
    }
}


