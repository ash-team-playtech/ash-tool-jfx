package com.playtech.utils.controllers;

import com.playtech.utils.controllers.font_fixer.FontFixerController;
import com.playtech.utils.services.UtilType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ControllerFactory {

    @Autowired
    private List<IController> utilControllers;

    @Autowired
    private FontFixerController fontFixerController;

    public IController getController(UtilType utilType) {
        /*switch (utilType) {
            case UtilType.LINES_PARSER: return linesParserController;
            case UtilType.FONT_FIXER: return fontFixerController;
            default: return fontFixerController;
        }*/
        return fontFixerController;
    }

    public List<IController> getAllControllers() {
        return utilControllers;
    }
}
