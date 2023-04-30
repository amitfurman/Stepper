package dto;

import statistic.FlowAndStepStatisticData;
import statistic.StatisticData;

import java.util.LinkedList;
import java.util.List;

public class DTOFlowAndStepStatisticData {
    private List<DTOStatisticData> flowsStatisticData;
    private List<DTOStatisticData> stepsStatisticData;

    public DTOFlowAndStepStatisticData(FlowAndStepStatisticData statisticData ) {
        this.flowsStatisticData = new LinkedList<>();
        for (StatisticData flowStatisticData:statisticData.getFlowsStatisticData()) {
            flowsStatisticData.add(new DTOStatisticData(flowStatisticData));
        }
        this.stepsStatisticData = new LinkedList<>();
        for (StatisticData stepStatisticData:statisticData.getStepsStatisticData()) {
            stepsStatisticData.add(new DTOStatisticData(stepStatisticData));
        }
    }

    public List<DTOStatisticData> getFlowsStatisticData() {
        return flowsStatisticData;
    }
    public List<DTOStatisticData> getStepsStatisticData() {
        return stepsStatisticData;
    }


}
