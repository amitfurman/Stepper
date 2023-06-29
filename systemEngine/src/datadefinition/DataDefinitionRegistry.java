package datadefinition;

import datadefinition.api.DataDefinitions;
import datadefinition.impl.enumerator.zipper.ZipEnumeratorDataDefinition;
import datadefinition.impl.json.JsonDataDefinition;
import datadefinition.impl.list.FileListDataDefinition;
import datadefinition.impl.list.StringListDataDefinition;
import datadefinition.impl.mapping.NumberMappingDataDefinition;
import datadefinition.impl.numbers.DoubleDataDefinition;
import datadefinition.impl.numbers.NumberDataDefinition;
import datadefinition.impl.string.StringDataDefinition;
import datadefinition.impl.file.FileDataDefinition;
import datadefinition.impl.list.ListDataDefinition;
import datadefinition.impl.mapping.MappingDataDefinition;
import datadefinition.impl.relation.RelationDataDefinition;

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
    MAPPING2NUMBERS(new NumberMappingDataDefinition()),
    ENUMERATOR(new ZipEnumeratorDataDefinition()),
    JSON(new JsonDataDefinition()),
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