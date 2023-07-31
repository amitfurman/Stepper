package steps.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jayway.jsonpath.JsonPath;
import datadefinition.DataDefinitionRegistry;
import flow.execution.context.StepExecutionContext;
import javafx.collections.ObservableList;
import net.minidev.json.JSONArray;
import steps.api.AbstractStepDefinition;
import steps.api.DataDefinitionDeclarationImpl;
import steps.api.DataNecessity;
import steps.api.StepResult;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class JsonDataExtractor extends AbstractStepDefinition {
    public JsonDataExtractor() {
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
            String[] dataArray = jsonPath.split("\\|");
            StringBuilder result = new StringBuilder();
            StringBuilder jsonPathStr = new StringBuilder();

            for (String path : dataArray) {
                result.append(JsonPath.read(json, path.trim()).toString()).append(", ");
            }

/*
            String result = JsonPath.read(json, jsonPath);
*/

            if (result.toString().equals("")) {
                // No value found for the JSON path
                String logMessage = "No value found for json path " + jsonPath;
                context.storeLogLineAndSummaryLine(logMessage);
                context.storeStepTotalTime(start);
                return StepResult.SUCCESS;
            }

            context.storeDataValue("VALUE", result.toString());

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

/*            JsonParser jsonParser = new JsonParser();
            JsonElement jsonElement = jsonParser.parse(json);*/
/*
            JsonElement jsonElement = JsonParser.parseString(json);
*/


// Extract values based on the JSON path
/*
            Object result = JsonPath.read(jsonElement.getAsString(), jsonPath);
*/
/*
            List<String> results = new ArrayList<>();
            String[] paths = jsonPath.split("\\|");
            for (String path : paths) {
                JsonElement resultElement = JsonPath.read(jsonElement, path);
                if (resultElement != null) {
                    results.add(resultElement.getAsJsonPrimitive().getAsString());
                }
            }
            Object result = String.join(",", results);


            if (result == null) {
                // No value found for the JSON path
                String logMessage = "No value found for json path " + jsonPath;
                context.storeLogLineAndSummaryLine(logMessage);
                context.storeStepTotalTime(start);
                return StepResult.SUCCESS;
            }
*/

       /*     if (result instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) result;
                // Concatenate the extracted values with ","
                String concatenatedValues = String.join(",", jsonArray.toJSONString());
                context.storeDataValue("VALUE", concatenatedValues);
            } else {
                context.storeDataValue("VALUE", result.toString());
            }*/
