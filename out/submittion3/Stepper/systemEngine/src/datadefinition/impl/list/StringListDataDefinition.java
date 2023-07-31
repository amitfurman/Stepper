package datadefinition.impl.list;

import datadefinition.api.AbstractDataDefinition;

public class StringListDataDefinition extends AbstractDataDefinition {
    public StringListDataDefinition() {
        super("String List", false, StringListData.class);
    }
}
