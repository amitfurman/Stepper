package dto;

import java.util.Map;

public class DTOFreeInputs {
    private Map<String, Object>  freeInputsMap;

    public DTOFreeInputs(Map<String, Object> freeInputsMap) {
        this.freeInputsMap = freeInputsMap;
    }
    public Map<String, Object> getFreeInputMap() {
        return freeInputsMap;
    }

}
