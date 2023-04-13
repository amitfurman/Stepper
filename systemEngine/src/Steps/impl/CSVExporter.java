package Steps.impl;

import DataDefinition.DataDefinitionRegistry;
import DataDefinition.api.IO_NAMES;
import DataDefinition.impl.relation.RelationDataDefinition;
import Steps.api.AbstractStepDefinition;
import Steps.api.DataDefinitionDeclarationImpl;
import Steps.api.DataNecessity;
import Steps.api.StepResult;
import flow.execution.context.StepExecutionContext;

import javax.management.relation.Relation;
import java.io.File;

public class CSVExporter extends AbstractStepDefinition {

    public CSVExporter() {
        super("CSV Exporter", true);

        // step inputs
        addInput(new DataDefinitionDeclarationImpl("SOURCE", DataNecessity.MANDATORY, "Source data", DataDefinitionRegistry.RELATION));

        // step outputs
        addOutput(new DataDefinitionDeclarationImpl("RESULT", DataNecessity.NA, "CSV export result", DataDefinitionRegistry.STRING));
    }

    @Override
    public StepResult invoke(StepExecutionContext context) {
       Relation source = context.getDataValue(IO_NAMES.SOURCE, Relation.class);

        StringBuilder csvBuilder = new StringBuilder();

        // Checking if source data is empty
        if (source == null) {
            System.out.println("Warning: Source data is empty");
            // add summaryLine
            csvBuilder.append("COLUMN1, COLUMN2, COLUMN3\n");


           // context.storeDataValue("RESULT",  csvBuilder.append());
            return StepResult.WARNING;
        }
        else {
            // Appending column names to CSV
            csvBuilder.append("COLUMN1, COLUMN2, COLUMN3\n");
            // Logging total lines of data to be processed
            /*System.out.println("About to process " + source.size() + " lines of data before starting to work on the table");
            // Looping through source data and appending rows to CSV
            for (Map<String, String> row : source) {
                csvBuilder.append(row.get("COLUMN1")).append(", ").append(row.get("COLUMN2")).append(", ").append(row.get("COLUMN3")).append("\n");
            }*/
        }

        // Returning CSV string as result
        //return csvBuilder.toString();

        return null;
    }
}
