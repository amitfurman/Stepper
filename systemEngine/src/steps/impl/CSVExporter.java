package steps.impl;

import datadefinition.DataDefinitionRegistry;
import datadefinition.api.IO_NAMES;
import datadefinition.impl.relation.RelationData;
import steps.api.AbstractStepDefinition;
import steps.api.DataDefinitionDeclarationImpl;
import steps.api.DataNecessity;
import steps.api.StepResult;
import flow.execution.context.StepExecutionContext;
import java.util.List;

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
       RelationData source = context.getDataValue(IO_NAMES.SOURCE, RelationData.class);
        StringBuilder csvBuilder = new StringBuilder();

        int totalLines = source.numOfRows() + 1; // Include header row?
        context.storeLogLine("About to process " + totalLines + " lines of data before starting to work on the table");

        // Write column names to CSV
        List<String> columns = source.getColumns();
        csvBuilder.append(String.join(",", columns));
        csvBuilder.append("\n");

        if (source.isEmpty()) {
            context.storeLogLine("Warning! Source data is empty");
            String summaryLine = "The table is empty of content, so we converted only the column names of the table to the CSV format file.";
            context.storeDataValue("RESULT", csvBuilder);
            return StepResult.WARNING;
        }

            // Write row data to CSV
        for (int rowId = 0; rowId < source.numOfRows(); rowId++) {
            List<String> rowData = source.getRowDataByColumnsOrder(rowId);
            if (!rowData.isEmpty()) {
                csvBuilder.append(String.join(",", rowData));
                csvBuilder.append("\n");
            }
        }

        context.storeDataValue("RESULT", csvBuilder);
        return StepResult.SUCCESS;
    }
}
