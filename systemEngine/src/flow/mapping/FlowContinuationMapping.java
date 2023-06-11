package flow.mapping;

import java.util.HashMap;
import java.util.Map;

public class FlowContinuationMapping {
    private Map<String,String> source2targetDataMapping; //The values were saved in the map like this: SourceData, TargetData
    private String sourceFlow;
    private String targetFlow;

    public FlowContinuationMapping(String sourceFlow, String targetFlow) {
        this.source2targetDataMapping = new HashMap<>();
        this.sourceFlow = sourceFlow;
        this.targetFlow = targetFlow;
    }
    public void addToSource2targetDataMapping(String sourceDataName, String targetDataName) {
        this.source2targetDataMapping.put(sourceDataName,targetDataName);
    }
    public void setSourceFlow(String sourceFlow) {this.sourceFlow = sourceFlow;}
    public void setTargetFlow(String targetFlow) {
        this.targetFlow = targetFlow;
    }
    public Map<String,String> getSource2targetDataMapping() {
        return source2targetDataMapping;
    }
    public String getSourceFlow() {
        return sourceFlow;
    }
    public String getTargetFlow() {
        return targetFlow;
    }
}
