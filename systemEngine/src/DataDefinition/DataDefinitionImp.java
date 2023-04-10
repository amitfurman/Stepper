package DataDefinition;

public class DataDefinitionImp<T> implements DataDefinitions {
    private T value;

    public DataDefinitionImp() {
    }

    public DataDefinitionImp(T value) {
        setValue(value);
    }
    @Override
    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public String userPresentation() {
        return null;
    }

    @Override
    public boolean isUserFriendly() {
        return false;
    }

    @Override
    public String name() {
        return "DataDefinitionImp<T>";
    }

}
