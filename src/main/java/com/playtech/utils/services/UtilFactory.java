package com.playtech.utils.services;

import com.playtech.utils.services.dimentions_checker.DimensionsChecker;
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

    @Autowired
    private DimensionsChecker dimensionsChecker;

    public Util getUtil(UtilType utilType) {
        switch (utilType) {
            case LINES_PARSER: return linesParser;
            case FONT_FIXER: return fontFixerFactory;
            case DIMENSIONS_CHECKER: return dimensionsChecker;
            default: return duplicatesRemover;
        }
    }
}
