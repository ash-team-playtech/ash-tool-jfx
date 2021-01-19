package com.playtech.utils.controllers;

import com.playtech.utils.controllers.status.StatusBarController;
import com.playtech.utils.services.UtilType;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.*;

@Controller
public class MainController implements Initializable {

    @Autowired
    private List<IController> utilControllers;

    @Autowired
    @Lazy
    private StatusBarController statusBarController;

    private Map<UtilType, Pane> utilContainers = new HashMap<>();

    @FXML private ChoiceBox<UtilType> utilsChoiceBox;
    @FXML private Pane utilsContainer;
    @FXML private Label statusBar;
    @FXML private HBox statusBarContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for (Node child : utilsContainer.getChildren()) {
            String id = child.getId();
            Arrays.stream(UtilType.values())
                    .filter(utilType -> utilType.getUtilId().equals(id))
                    .findFirst()
                    .ifPresent(util -> utilContainers.put(util, (Pane) child));
        }
        utilsChoiceBox.getItems().addAll(FXCollections.observableArrayList(UtilType.values()));
        utilsChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            updateUtilContainersVisibility(newValue);
            resetAllUtilContainers();
        });
    }

    private void updateUtilContainersVisibility(UtilType selectedUtil) {
        statusBarContainer.setVisible(true);
        statusBarController.reset();
        utilContainers.values().forEach(container -> container.setVisible(false));
        utilContainers.get(selectedUtil).setVisible(true);
    }

    private void resetAllUtilContainers() {
        utilControllers.forEach(IController::reset);
    }

    @Bean
    public Label getStatusBar() {
        return statusBar;
    }
}
