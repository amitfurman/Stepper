package dto;

import steps.api.Logger;
public class DTOLogger {
    private String log;
    private String logTime;

    public DTOLogger(Logger log) {
        this.log = log.getLog();
        logTime = log.getLogTime();
    }
    public String getLog() {
        return log;
    }
    public String getLogTime() { return logTime;}
}
