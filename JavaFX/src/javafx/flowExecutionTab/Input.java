package javafx.flowExecutionTab;


import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Input {
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty mandatory = new SimpleStringProperty();
    private final ObjectProperty value = new SimpleObjectProperty();

    public StringProperty nameProperty() {
        return name;
    }
    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty mandatoryProperty() {
        return mandatory;
    }
    public String getMandatory() {
        return mandatory.get();
    }

    public void setMandatory(String mandatory) {
        this.mandatory.set(mandatory);
    }

    public ObjectProperty valueProperty() {
        return value;
    }
    public Object getValue() {
        return value.get();
    }

    public void setValue(Object value) {
        this.value.set(value);
    }

}
