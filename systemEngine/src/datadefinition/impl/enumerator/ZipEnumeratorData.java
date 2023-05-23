package datadefinition.impl.enumerator;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class ZipEnumeratorData implements Serializable {
    private Set<String> values;
    private String value;

    public ZipEnumeratorData() {
        values = new HashSet<>();
        values.add("ZIP");
        values.add("UNZIP");
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        if (values.contains(value)) {
            this.value = value;
        }
    }

}
