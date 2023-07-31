package dto;

import java.util.HashMap;
import java.util.Map;

public class DTOContinuationMapping {
    private Map<String,String> source2targetDataMapping; //The values were saved in the map like this: SourceData, TargetData
    private String sourceFlow;
    private String targetFlow;

    public DTOContinuationMapping(Map<String,String> source2targetDataMapping ,String sourceFlow, String targetFlow) {
            this.source2targetDataMapping = source2targetDataMapping;
            this.sourceFlow = sourceFlow;
            this.targetFlow = targetFlow;
        }
        public Map<String,String> getSource2targetDataMapping() {return source2targetDataMapping;}
        public String getSourceFlow() {return sourceFlow;}
        public String getTargetFlow() {return targetFlow;}
}
