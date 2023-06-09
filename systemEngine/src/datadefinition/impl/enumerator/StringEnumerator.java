package datadefinition.impl.enumerator;

import java.util.HashSet;
import java.util.Set;

public class StringEnumerator {
    private final Set<String> enumerator = new HashSet<>();

    public Set<String> getEnumerator() {
        return this.enumerator;
    }

    public void add(String str) {
        enumerator.add(str);
    }
}