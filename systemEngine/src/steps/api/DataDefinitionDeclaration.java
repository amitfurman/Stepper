package steps.api;

import datadefinition.api.DataDefinitions;

public interface DataDefinitionDeclaration {
    String getName();
    DataNecessity necessity();
    String userString();
    DataDefinitions dataDefinition();
}
