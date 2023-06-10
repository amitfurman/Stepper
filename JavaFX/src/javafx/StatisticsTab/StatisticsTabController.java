
package javafx.StatisticsTab;

import dto.DTOFlowAndStepStatisticData;
import dto.DTOStatisticData;
import javafx.Controller;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StatisticsTabController {

    private Controller mainController;
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

            FlowNumberOfRunsChartXAxis.getChildrenUnmodifiable().addListener((ListChangeListener<Node>) p -> {
                if (p.next()) {
                    p.getAddedSubList().forEach(node -> {
                        if (node != null && node instanceof Text) {
                            final Text textNode = (Text) node;
                            final Optional<XYChart.Data<String, Number>> data = FlowNumberOfRunsChart.getData().get(0).getData().stream().filter(item -> item.getXValue().equalsIgnoreCase(textNode.getText())).findFirst();
                            data.ifPresent(d -> {
                                Tooltip.install(textNode, new Tooltip(d.getXValue() + ":\n" + d.getYValue()));
                            });
                        }
                    });
                }
            });

            AverageFlowsRunTimeChartXAxis.getChildrenUnmodifiable().addListener((ListChangeListener<Node>) p -> {
                if (p.next()) {
                    p.getAddedSubList().forEach(node -> {
                        if (node != null && node instanceof Text) {
                            final Text textNode = (Text) node;
                            final Optional<XYChart.Data<String, Number>> data = AverageFlowsRunTimeChart.getData().get(0).getData().stream().filter(item -> item.getXValue().equalsIgnoreCase(textNode.getText())).findFirst();
                            data.ifPresent(d -> {
                                Tooltip.install(textNode, new Tooltip(d.getXValue() + ":\n" + d.getYValue()));
                            });
                        }
                    });
                }
            });

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

            StepNumberOfRunsChartXAxis.getChildrenUnmodifiable().addListener((ListChangeListener<Node>) p -> {
                if (p.next()) {
                    p.getAddedSubList().forEach(node -> {
                        if (node != null && node instanceof Text) {
                            final Text textNode = (Text) node;
                            final Optional<XYChart.Data<String, Number>> data = StepNumberOfRunsChart.getData().get(0).getData().stream().filter(item -> item.getXValue().equalsIgnoreCase(textNode.getText())).findFirst();
                            data.ifPresent(d -> {
                                Tooltip.install(textNode, new Tooltip(d.getXValue() + ":\n" + d.getYValue()));
                            });
                        }
                    });
                }
            });

            AverageStepsRunTimeChartXAxis.getChildrenUnmodifiable().addListener((ListChangeListener<Node>) p -> {
                if (p.next()) {
                    p.getAddedSubList().forEach(node -> {
                        if (node != null && node instanceof Text) {
                            final Text textNode = (Text) node;
                            final Optional<XYChart.Data<String, Number>> data = AverageStepsRunTimeChart.getData().get(0).getData().stream().filter(item -> item.getXValue().equalsIgnoreCase(textNode.getText())).findFirst();
                            data.ifPresent(d -> {
                                Tooltip.install(textNode, new Tooltip(d.getXValue() + ":\n" + d.getYValue()));
                            });
                        }
                    });
                }
            });

            StepNumberOfRunsChart.setAnimated(false);
            AverageStepsRunTimeChart.setAnimated(false);
        });
    }
    public void setMainController(Controller mainController) {
        this.mainController = mainController;
    }
}

