package DataDefinition.impl.mapping;

import DataDefinition.api.AbstractDataDefinition;

public class NumberMappingDataDefinition extends AbstractDataDefinition {
    public NumberMappingDataDefinition() {
        super("Numbers Mapping", false, NumberMappingData.class);
    }
}
