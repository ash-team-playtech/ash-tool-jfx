package com.playtech.utils.controllers.dimensions_checker;

import com.playtech.utils.controllers.IController;
import com.playtech.utils.services.dimentions_checker.DimensionsChecker;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Controller
public class DimensionsCheckerController implements IController, Initializable {

    @FXML private Label statusBar;
    @FXML private TextField projectDirectory;
    @FXML private TextArea resultsField;
    @FXML private Button startBtn;

    @Autowired
    private DimensionsChecker dimensionsCheckerImpl;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @Override
    public void start(ActionEvent actionEvent) {
        startBtn.setDisable(true);
        Platform.runLater(() -> {
            List<String> results = dimensionsCheckerImpl.run();
            if (results.isEmpty()) {
                // update status
            } else {
                resultsField.setText(String.join("\n", results));
            }
        });
    }

    @Override
    public void reset() {
        projectDirectory.clear();
        resultsField.clear();
        startBtn.setDisable(true);
    }

    @FXML
    private void chooseDirectory(ActionEvent actionEvent) {
        reset();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(projectDirectory.getScene().getWindow());
        projectDirectory.setText(selectedDirectory.getAbsolutePath());
        dimensionsCheckerImpl.setProjectPath(selectedDirectory.getAbsolutePath());
        startBtn.setDisable(false);
    }
}
