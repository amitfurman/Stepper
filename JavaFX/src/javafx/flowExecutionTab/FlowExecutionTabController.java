
package javafx.flowExecutionTab;

import dto.DTOFlowExecution;
import dto.DTOFreeInputsFromUser;
import dto.DTOSingleFlowIOData;
import exceptions.TaskIsCanceledException;
import javafx.Controller;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;

import javafx.util.Duration;

import java.util.*;
import java.util.concurrent.*;

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

    private ExecutorService executorService;
    private List<FlowExecutionTask> flowExecutionTasks;

    private Timeline progressTimeline;

    private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    ;
    private final long EXECUTE_CHECK_INTERVAL = 200; // Check interval in milliseconds



    @FXML
    public void initialize() {
        freeInputMap = new HashMap<>();
        executeButton.setDisable(true);
        AnchorPane.setTopAnchor(borderPane, 0.0);
        AnchorPane.setBottomAnchor(borderPane, 0.0);
        AnchorPane.setLeftAnchor(borderPane, 0.0);
        AnchorPane.setRightAnchor(borderPane, 0.0);

        int numThreads = 5; // Set the desired number of threads
        executorService = Executors.newFixedThreadPool(numThreads);
        flowExecutionTasks = new ArrayList<>();

    }

    public void setMainController(Controller mainController) {
        this.mainController = mainController;
    }

    public void initInputsTable(List<DTOSingleFlowIOData> freeInputs) {
        executeButton.setDisable(true);
        inputValuesHBox.getChildren().clear();

        // Populate inputList from freeInputs
        freeInputs.forEach(freeInput -> {
            Input input = new Input();
            input.setFinalName(freeInput.getFinalName());
            input.setOriginalName(freeInput.getOriginalName());
            input.setStepName(freeInput.getStepName());
            input.setMandatory(freeInput.getNecessity().toString());
            input.setType(freeInput.getType());
            inputList.add(input);

            // Create label for the node
            Label label = new Label(input.getFinalName() + " (" + input.getMandatory() + ")");
            label.setWrapText(true);
            label.setAlignment(Pos.CENTER_LEFT);
            label.setTextAlignment(TextAlignment.LEFT);
            label.setTextOverrun(OverrunStyle.CLIP); // Clip the text if it exceeds the label width

            String simpleName = input.getType().getType().getSimpleName();

            VBox vbox = new VBox();
            vbox.setAlignment(Pos.CENTER_LEFT);
            vbox.setSpacing(10);

            vbox.setVgrow(label, Priority.ALWAYS);

            Spinner<Integer> spinner;
            TextField textField;
            if (simpleName.equals("String")) {
                textField = new TextField();
                textField.getStyleClass().add("text-field");
                textField.setOnAction(event -> {
                    commitEdit(textField.getText(), input);
                });
                textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                    if (!isNowFocused) {
                        commitEdit(textField.getText(), input);
                    }
                });

                vbox.getChildren().addAll(label, textField);
                vbox.setVgrow(textField, Priority.ALWAYS);

            }
            else {
                spinner = new Spinner<>();
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
                vbox.setVgrow(spinner, Priority.ALWAYS);
                vbox.getChildren().addAll(label, spinner);

            }

            inputValuesHBox.getChildren().add(vbox);
            inputValuesHBox.setSpacing(50);
        });
    }

    private void commitEdit(Object newValue, Input input) {
        input.setValue(newValue);
        updateFreeInputMap(input, newValue);
        boolean hasAllMandatoryInputs = hasAllMandatoryInputs(freeInputMap);
        executeButton.setDisable(!hasAllMandatoryInputs);
    }

    private void updateFreeInputMap(Input input, Object newValue) {
        freeInputMap.put(input.getStepName() + "." + input.getOriginalName(), newValue);
    }

    private boolean hasAllMandatoryInputs(Map<String, Object> freeInputMap) {
        for (Node node : inputValuesHBox.getChildren()) {
            VBox vbox = (VBox) node;
            Label label = (Label) vbox.getChildren().get(0);
            String labelText = label.getText();
            int endIndex = labelText.lastIndexOf(" (");
            String finalName = labelText.substring(0, endIndex);
            String mandatoryValue = labelText.substring(endIndex + 2, labelText.length() - 1);
            if (mandatoryValue.equals("MANDATORY")) {
                Optional<Input> optionalInput = inputList.stream().filter(input1 -> input1.getFinalName().equals(finalName)).findFirst();
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

}
/*    @FXML
    void StartExecuteFlowButton(ActionEvent event) {
        System.out.println(freeInputMap);
        CustomTask flowExecutionTask = new CustomTask();

        flowExecutionTask.setOnSucceeded(taskEvent -> {
            DTOFlowExecution flowExecution = flowExecutionTask.getDtoFlowExecution();

            // Process the flowExecution or perform any other necessary actions
            // Stop the progress update mechanism
            stopProgressUpdates();
            mainController.goToStatisticsTab();
        });

        flowExecutionTask.setOnFailed(taskEvent -> {
            Throwable exception = flowExecutionTask.getException();
            stopProgressUpdates();
        });

        executorService.submit(flowExecutionTask);

        System.out.println(flowExecutionTask.getDtoFlowExecution().getUniqueId());
        System.out.println(flowExecutionTask.getDtoFlowExecution().getFlowName());
        startProgressUpdates(flowExecutionTask.getDtoFlowExecution().getUniqueIdByUUID());

        freeInputMap = new HashMap<>();
        System.out.println(freeInputMap);
    }

    // Custom Task subclass with the getter method
    private class CustomTask extends Task<DTOFlowExecution> {
        private DTOFlowExecution dtoFlowExecution;

        @Override
        protected DTOFlowExecution call() throws Exception {
            DTOFreeInputsFromUser freeInputs = new DTOFreeInputsFromUser(freeInputMap);
            dtoFlowExecution = mainController.getSystemEngineInterface().activateFlowByName(mainController.getFlowName(), freeInputs);
            return dtoFlowExecution;
        }

        public DTOFlowExecution getDtoFlowExecution() {
            return dtoFlowExecution;
        }
    }*/

    /*
       @FXML
    void StartExecuteFlowButton(ActionEvent event) {
        System.out.println(freeInputMap);
        DTOFreeInputsFromUser freeInputs = new DTOFreeInputsFromUser(freeInputMap);

           class FlowExecutionTask extends Task<DTOFlowExecution> {
               private DTOFlowExecution dtoFlowExecution;
               private UUID flowSessionId;

               @Override
               protected DTOFlowExecution call() throws Exception {
                   dtoFlowExecution = mainController.getSystemEngineInterface().activateFlowByName(mainController.getFlowName(), freeInputs);
                   flowSessionId = dtoFlowExecution.getUniqueIdByUUID();
                   startProgressUpdates(flowSessionId);
                   return dtoFlowExecution;
               }

               public UUID getFlowSessionId() {
                   return flowSessionId;
               }
           }

           FlowExecutionTask flowExecutionTask = new FlowExecutionTask();

           flowExecutionTask.setOnSucceeded(taskEvent -> {
               DTOFlowExecution flowExecution = flowExecutionTask.getValue();
               stopProgressUpdates();
               mainController.goToStatisticsTab();
           });

           flowExecutionTask.setOnFailed(taskEvent -> {
               Throwable exception = flowExecutionTask.getException();
               stopProgressUpdates();
           });

           System.out.println("submitting task");
           executorService.submit(flowExecutionTask);

           flowExecutionTask.valueProperty().addListener((observable, oldValue, newValue) -> {
               if (newValue != null) {
                   System.out.println("in update");
                   DTOFlowExecution intermediateFlowExecution = newValue;
                   // Call a method to handle the intermediate flowExecution or update the UI
                   //updateUIWithProgressDetails(intermediateFlowExecution);
               }
           });

           Timeline updateTimeline = new Timeline(new KeyFrame(Duration.millis(200), exeEvent -> {
               updateProgressDetails(flowSessionId);
           }));
           updateTimeline.setCycleCount(Timeline.INDEFINITE);
           updateTimeline.play();

        freeInputMap = new HashMap<>();
        System.out.println(freeInputMap);
    }
*/

/*
    private void startProgressUpdates(UUID flowSessionId) {
        Duration updateInterval = Duration.millis(200);
        progressTimeline = new Timeline(new KeyFrame(updateInterval, event -> {
            updateProgressDetails(flowSessionId);
        }));
        progressTimeline.setCycleCount(Timeline.INDEFINITE);
        progressTimeline.play();
    }

    private void stopProgressUpdates() {
        if (progressTimeline != null) {
            progressTimeline.stop();
        }
    }

    private void updateProgressDetails(UUID flowSessionId) {
        DTOFlowExecution flowExecution = mainController.getSystemEngineInterface().getFlowExecutionStatus(flowSessionId);

        if (flowExecution.isComplete()) {
            stopProgressUpdates();
            mainController.goToStatisticsTab();
        } else {
            // Flow execution is still in progress
            // Update the UI with the latest progress details
            // ...
        }
    }*/


   /* private void startProgressUpdates(UUID flowSessionId) {
        System.out.println("startProgressUpdates");
        Duration updateInterval = Duration.millis(100);
        progressTimeline = new Timeline(new KeyFrame(updateInterval, event -> {
            //updateGeneralInfo(flowExecution);
            updateProgressDetails(flowSessionId);
            // Check if the flow execution is complete
    *//*        if (flowExecution.isComplete()) {
                progressTimeline.stop();
                System.out.println("flow is complete" + flowExecution.getFlowName());
               // handleFlowCompletion(flowExecution);
            }*//*
        }));
        progressTimeline.setCycleCount(Timeline.INDEFINITE);
        progressTimeline.play();
    }

    private void stopProgressUpdates() {
        System.out.println("stopProgressUpdates");
        if (progressTimeline != null) {
            progressTimeline.stop();
        }
    }

    private void updateProgressDetails(UUID flowSessionId) {
        System.out.println("updateProgressDetails");
        DTOFlowExecution flowExecution = mainController.getSystemEngineInterface().getFlowExecutionStatus(flowSessionId);
        System.out.println(flowExecution.getFlowName());
        System.out.println(flowExecution.isComplete());

        if (flowExecution.isComplete()) {
            progressTimeline.stop();
            System.out.println("flow is complete" + flowExecution.getFlowName());
        }
        else {
            System.out.println("flow is not complete" + flowExecution.getFlowName());
        }


  *//*      // Query the execution status and progress

        // Extract the necessary progress details from the flowExecution object
        int stepsTaken = flowExecution.getStepsTaken();
        int totalSteps = flowExecution.getTotalSteps();
        double completionPercentage = (double) stepsTaken / totalSteps * 100;

        // Update the UI with the latest progress details
        Platform.runLater(() -> {
            // Update labels, progress bars, or any other UI elements
            stepsTakenLabel.setText(String.valueOf(stepsTaken));
            totalStepsLabel.setText(String.valueOf(totalSteps));
            completionPercentageLabel.setText(String.format("%.2f%%", completionPercentage));
            progressBar.setProgress(completionPercentage / 100);
        });*//*
    }*/

    /*    private void startProgressUpdates() {
        Duration updateInterval = Duration.millis(100);
        progressTimeline = new Timeline(new KeyFrame(updateInterval, event -> {
            // Query the execution status and progress
            // Update the UI with the latest progress details
            updateProgressDetails();
        }));
        progressTimeline.setCycleCount(Timeline.INDEFINITE);
        progressTimeline.play();
    }*/


//}

