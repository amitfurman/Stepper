package javafx.executionsHistoryTab;
public class ExecutionHistoryEntry {
    private String flowName;
    private String startDate;
    private String runResult;
    public ExecutionHistoryEntry(String flowName, String startDate, String runResult) {
        this.flowName = flowName;
        this.startDate = startDate;
        this.runResult = runResult;
    }
    public String getFlowName() {
        return flowName;
    }
    public void setFlowName(String flowName) {this.flowName = flowName; }
    public String getStartDate() {return startDate;}
    public void setStartDate(String startDate) {this.startDate = startDate;}
    public String getRunResult() {
        return runResult;
    }
    public void setRunResult(String runResult) {
        this.runResult = runResult;
    }
}
