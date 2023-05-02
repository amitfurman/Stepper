package statistic;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class FlowAndStepStatisticData  implements Serializable {
    private List<StatisticData> flowsStatisticData;
    private List<StatisticData> stepsStatisticData;

    public FlowAndStepStatisticData() {
        this.flowsStatisticData = new LinkedList<>();
        this.stepsStatisticData = new LinkedList<>();
    }

    public FlowAndStepStatisticData( List<StatisticData> flowsStatisticData, List<StatisticData> stepsStatisticData) {
        this.flowsStatisticData = new LinkedList<>(flowsStatisticData);
        this.stepsStatisticData = new LinkedList<>(stepsStatisticData);
    }

    public void addFlowToStatistic(StatisticData flow) {
        flowsStatisticData.add(flow);
    }
    public void addStepToStatistic(StatisticData step) {
        stepsStatisticData.add(step);
    }
    public List<StatisticData> getFlowsStatisticData() {
        return flowsStatisticData;
    }
    public List<StatisticData> getStepsStatisticData() {
        return stepsStatisticData;
    }

}
