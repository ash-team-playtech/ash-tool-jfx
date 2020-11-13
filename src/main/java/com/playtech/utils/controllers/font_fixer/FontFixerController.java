package com.playtech.utils.controllers.font_fixer;

import com.playtech.utils.controllers.IController;
import com.playtech.utils.controllers.status.StatusBarController;
import com.playtech.utils.services.AbstractUtil;
import com.playtech.utils.services.font_fixer.AbstractFontParameters;
import com.playtech.utils.services.font_fixer.FontFixerFactory;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.ResourceBundle;

@Controller
public class FontFixerController implements IController, Initializable {

    @FXML private Pane fontFixer;
    @FXML private Label fileNameLabel;
    @FXML private Label dragBox;
    @FXML private HBox propertiesContainer;
    @FXML private TextField xOffsetField;
    @FXML private TextField yOffsetField;
    @FXML private TextField xAdvancedField;
    @FXML private CheckBox overrideCheckBox;
    @FXML private Button startBtn;

    @Autowired
    private FontFixerFactory fontFixerFactory;

    @Autowired
    @Lazy
    private StatusBarController statusBarController;

    private final Map<AbstractFontParameters.ParameterType, Boolean> fontFixerCorrectInputs = new HashMap<>() {{
        for (AbstractFontParameters.ParameterType parameterType : AbstractFontParameters.ParameterType.values()) {
            put(parameterType, true);
        }
    }};

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        overrideCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> AbstractUtil.setOverride(observable.getValue()));
        fontFixer.visibleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                statusBarController.setStatus(StatusBarController.Status.WAITING_FOR_FONT);
            }
        });
    }

    private int validateDeltaInput(AbstractFontParameters.ParameterType parameterType, TextField textField, String inputValue) {
        int newValue = 0;
        try {
            if (inputValue.length() > 0) {
                newValue = Integer.parseInt(inputValue);
            }
            fontFixerCorrectInputs.put(parameterType, true);
            textField.setId("text-field-base");
        } catch (NumberFormatException e) {
            fontFixerCorrectInputs.put(parameterType, false);
            textField.setId("text-field-incorrect");
        }
        updateStartButtonState();
        return newValue;
    }

    private void updateStartButtonState() {
        boolean isAnyDeltaSpecified = xOffsetField.getText().length() > 0 || yOffsetField.getText().length() > 0 || xAdvancedField.getText().length() > 0;
        boolean isAllInputsCorrect = !fontFixerCorrectInputs.containsValue(false);
        startBtn.setDisable(!(isAnyDeltaSpecified && isAllInputsCorrect));
    }

    @Override
    public void start(ActionEvent actionEvent) {
        startBtn.setDisable(true);
        statusBarController.setStatus(StatusBarController.Status.PROCESSING);
        fontFixerFactory.run();
        statusBarController.setStatus(StatusBarController.Status.CONFIG_CHANGE_FINISHED);
        reset();
        Platform.runLater(() -> {
            try {
                Thread.sleep(5000);
                if (statusBarController.getStatus().equals(StatusBarController.Status.CONFIG_CHANGE_FINISHED)) {
                    statusBarController.setStatus(StatusBarController.Status.WAITING_FOR_FONT);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void reset() {
        xOffsetField.clear();
        yOffsetField.clear();
        xAdvancedField.clear();
        fileNameLabel.setText("empty");
        fileNameLabel.setTextFill(Color.RED);
        dragBox.setDisable(false);
        startBtn.setDisable(true);
        propertiesContainer.setDisable(true);
        overrideCheckBox.setDisable(true);
        overrideCheckBox.selectedProperty().setValue(false);
        fontFixerFactory.reset();
    }

    @FXML
    private void processDraggedFile(DragEvent dragEvent) {
        File file = new LinkedList<>(dragEvent.getDragboard().getFiles()).getFirst();
        if (fontFixerFactory.isValidFile(file.getName())) {
            AbstractUtil.setFilePath(file.getParent() + "\\");
            AbstractUtil.setFileName(file.getName());
            fileNameLabel.setText(file.getName());
            fileNameLabel.setTextFill(Color.GREEN);
            dragBox.setDisable(true);
            addPropertiesListeners();
            propertiesContainer.setDisable(false);
            overrideCheckBox.setDisable(false);
            statusBarController.setStatus(StatusBarController.Status.WAITING_FOR_CONFIGURATION);
        } else {
            statusBarController.setStatus(StatusBarController.Status.INCORRECT_FILE);
            reset();
        }
    }

    private void addPropertiesListeners() {
        xOffsetField.textProperty().addListener((observable, oldValue, newValue) -> fontFixerFactory.getFontFixer().setXOffset(validateDeltaInput(AbstractFontParameters.ParameterType.X_OFFSET, xOffsetField, newValue)));
        yOffsetField.textProperty().addListener((observable, oldValue, newValue) -> fontFixerFactory.getFontFixer().setYOffset(validateDeltaInput(AbstractFontParameters.ParameterType.Y_OFFSET, yOffsetField, newValue)));
        xAdvancedField.textProperty().addListener((observable, oldValue, newValue) -> fontFixerFactory.getFontFixer().setXAdvanced(validateDeltaInput(AbstractFontParameters.ParameterType.X_ADVANCE, xAdvancedField, newValue)));
    }

    @FXML
    private void onDragOver(DragEvent dragEvent) {
        // allow for both copying and moving, whatever user chooses
        if (dragEvent.getGestureSource() != this && dragEvent.getDragboard().hasFiles()) {
            dragEvent.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        dragEvent.consume();
    }
}
