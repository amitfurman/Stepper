package DataDefinition.impl.list;

import java.util.List;

public class ListData<T> {

    private List<T> list;
    public ListData(List<T> source) {
        this.list =source;
    }

    public List<T> getItems() {
        return list;
    }

/*    public String userPresentation() {
        StringBuilder sb = new StringBuilder();
        int counter = 1;
        for (DataDefinitions element : this.list) {
            sb.append(counter++);
            sb.append(". ");
            sb.append(element.getValue());
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }*/
}
