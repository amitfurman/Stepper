package DataDefinition.impl.mapping;


import DataDefinition.api.AbstractDataDefinition;
import DataDefinition.api.DataDefinitions;
import DataDefinition.impl.list.ListData;

public class MappingDataDefinition extends AbstractDataDefinition {

    public MappingDataDefinition() {
        super("Mapping", false, MappingData.class);
    }
}

