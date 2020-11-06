package com.playtech.utils.services;

import com.playtech.utils.services.duplicates_remover.DuplicatesRemover;
import com.playtech.utils.services.font_fixer.FontFixerFactory;
import com.playtech.utils.services.lines_parser.LinesParser;
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
        switch (utilType) {
            case LINES_PARSER: return linesParser;
            case FONT_FIXER: return fontFixerFactory;
            default: return duplicatesRemover;
        }
    }
}
