package com.playtech.utils.services;

import com.playtech.utils.services.font_fixer.FontFixerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UtilFactory {

    @Autowired
    private LinesParser linesParser;

    @Autowired
    private FontFixerFactory fontFixerFactory;

    @Autowired
    private DuplicatesRemover duplicatesRemover;

    public Util getUtil(UtilType utilType) {
        switch (utilType.getUtilID()) {
            case 0: return linesParser;
            case 1: return fontFixerFactory;
            default: return duplicatesRemover;
        }
    }
}
