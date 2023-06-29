package datadefinition.impl.json;

import datadefinition.api.AbstractDataDefinition;

public class JsonDataDefinition  extends AbstractDataDefinition {
    public JsonDataDefinition() {
        super("Json", true, String.class);
    }
}
