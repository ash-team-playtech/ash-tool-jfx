package com.playtech.utils.controllers;

import com.playtech.utils.services.UtilType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainController {

    @FXML
    private ChoiceBox utilsChoiceBox;

    @FXML
    private Pane utilsContainer;

    private Map<UtilType, Pane> utilContainers = new HashMap<>();

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
    }

    public void processDraggedFile(DragEvent dragEvent) {

    }

    public void processOverrideConfig(ActionEvent actionEvent) {

    }

    public void execute(ActionEvent actionEvent) {

    }

    private void hideAllUtilContainers() {
        for (Pane container : utilContainers.values()) {
            container.setVisible(false);
        }
    }
}
