package com.playtech.utils.services;

import com.playtech.utils.services.font_fixer.GpasFontFixer;
import com.playtech.utils.services.font_fixer.NgmFontFixer;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UtilFactory implements FactoryBean<Util> {

    @Value("${util.id}")
    private int serviceId;

    @Autowired
    private Renamer renamer;

    @Autowired
    private LinesParser linesParser;

    @Autowired
    private NgmFontFixer ngmFontFixer;

    @Autowired
    private GpasFontFixer gpasFontFixer;

    @Autowired
    private DuplicatesRemover duplicatesRemover;

    @Override
    public Util getObject() {
        switch (serviceId) {
            case 1: return renamer;
            case 2: return linesParser;
            case 3: return ngmFontFixer;
            case 4: return gpasFontFixer;
            default: return duplicatesRemover;
        }
    }

    @Override
    public Class<?> getObjectType() {
        return Util.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
