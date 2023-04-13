package DataDefinition.impl.list;

import DataDefinition.api.DataDefinitions;
import java.util.ArrayList;
import java.util.List;

public class ListData {
    public List<DataDefinitions> list;
    public ListData() {
        this.list = new ArrayList<>();
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
