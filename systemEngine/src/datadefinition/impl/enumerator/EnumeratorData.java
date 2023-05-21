package datadefinition.impl.enumerator;

import java.io.Serializable;

public class EnumeratorData implements Serializable {
    private String value;

    public EnumeratorData(String value) {
       setValue(value);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
