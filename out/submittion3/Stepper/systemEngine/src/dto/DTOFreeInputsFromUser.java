package dto;

import java.util.Map;

public class DTOFreeInputsFromUser {
    private Map<String, Object>  freeInputsMap;
    public DTOFreeInputsFromUser(Map<String, Object> freeInputsMap) {
        this.freeInputsMap = freeInputsMap;
    }
    public Map<String, Object> getFreeInputMap() {
        return freeInputsMap;
    }

}
