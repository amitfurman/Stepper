package DataDefinition;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ListDataDefinition<F> implements DataDefinitions, Iterable<F> {

    private List<DataDefinitions> list;

    public ListDataDefinition() {
        this.list = new ArrayList<>();
    }

/*    public ListDataDefinition(ListDataDefinition<F> originList) {
        this(); // call default constructor to create a new instance
        for (DataDefinitions element : originList.getList()) {
            // make a copy of each element and add it to the new instance
            FileDataDefinition copy = element.makeCopy();
            list.add(copy);
        }
    }*/

    @Override
    public boolean isUserFriendly() {
        return false;
    }

    @Override
    public String name() {
        return "List";
    }

    @Override
    public String toString() {
        return "I am of type list and my elements is" + userPresentation();
    }

    @Override
    public String userPresentation() {
        StringBuilder sb = new StringBuilder();/**/
        int counter = 1;
        for (DataDefinitions element : this.list) {
            sb.append(counter++);
            sb.append(". ");
            sb.append(element.getValue());
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

    @Override
    public DataDefinitions getValue() {
        return list.get(0);
    }

    public void addElement(DataDefinitions element) {
        this.list.add(element);
    }

    public DataDefinitions getElement(int index) {
        return this.list.get(index);
    }

    public int numberOfElements() {
        return list.size();
    }

    public List<DataDefinitions> getList() {
        return this.list;
    }

    @Override
    public Iterator iterator() {
        return this.list.iterator();
    }
    public DataDefinitions next() {
        return this.list.iterator().next();
    }

    public boolean deleteElement(DataDefinitions element) {
         return this.list.remove(element);
    }

    public boolean deleteElementByVal(DataDefinitions element) {
        return this.list.remove(String.valueOf(element.getValue()));
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }
}
