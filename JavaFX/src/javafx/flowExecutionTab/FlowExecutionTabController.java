
package javafx.flowExecutionTab;

import dto.DTOFlowExecution;
import dto.DTOFreeInputsFromUser;
import dto.DTOSingleFlowIOData;
import dto.DTOStepExecutionData;
import javafx.Controller;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.flowExecutionTab.MasterDetail.MasterDetailController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.MasterDetailPane;
import steps.api.StepResult;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class FlowExecutionTabController {
    private Controller mainController;
    @FXML
    private BorderPane borderPane;
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


   // private ExecutorService executorService;

   // private List<FlowExecutionTask> flowExecutionTasks;

   // private Timeline progressTimeline;

   // private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

  // private final long EXECUTE_CHECK_INTERVAL = 200; // Check interval in milliseconds


    @FXML
    public void initialize() throws IOException {
        freeInputMap = new HashMap<>();
        executeButton.setDisable(true);
        AnchorPane.setTopAnchor(borderPane, 0.0);
        AnchorPane.setBottomAnchor(borderPane, 0.0);
        AnchorPane.setLeftAnchor(borderPane, 0.0);
        AnchorPane.setRightAnchor(borderPane, 0.0);

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
        borderPane.setCenter(masterDetailPaneVbox);

        Text asterisk1 = new Text("*");
        asterisk1.setFill(Color.RED);
        MandatoryLabel.setGraphic(asterisk1);

        //MasterDetailComponent.setDetailNode()// int numThreads = 5; // Set the desired number of threads
        //executorService = Executors.newFixedThreadPool(numThreads);
       // flowExecutionTasks = new ArrayList<>();
    }

    public void initFlowExecutionTab() {
        masterDetailController.initMasterDetailPaneController();
    }
    public void setMasterDetailsController(MasterDetailController masterDetailComponentController) {
        this.masterDetailController = masterDetailComponentController;
        masterDetailComponentController.setFlowExecutionTabController(this);
    }

    public void setMainController(Controller mainController) {
        this.mainController = mainController;
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

            // Check if input.getMandatory() is "MANDATORY"
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

            System.out.println(input.getOriginalName());

            if(simpleName.equals("String")) {
                setTextFieldSetting(textField, input);
                if(input.getOriginalName().equals("FILE_NAME")){
                    openFileChooser(textField);
                    textField.setCursor(Cursor.HAND);

                }else if(input.getOriginalName().equals("FOLDER_NAME")){
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
        vbox.setSpacing(10);
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
        System.out.println(freeInputMap);
        boolean hasAllMandatoryInputs = hasAllMandatoryInputs(freeInputMap);
        executeButton.setDisable(!hasAllMandatoryInputs);
    }

    public void updateFreeInputMap(Input input, Object newValue) {
        freeInputMap.put(input.getStepName() + "." + input.getOriginalName(), newValue);
    }

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

        System.out.println(finalName);
        System.out.println("Starts with asterisk: " + startsWithAsterisk); // Print if label starts with asterisk

        if (startsWithAsterisk) {
            String finalName1 = finalName;
            Optional<Input> optionalInput = inputList.stream().filter(input1 -> input1.getFinalName().equals(finalName1)).findFirst();
            System.out.println(optionalInput);
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
}


    @FXML
    void StartExecuteFlowButton(ActionEvent event){
        System.out.println(freeInputMap);

        DTOFreeInputsFromUser freeInputs = new DTOFreeInputsFromUser(freeInputMap);
        System.out.println(freeInputs);
        DTOFlowExecution flowExecution = mainController.getSystemEngineInterface().activateFlowByName(mainController.getFlowName(), freeInputs);
        freeInputMap = new HashMap<>();
        //  createTask();
        masterDetailController.initMasterDetailComponent(flowExecution);
        mainController.goToStatisticsTab();

    }





    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                boolean isDone = false;
                while (!isDone) {//maybe instead of name need to do with id
                     isDone = mainController.getSystemEngineInterface().isCurrFlowExecutionDone(mainController.getFlowName());
                    if (!isDone) {
                        Thread.sleep(200);
                        continue;
                    }
                    // If done is true, get the DTO object
                   // DTOFlowExecution dto = mainController.getSystemEngineInterface().getItem();

                    // Update UI if needed
                    Platform.runLater(() -> {
                        // Update UI components with the DTO object
                        // For example, update a label with DTO properties
                    });

                    // Sleep for 200ms before the next iteration
                    //Thread.sleep(200);
                }
                return null;
            }
        };
    }

