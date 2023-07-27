package components.body.executionsHistoryTab;
public class AdminExecutionHistoryEntry {
    private String flowName;
    private String startDate;
    private String runResult;
    private String userName;


    public AdminExecutionHistoryEntry(String flowName, String startDate, String runResult, String userName) {
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
