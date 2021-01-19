package com.playtech.utils.controllers.dimensions_checker;

import com.playtech.utils.controllers.IController;
import com.playtech.utils.controllers.status.StatusBarController;
import com.playtech.utils.services.dimentions_checker.CustomDimension;
import com.playtech.utils.services.dimentions_checker.DimensionsChecker;
import com.playtech.utils.services.dimentions_checker.ProblematicImage;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.ResourceBundle;

@Controller
public class DimensionsCheckerController implements IController, Initializable {

    @FXML private Pane dimensionsChecker;
    @FXML private TextField projectDirectory;
    @FXML private Button chooseDirectoryBtn;
    @FXML private TableView<ProblematicImage> resultsTable;
    @FXML private TableColumn<ProblematicImage, Path> filePath;
    @FXML private TableColumn<ProblematicImage, CustomDimension> expectedDimension;
    @FXML private TableColumn<ProblematicImage, CustomDimension> actualDimension;
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
        projectDirectory.setOnKeyTyped(mouseEvent -> {
            statusBarController.setStatus(StatusBarController.Status.PRESS_START_TO_CONTINUE);
            startBtn.setDisable(false);
        });

        filePath.setCellValueFactory(new PropertyValueFactory<>("filePath"));
        expectedDimension.setCellValueFactory(new PropertyValueFactory<>("expectedDimension"));
        actualDimension.setCellValueFactory(new PropertyValueFactory<>("actualDimension"));

        initFilePathColumn();
    }

    /** Used to fill all available space in the table by this column */
    private void initFilePathColumn() {
        double width = expectedDimension.widthProperty().get();
        width += actualDimension.widthProperty().get();
        filePath.prefWidthProperty().bind(resultsTable.widthProperty().subtract(width));
    }

    @Override
    public void start(ActionEvent actionEvent) {
        clearPreviousResults();
        changeUIState(false);

        if (isProjectDirectoryValid()) {
            statusBarController.setStatus(StatusBarController.Status.PROCESSING);

            if (executionThread != null) {
                executionThread.interrupt();
            }
            executionThread = new Thread(() -> {
                List<ProblematicImage> results = dimensionsCheckerImpl.run();
                Platform.runLater(() -> {
                    changeUIState(true);
                    if (results.isEmpty()) {
                        statusBarController.setStatus(StatusBarController.Status.ALL_IMG_CORRECT);
                    } else {
                        statusBarController.setStatus(StatusBarController.Status.PROBLEMATIC_FILES_FOUND);
                        resultsTable.getItems().setAll(results);
                    }
                });
            });
            executionThread.start();
        }
    }

    private boolean isProjectDirectoryValid() {
        String path = projectDirectory.getText();
        File selectedDirectory = new File(path);
        if (selectedDirectory.exists()) {
            dimensionsCheckerImpl.setProjectPath(selectedDirectory.getAbsolutePath());
            return true;
        } else {
            statusBarController.setStatus(StatusBarController.Status.INCORRECT_DIRECTORY);
            changeUIState(true);
            return false;
        }
    }

    private void changeUIState(boolean enabled) {
        projectDirectory.setDisable(!enabled);
        chooseDirectoryBtn.setDisable(!enabled);
    }

    @Override
    public void reset() {
        projectDirectory.clear();
        clearPreviousResults();
    }

    private void clearPreviousResults() {
        resultsTable.getItems().clear();
        dimensionsCheckerImpl.reset();
        startBtn.setDisable(true);
        changeUIState(true);
    }

    @FXML
    private void chooseDirectory(ActionEvent actionEvent) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(projectDirectory.getScene().getWindow());
        if (selectedDirectory != null) {
            statusBarController.setStatus(StatusBarController.Status.PRESS_START_TO_CONTINUE);
            projectDirectory.setText(selectedDirectory.getAbsolutePath());
            startBtn.setDisable(false);
        }
    }
}
