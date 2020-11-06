package com.playtech.utils.controllers;

import com.playtech.utils.services.AbstractUtil;
import com.playtech.utils.services.UtilFactory;
import com.playtech.utils.services.UtilType;
import com.playtech.utils.services.font_fixer.AbstractFontParameters;
import com.playtech.utils.services.font_fixer.FontFixerFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.ResourceBundle;

@Controller
public class FontFixerController implements IController, Initializable {

    @FXML private Label fileNameLabel;
    @FXML private Label dragBox;
    @FXML private VBox propertiesContainer;
    @FXML private TextField xOffsetField;
    @FXML private TextField yOffsetField;
    @FXML private TextField xAdvancedField;
    @FXML private CheckBox overrideCheckBox;
    @FXML private Button startBtn;

    @Autowired
    private UtilFactory utilFactory;

    private Map<AbstractFontParameters.ParameterType, Boolean> fontFixerCorrectInputs = new HashMap<AbstractFontParameters.ParameterType, Boolean>() {{
        for (AbstractFontParameters.ParameterType parameterType : AbstractFontParameters.ParameterType.values()) {
            put(parameterType, true);
        }
    }};

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        overrideCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> AbstractUtil.setOverride(observable.getValue()));
        xOffsetField.textProperty().addListener((observable, oldValue, newValue) -> ((FontFixerFactory) utilFactory.getUtil(UtilType.FONT_FIXER)).getFontFixer().setXOffset(validateDeltaInput(AbstractFontParameters.ParameterType.X_OFFSET, xOffsetField, newValue)));
        yOffsetField.textProperty().addListener((observable, oldValue, newValue) -> ((FontFixerFactory) utilFactory.getUtil(UtilType.FONT_FIXER)).getFontFixer().setYOffset(validateDeltaInput(AbstractFontParameters.ParameterType.Y_OFFSET, yOffsetField, newValue)));
        xAdvancedField.textProperty().addListener((observable, oldValue, newValue) -> ((FontFixerFactory) utilFactory.getUtil(UtilType.FONT_FIXER)).getFontFixer().setXAdvanced(validateDeltaInput(AbstractFontParameters.ParameterType.X_ADVANCE, xAdvancedField, newValue)));
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

    @Override
    public void start(ActionEvent actionEvent) {

    }

    @Override
    public void reset() {

    }

    @FXML
    private void processDraggedFile(DragEvent dragEvent) {
        File file = new LinkedList<>(dragEvent.getDragboard().getFiles()).getFirst();
        if (((FontFixerFactory) utilFactory.getUtil(UtilType.FONT_FIXER)).isValidFile(file.getName())) {
            AbstractUtil.setFilePath(file.getParent() + "\\");
            AbstractUtil.setFileName(file.getName());
            fileNameLabel.setText(file.getName());
            fileNameLabel.setTextFill(Color.GREEN);
            dragBox.setDisable(true);
            propertiesContainer.setDisable(false);
            overrideCheckBox.setDisable(false);
        } else {
            dragBox.setText("Incorrect file type. Supported extensions: " + ((FontFixerFactory) utilFactory.getUtil(UtilType.FONT_FIXER)).getSupportedFileTypes());
            dragBox.setTextFill(Color.GREEN);
        }
    }

    @FXML
    private void onDragOver(DragEvent dragEvent) {
        // allow for both copying and moving, whatever user chooses
        if (dragEvent.getGestureSource() != this && dragEvent.getDragboard().hasFiles()) {
            dragEvent.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        dragEvent.consume();
    }

    private void updateStartButtonState() {
        boolean isAnyDeltaSpecified = xOffsetField.getText().length() > 0 || yOffsetField.getText().length() > 0 || xAdvancedField.getText().length() > 0;
        boolean isAllInputsCorrect = !fontFixerCorrectInputs.values().contains(false);
        startBtn.setDisable(!(isAnyDeltaSpecified && isAllInputsCorrect));
    }
}
