package com.playtech.utils.controllers;

import com.playtech.utils.services.UtilFactory;
import com.playtech.utils.services.UtilType;
import com.playtech.utils.services.font_fixer.AbstractFontParameters;
import com.playtech.utils.services.font_fixer.FontFixerFactory;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class MainController {

    @Autowired
    private UtilFactory utilFactory;

    @FXML private ChoiceBox utilsChoiceBox;
    @FXML private Pane utilsContainer;
    @FXML public Button startBtn;

    @FXML public Label fontFileNameLabel;
    @FXML public TextField xOffsetField;
    @FXML public TextField yOffsetField;
    @FXML public TextField xAdvancedField;
    @FXML public Label dragBox;
    @FXML public CheckBox overrideCheckBox;

    private Map<UtilType, Pane> utilContainers = new HashMap<>();

    private Map<AbstractFontParameters.ParameterType, Boolean> fontFixerCorrectInputs = new HashMap<AbstractFontParameters.ParameterType, Boolean>() {{
        for (AbstractFontParameters.ParameterType parameterType : AbstractFontParameters.ParameterType.values()) {
            put(parameterType, true);
        }
    }};

    @FXML
    public void initialize() {
        for (Node child : utilsContainer.getChildren()) {
            utilContainers.put(UtilType.getTypeById(Integer.parseInt(child.getId())), (Pane) child);
        }
    }

    @PostConstruct
    public void init() {
        utilsChoiceBox.getItems().addAll(FXCollections.observableArrayList(UtilType.values()));
        utilsChoiceBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            hideAllUtilContainers();
            utilContainers.get(UtilType.getTypeById((Integer) newValue)).setVisible(true);
        });
        overrideCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> ((FontFixerFactory) utilFactory.getUtil(UtilType.FONT_FIXER)).getFontFixer().setOverride(observable.getValue()));
        xOffsetField.textProperty().addListener((observable, oldValue, newValue) -> ((FontFixerFactory) utilFactory.getUtil(UtilType.FONT_FIXER)).getFontFixer().setXOffset(validateDeltaInput(AbstractFontParameters.ParameterType.X_OFFSET, xOffsetField, newValue)));
        yOffsetField.textProperty().addListener((observable, oldValue, newValue) -> ((FontFixerFactory) utilFactory.getUtil(UtilType.FONT_FIXER)).getFontFixer().setYOffset(validateDeltaInput(AbstractFontParameters.ParameterType.Y_OFFSET, yOffsetField, newValue)));
        xAdvancedField.textProperty().addListener((observable, oldValue, newValue) -> ((FontFixerFactory) utilFactory.getUtil(UtilType.FONT_FIXER)).getFontFixer().setXAdvanced(validateDeltaInput(AbstractFontParameters.ParameterType.X_ADVANCE, xAdvancedField, newValue)));
    }

    private void hideAllUtilContainers() {
        for (Pane container : utilContainers.values()) {
            container.setVisible(false);
        }
    }

    public void execute(ActionEvent actionEvent) {
        utilFactory.getUtil(((UtilType) utilsChoiceBox.getValue())).execute();
    }

    private int validateDeltaInput(AbstractFontParameters.ParameterType parameterType, TextField textField, String inputValue) {
        int newValue = 0;
        try {
            if (inputValue.length() > 0) {
                newValue = Integer.parseInt(inputValue);
            }
            fontFixerCorrectInputs.put(parameterType, true);
            textField.setStyle("-fx-border-color: none;");
        } catch (NumberFormatException e) {
            fontFixerCorrectInputs.put(parameterType, false);
            textField.setStyle("-fx-border-color: red;");
        }
        updateStartButtonState();
        return newValue;
    }

    private void updateStartButtonState() {
        boolean isAnyDeltaSpecified = xOffsetField.getText().length() > 0 || yOffsetField.getText().length() > 0 || xAdvancedField.getText().length() > 0;
        boolean isAllInputsCorrect = !fontFixerCorrectInputs.values().contains(false);
        startBtn.setDisable(!(isAnyDeltaSpecified && isAllInputsCorrect));
    }

    public void processDraggedFile(DragEvent dragEvent) {
        File file = new LinkedList<>(dragEvent.getDragboard().getFiles()).getFirst();
        String fileType = file.getName().substring(file.getName().indexOf(".") + 1);
        FontFixerFactory fontFixerFactory = (FontFixerFactory) utilFactory.getUtil(((UtilType) utilsChoiceBox.getValue()));
        fontFixerFactory.setFileType(fileType);
        fontFixerFactory.getFontFixer().setFontFilePath(file.getParent() + "\\");
        fontFixerFactory.getFontFixer().setFontFileName(file.getName());
        fontFileNameLabel.setText(file.getName());
        fontFileNameLabel.setTextFill(Color.GREEN);
        dragBox.setDisable(true);
        xOffsetField.setDisable(false);
        yOffsetField.setDisable(false);
        xAdvancedField.setDisable(false);
        overrideCheckBox.setDisable(false);
    }

    public void onDragOver(DragEvent dragEvent) {
        // allow for both copying and moving, whatever user chooses
        if (dragEvent.getGestureSource() != this && dragEvent.getDragboard().hasFiles()) {
            dragEvent.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        dragEvent.consume();
    }
}
