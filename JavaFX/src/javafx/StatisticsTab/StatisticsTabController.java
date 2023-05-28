package javafx.StatisticsTab;

import dto.DTOFlowAndStepStatisticData;
import dto.DTOStatisticData;
import javafx.Controller;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;

import java.util.List;

public class StatisticsTabController {

    private Controller mainController;
    @FXML
    private BarChart FlowNumberOfRunsChart;
    @FXML
    private BarChart StepNumberOfRunsChart;
    @FXML
    private BarChart AverageFlowsRunTimeChart;
    @FXML
    private BarChart AverageStepsRunTimeChart;

    public void setMainController(Controller mainController) {
        this.mainController = mainController;
    }

    public void initCharts(DTOFlowAndStepStatisticData statisticData) {
        System.out.println(statisticData.getFlowsStatisticData().size());
        System.out.println(statisticData.getStepsStatisticData().size());

        initFlowsChart(statisticData.getFlowsStatisticData());
        initStepsChart(statisticData.getStepsStatisticData());
    }

    public void initFlowsChart(List<DTOStatisticData> flowsStatisticData) {
        FlowNumberOfRunsChart.getData().clear();
        AverageFlowsRunTimeChart.getData().clear();

        XYChart.Series series1 = new XYChart.Series();
        flowsStatisticData.stream().forEach(flowStatisticData -> {
            series1.getData().add(new XYChart.Data(flowStatisticData.getName(), flowStatisticData.getTimesRun()));
        });
        FlowNumberOfRunsChart.getData().add(series1);

        if (flowsStatisticData.size() == 1) {
            double barWidth = 0.3; // Set the desired width for a single bar
            for (XYChart.Data<String, Number> data : (ObservableList<XYChart.Data<String, Number>>) series1.getData()) {
                Node bar = data.getNode();
                bar.setStyle("-fx-bar-fill: #ffeaec;");

            }
        }



        XYChart.Series series2 = new XYChart.Series();
        flowsStatisticData.stream().forEach(flowStatisticData -> {
            series2.getData().add(new XYChart.Data(flowStatisticData.getName(), flowStatisticData.getAverageTime()));
        });
        AverageFlowsRunTimeChart.getData().add(series2);

    }

        public void initStepsChart (List < DTOStatisticData > stepsStatisticData) {
            StepNumberOfRunsChart.getData().clear();
            AverageStepsRunTimeChart.getData().clear();

            XYChart.Series series1 = new XYChart.Series();
            stepsStatisticData.stream().forEach(stepStatisticData -> {
                series1.getData().add(new XYChart.Data(stepStatisticData.getName(), stepStatisticData.getTimesRun()));
            });
            StepNumberOfRunsChart.getData().add(series1);


            XYChart.Series series2 = new XYChart.Series();
            stepsStatisticData.stream().forEach(stepStatisticData -> {
                series2.getData().add(new XYChart.Data(stepStatisticData.getName(), stepStatisticData.getAverageTime()));
            });
            AverageStepsRunTimeChart.getData().add(series2);

        }
}
