package DataDefinition.impl.numbers;

import DataDefinition.api.AbstractDataDefinition;
public class NumberDataDefinition extends AbstractDataDefinition{
    public NumberDataDefinition() {
        super("Number", true, Integer.class);
    }
}
