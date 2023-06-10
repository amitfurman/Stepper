package steps.impl;

import datadefinition.DataDefinitionRegistry;
import datadefinition.api.IO_NAMES;
import datadefinition.impl.relation.RelationData;
import steps.api.AbstractStepDefinition;
import steps.api.DataDefinitionDeclarationImpl;
import steps.api.DataNecessity;
import steps.api.StepResult;
import flow.execution.context.StepExecutionContext;

import java.time.Instant;
import java.util.List;
import java.util.Map;

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
        Instant start = Instant.now();
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
            context.storeSummaryLine("The table is empty of content, so we converted only the column names of the table to the CSV format file.");
            context.storeDataValue("RESULT", csvBuilder.toString());
            context.storeStepTotalTime(start);
            return StepResult.WARNING;
        }

            // Write row data to CSV
        for (RelationData.SingleRow row :source.getRows()) {
            Map<String, String> rowData = row.getRowData();
            csvBuilder.append("\t");
            for (String column : columns) {
                csvBuilder.append(rowData.get(column)); // append column value
                csvBuilder.append(","); // append comma
            }
            csvBuilder.setLength(csvBuilder.length() - 1); // remove trailing comma
            csvBuilder.append("\n");
        }

        context.storeDataValue("RESULT", csvBuilder.toString());
        context.storeSummaryLine("The source data was converted successfully to the CSV export result");
        context.storeStepTotalTime(start);
        return StepResult.SUCCESS;
    }
}
