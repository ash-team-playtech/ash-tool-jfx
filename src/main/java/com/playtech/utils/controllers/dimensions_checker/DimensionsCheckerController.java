package com.playtech.utils.controllers.dimensions_checker;

import com.playtech.utils.controllers.IController;
import com.playtech.utils.controllers.status.StatusBarController;
import com.playtech.utils.services.dimentions_checker.DimensionsChecker;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Controller
public class DimensionsCheckerController implements IController, Initializable {

    @FXML private Pane dimensionsChecker;
    @FXML private TextField projectDirectory;
    @FXML private Button chooseDirectoryBtn;
    @FXML private TextArea resultsField;
    @FXML private Button startBtn;

    @Autowired
    private DimensionsChecker dimensionsCheckerImpl;

    @Autowired
    @Lazy
    private StatusBarController statusBarController;

    private Thread executionThread;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dimensionsChecker.visibleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                statusBarController.setStatus(StatusBarController.Status.WAITING_FOR_PROJECT_DIRECTORY);
            }
        });
    }

    @Override
    public void start(ActionEvent actionEvent) {
        startBtn.setDisable(true);
        projectDirectory.setDisable(true);
        chooseDirectoryBtn.setDisable(true);
        statusBarController.setStatus(StatusBarController.Status.PROCESSING);

        if (executionThread != null) {
            executionThread.interrupt();
        }
        executionThread = new Thread(() -> {
            List<String> results = dimensionsCheckerImpl.run();
            Platform.runLater(() -> {
                projectDirectory.setDisable(false);
                chooseDirectoryBtn.setDisable(false);
                if (results.isEmpty()) {
                    statusBarController.setStatus(StatusBarController.Status.ALL_IMG_CORRECT);
                } else {
                    statusBarController.setStatus(StatusBarController.Status.PROBLEMATIC_FILES_FOUND);
                    resultsField.setText(String.join("\n", results));
                }
            });
        });
        executionThread.start();
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
