package DataDefinition;

public class DoubleDataDefinition implements DataDefinitions,NumericValue{
    private Double value;

    public DoubleDataDefinition(double value) {
        setValue(value);
    }

    public void setValue(double value) {
        this.value = value;
    }
    @Override
    public Double getValue() {
        return this.value;
    }

    @Override
    public boolean isNegative() {
        return (value<0) ? true : false;
    }

    @Override
    public String toString() {
       return "I am of type double and my value is" + value;
    }

    @Override
    public boolean isUserFriendly() {
        return true;
    }

    @Override
    public String name() {
        return "Double";
    }

    @Override
    public String userPresentation() {
        return String.valueOf(value);
    }
}
