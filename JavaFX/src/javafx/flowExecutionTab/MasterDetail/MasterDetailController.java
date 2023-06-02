package javafx.flowExecutionTab.MasterDetail;
import javafx.flowExecutionTab.FlowExecutionTabController;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import org.controlsfx.control.MasterDetailPane;

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
}
