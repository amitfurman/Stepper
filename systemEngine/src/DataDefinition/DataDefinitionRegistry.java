package DataDefinition;

import DataDefinition.api.DataDefinitions;
import DataDefinition.impl.list.FileListDataDefinition;
import DataDefinition.impl.list.StringListDataDefinition;
import DataDefinition.impl.mapping.NumberMappingDataDefinition;
import DataDefinition.impl.numbers.DoubleDataDefinition;
import DataDefinition.impl.numbers.NumberDataDefinition;
import DataDefinition.impl.string.StringDataDefinition;
import DataDefinition.impl.file.FileDataDefinition;
import DataDefinition.impl.list.ListDataDefinition;
import DataDefinition.impl.mapping.MappingDataDefinition;
import DataDefinition.impl.relation.RelationDataDefinition;

public enum DataDefinitionRegistry implements DataDefinitions {
    STRING(new StringDataDefinition()),
    NUMBER(new NumberDataDefinition()),
    DOUBLE(new DoubleDataDefinition()),
    RELATION(new RelationDataDefinition()),
    LIST(new ListDataDefinition()),
    FILE(new FileDataDefinition()),
    FILE_LIST(new FileListDataDefinition()),
    STRING_LIST(new StringListDataDefinition()),
    MAPPING(new MappingDataDefinition()),
    MAPPING2NUMBERS(new NumberMappingDataDefinition())
    ;

    private final DataDefinitions dataDefinition;

    DataDefinitionRegistry(DataDefinitions dataDefinition) {
        this.dataDefinition = dataDefinition;
    }

    @Override
    public String getName() {
        return dataDefinition.getName();
    }

    @Override
    public boolean isUserFriendly() {
        return dataDefinition.isUserFriendly();
    }

    @Override
    public Class<?> getType() {
        return dataDefinition.getType();
    }
}