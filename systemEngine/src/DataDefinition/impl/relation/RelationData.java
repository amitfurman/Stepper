package DataDefinition.impl.relation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RelationData {

    private List<String> columns;
    private List<SingleRow> rows;

    public RelationData(List<String> columns) {
        this.columns = columns;
        rows = new ArrayList<>();
    }

    public int numOfColumns() { return columns.size();}

    public int numOfRows() {return rows.size();}

    public List<String> getRowDataByColumnsOrder(int rowId) {
        return new ArrayList<>();
    }

    public void addRow(List<String> list) {
        SingleRow row = new SingleRow();
        for (int i=0;i < columns.size();i++) {
            row.addData(columns.get(i), list.get(i));
        }
        rows.add(row);
    }

    public List<String> getColumns() {
        return columns;
    }

    public boolean isEmpty() {
        return rows.isEmpty();
    }


        private static class SingleRow {
        private Map<String, String> data;

        public SingleRow() {
            data = new HashMap<>();
        }

        public void addData(String columnName, String value) {
            data.put(columnName, value);
        }
    }
}
