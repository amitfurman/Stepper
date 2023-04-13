package DataDefinition;

import DataDefinition.api.DataDefinitions;
import DataDefinition.impl.DoubleDataDefinition;
import DataDefinition.impl.NumberDataDefinition;
import DataDefinition.impl.StringDataDefinition;
import DataDefinition.impl.file.FileDataDefinition;
import DataDefinition.impl.list.ListDataDefinition;
import DataDefinition.impl.mapping.MappingDataDefinition;
import DataDefinition.impl.relation.RelationDataDefinition;
import com.sun.org.apache.bcel.internal.generic.NEW;

public enum DataDefinitionRegistry implements DataDefinitions {
    STRING(new StringDataDefinition()),
    NUMBER(new NumberDataDefinition()),
    DOUBLE(new DoubleDataDefinition()),
    RELATION(new RelationDataDefinition()),
    LIST(new ListDataDefinition()),
    FILE(new FileDataDefinition()),
    MAPPING(new MappingDataDefinition())
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