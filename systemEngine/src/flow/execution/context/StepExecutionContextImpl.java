package flow.execution.context;

import DataDefinition.api.DataDefinitions;
import DataDefinition.api.IO_NAMES;
import flow.execution.context.StepExecutionContext;

import java.util.HashMap;
import java.util.Map;

public class StepExecutionContextImpl implements StepExecutionContext {

    private final Map<String, Object> dataValues;

    public StepExecutionContextImpl() {
        dataValues = new HashMap<>();
    }

    ///////////overview the exception
    @Override
    public <T> T getDataValue(String dataName, Class<T> expectedDataType) {
        //return the data definition from the name
        DataDefinitions theExpectedDataDefinition = IO_NAMES.name2DataDefinition.get(dataName);

        if (expectedDataType.isAssignableFrom(theExpectedDataDefinition.getType())) {
            Object aValue = dataValues.get(dataName);
            if (aValue != null) {
                // If the value exists, cast and return it
                return expectedDataType.cast(aValue);
            } else {
                // If the value does not exist, throw an exception or return a default value as needed
                // For example, throw an exception:
                throw new NullPointerException("Data value for " + dataName + " is null.");
            }
        }
        else {
            // error handling of some sort...
            // If the data definition is not found or expected data type is not compatible, throw an exception or handle the error as needed
            // For example, throw an exception:
            throw new IllegalArgumentException("Data definition for " + dataName + " is not found or expected data type is not compatible.");
        }

    }

    @Override
    public boolean storeDataValue(String dataName, Object value) {
        // assuming that from the data name we can get to its data definition
        DataDefinitions theData = null;

        // we have the DD type so we can make sure that its from the same type
        if (theData.getType().isAssignableFrom(value.getClass())) {
            dataValues.put(dataName, value);
        } else {
            // error handling of some sort...
        }

        return false;
    }


}