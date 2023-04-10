package DataDefinition;

public class StringDataDefinition implements DataDefinitions {
    private String data;

    public StringDataDefinition(String data) {
        setValue(data);
    }

    public void setValue(String data) {
        this.data = data;
    }

    @Override
    public String getValue() {
        return data;
    }

    public int stringLength() {
        return data.length();
    }

    public char specificCharFromString(int index) {
        return data.charAt(index);
    }

    public StringDataDefinition concat(StringDataDefinition other) {
        String result = data;
        result.concat(other.getValue());
        return new StringDataDefinition(result);
    }

    public boolean isTheObjectIsString(Object other) {
        if (!(other instanceof StringDataDefinition)) {
            return false;
        }
        StringDataDefinition s = (StringDataDefinition) other;
        if (stringLength() != s.stringLength()) {
            return false;
        }
        for (int i = 0; i < stringLength(); i++) {
            if (data.charAt(i) != s.specificCharFromString(i)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isUserFriendly() {
        return true;
    }

    @Override
    public String name() {
        return "String";
    }

    public String toString() {
        return data;
    }

    public char[] toCharArr() {
        char result[] = new char[data.length()];
        System.arraycopy(data, 0, result, 0, data.length());
        return result;
    }
    @Override
    public String userPresentation() {
        return String.valueOf(data);
    }


}
