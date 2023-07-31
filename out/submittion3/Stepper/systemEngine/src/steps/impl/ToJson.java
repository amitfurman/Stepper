package steps.impl;
/*
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;*/
import com.google.gson.*;
import datadefinition.DataDefinitionRegistry;
import datadefinition.api.IO_NAMES;
import flow.execution.context.StepExecutionContext;
import steps.api.AbstractStepDefinition;
import steps.api.DataDefinitionDeclarationImpl;
import steps.api.DataNecessity;
import steps.api.StepResult;

import java.time.Instant;

public class ToJson extends AbstractStepDefinition {
    public ToJson() {
       super("To Json", true);

    // step inputs
    addInput(new DataDefinitionDeclarationImpl("CONTENT", DataNecessity.MANDATORY, "Content",DataDefinitionRegistry.STRING));

    // step outputs
    addOutput(new DataDefinitionDeclarationImpl("JSON", DataNecessity.NA, "Json representation", DataDefinitionRegistry.JSON));
    }

    @Override
    public StepResult invoke(StepExecutionContext context) {
        Instant start = Instant.now();
        String content = context.getDataValue(IO_NAMES.CONTENT, String.class);

        try {
            Gson gson = new Gson();
           // Object jsonObject = gson.fromJson(content, Object.class);
            context.storeLogLineAndSummaryLine("Content is JSON string. Converting it to JSON...");
           // JsonObject json = gson.toJsonTree(jsonObject).getAsJsonObject(); ////check
           // JsonObject object = JsonParser.parseString(content).getAsJsonObject();

            JsonParser parser = new JsonParser();
            JsonElement json = parser.parse(content);

            context.storeDataValue("JSON", json.toString());


        } catch (JsonSyntaxException e) {
            context.storeLogLineAndSummaryLine("Content is not a valid JSON representation");
            context.storeStepTotalTime(start);
            return StepResult.FAILURE;
        }
        context.storeStepTotalTime(start);
        return StepResult.SUCCESS;
    }

}
