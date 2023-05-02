package steps.api;

import java.io.Serializable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Logger implements Serializable {
    private String log;
    private LocalTime logTime;

    public Logger(String log) {
        this.log = log;
        this.logTime = LocalTime.now();
    }

    public String getLog() {
        return log;
    }

    public String getLogTime() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
        return logTime.format(format);
    }
}
