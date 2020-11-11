package com.playtech.utils.controllers.font_fixer;

import com.playtech.utils.services.font_fixer.FontFixerFactory;
import javafx.scene.control.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StatusBarController {

    public enum Status {
        WAITING_FOR_FONT("Waiting for the font file", Style.INFO),
        WAITING_FOR_CONFIGURATION("Waiting for desired configuration change to be entered", Style.INFO),
        PROCESSING("Processing...", Style.INFO),
        DONE("Configuration change is successfully finished", Style.SUCCESSFUL),
        INCORRECT_FILE("Incorrect file type. Supported extensions: " + FontFixerFactory.getSupportedFileTypes(), Style.ERROR);

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
        init();
    }

    private void init() {
        setStatus(Status.WAITING_FOR_FONT);
    }

    public Status getStatus() {
        return currentStatus;
    }

    public void setStatus(Status status) {
        currentStatus = status;
        statusBar.setText(status.getMessage());
        statusBar.setId(status.getTextStyle().getStyleId());
    }
}
