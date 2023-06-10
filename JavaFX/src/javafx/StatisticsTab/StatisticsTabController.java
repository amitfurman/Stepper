
package javafx.StatisticsTab;

import dto.DTOFlowAndStepStatisticData;
import dto.DTOStatisticData;
import javafx.Controller;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.layout.FlowPane;
import javafx.util.Duration;

import java.util.List;
import java.util.stream.Collectors;

public class StatisticsTabController {

    private Controller mainController;
/*    @FXML
    private BarChart FlowNumberOfRunsChart;
    @FXML
    private BarChart StepNumberOfRunsChart;
    @FXML
    private BarChart AverageFlowsRunTimeChart;
    @FXML
    private BarChart AverageStepsRunTimeChart;*/

    @FXML
    private BarChart<String, Number> FlowNumberOfRunsChart;
    @FXML
    private BarChart<String, Number> StepNumberOfRunsChart;
    @FXML
    private BarChart<String, Number> AverageFlowsRunTimeChart;
    @FXML
    private BarChart<String, Number> AverageStepsRunTimeChart;

    @FXML
    private CategoryAxis FlowNumberOfRunsChartXAxis;
    @FXML
    private CategoryAxis StepNumberOfRunsChartXAxis;
    @FXML
    private CategoryAxis AverageFlowsRunTimeChartXAxis;
    @FXML
    private CategoryAxis AverageStepsRunTimeChartXAxis;

    @FXML
    private void initialize() {
        Node label1 = FlowNumberOfRunsChartXAxis.lookup(".axis-label");
        label1.setTranslateY(10);
        Node label2 = StepNumberOfRunsChartXAxis.lookup(".axis-label");
        label2.setTranslateY(10);
        Node label3 = AverageFlowsRunTimeChartXAxis.lookup(".axis-label");
        label3.setTranslateY(10);
        Node label4 = AverageStepsRunTimeChartXAxis.lookup(".axis-label");
        label4.setTranslateY(10);
    }

    public void initCharts(DTOFlowAndStepStatisticData statisticData) {
        initFlowsChart(statisticData.getFlowsStatisticData());
        initStepsChart(statisticData.getStepsStatisticData());
    }

    public void initFlowsChart(List<DTOStatisticData> flowsStatisticData) {
        Platform.runLater(() -> {
            FlowNumberOfRunsChart.getData().clear();
            AverageFlowsRunTimeChart.getData().clear();

            FlowNumberOfRunsChart.setAnimated(true);
            AverageFlowsRunTimeChart.setAnimated(true);

            XYChart.Series<String, Number> series1 = new XYChart.Series<>();
            flowsStatisticData.stream().forEach(flowStatisticData -> {
                series1.getData().add(new XYChart.Data<>(flowStatisticData.getName(), flowStatisticData.getTimesRun()));
            });
            FlowNumberOfRunsChart.getData().add(series1);

            XYChart.Series<String, Number> series2 = new XYChart.Series<>();
            flowsStatisticData.stream().forEach(flowStatisticData -> {
                series2.getData().add(new XYChart.Data<>(flowStatisticData.getName(), flowStatisticData.getAverageTime()));
            });
            AverageFlowsRunTimeChart.getData().add(series2);

            FlowNumberOfRunsChart.setAnimated(false);
            AverageFlowsRunTimeChart.setAnimated(false);
        });
    }

    public void initStepsChart(List<DTOStatisticData> stepsStatisticData) {
        Platform.runLater(() -> {
            StepNumberOfRunsChart.getData().clear();
            AverageStepsRunTimeChart.getData().clear();

            StepNumberOfRunsChart.setAnimated(true);
            AverageStepsRunTimeChart.setAnimated(true);

            XYChart.Series<String, Number> series1 = new XYChart.Series<>();
            stepsStatisticData.stream().forEach(stepStatisticData -> {
                series1.getData().add(new XYChart.Data<>(stepStatisticData.getName(), stepStatisticData.getTimesRun()));
            });
            StepNumberOfRunsChart.getData().add(series1);

            XYChart.Series<String, Number> series2 = new XYChart.Series<>();
            stepsStatisticData.stream().forEach(stepStatisticData -> {
                series2.getData().add(new XYChart.Data<>(stepStatisticData.getName(), stepStatisticData.getAverageTime()));
            });
            AverageStepsRunTimeChart.getData().add(series2);

            StepNumberOfRunsChart.setAnimated(false);
            AverageStepsRunTimeChart.setAnimated(false);
        });
    }


    public void setMainController(Controller mainController) {
        this.mainController = mainController;
    }


    /*@FXML
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
    }




    public void initFlowsChart(List<DTOStatisticData> flowsStatisticData) {
        Platform.runLater(() -> {
            FlowNumberOfRunsChart.getData().clear();
            AverageFlowsRunTimeChart.getData().clear();

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
*//*
            PauseTransition pause = new PauseTransition(Duration.seconds(0.1));
            pause.setOnFinished(event -> {
                FlowNumberOfRunsChart.layout();
                AverageFlowsRunTimeChart.layout();
            });
            pause.play();*//*
        });

    }


    public void initStepsChart (List < DTOStatisticData > stepsStatisticData) {
        Platform.runLater(() -> {
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
        });

    }*/






    /*private void setMaxBarWidth(BarChart<String, Number> barChart, double maxBarWidth, double minCategoryGap) {
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
    }*/
}

