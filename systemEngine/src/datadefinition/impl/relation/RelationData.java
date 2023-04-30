package datadefinition.impl.relation;
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
    public List<SingleRow> getRows() {
        return rows;
    }
    public void addRow(List<String> list) {
        SingleRow row = new SingleRow();
        for (int i=0;i < columns.size();i++) {
            row.addData(columns.get(i), list.get(i));
        }
        rows.add(row);
    }
    public List<String> getRowDataByColumnsOrder(int rowId) {
        return new ArrayList();
    }
    public List<String> getColumns() {
        return columns;
    }
    public boolean isEmpty() {
        return rows.isEmpty();
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\tThe column names are:" + System.lineSeparator());
        sb.append("\t");
        for (String column : columns) {
            sb.append(column);
            sb.append("   ");
        }
        sb.append(System.lineSeparator());
        sb.append("\tThe number of rows: " + rows.size());

        return sb.toString();
    }
    public static class SingleRow {
        private Map<String, String> data;
        public Map<String, String> getRowData() {
            return data;
        }
        public SingleRow() {
            data = new HashMap<>();
        }
        public void addData(String columnName, String value) {
            data.put(columnName, value);
        }
        public Map<String, String> getMap() {
            return this.data;
        }
    }
}
