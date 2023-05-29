package javafx.flowExecutionTab;


import datadefinition.api.DataDefinitions;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Input {
    private final StringProperty finalName = new SimpleStringProperty();
    private final StringProperty originalName = new SimpleStringProperty();
    private final StringProperty stepName = new SimpleStringProperty();
    private final StringProperty mandatory = new SimpleStringProperty();
    private  Object value ;//= new SimpleObjectProperty<>();
    private DataDefinitions type;

    public StringProperty finalNameProperty() {
        return finalName;
    }

    public String getFinalName() {
        return finalName.get();
    }

    public void setFinalName(String name) {
        this.finalName.set(name);
    }

    public StringProperty originalNameProperty() {
        return originalName;
    }

    public String getOriginalName() {
        return originalName.get();
    }

    public void setOriginalName(String name) {
        this.originalName.set(name);
    }

    public StringProperty stepNameProperty() {
        return stepName;
    }

    public String getStepName() { return stepName.get();}

    public void setStepName(String name) {
        this.stepName.set(name);
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
/*
    public ObjectProperty<Object> valueProperty() {
        return value;
    }

    public Object getValue() {
        return value.get();
    } */

    public Object getValue() {
       return this.value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public DataDefinitions getType() {
        return type;
    }

    public void setType(DataDefinitions type) {
        this.type = type;
    }
}

