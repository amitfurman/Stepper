
package components.body.flowExecutionTab;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import components.body.flowExecutionTab.MasterDetail.ClientMasterDetailController;

import components.commonComponents.ClientCommonController;
import dto.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import okhttp3.*;
import org.controlsfx.control.MasterDetailPane;
import org.jetbrains.annotations.NotNull;
import systemengine.Input;
import util.Constants;
import util.http.HttpClientUtil;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import static util.Constants.*;

public class ClientFlowExecutionTabController {
    private ClientCommonController mainController;
    @FXML
    private GridPane flowExecutionGridPane;
    @FXML
    private HBox inputValuesHBox;
    @FXML
    private Button executeButton;
    @FXML
    private Label MandatoryLabel;

    private Map<String, Object> freeInputMap; //StepName.OriginalName, newValue

    private ObservableList<Input> inputList = FXCollections.observableArrayList();

    private ClientMasterDetailController clientMasterDetailController;

    private MasterDetailPane masterDetailPane;

    private final SimpleStringProperty executedFlowIDProperty;

    private TableView<DTOContinuationMapping> continuationTableView;

    @FXML
    private AnchorPane continuationAnchorPane;

    public ClientFlowExecutionTabController() {
        executedFlowIDProperty = new SimpleStringProperty();
    }

    public void setExecutedFlowID(UUID id) {
        this.executedFlowIDProperty.set(id.toString());
    }

    @FXML
    public void initialize() throws IOException {
        freeInputMap = new HashMap<>();

        executeButton.setDisable(true);
        flowExecutionGridPane.setVgap(5);
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("MasterDetail/ClientMasterDetails.fxml");
        fxmlLoader.setLocation(url);
        MasterDetailPane MasterDetailComponent = fxmlLoader.load(url.openStream());
        ClientMasterDetailController masterDetailController = fxmlLoader.getController();
        this.setMasterDetailsController(masterDetailController);
        this.masterDetailPane = MasterDetailComponent;
        if (masterDetailController != null) {
            masterDetailController.setFlowExecutionTabController(this);
        }

        VBox masterDetailPaneVbox = new VBox(MasterDetailComponent);
        VBox.setVgrow(masterDetailPane, Priority.ALWAYS);
        flowExecutionGridPane.add(masterDetailPaneVbox, 0, 1);

        Text asterisk1 = new Text("*");
        asterisk1.setFill(Color.RED);
        MandatoryLabel.setGraphic(asterisk1);
    }

    public void initContinuationVbox() {
        if (continuationTableView != null)
            continuationTableView.getItems().clear();
    }

    public void setMainController(ClientCommonController mainController) {
        this.mainController = mainController;
    }

    public ClientCommonController getMainController() {
        return mainController;
    }

    public void setMasterDetailsController(ClientMasterDetailController masterDetailComponentController) {
        this.clientMasterDetailController = masterDetailComponentController;
        masterDetailComponentController.setFlowExecutionTabController(this);
    }

    public void initDataInFlowExecutionTab() {
        clientMasterDetailController.initMasterDetailPaneController();
        initContinuationVbox();
    }

    public void initInputsInFlowExecutionTab() {
        executeButton.setDisable(true);
        inputValuesHBox.getChildren().clear();
    }

