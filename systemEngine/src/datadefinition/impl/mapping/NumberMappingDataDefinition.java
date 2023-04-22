package datadefinition.impl.mapping;

import datadefinition.api.AbstractDataDefinition;

public class NumberMappingDataDefinition extends AbstractDataDefinition {
    public NumberMappingDataDefinition() {
        super("Numbers Mapping", false, NumberMappingData.class);
    }
}
