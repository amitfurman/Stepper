package Steps.api;

import DataDefinition.api.DataDefinitions;

public interface DataDefinitionDeclaration {
    String getName();
    DataNecessity necessity();
    String userString();
    DataDefinitions dataDefinition();
}
