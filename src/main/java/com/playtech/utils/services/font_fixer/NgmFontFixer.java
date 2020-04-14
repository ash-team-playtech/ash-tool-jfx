package com.playtech.utils.services.font_fixer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NgmFontFixer extends AbstractFontFixer {

    @Autowired
    public NgmFontFixer(NgmFontParameters fontParameters) {
        super(fontParameters);
    }

    @Override
    @Value("${ngm.font-fixer.file-name}")
    public void setFontFileName(String fontFileName) {
        this.fontFileName = fontFileName;
    }

    @Override
    @Value("${ngm.font-fixer.line-config}")
    public void setConfigLineBeginning(String configLineBeginning) {
        this.configLineBeginning = configLineBeginning;
    }

    @Override
    @Value("${ngm.font-fixer.param-separator}")
    public void setParametersSeparator(String parametersSeparator) {
        this.parametersSeparator = parametersSeparator;
    }
}