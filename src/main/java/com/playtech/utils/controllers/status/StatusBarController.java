package com.playtech.utils.controllers.status;

import com.playtech.utils.services.font_fixer.FontFixerFactory;
import javafx.scene.control.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class StatusBarController {

    public enum Status {
        WAITING_FOR_FONT("Waiting for the font file", StatusBarController.Style.INFO),
        WAITING_FOR_CONFIGURATION("Waiting for desired configuration change to be entered", StatusBarController.Style.INFO),
        WAITING_FOR_PROJECT_DIRECTORY("Waiting for project directory to be checked", StatusBarController.Style.INFO),
        PRESS_START_TO_CONTINUE("Press START to continue", StatusBarController.Style.INFO),
        PROCESSING("Processing...", StatusBarController.Style.INFO),
        CONFIG_CHANGE_FINISHED("Configuration change is successfully finished", StatusBarController.Style.SUCCESSFUL),
        ALL_IMG_CORRECT("All images under specified directory are correct", StatusBarController.Style.SUCCESSFUL),
        INCORRECT_FILE("Incorrect file type. Supported extensions: " + FontFixerFactory.getSupportedFileTypes(), StatusBarController.Style.ERROR),
        INCORRECT_DIRECTORY("Incorrect project directory", StatusBarController.Style.ERROR),
        PROBLEMATIC_FILES_FOUND("There are some problematic files. See results field", StatusBarController.Style.ERROR),
        EMPTY("", StatusBarController.Style.INFO);

        private final String message;
        private final Style textStyle;

        Status(String message, Style textStyle) {
            this.message = message;
            this.textStyle = textStyle;
        }

        public String getMessage() {
            return message;
        }

        public Style getTextStyle() {
            return textStyle;
        }
    }

    private enum Style {
        INFO("status-bar-info"),
        SUCCESSFUL("status-bar-successful"),
        ERROR("status-bar-error");

        private final String styleId;

        Style(String styleId) {
            this.styleId = styleId;
        }

        public String getStyleId() {
            return styleId;
        }
    }

    private final Label statusBar;
    private Status currentStatus;

    @Autowired
    public StatusBarController(Label statusBar) {
        this.statusBar = statusBar;
    }

    public Status getStatus() {
        return currentStatus;
    }

    public void setStatus(Status status) {
        currentStatus = status;
        statusBar.setText(status.getMessage());
        statusBar.setId(status.getTextStyle().getStyleId());
    }

    public void reset() {
        setStatus(Status.EMPTY);
    }
}
