package DataDefinition;


import java.util.HashMap;
import java.util.Map;

public class MappingDataDefinition<K extends DataDefinitions, V extends DataDefinitions> implements DataDefinitions {
    private K car;
    private V cdr;

    public MappingDataDefinition(K car, V cdr) {
        this.car = car;
        this.cdr = cdr;
    }

    @Override
    public boolean isUserFriendly() {
        return false;
    }

    @Override
    public String name() {
        return "Mapping";
    }
    public K getCar() { return car;}
    public V getCdr() { return cdr;}
    public void setCar(K car) { this.car = car; }
    public void setCdr(V cdr) { this.cdr = cdr;}

    @Override
    public String userPresentation() {
        return String.format("car: %s\ncdr: %s", car.userPresentation(), cdr.userPresentation());
    }

    @Override
    public Object getValue() {
      return null;
        //  return new HashMap<>(getCar(), getCdr()) {};
    }
}
