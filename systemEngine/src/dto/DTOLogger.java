package dto;

import steps.api.Logger;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

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
