
package javafx.flowExecutionTab;

import dto.DTOFlowExecution;
import dto.DTOFreeInputsFromUser;
import dto.DTOSingleFlowIOData;
import javafx.Controller;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

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

    Map<String, Object> freeInputMap = new HashMap<>();

/*    @FXML
    TableView<Input> freeInputsTable;
    @FXML
    private TableColumn<Input, String> nameColumn;
    @FXML
    private TableColumn<Input, String> necessityColumn;
    @FXML
    private TableColumn<Input, Object> valueColumn;


    @FXML
    private HBox hbox;
    @FXML
    private BorderPane borderPane;
     */


    @FXML
    public void initialize() {
        executeButton.setDisable(true);
         AnchorPane.setTopAnchor(borderPane, 0.0);
        AnchorPane.setBottomAnchor(borderPane, 0.0);
        AnchorPane.setLeftAnchor(borderPane, 0.0);
        AnchorPane.setRightAnchor(borderPane, 0.0);
/*
        hbox.setPrefWidth(400); // Set an initial width for the HBox
        freeInputsTable.prefWidthProperty().bind(hbox.widthProperty());

        nameColumn.prefWidthProperty().bind(hbox.widthProperty().multiply(0.3));
        necessityColumn.prefWidthProperty().bind(hbox.widthProperty().multiply(0.2));
        valueColumn.prefWidthProperty().bind(hbox.widthProperty().multiply(0.49));


            valueColumn.setCellFactory(column -> {
                return new TableCell<Input, Object>() {
                    private final TextField textField = new TextField();
                    private final Spinner<Integer> spinner = new Spinner<>(Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
                    private Object storedValue; // Store the entered value separately
                    {
                        // TextField setup
                        textField.getStyleClass().add("text-field");
                        textField.setPrefWidth(Double.MAX_VALUE);
                        textField.setMaxWidth(Double.MAX_VALUE);

                        textField.setOnAction(event -> {
                            commitEdit(textField.getText());
                        });

                        textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                            if (!isNowFocused) {
                                commitEdit(textField.getText());
                            }
                        });

                        // Spinner setup
                        spinner.setEditable(true);
                        spinner.getEditor().setAlignment(Pos.CENTER_RIGHT);


                        spinner.setOnMouseClicked(event -> {
                            if (spinner.isEditable()) {
                                spinner.increment(0); // Increment by 0 to trigger commitEdit
                            }
                        });

                        spinner.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                            if (!isNowFocused && spinner.isEditable()) {
                                spinner.increment(0); // Increment by 0 to trigger commitEdit
                            }
                        });

                        spinner.getEditor().focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                            if (!isNowFocused && spinner.isEditable()) {
                                commitEdit(spinner.getValue());
                            }
                        });
                    }

                    @Override
                    protected void updateItem(Object item, boolean empty) {
                        System.out.println("in updateItem");

                        if (getTableRow() == null) {
                            super.updateItem(item, empty);
                            setGraphic(null);
                            return;
                        }

                        System.out.println(getTableRow().getItem());
                        System.out.println(item);


                       // if (item == null || empty || getTableRow().getItem() == null) {
                            //
                        if (empty) {
                            setGraphic(null);
                        } else {
                            Input input = (Input) getTableRow().getItem();
                            String simpleName = input.getType().getType().getSimpleName();

                            if (simpleName.equals("String")) {
                                textField.setText(item != null ? item.toString() : "");
                                setGraphic(textField);
                            } else if (simpleName.equals("Integer")) {
                                if (item != null && item instanceof Integer) {
                                    spinner.getValueFactory().setValue((Integer) item);
                                } else {
                                   // spinner.getValueFactory().setValue(0);
                                }

                                setGraphic(spinner);
                            } else {
                                setGraphic(null);
                            }
                        }
                    }

                    @Override
                    public void commitEdit(Object newValue) {
                        super.commitEdit(newValue);

                        Input input = (Input) getTableRow().getItem();
                        updateFreeInputMap(input, newValue);

                        // Check if all mandatory inputs are provided
                        boolean hasAllMandatoryInputs = hasAllMandatoryInputs(freeInputMap);
                        // Enable/disable the button accordingly
                        executeButton.setDisable(!hasAllMandatoryInputs);
                    }

                    private void updateFreeInputMap(Input input, Object newValue) {
                        freeInputMap.put(input.getStepName() + "." + input.getOriginalName(), newValue);
                    }

            };
        });
*/
    }

    public void setMainController(Controller mainController) {
        this.mainController = mainController;
    }

    public void initInputsTable(List<DTOSingleFlowIOData> freeInputs) {
        ObservableList<Input> inputList = FXCollections.observableArrayList();

        // Populate inputList from freeInputs
        freeInputs.forEach(freeInput -> {
            Input input = new Input();
            input.setFinalName(freeInput.getFinalName());
            input.setOriginalName(freeInput.getOriginalName());
            input.setStepName(freeInput.getStepName());
            input.setMandatory(freeInput.getNecessity().toString());
            input.setType(freeInput.getType());
            inputList.add(input);
        });

        for (int i = 0; i < inputList.size(); i++) {
            Input input = inputList.get(i);

            // Create label for the node
            Label label = new Label(input.getFinalName() + " (" + input.getMandatory() +")");
            label.setAlignment(Pos.CENTER_LEFT);
            label.setTextAlignment(TextAlignment.LEFT);
            label.setTextOverrun(OverrunStyle.CLIP); // Clip the text if it exceeds the label width

            // Create text field for the node
            TextField textField = new TextField();

            VBox vbox = new VBox();
            vbox.setAlignment(Pos.CENTER_LEFT);
            vbox.setSpacing(10);
            vbox.getChildren().addAll(label, textField);

            inputValuesHBox.getChildren().add(vbox);
            inputValuesHBox.setSpacing(50);
        }
        gridPane.add(inputValuesHBox, 0, 0);
        gridPane.setHgap(10); // Adjust the horizontal spacing between columns

       /* nameColumn.setCellValueFactory(new PropertyValueFactory<>("finalName"));
        necessityColumn.setCellValueFactory(new PropertyValueFactory<>("mandatory"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));

        freeInputs.forEach(freeInput -> {
            Input input = new Input();
            input.setFinalName(freeInput.getFinalName());
            input.setOriginalName(freeInput.getOriginalName());
            input.setStepName(freeInput.getStepName());
            input.setMandatory(freeInput.getNecessity().toString());
            input.setType(freeInput.getType());
            inputList.add(input);
        });

        System.out.println(inputList);

        freeInputsTable.setEditable(true);
        freeInputsTable.setItems(inputList);*/
    }

    private boolean hasAllMandatoryInputs(Map<String, Object> freeInputMap) {
       /* for (Input input : freeInputsTable.getItems()) {
            if (input.getMandatory().equals("MANDATORY")) {
                String key = input.getStepName() + "." + input.getOriginalName();
                if (!freeInputMap.containsKey(key) || freeInputMap.get(key).equals("")) {
                    return false;
                }
            }
        }*/
        return true;
    }

    @FXML
    void StartExecuteFlowButton(ActionEvent event) {
        DTOFreeInputsFromUser freeInputs = new DTOFreeInputsFromUser(freeInputMap);
        DTOFlowExecution flowExecution = mainController.getSystemEngineInterface().activateFlowByName(mainController.getFlowName(), freeInputs);
        mainController.goToStatisticsTab();

        /*        System.out.println(freeInputMap);
        System.out.println(flowExecution.getFlowName());
        System.out.println(flowExecution.getFlowExecutionResult());
        System.out.println(flowExecution.getFreeInputsList());
        System.out.println(flowExecution.getStepExecutionDataList());*/
    }

}
