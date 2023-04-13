package DataDefinition.impl.mapping;

import DataDefinition.api.DataDefinitions;

import java.util.HashMap;
import java.util.Map;

public class MappingData {
    private Map<DataDefinitions , DataDefinitions> map;

    public MappingData(DataDefinitions car, DataDefinitions cdr) {
        map = new HashMap<>();
        map.put(car, cdr);
    }
}

/*@Override
public String userPresentation() {
    return String.format("car: %s\ncdr: %s", car.userPresentation(), cdr.userPresentation());
}

*/
