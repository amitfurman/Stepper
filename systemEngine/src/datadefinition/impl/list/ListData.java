package datadefinition.impl.list;

import java.io.Serializable;
import java.util.List;

public class ListData<T> implements Serializable {
    private List<T> list;
    public ListData(List<T> source) {
        this.list = source;
    }
    public List<T> getItems() {
        return list;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        int counter = 1;
        for (T element : this.list) {
            sb.append(counter++);
            sb.append(". ");
            sb.append(element.toString());
            sb.append(System.lineSeparator());
            sb.append("\t");
        }
        return sb.toString();
    }
}

