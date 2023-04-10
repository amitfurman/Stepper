package DataDefinition;

public class NumberDataDefinition implements DataDefinitions, NumericValue{
    private Integer value;

    public NumberDataDefinition(int value) {
        setValue(value);
    }

    public void setValue(int value) {
        this.value = value;
    }
    @Override
    public Integer getValue() {
        return this.value;
    }
    @Override
    public boolean isNegative() {
        return (value<0) ? true : false;
    }

    public boolean isEqualToZero()
    {
        return (value==0) ? true : false;
    }

    @Override
    public boolean isUserFriendly() {
        return true;
    }

    @Override
    public String name() {
        return "Number";
    }

    @Override
    public String toString() {
        return "I am of type number and my value is" + value;
    }


    @Override
    public String userPresentation() {
        return String.valueOf(value);
    }
}
