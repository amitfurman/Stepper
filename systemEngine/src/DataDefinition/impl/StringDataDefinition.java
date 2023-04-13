package DataDefinition.impl;

import DataDefinition.api.AbstractDataDefinition;
public class StringDataDefinition extends AbstractDataDefinition {
    public StringDataDefinition() {
        super("String", true, String.class);
    }
}
