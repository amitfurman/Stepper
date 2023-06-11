
package javafx.flowExecutionTab;

import dto.DTOFlowExecution;
import dto.DTOFreeInputsFromUser;
import dto.DTOSingleFlowIOData;
import flow.mapping.FlowContinuationMapping;
import javafx.Controller;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.flowExecutionTab.MasterDetail.MasterDetailController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.controlsfx.control.MasterDetailPane;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class FlowExecutionTabController {
    private Controller mainController;
    @FXML
    private GridPane flowExecutionGridPane;
    @FXML
    private GridPane gridPane;
    @FXML
    private HBox inputValuesHBox;
    @FXML
    private Button executeButton;

    private Map<String, Object> freeInputMap;

    private ObservableList<Input> inputList = FXCollections.observableArrayList();

    private MasterDetailController masterDetailController;

    private MasterDetailPane masterDetailPane;
    @FXML
    private Label MandatoryLabel;
    private final SimpleStringProperty executedFlowIDProperty;
    private TableView<FlowContinuationMapping> continuationTableView;
    private VBox continuationVbox;

    public FlowExecutionTabController() {
        executedFlowIDProperty = new SimpleStringProperty();
    }
    public void setExecutedFlowID(UUID id) {
        this.executedFlowIDProperty.set(id.toString());
    }
    @FXML
    public void initialize() throws IOException {
        freeInputMap = new HashMap<>();
        continuationVbox = new VBox();
        executeButton.setDisable(true);

        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("MasterDetail/masterDetails.fxml");
        fxmlLoader.setLocation(url);
        MasterDetailPane MasterDetailComponent = fxmlLoader.load(url.openStream());
        MasterDetailController masterDetailController = fxmlLoader.getController();
        this.setMasterDetailsController(masterDetailController);
        this.masterDetailPane = MasterDetailComponent;
        if (masterDetailController != null) {
            masterDetailController.setFlowExecutionTabController(this);
        }

        VBox masterDetailPaneVbox = new VBox(MasterDetailComponent);
        VBox.setVgrow(masterDetailPane, Priority.ALWAYS);
        flowExecutionGridPane.add(masterDetailPaneVbox,0,1);
        flowExecutionGridPane.add(continuationVbox,0,2);

        Text asterisk1 = new Text("*");
        asterisk1.setFill(Color.RED);
        MandatoryLabel.setGraphic(asterisk1);
    }
    public void initContinuationVbox(){
        continuationVbox = new VBox();
        flowExecutionGridPane.add(continuationVbox,0,2);
    }
    public void setMainController(Controller mainController) {
        this.mainController = mainController;
    }
    public Controller getMainController() {
        return mainController;
    }
    public void setMasterDetailsController(MasterDetailController masterDetailComponentController) {
        this.masterDetailController = masterDetailComponentController;
        masterDetailComponentController.setFlowExecutionTabController(this);
    }
    public void initDataInFlowExecutionTab() {
        masterDetailController.initMasterDetailPaneController();
        initContinuationVbox();
    }
    public void initInputsInFlowExecutionTab() {
        executeButton.setDisable(true);
        inputValuesHBox.getChildren().clear();
    }
    public void initInputsTable(List<DTOSingleFlowIOData> freeInputs) {
        executeButton.setDisable(true);
        inputValuesHBox.getChildren().clear();

        freeInputs.forEach(freeInput -> {// Populate inputList from freeInputs
            Input input = new Input();
            setInputValues(input, freeInput);
            inputList.add(input);


            Label label = new Label((input.getFinalName().equals("TIME_TO_SPEND") ? input.getFinalName() + " (sec)" : input.getFinalName()));
            setLabelSetting(label);
            if (input.getMandatory().equals("MANDATORY")) {
                Text asterisk1 = new Text("*");
                asterisk1.setFill(Color.RED);
                label.setGraphic(asterisk1);
            }

            String simpleName = input.getType().getType().getSimpleName();
            VBox vbox = new VBox();
            setVBoxSetting(vbox, label);

            Spinner<Integer> spinner = new Spinner<>();
            TextField textField = new TextField();
            
            if(simpleName.equals("String")) {
                setTextFieldSetting(textField, input);
                    if(input.getOriginalName().equals("FOLDER_NAME")){
                    openDirectoryChooser(textField);
                    textField.setCursor(Cursor.HAND);

                }
                else if(input.getOriginalName().equals("SOURCE")){
                    openChooseDialog(textField);
                    textField.setCursor(Cursor.HAND);

                }
                vbox.getChildren().addAll(label, textField);
                vbox.setVgrow(textField, Priority.ALWAYS);
            } else {
                setSpinnerSetting(spinner, input);
                vbox.setVgrow(spinner, Priority.ALWAYS);
                vbox.getChildren().addAll(label, spinner);
            }
            Tooltip tooltip1 = new Tooltip(textField.getText().toString());
            textField.setTooltip(tooltip1);
            textField.textProperty().addListener((observable, oldValue, newValue) -> {
                tooltip1.setText(newValue);
            });
            inputValuesHBox.getChildren().add(vbox);
            inputValuesHBox.setSpacing(50);
        });
    }
    public void setInputValues(Input input, DTOSingleFlowIOData freeInput){
        input.setFinalName(freeInput.getFinalName());
        input.setOriginalName(freeInput.getOriginalName());
        input.setStepName(freeInput.getStepName());
        input.setMandatory(freeInput.getNecessity().toString());
        input.setType(freeInput.getType());
    }
    public void openChooseDialog(TextField textField) {
        textField.setOnMouseClicked(e -> {
            if (e.getClickCount() == 1) {
                JFileChooser chooser = new JFileChooser(".");
                chooser.setMultiSelectionEnabled(true);
                chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                int ret = chooser.showOpenDialog(null);

                if(ret == JFileChooser.APPROVE_OPTION) {
                    File[] selectedDirectory = chooser.getSelectedFiles();
                    for (File file : selectedDirectory) {
                        textField.setText(file.getAbsolutePath());
                    }
                }
            }
        });

    }
    public void openDirectoryChooser(TextField textField) {
        textField.setOnMouseClicked(e -> {
            if (e.getClickCount() == 1) {
                DirectoryChooser directoryChooser = new DirectoryChooser();
                directoryChooser.setTitle("Choose Directory");

                Stage stage = (Stage) textField.getScene().getWindow();
                File selectedDirectory = directoryChooser.showDialog(stage);

                if (selectedDirectory != null) {
                    textField.setText(selectedDirectory.getAbsolutePath());
                }
            }
        });
    }
    public void openFileChooser(TextField textField){
        textField.setOnMouseClicked(e -> {
            if (e.getClickCount() == 1) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Choose File");

                Stage stage = (Stage) textField.getScene().getWindow();
                File selectedFile = fileChooser.showOpenDialog(stage);

                if (selectedFile != null) {
                    textField.setText(selectedFile.getAbsolutePath());
                }
            }
        });
    }
    public void setLabelSetting(Label label){
       label.setWrapText(true);
       label.setAlignment(Pos.CENTER_LEFT);
       label.setTextAlignment(TextAlignment.LEFT);
       label.setTextOverrun(OverrunStyle.CLIP); // Clip the text if it exceeds the label width
   }
    public void setVBoxSetting(VBox vbox,Label label){
        vbox.setAlignment(Pos.CENTER_LEFT);
        vbox.setSpacing(5);
        vbox.setVgrow(label, Priority.ALWAYS);
    }
    public void setTextFieldSetting(TextField textField, Input input){
        textField.getStyleClass().add("text-field");
        textField.setOnAction(event -> {
            commitEdit(textField.getText(), input);
        });
        textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) {
                commitEdit(textField.getText(), input);
            }
        });

    }
    public void setSpinnerSetting(Spinner spinner, Input input){
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0);
        spinner.setEditable(true);
        spinner.getEditor().setAlignment(Pos.CENTER_RIGHT);

        spinner.setOnMouseClicked(event -> {
            if (spinner.isEditable()) {
                spinner.increment(0);
            }
        });
        spinner.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused && spinner.isEditable()) {
                String text = spinner.getEditor().getText();
                if (text.isEmpty()) {
                    spinner.getValueFactory().setValue(0);
                } else {
                    spinner.increment(0); // Increment by 0 to trigger commitEdit
                }
            }
        });
        spinner.getEditor().focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused && spinner.isEditable()) {
                String text = spinner.getEditor().getText();
                int newValue = text.isEmpty() ? 0 : Integer.parseInt(text);
                commitEdit(newValue, input);
            }
        });
        spinner.setValueFactory(valueFactory);
    }
    public void commitEdit(Object newValue, Input input) {
        input.setValue(newValue);
        updateFreeInputMap(input, newValue);
        boolean hasAllMandatoryInputs = hasAllMandatoryInputs(freeInputMap);
        executeButton.setDisable(!hasAllMandatoryInputs);
    }

    public void updateFreeInputMap(Input input, Object newValue) {
        freeInputMap.put(input.getStepName() + "." + input.getOriginalName(), newValue);
    }

    /*
    public boolean hasAllMandatoryInputs(Map<String, Object> freeInputMap) {
    for (Node node : inputValuesHBox.getChildren()) {
        VBox vbox = (VBox) node;
        Label label = (Label) vbox.getChildren().get(0);
        String finalName;

        String labelText = label.getText();
        boolean startsWithAsterisk = false;
        Node graphic = label.getGraphic();
        if (graphic instanceof Text) {
            Text asterisk = (Text) graphic;
            startsWithAsterisk = asterisk.getText().equals("*");
        }

        finalName = labelText;

        int endIndex = finalName.lastIndexOf(" (");
        if (endIndex != -1) {
            finalName = finalName.substring(0, endIndex);
        }
        
        if (startsWithAsterisk) {
            String finalName1 = finalName;
            Optional<Input> optionalInput = inputList.stream().filter(input1 -> input1.getFinalName().equals(finalName1)).findFirst();
            if (optionalInput.isPresent()) {
                Input input = optionalInput.get();
                String key = input.getStepName() + "." + input.getOriginalName();
                if (!freeInputMap.containsKey(key) || freeInputMap.get(key).equals("")) {
                    return false;
                }
            }
        }
    }
    return true;
}*/


    public boolean hasAllMandatoryInputs(Map<String, Object> freeInputMap) {
        for (Node node : inputValuesHBox.getChildren()) {
            VBox vbox = (VBox) node;
            Label label = (Label) vbox.getChildren().get(0);
            String finalName;

            String labelText = label.getText();
            boolean startsWithAsterisk = false;
            Node graphic = label.getGraphic();
            if (graphic instanceof Text) {
                Text asterisk = (Text) graphic;
                startsWithAsterisk = asterisk.getText().equals("*");
            }

            finalName = labelText;

            int endIndex = finalName.lastIndexOf(" (");
            if (endIndex != -1) {
                finalName = finalName.substring(0, endIndex);
            }

            if (startsWithAsterisk) {
                String finalName1 = finalName;
                Optional<Input> optionalInput = inputList.stream().filter(input1 -> input1.getFinalName().equals(finalName1)).findFirst();
                if (optionalInput.isPresent()) {
                    Input input = optionalInput.get();
                    String key = input.getStepName() + "." + input.getOriginalName();
                    if (!freeInputMap.containsKey(key) || freeInputMap.get(key).equals("")) {
                        // Check if the TextField or Spinner has a value set programmatically
                        Node inputNode = vbox.getChildren().get(1);
                        if (inputNode instanceof TextField) {
                            TextField textField = (TextField) inputNode;
                            if (textField.getText().isEmpty()) {
                                return false;
                            }
                        } else if (inputNode instanceof Spinner) {
                            Spinner<Integer> spinner = (Spinner<Integer>) inputNode;
                            if (spinner.getValue() == null) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    @FXML
    void StartExecuteFlowButton(ActionEvent event){
        masterDetailPane = new MasterDetailPane();
        DTOFreeInputsFromUser freeInputs = new DTOFreeInputsFromUser(freeInputMap);

        System.out.println(freeInputMap);
        DTOFlowExecution flowExecution = mainController.getSystemEngineInterface().activateFlowByName(mainController.getFlowName(), freeInputs);
        setExecutedFlowID(flowExecution.getUniqueIdByUUID());

        freeInputMap = new HashMap<>();
        ExecuteFlowTask currentRunningTask = new ExecuteFlowTask(UUID.fromString(executedFlowIDProperty.getValue()),
                masterDetailController,executedFlowIDProperty, new SimpleBooleanProperty(false));

        new Thread(currentRunningTask).start();
    }

    public void initFlowContinuationTableView(List<FlowContinuationMapping> mappings) {
        Platform.runLater(() -> {
            if (continuationTableView == null) {
                continuationTableView = new TableView<>();
                Label titleLabel = new Label("Flow Continuation Table");
                titleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

                VBox vbox = new VBox();
                vbox.getChildren().addAll(titleLabel, continuationTableView);

                vbox.getStyleClass().add("flow-continuation-table");
                continuationVbox.getChildren().add(vbox);
            } else {
                continuationTableView.getItems().clear();
            }

            TableColumn<FlowContinuationMapping, String> targetFlowColumn = new TableColumn<>("Target Flow");
            targetFlowColumn.setCellValueFactory(new PropertyValueFactory<>("targetFlow"));
            targetFlowColumn.prefWidthProperty().bind(continuationTableView.widthProperty().multiply(0.5)); // Set to 50% width
            TableColumn<FlowContinuationMapping, FlowContinuationMapping> actionColumn = new TableColumn<>("");
            actionColumn.setCellFactory(param -> new TableCell<FlowContinuationMapping, FlowContinuationMapping>() {
                private final Button btn = new Button("Continue To Flow");
                {
                    btn.getStyleClass().add("continue-to-flow-button");
                    btn.setOnAction(event -> {
                                FlowContinuationMapping mapping = getTableView().getItems().get(getIndex());
                                Map<String, String> source2targetDataMapping = mapping.getSource2targetDataMapping();
                                String targetFlow = mapping.getTargetFlow();
                                String sourceFlow = mapping.getSourceFlow();

                                Map<String, Object> continuationMap = getMainController().getSystemEngineInterface().continuationFlowExecution(sourceFlow, targetFlow);
                                getMainController().goToFlowExecutionTab(targetFlow);
                                getMainController().initDataInFlowExecutionTab();

                                setInputValuesFromContinuationMap(continuationMap);
                     });

                }

                @Override
                protected void updateItem(FlowContinuationMapping item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(btn);
                    }
                }
            });
            actionColumn.prefWidthProperty().bind(continuationTableView.widthProperty().multiply(0.5)); // Set to 50% width

            continuationTableView.getColumns().setAll(targetFlowColumn, actionColumn);
            continuationTableView.setItems(FXCollections.observableArrayList(mappings));
        });
    }

    public void setInputValuesFromContinuationMap(Map<String, Object> valuesMap) {
        for (Node node : inputValuesHBox.getChildren()) {
            if (node instanceof VBox) {
                VBox vbox = (VBox) node;
                Label label = (Label) vbox.getChildren().get(0); // Assuming the label is always at index 0 in the VBox
                Node inputNode = vbox.getChildren().get(1); // Assuming the input field is always at index 1 in the VBox

                String originalName = label.getText();
                if(label.getText().equals("TIME_TO_SPEND (sec)")){
                    originalName = "TIME_TO_SPEND";
                }

                if (inputNode instanceof TextField) {
                    TextField textField = (TextField) inputNode;

                    if (valuesMap.containsKey(originalName)) {
                        Object value = valuesMap.get(originalName);
                        if (value instanceof String) {
                            textField.setText((String) value);
                        }
                    }
                } else if (inputNode instanceof Spinner) {
                    Spinner<Integer> spinner = (Spinner<Integer>) inputNode;

                    if (valuesMap.containsKey(originalName)) {
                        Object value = valuesMap.get(originalName);
                        if (value instanceof Integer) {
                            spinner.getValueFactory().setValue((Integer) value);
                           // spinner.requestFocus(); // Set focus on the spinner

                        }
                    }
                }
            }
        }
        System.out.println("inputValuesHBox.getChildren(): " + inputValuesHBox.getChildren());
    }


    public void backToFlowExecutionTabAfterExecution() {
        getMainController().goToStatisticsTab();
        initFlowContinuationTableView(mainController.getSystemEngineInterface().getAllContinuationMappingsWithSameSourceFlow(mainController.getFlowName()));
        getMainController().initExecutionHistoryTableInExecutionsHistoryTab();
    }
}
