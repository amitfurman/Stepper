package javafx.flowExecutionTab.MasterDetail;
import dto.DTOFlowExecution;
import dto.DTOStepExecutionData;
import javafx.flowExecutionTab.FlowExecutionTabController;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.controlsfx.control.MasterDetailPane;
import steps.api.StepResult;

public class MasterDetailController {

    @FXML
    private MasterDetailPane FlowMasterDetails;

    FlowExecutionTabController flowExecutionTabController;

    @FXML
    public void initialize(){
        FlowMasterDetails.setDetailSide(Side.LEFT);
        FlowMasterDetails.setDividerPosition(0.3);


    }

    public void setFlowExecutionTabController(FlowExecutionTabController flowExecutionTabController) {
        this.flowExecutionTabController = flowExecutionTabController;
    }

    public MasterDetailPane getMasterDetailsComponent() {
        return FlowMasterDetails;
    }

    public void initMasterDetailComponent(DTOFlowExecution flowExecution) {

        // Create the detail content
        VBox detailPane = new VBox();
        detailPane.setPadding(new Insets(10));
        detailPane.setSpacing(5);

        Label FlowdetailLabel = createDetailLabel(flowExecution.getFlowName(), FlowMasterDetails, true);
        detailPane.getChildren().add(FlowdetailLabel);

        int counter = 1;
        for (DTOStepExecutionData stepExecution: flowExecution.getStepExecutionDataList()) {
            Label detailLabel = createDetailLabel("Step " + counter++ + ": " + stepExecution.getFinalNameStep(), FlowMasterDetails, false);
            ImageView statusImage = new ImageView();
            if (stepExecution.getResult().equals(StepResult.FAILURE)) {
                statusImage.setImage(new Image(getClass().getResource("icons8-close-16.png").toString()));
                detailLabel.setGraphic(statusImage);

            } else if (stepExecution.getResult().equals(StepResult.SUCCESS)) {
                statusImage.setImage(new Image(getClass().getResource("icons8-checkmark-16.png").toString()));
                detailLabel.setGraphic(statusImage);
            } else {
                statusImage.setImage(new Image(getClass().getResource("icons8-error-16.png").toString()));
                detailLabel.setGraphic(statusImage);
            }
            detailPane.getChildren().add(detailLabel);
        }

        ScrollPane scrollPane = new ScrollPane(detailPane);
        scrollPane.setFitToWidth(true);
        FlowMasterDetails.setDetailNode(scrollPane);
        FlowMasterDetails.setDividerPosition(0.3);


        Label masterLabel = new Label("Master");
        StackPane masterPane = new StackPane(masterLabel);

        FlowMasterDetails.setMasterNode(masterPane);
    }

    private Label createDetailLabel(String text, MasterDetailPane masterDetailPane, boolean isFirstLabel) {
        Label detailLabel = new Label(text);
        detailLabel.setOnMouseClicked(event -> {
            masterDetailPane.setMasterNode(new Label("Full Details: " + text));
            masterDetailPane.setDividerPosition(0.3);

        });
        if (isFirstLabel) {
            detailLabel.getStyleClass().add("first-label");
        } else {
            detailLabel.getStyleClass().add("detail-label");
        }

        detailLabel.setCursor(Cursor.HAND);
        detailLabel.setOnMouseEntered(event -> {
            detailLabel.setUnderline(true);
        });
        detailLabel.setOnMouseExited(event -> {
            detailLabel.setUnderline(false);
        });

        return detailLabel;
    }

}
