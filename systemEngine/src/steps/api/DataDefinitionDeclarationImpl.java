package steps.api;

import datadefinition.api.DataDefinitions;

public class DataDefinitionDeclarationImpl implements DataDefinitionDeclaration {

    private final String name;
    private final DataNecessity necessity;
    private final String userString;
    private final DataDefinitions dataDefinition;

    public DataDefinitionDeclarationImpl(String name, DataNecessity necessity, String userString, DataDefinitions dataDefinition) {
        this.name = name;
        this.necessity = necessity;
        this.userString = userString;
        this.dataDefinition = dataDefinition;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public DataNecessity necessity() {
        return necessity;
    }

    @Override
    public String userString() {
        return userString;
    }

    @Override
    public DataDefinitions dataDefinition() {
        return dataDefinition;
    }
}