package json;

import com.google.gson.*;
import dto.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DTOFlowsDefinitionInRolesDeserializer implements JsonDeserializer<DTOFlowsDefinitionInRoles> {
    @Override
    public DTOFlowsDefinitionInRoles deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Set<DTOFlowDefinitionInRoles> flowsDefinitionInRoles = new HashSet<>();

        if (json.isJsonObject()) {
            JsonObject jsonObject = json.getAsJsonObject();
            JsonArray flowsArray = jsonObject.getAsJsonArray("flowsDefinitionInRoles");

            for (JsonElement element : flowsArray) {
                if (element.isJsonObject()) {
                    JsonObject flowObject = element.getAsJsonObject();
                    String flowName = flowObject.get("flowName").getAsString();
                    String description = flowObject.get("description").getAsString();
                    Integer numberOfSteps = flowObject.get("numberOfSteps").getAsInt();
                    Integer numberOfFreeInputs = flowObject.get("numberOfFreeInputs").getAsInt();
                    Integer numberOfContinuations = flowObject.get("numberOfContinuations").getAsInt();
                    Boolean isReadOnly = flowObject.get("isReadOnly").getAsBoolean();
                    JsonArray flowFormalOutputsArray = flowObject.getAsJsonArray("flowFormalOutputs");
                    List<String> flowFormalOutputs = new ArrayList<>();
                    flowFormalOutputsArray.forEach(elem -> flowFormalOutputs.add(elem.getAsString()));

                    JsonArray stepsArray = flowObject.getAsJsonArray("stepsInFlow");
                    List<DTOStepUsageDeclaration> flowSteps = new ArrayList<>();

                    stepsArray.forEach(obj -> {
                        JsonObject stepObject = obj.getAsJsonObject();
                        Gson gson = new Gson();
                        DTOStepUsageDeclaration dtoStepUsageDeclaration = gson.fromJson(stepObject, DTOStepUsageDeclaration.class);
                        flowSteps.add(dtoStepUsageDeclaration);
                    });

                    JsonArray outputsArray = flowObject.getAsJsonArray("allOutputs");
                    List<DTOFlowOutputs> flowOutputs = new ArrayList<>();

                    outputsArray.forEach(object -> {
                        JsonObject outputObject = object.getAsJsonObject();
                        Gson gson = new Gson();
                        DTOFlowOutputs dtoFlowOutputs = gson.fromJson(outputObject, DTOFlowOutputs.class);
                        flowOutputs.add(dtoFlowOutputs);
                    });
                    JsonArray freeInputsArray = flowObject.getAsJsonArray("freeInputs");
                    List<DTOFreeInputs> flowFreeInputs = new ArrayList<>();

                    freeInputsArray.forEach(input -> {
                        JsonObject freeInputsObject = input.getAsJsonObject();
                        Gson gson = new Gson();
                        DTOFreeInputs dtoFlowFreeInputs = gson.fromJson(freeInputsObject, DTOFreeInputs.class);
                        flowFreeInputs.add(dtoFlowFreeInputs);
                    });

                    DTOFlowDefinitionInRoles dtoFlowDefinitionInRoles = new DTOFlowDefinitionInRoles(
                            flowName, description, numberOfSteps, numberOfFreeInputs, numberOfContinuations, isReadOnly, flowFormalOutputs,
                            flowSteps, flowOutputs,flowFreeInputs);

                    flowsDefinitionInRoles.add(dtoFlowDefinitionInRoles);
                }
            }
        }

        DTOFlowsDefinitionInRoles dtoFlowsDefinitionInRoles = new DTOFlowsDefinitionInRoles(flowsDefinitionInRoles);
        return dtoFlowsDefinitionInRoles;
    }
}


