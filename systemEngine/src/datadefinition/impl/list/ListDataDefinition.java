package datadefinition.impl.list;

import datadefinition.api.AbstractDataDefinition;
public class ListDataDefinition extends AbstractDataDefinition {
    public ListDataDefinition() {
        super("List", false, ListData.class);
    }
}