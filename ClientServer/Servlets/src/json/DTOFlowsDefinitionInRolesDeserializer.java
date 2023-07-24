package json;

import com.google.gson.*;
import dto.DTOFlowDefinitionInRoles;
import dto.DTOFlowsDefinitionInRoles;

import java.lang.reflect.Type;
import java.util.HashSet;
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

                    // Extract and build each individual DTOFlowDefinitionInRoles object
                    DTOFlowDefinitionInRoles dtoFlowDefinitionInRoles = new DTOFlowDefinitionInRoles(
                            flowName, description, numberOfSteps, numberOfFreeInputs, numberOfContinuations);

                    // If there are more properties in the JSON that need to be deserialized, do so here

                    flowsDefinitionInRoles.add(dtoFlowDefinitionInRoles);
                }
            }
        }

        DTOFlowsDefinitionInRoles dtoFlowsDefinitionInRoles = new DTOFlowsDefinitionInRoles(flowsDefinitionInRoles);
        return dtoFlowsDefinitionInRoles;
    }
}


/*public class DTOFlowsDefinitionInRolesDeserializer implements JsonDeserializer<DTOFlowDefinitionInRoles> {
    @Override
    public DTOFlowDefinitionInRoles deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        // extract raw data
        String name = json.getAsJsonObject().get("flowName").getAsString();
        String description = json.getAsJsonObject().get("description").getAsString();
        Integer numberOfSteps = json.getAsJsonObject().get("numberOfSteps").getAsInt();
        Integer numberOfFreeInputs = json.getAsJsonObject().get("numberOfFreeInputs").getAsInt();
        Integer numberOfContinuations = json.getAsJsonObject().get("numberOfContinuations").getAsInt();


        // build object manually
        DTOFlowDefinitionInRoles result = new DTOFlowDefinitionInRoles(name, description, numberOfSteps, numberOfFreeInputs, numberOfContinuations);


        // determine manually the interface concrete class
        WindBlower windBlower = new WindBlower();
        windBlower.setWindPower(windPower);

        // or tell GSON exactly which object you would like to construct a specific object from the (sub) object
        // that was originally declared through an interface reference
        //WindBlower windBlower = context.deserialize(json.getAsJsonObject().get("windBlower"), WindBlower.class);

        result.setWindBlower(windBlower);

        return result;
    }*/