    public void initInputsTable(List<DTOFlowFreeInputs> freeInputs) {
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

            //String simpleName = input.getType().getType().getSimpleName();
            String simpleName = input.getIoType();
            VBox vbox = new VBox();
            setVBoxSetting(vbox, label);

            Spinner<Integer> spinner = new Spinner<>();
            TextField textField = new TextField();
            ComboBox<String> comboBox = new ComboBox<>();

            if (input.getOriginalName().equals("METHOD") || input.getOriginalName().equals("OPERATION")) {
                setComboBox(comboBox, input);
                vbox.setVgrow(spinner, Priority.ALWAYS);
                vbox.getChildren().addAll(label, comboBox);
            }
            else if (simpleName.equals("STRING") || simpleName.equals("JSON")) {
                setTextFieldSetting(textField, input);
                if (input.getOriginalName().equals("FOLDER_NAME")) {
                    openDirectoryChooser(textField);
                    textField.setCursor(Cursor.HAND);
                } else if (input.getOriginalName().equals("SOURCE")) {
                    openChooseDialog(textField);
                    textField.setCursor(Cursor.HAND);
                } else if (input.getOriginalName().equals("FILE_NAME")) {
                    openFileChooser(textField);
                    textField.setCursor(Cursor.HAND);
                }
                vbox.getChildren().addAll(label, textField);
                vbox.setVgrow(textField, Priority.ALWAYS);
            }else {
                setSpinnerSetting(spinner, input);
                vbox.setVgrow(spinner, Priority.ALWAYS);
                vbox.getChildren().addAll(label, spinner);
            }
            Tooltip tooltip1 = new Tooltip(textField.getText().toString());
            textField.setTooltip(tooltip1);
            comboBox.setTooltip(tooltip1);
            textField.textProperty().addListener((observable, oldValue, newValue) -> {
                tooltip1.setText(newValue);
            });
            comboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
                tooltip1.setText(newValue);
            });
            inputValuesHBox.getChildren().add(vbox);
            inputValuesHBox.setSpacing(50);
        });
    }

    public void setComboBox(ComboBox comboBox, Input input) {

        if(input.getOriginalName().equals("METHOD")){
            comboBox.getItems().addAll("PUT", "GET","POST", "DELETE");
        }
        if(input.getOriginalName().equals("OPERATION")) {
            comboBox.getItems().addAll("ZIP", "UNZIP");
        }

        comboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                commitEdit(t1,input); // Call the commitEdit() method with the selected value (t1)
            }
        });

    }
    public void getFreeInputs(String flowName) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constants.FREE_INPUTS_BY_FLOW_NAME).newBuilder();
        urlBuilder.addQueryParameter("flow_name", flowName);
        String finalUrl = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        OkHttpClient HTTP_CLIENT = new OkHttpClient();
        Call call = HTTP_CLIENT.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("Something want wrong.. " + e.getMessage() + "in getFreeInputs");

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonResponse = response.body().string();
                List<DTOFlowFreeInputs> flowFreeInputs = GSON_INSTANCE.fromJson(jsonResponse, new TypeToken<List<DTOFlowFreeInputs>>(){}.getType());
                Platform.runLater(() -> {
                    initInputsTable(flowFreeInputs);
                });
            }
        });
    }
    public void setInputValues(Input input, DTOFlowFreeInputs freeInput){
        input.setFinalName(freeInput.getFinalName());
        input.setOriginalName(freeInput.getOriginalName());
        input.setStepName(freeInput.getStepName());
        input.setMandatory(freeInput.getNecessity());
        input.setIoType(freeInput.getType());
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
                Integer newValue = text.isEmpty() ? 0 : Integer.parseInt(text);
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
    public void updateFreeInputMap(DTOInput input, Object newValue) {
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
                        } else if(inputNode instanceof ComboBox) {
                            ComboBox<String> comboBox = (ComboBox<String>) inputNode;
                            if (comboBox.getItems().size() == 0) {
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
        activateFlow(mainController.getFlowName(), freeInputs);

    }

    public void activateFlow(String flowName, DTOFreeInputsFromUser freeInputs) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("flowName", flowName);
        jsonObject.addProperty("userName", mainController.getHeaderClientComponentController().getUserName());
        jsonObject.add("freeInputs", new Gson().toJsonTree(freeInputs));
        String jsonPayload = new Gson().toJson(jsonObject);
        RequestBody body = RequestBody.create(jsonPayload, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(ACTIVATE_FLOW)
                .post(body)
                .build();

        String finalUrl = HttpUrl
                .parse(ACTIVATE_FLOW)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runAsyncPost(finalUrl, request.body(), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                    System.out.println("Something went wrong: " + e.getMessage() + "in activateFlow");
                });
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    DTOFlowID flowExecution = new Gson().fromJson(response.body().string(), DTOFlowID.class);
                    setExecutedFlowID(flowExecution.getUniqueIdByUUID());

                    freeInputMap = new HashMap<>();
                    ExecuteFlowTask currentRunningTask = new ExecuteFlowTask(UUID.fromString(executedFlowIDProperty.getValue()),
                            clientMasterDetailController,executedFlowIDProperty, new SimpleBooleanProperty(false));

                    new Thread(currentRunningTask).start();
                } else {
                    String errorMessage = response.body().string();

                    Platform.runLater(() -> {
                        System.out.println("Received message from server: " + errorMessage);

                    });
                }

            }
        });

    }
    public void backToFlowExecutionTabAfterExecution(String flowName) {
        getAllContinuationMap(flowName);
    }
    public void getAllContinuationMap(String flowName) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constants.GET_CONTINUATION).newBuilder();
        urlBuilder.addQueryParameter("flowName", flowName);
        String finalUrl = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        OkHttpClient HTTP_CLIENT = new OkHttpClient();
        Call call = HTTP_CLIENT.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("on failure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonArrayOfUsersRoles = response.body().string();
                LinkedList<DTOContinuationMapping> continuationMapping = GSON_INSTANCE.fromJson(jsonArrayOfUsersRoles, new TypeToken<LinkedList<DTOContinuationMapping>>(){}.getType());
                initFlowContinuationTableView(continuationMapping);
            }
        });
    }

    public void initFlowContinuationTableView(List<DTOContinuationMapping> mappings) {
        Platform.runLater(() -> {
            if (continuationTableView == null) {

                continuationTableView = new TableView<>();
                continuationAnchorPane.getChildren().add(continuationTableView);
                continuationAnchorPane.setTopAnchor(continuationTableView, 0.0);
                continuationAnchorPane.setBottomAnchor(continuationTableView, 0.0);
                continuationAnchorPane.setLeftAnchor(continuationTableView, 0.0);
                continuationAnchorPane.setRightAnchor(continuationTableView, 0.0);
                continuationTableView.setMinHeight(0.0);
                continuationTableView.setMaxHeight(0.0);

                continuationTableView.getStyleClass().add("flow-continuation-table");
            } else {
                continuationTableView.getItems().clear();
            }

            TableColumn<DTOContinuationMapping, String> targetFlowColumn = new TableColumn<>("Target Flow");
            targetFlowColumn.setCellValueFactory(new PropertyValueFactory<>("targetFlow"));
            targetFlowColumn.prefWidthProperty().bind(continuationTableView.widthProperty().multiply(0.5)); // Set to 50% width
            TableColumn<DTOContinuationMapping, DTOContinuationMapping> actionColumn = new TableColumn<>("");
            actionColumn.setCellFactory(param -> new TableCell<DTOContinuationMapping, DTOContinuationMapping>() {
                private final Button btn = new Button("Continue To Flow");
                {

                    btn.getStyleClass().add("continue-to-flow-button");
                    btn.setOnAction(event -> {
                                DTOContinuationMapping mapping = getTableView().getItems().get(getIndex());
                                String targetFlow = mapping.getTargetFlow();
                                String sourceFlow = mapping.getSourceFlow();
                                getMainController().goToClientFlowExecutionTab(targetFlow);
                                getMainController().initDataInFlowExecutionTab();
                                getValueList(sourceFlow, targetFlow);
                    });
                }
                @Override
                protected void updateItem(DTOContinuationMapping item, boolean empty) {
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
            getMainController().initExecutionHistoryTableInExecutionsHistoryTab();
        });
    }

    public void getValueList(String sourceFlow, String targetFlow){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constants.GET_CONTINUATION_VALUES).newBuilder();
        urlBuilder.addQueryParameter("sourceFlow", sourceFlow);
        urlBuilder.addQueryParameter("targetFlow", targetFlow);
        String finalUrl = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        OkHttpClient HTTP_CLIENT = new OkHttpClient();
        Call call = HTTP_CLIENT.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("on failure in getValueList");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonArrayOfUsersRoles = response.body().string();
                List<DTOInput> valuesList  = GSON_INSTANCE.fromJson(jsonArrayOfUsersRoles, new TypeToken<List<DTOInput>>(){}.getType());
                setInputValuesFromContinuationMap(valuesList);
                initContinuationVbox();
            }
        });

    }

    public void setInputValuesFromContinuationMap(List<DTOInput> valuesList) {
        for (Node node : inputValuesHBox.getChildren()) {
            if (node instanceof VBox) {
                VBox vbox = (VBox) node;
                Label label = (Label) vbox.getChildren().get(0); // Assuming the label is always at index 0 in the VBox
                Node inputNode = vbox.getChildren().get(1); // Assuming the input field is always at index 1 in the VBox

                String originalName = label.getText();
                if (label.getText().equals("TIME_TO_SPEND (sec)")) {
                    originalName = "TIME_TO_SPEND";
                }
                String finalOriginalName = originalName;
                valuesList.stream()
                        .filter(data -> data.getOriginalName().equals(finalOriginalName))
                        .findFirst()
                        .ifPresent(input -> {
                            if (inputNode instanceof TextField) {
                                TextField textField = (TextField) inputNode;
                                Object value = input.getValue();
                                if (value instanceof String) {
                                    textField.setText((String) value);
                                    updateFreeInputMap(input, value);
                                }
                            } else if (inputNode instanceof Spinner) {
                                Spinner<Integer> spinner = (Spinner<Integer>) inputNode;
                                Object value = input.getValue();
                                if (value instanceof Integer) {
                                    spinner.getValueFactory().setValue((Integer) value);
                                    updateFreeInputMap(input, value);
                                } else if (value instanceof Double) {
                                    int roundedValue = (int) Math.round((Double) value);
                                    spinner.getValueFactory().setValue(roundedValue);
                                    updateFreeInputMap(input, roundedValue);
                                }
                            }
                            else if (inputNode instanceof ComboBox) {
                                ComboBox<String> comboBox = (ComboBox<String>) inputNode;
                                Object value = input.getValue();
                                if (value instanceof String) {
                                    comboBox.setValue((String) value);
                                    updateFreeInputMap(input, value);
                                }
                            }
                        } );
            }
        }
    }


}
