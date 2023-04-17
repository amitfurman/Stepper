package DataDefinition.impl.mapping;

import java.util.Map;

public class MappingData<K,V> {
    private Map<K,V> map;

    public MappingData(Map<K,V> source) {
        this.map = source;
    }
    public Map<K,V> getItems() {
        return map;
    }
}