/*
    @FXML
    void StartExecuteFlowButton(ActionEvent event) throws ExecutionException, InterruptedException {
        System.out.println(freeInputMap);
        DTOFreeInputsFromUser freeInputs = new DTOFreeInputsFromUser(freeInputMap);

        FlowExecutionTask flowExecutionTask = new FlowExecutionTask(freeInputs);
        flowExecutionTask.setOnSucceeded(this::handleFlowExecutionSuccess);
        flowExecutionTask.setOnFailed(this::handleFlowExecutionFailure);

        System.out.println("Starting flow execution");
        executorService.submit(flowExecutionTask);
        flowExecutionTasks.add(flowExecutionTask);

        ///////////////////null/////////
        DTOFlowExecution dtoFlowExecution = flowExecutionTask.get(); // Retrieve the returned value
        startExecuteButtonCheckThread(dtoFlowExecution.getUniqueIdByUUID());

        freeInputMap = new HashMap<>();
        System.out.println(freeInputMap);
    }


    private void startExecuteButtonCheckThread(UUID flowSessionId) {
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            System.out.println("Checking executeButton");
            System.out.println(flowSessionId.toString());
            updateProgressDetails(flowSessionId);

        }, 0, EXECUTE_CHECK_INTERVAL, TimeUnit.MILLISECONDS);
    }

    public void handleFlowExecutionSuccess(WorkerStateEvent event) {
        FlowExecutionTask flowExecutionTask = (FlowExecutionTask) event.getSource();
        DTOFlowExecution flowExecution = flowExecutionTask.getValue();
        System.out.println("Flow execution completed: " + flowExecution.getFlowName());
        flowExecutionTasks.remove(flowExecutionTask);

        if (flowExecutionTasks.isEmpty()) {
            System.out.println("All flows completed");
            // Stop the progress update mechanism or perform any other necessary actions
        }
    }

    private void handleFlowExecutionFailure(WorkerStateEvent event) {
        FlowExecutionTask flowExecutionTask = (FlowExecutionTask) event.getSource();
        Throwable exception = flowExecutionTask.getException();
        System.out.println("Flow execution failed: " + exception.getMessage());
        flowExecutionTasks.remove(flowExecutionTask);

        if (flowExecutionTasks.isEmpty()) {
            System.out.println("All flows completed");
            // Stop the progress update mechanism or perform any other necessary actions
        }
    }

    private class FlowExecutionTask extends Task<DTOFlowExecution> {
        private DTOFreeInputsFromUser freeInputs;
        private DTOFlowExecution dtoFlowExecution;

        public FlowExecutionTask(DTOFreeInputsFromUser freeInputs) {
            this.freeInputs = freeInputs;
        }

        @Override
        protected DTOFlowExecution call() throws Exception {
            System.out.println("call");
            this.dtoFlowExecution = mainController.getSystemEngineInterface().activateFlowByName(mainController.getFlowName(), freeInputs);

            // Start the progress update mechanism
            startProgressUpdates(dtoFlowExecution.getUniqueIdByUUID());

            // Simulate flow execution (replace with your actual flow execution code)
            // Thread.sleep(5000);
            // update UI
            return dtoFlowExecution;
        }

        public DTOFlowExecution getDTOFlowExecution() {
            return dtoFlowExecution;
        }
    }

    private void startProgressUpdates(UUID flowSessionId) {
        System.out.println("startProgressUpdates");
         progressTimeline = new Timeline(new KeyFrame(Duration.millis(200), event -> {
             System.out.println("Checking executeButton"); // Add this line to check the execution every 200ms
            // Update progress details for the flow with the given flowSessionId
            updateProgressDetails(flowSessionId);
        }));
        progressTimeline.setCycleCount(Timeline.INDEFINITE);
        progressTimeline.play();
    }

    private void updateProgressDetails(UUID flowSessionId) {
        System.out.println("updateProgressDetails");
        DTOFlowExecution flowExecution = mainController.getSystemEngineInterface().getFlowExecutionStatus(flowSessionId);
        System.out.println(flowExecution.isComplete());
        if (flowExecution.isComplete()) {
            // The flow execution is complete
            stopProgressUpdates();
            Platform.runLater(() -> {mainController.goToStatisticsTab(); });
        } else {
            System.out.println("still not complete");
        }
    }

    private void stopProgressUpdates() {
        System.out.println("stopProgressUpdates");
        System.out.println(progressTimeline);
        if (progressTimeline != null) {
            progressTimeline.stop();
        }
        scheduledExecutorService.shutdown();

    }

 */

}
