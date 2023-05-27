package javafx.flowExecutionTab;

import dto.DTOSingleFlowIOData;
import javafx.Controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.util.List;

public class FlowExecutionTabController {
    private Controller mainController;
    @FXML
    TableView freeInputsTable;
    @FXML
    private TableColumn<Input, String> nameColumn;

    @FXML
    private TableColumn<Input, String> necessityColumn;

    @FXML
    private TableColumn<Input, String> valueColumn;

    @FXML
    private HBox hbox;

    @FXML
    private BorderPane borderPane;

    @FXML
    public void initialize() {
        AnchorPane.setTopAnchor(borderPane, 0.0);
        AnchorPane.setBottomAnchor(borderPane, 0.0);
        AnchorPane.setLeftAnchor(borderPane, 0.0);
        AnchorPane.setRightAnchor(borderPane, 0.0);

        hbox.setPrefWidth(400); // Set an initial width for the HBox
        freeInputsTable.prefWidthProperty().bind(hbox.widthProperty());

        nameColumn.prefWidthProperty().bind(hbox.widthProperty().multiply(0.3));
        necessityColumn.prefWidthProperty().bind(hbox.widthProperty().multiply(0.2));
        valueColumn.prefWidthProperty().bind(hbox.widthProperty().multiply(0.49));

        // Resize TextField with column width
        valueColumn.setCellFactory(column -> {
            return new TableCell<Input, String>() {
                private final TextField textField = new TextField();
                {
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
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty) {
                        setGraphic(null);
                    } else {
                        setText(null);
                        textField.setText(item);
                        setGraphic(textField);
                    }
                }

                @Override
                public void startEdit() {
                    super.startEdit();

                    if (!isEmpty()) {
                        textField.setText(getItem());
                        setGraphic(textField);
                        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                        textField.requestFocus();
                    }
                }

                @Override
                public void cancelEdit() {
                    super.cancelEdit();

                    setText(getItem());
                    setGraphic(null);
                    setContentDisplay(ContentDisplay.TEXT_ONLY);
                }

                @Override
                public void commitEdit(String newValue) {
                    super.commitEdit(newValue);

                    Input item = (Input) getTableRow().getItem();
                    item.setValue(newValue);

                    setText(newValue);
                    setGraphic(null);
                    setContentDisplay(ContentDisplay.TEXT_ONLY);
                    getTableView().refresh();
                }

            };
        });
    }


    public void setMainController(Controller mainController) {
        this.mainController = mainController;
    }

    public void initInputsTable(List<DTOSingleFlowIOData> freeInputs) {
        ObservableList<Input> inputList = FXCollections.observableArrayList();

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        necessityColumn.setCellValueFactory(new PropertyValueFactory<>("mandatory"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));

        freeInputs.forEach(freeInput -> {
            Input input = new Input();
            input.setName(freeInput.getFinalName());
            input.setMandatory(freeInput.getNecessity().toString());
            input.setValue(freeInput.getValue());
            inputList.add(input);
        });

        freeInputsTable.setEditable(true);
        freeInputsTable.setItems(inputList);
    }
}