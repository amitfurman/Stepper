package datadefinition.impl.numbers;

import datadefinition.api.AbstractDataDefinition;
public class NumberDataDefinition extends AbstractDataDefinition{
    public NumberDataDefinition() {
        super("Number", true, Integer.class);
    }
}
