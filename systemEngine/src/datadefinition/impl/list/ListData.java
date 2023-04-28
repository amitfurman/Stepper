package datadefinition.impl.list;

import datadefinition.api.DataDefinitions;

import java.util.List;

public class ListData<T> {

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

