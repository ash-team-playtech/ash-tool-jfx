package com.playtech.utils.services.font_fixer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GpasFontFixer extends AbstractFontFixer {

    @Autowired
    public GpasFontFixer(GpasFontParameters fontParameters) {
        super(fontParameters);
    }

    @Override
    @Value("${gpas.font-fixer.line-config}")
    public void setConfigLineBeginning(String configLineBeginning) {
        this.configLineBeginning = configLineBeginning;
    }

    @Override
    @Value("${gpas.font-fixer.param-separator}")
    public void setParametersSeparator(String parametersSeparator) {
        this.parametersSeparator = parametersSeparator;
    }
}