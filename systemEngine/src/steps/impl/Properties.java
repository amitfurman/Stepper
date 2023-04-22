package steps.impl;

import datadefinition.DataDefinitionRegistry;
import datadefinition.api.IO_NAMES;
import datadefinition.impl.relation.RelationData;
import steps.api.AbstractStepDefinition;
import steps.api.DataDefinitionDeclarationImpl;
import steps.api.DataNecessity;
import steps.api.StepResult;
import flow.execution.context.StepExecutionContext;
import java.util.ArrayList;
import java.util.List;
public class Properties extends AbstractStepDefinition {
    public Properties() {
        super("Properties Exporter",true);
        // step inputs
        addInput(new DataDefinitionDeclarationImpl("SOURCE", DataNecessity.MANDATORY, "Source data", DataDefinitionRegistry.RELATION));

        // step outputs
        addOutput(new DataDefinitionDeclarationImpl("RESULT", DataNecessity.NA, "Properties export result", DataDefinitionRegistry.STRING));
    }

    @Override
    public StepResult invoke(StepExecutionContext context) {
        RelationData source = context.getDataValue(IO_NAMES.SOURCE, RelationData.class);
        StringBuilder properties = new StringBuilder();

        if (source.isEmpty()) {
            context.storeLogLine("Warning! The source data is empty.");
            context.storeDataValue("RESULT", null);
            return StepResult.WARNING;
        }

        context.storeLogLine("About to process " + source.numOfRows() + " lines of data");
        List<String> columns = source.getColumns();
        List<String> rowKeys = new ArrayList<>();
        for (int i = 0; i < source.numOfRows(); i++) {
            List<String> rowData = source.getRowDataByColumnsOrder(i);
            String rowKey = "row-" + (i + 1);
            rowKeys.add(rowKey);
            for (int j = 0; j < rowData.size(); j++) {
                String columnName = columns.get(j);
                String cellValue = rowData.get(j);
                String property = rowKey + "." + columnName + "=" + cellValue;
                properties.append(property).append("\n");
            }
        }
        context.storeLogLine("Extracted total of " + rowKeys.size());
        context.storeDataValue("RESULT",properties);
        return StepResult.SUCCESS;
    }
}
