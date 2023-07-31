package statistic;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class StatisticData implements Serializable {
    private String name;
    private int timesRun;
    private Duration totalTime;

    public StatisticData(String name) {
        this.name = null;
        this.timesRun = 1 ;
        this.totalTime = Duration.ZERO;
    }

    public StatisticData(String name , Duration totalTime) {
        this.name = name;
        this.timesRun = 1 ;
        this.totalTime = totalTime;
    }

    public String getName() {
        return name;
    }
    public int getTimesRun() {
        return timesRun;
    }
    public Duration getTotalTime() {return totalTime; }
    public double getAverageTime() {
        return timesRun == 0 ? 0 : totalTime.toMillis() / timesRun;
    }
    public void incrementTimesRun() {
        timesRun++;
    }
    public void addToTotalTime(Duration time) {
        totalTime = totalTime.plus(time);
    }
}
