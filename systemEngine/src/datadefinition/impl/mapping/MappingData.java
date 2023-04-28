package datadefinition.impl.mapping;

import java.util.Map;

public class MappingData<K,V> {
    private Map<K,V> map;

    public MappingData(Map<K,V> source) {
        this.map = source;
    }
    public Map<K,V> getItems() {
        return map;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("car:" + map.get(0).toString() + System.lineSeparator());
        sb.append("\tcdr:" + map.get(1).toString());

        return sb.toString();
    }
}
