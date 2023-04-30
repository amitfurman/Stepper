package datadefinition.impl.numbers;

import datadefinition.api.AbstractDataDefinition;
public class DoubleDataDefinition extends AbstractDataDefinition {
    public DoubleDataDefinition() {
        super("Double", true, Double.class);
    }
}