package steps.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jayway.jsonpath.JsonPath;
import datadefinition.DataDefinitionRegistry;
import flow.execution.context.StepExecutionContext;
import net.minidev.json.JSONArray;
import steps.api.AbstractStepDefinition;
import steps.api.DataDefinitionDeclarationImpl;
import steps.api.DataNecessity;
import steps.api.StepResult;

import java.time.Instant;

public class JsonDataExtractor extends AbstractStepDefinition {
    JsonDataExtractor() {
        super("Json Data Extractor", true);

        // step inputs
        addInput(new DataDefinitionDeclarationImpl("JSON", DataNecessity.MANDATORY, "Json source", DataDefinitionRegistry.JSON));
        addInput(new DataDefinitionDeclarationImpl("JSON_PATH", DataNecessity.MANDATORY, "data",DataDefinitionRegistry.STRING));

        // step outputs
        addOutput(new DataDefinitionDeclarationImpl("VALUE", DataNecessity.NA, "Data value", DataDefinitionRegistry.STRING));
    }

    @Override
    public StepResult invoke(StepExecutionContext context) {
        Instant start = Instant.now();
        String json = context.getDataValue("JSON", String.class); //need to be JSON?
        String jsonPath = context.getDataValue("JSON_PATH", String.class);

        try {
            // Parse the JSON source string
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonElement = jsonParser.parse(json);

            // Extract values based on the JSON path
            Object result = JsonPath.read(jsonElement.toString(), jsonPath);

            if (result == null) {
                // No value found for the JSON path
                String logMessage = "No value found for json path " + jsonPath;
                context.storeLogLineAndSummaryLine(logMessage);
                context.storeStepTotalTime(start);
                return StepResult.SUCCESS;
            }

            if (result instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) result;
                // Concatenate the extracted values with ","
                String concatenatedValues = String.join(",", jsonArray.toJSONString());
                context.storeDataValue("VALUE", concatenatedValues);
            } else {
                context.storeDataValue("VALUE", result.toString());
            }

            // Log the extraction details
            context.storeLogLineAndSummaryLine("Extracting data " + jsonPath + ". Value: " + result);
            context.storeStepTotalTime(start);
            return StepResult.SUCCESS;
        } catch (Exception e) {
            context.storeLogLineAndSummaryLine("Failed to extract data. Error: " + e.getMessage());
            context.storeStepTotalTime(start);
            return StepResult.FAILURE;
        }
    }
}
