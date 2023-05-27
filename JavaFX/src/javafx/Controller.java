package javafx;

import dto.DTOSingleFlowIOData;
import javafx.flowDefinitionTab.FlowDefinitionTabController;
import javafx.flowExecutionTab.FlowExecutionTabController;
import javafx.fxml.FXML;
import javafx.header.HeaderController;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeView;
import javafx.scene.layout.GridPane;
import steps.api.DataNecessity;
import systemengine.systemengine;
import systemengine.systemengineImpl;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Controller {
    private final systemengine systemEngineInterface ;
    @FXML
    private ScrollPane scrollPane;
    @FXML private HeaderController headerComponentController;
    @FXML private FlowDefinitionTabController flowDefinitionTabController;
    @FXML private FlowExecutionTabController flowExecutionTabController;
    @FXML private GridPane headerComponent;


    @FXML
    public void initialize() {
        if(headerComponentController!=null){
            headerComponentController.setMainController(this);
        }
       if(flowDefinitionTabController!=null){
            flowDefinitionTabController.setMainController(this);
        }

        if(flowExecutionTabController!=null){
            flowExecutionTabController.setMainController(this);
        }
        double threshold = 500; // Set your threshold value here

        scrollPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.doubleValue() <= threshold) {
                scrollPane.setFitToWidth(false);
            } else {
                scrollPane.setFitToWidth(true);
            }
        });

        scrollPane.heightProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.doubleValue() <= threshold) {
                scrollPane.setFitToHeight(false);
            } else {
                scrollPane.setFitToHeight(true);
            }
        });

    }

    public Controller() {
        this.systemEngineInterface = new systemengineImpl();
    }

    public systemengine getSystemEngineInterface(){
        return systemEngineInterface;
    }

    public void showFlowsTree() {
        flowDefinitionTabController.showFlowsTree();
    }

    public TreeView<String> getFlowsTree(){
        return flowDefinitionTabController.getFlowsTree();
    }

    public void setHeaderComponentController(HeaderController headerComponentController) {
        this.headerComponentController = headerComponentController;
        headerComponentController.setMainController(this);
    }

    public void setFlowDefinitionTabController(FlowDefinitionTabController flowDefinitionTabComponentController) {
        this.flowDefinitionTabController = flowDefinitionTabComponentController;
        flowDefinitionTabController.setMainController(this);
    }

    public void setFlowExecutionTabController(FlowExecutionTabController flowExecutionTabComponentController) {
        this.flowExecutionTabController = flowExecutionTabComponentController;
        flowExecutionTabController.setMainController(this);
    }

    public void setFlowDetailsTree(){
        flowDefinitionTabController.setFlowDetailsTree();
    }

    public void goToFlowExecutionTab(String ChosenFlowName){
        List<DTOSingleFlowIOData> freeInputs =  systemEngineInterface.getAllFlows().getFlowByName(ChosenFlowName).getFlowFreeInputs();

        List<DTOSingleFlowIOData> sortedList = freeInputs.stream()
                .sorted(Comparator.comparing(obj -> obj.getNecessity().equals(DataNecessity.MANDATORY) ? 0 : 1))
                .collect(Collectors.toList());
        flowExecutionTabController.initInputsTable(sortedList);
    }

}
