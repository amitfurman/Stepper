
package javafx.StatisticsTab;

import dto.DTOFlowAndStepStatisticData;
import dto.DTOStatisticData;
import javafx.Controller;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
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

    @FXML
    private void initialize() {

        CategoryAxis FlowNumberOfRunsChartcategoryAxis = (CategoryAxis) FlowNumberOfRunsChart.getXAxis();
        Node label1 = FlowNumberOfRunsChartcategoryAxis.lookup(".axis-label");
        label1.setTranslateY(10);
        CategoryAxis StepNumberOfRunsChartcategoryAxis = (CategoryAxis) StepNumberOfRunsChart.getXAxis();
        Node label2 = StepNumberOfRunsChartcategoryAxis.lookup(".axis-label");
        label2.setTranslateY(10);
        CategoryAxis AverageFlowsRunTimeChartcategoryAxis = (CategoryAxis) AverageFlowsRunTimeChart.getXAxis();
        Node label3 = AverageFlowsRunTimeChartcategoryAxis.lookup(".axis-label");
        label3.setTranslateY(10);
        CategoryAxis AverageStepsRunTimeChartcategoryAxis = (CategoryAxis) AverageStepsRunTimeChart.getXAxis();
        Node label4 = AverageStepsRunTimeChartcategoryAxis.lookup(".axis-label");
        label4.setTranslateY(10);
/*
        FlowNumberOfRunsChart.widthProperty().addListener((obs,b,b1)->{
            Platform.runLater(()->setMaxBarWidth(FlowNumberOfRunsChart ,40, 10));
        });

        StepNumberOfRunsChart.widthProperty().addListener((obs,b,b1)->{
            Platform.runLater(()->setMaxBarWidth(StepNumberOfRunsChart ,40, 10));
        });*/
/*
        AverageFlowsRunTimeChart.widthProperty().addListener((obs,b,b1)->{
            Platform.runLater(()->setMaxBarWidth(AverageFlowsRunTimeChart ,40, 10));
        });

        AverageStepsRunTimeChart.widthProperty().addListener((obs,b,b1)->{
            Platform.runLater(()->setMaxBarWidth(AverageStepsRunTimeChart ,40, 10));
        });
*/

    }

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

        //  setMaxBarWidth(FlowNumberOfRunsChart, 40, 10);
        //  setMaxBarWidth(AverageFlowsRunTimeChart, 40, 10);
        XYChart.Series series1 = new XYChart.Series();
        flowsStatisticData.stream().forEach(flowStatisticData -> {
            series1.getData().add(new XYChart.Data(flowStatisticData.getName(), flowStatisticData.getTimesRun()));
        });
        FlowNumberOfRunsChart.getData().add(series1);


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


    private void setMaxBarWidth(BarChart<String, Number> barChart, double maxBarWidth, double minCategoryGap) {
        Axis<String> xAxis = barChart.getXAxis();
        double axisWidth = xAxis.getWidth();
        int numCategories = barChart.getData().size();
        double categorySpacing = axisWidth / numCategories;

        double barWidth = 0;
        double categoryGap = barChart.getCategoryGap();
        double barGap = barChart.getBarGap();

        do {
            double availableBarSpace = categorySpacing - (categoryGap + barGap);
            barWidth = (availableBarSpace / numCategories) - barGap;

            if (barWidth > maxBarWidth) {
                double requiredBarSpace = (maxBarWidth + barGap) * numCategories;
                categoryGap = categorySpacing - requiredBarSpace - barGap;
                barChart.setCategoryGap(categoryGap);
            }
        } while (barWidth > maxBarWidth);

        do {
            double availableBarSpace = categorySpacing - (minCategoryGap + barGap);
            barWidth = Math.min(maxBarWidth, (availableBarSpace / numCategories) - barGap);
            double requiredBarSpace = (barWidth + barGap) * numCategories;
            categoryGap = categorySpacing - requiredBarSpace - barGap;
            barChart.setCategoryGap(categoryGap);
        } while (barWidth < maxBarWidth && categoryGap > minCategoryGap);
    }
}

