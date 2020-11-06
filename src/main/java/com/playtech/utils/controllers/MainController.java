package com.playtech.utils.controllers;

import com.playtech.utils.services.UtilType;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.Pane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

@Controller
public class MainController implements Initializable {

    @Autowired
    private ControllerFactory controllerFactory;

    private Map<UtilType, Pane> utilContainers = new HashMap<>();

    @FXML private ChoiceBox<UtilType> utilsChoiceBox;
    @FXML private Pane utilsContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for (Node child : utilsContainer.getChildren()) {
            String id = child.getId();
            Arrays.stream(UtilType.values()).filter(utilType -> utilType.getUtilId().equals(id))
                    .findFirst().ifPresent(util -> utilContainers.put(util, (Pane) child));
        }
        utilsChoiceBox.getItems().addAll(FXCollections.observableArrayList(UtilType.values()));
        utilsChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            updateUtilContainersVisibility(newValue);
            resetAllUtilContainers();
        });
    }

    private void updateUtilContainersVisibility(UtilType selectedUtil) {
        utilContainers.values().forEach(container -> container.setVisible(false));
        utilContainers.get(selectedUtil).setVisible(true);
    }

    private void resetAllUtilContainers() {
        controllerFactory.getAllControllers().forEach(IController::reset);
    }
}
