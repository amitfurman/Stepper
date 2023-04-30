package dto;

import statistic.StatisticData;

import java.time.Duration;

public class DTOStatisticData {
    private String name;
    private int timesRun;
    private Duration totalTime;

    public DTOStatisticData(StatisticData statisticData) {
        this.name = statisticData.getName();
        this.timesRun = statisticData.getTimesRun();
        this.totalTime = statisticData.getTotalTime();
    }

    public String getName() {
        return name;
    }
    public int getTimesRun() {
        return timesRun;
    }
    public double getAverageTime() {
        return timesRun == 0 ? 0 : totalTime.toMillis() / timesRun;
    }
}
