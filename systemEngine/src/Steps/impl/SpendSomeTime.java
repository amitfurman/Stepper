package Steps.impl;

import DataDefinition.DataDefinitionRegistry;
import DataDefinition.api.IO_NAMES;
import DataDefinition.api.NumericValue;
import DataDefinition.impl.NumberDataDefinition;
import Steps.api.AbstractStepDefinition;
import Steps.api.DataDefinitionDeclarationImpl;
import Steps.api.DataNecessity;
import Steps.api.StepResult;
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
            System.out.println("Failed. Time to spend must be a positive number.");
            return StepResult.FAILURE;
        }

        // Log before sleeping
        System.out.println("About to sleep for " + timeToSpend + " seconds...");

        try{
            Thread.sleep(timeToSpend * 1000); }
        catch (InterruptedException e) {
            throw new RuntimeException(e);//check if needed here
        }

        // Log after sleeping
        System.out.println("Done sleeping...");

        // Return result
        return StepResult.SUCCESS;
    }
}


