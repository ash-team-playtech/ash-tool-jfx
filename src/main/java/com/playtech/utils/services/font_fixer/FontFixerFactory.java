package com.playtech.utils.services.font_fixer;

import com.playtech.utils.services.AbstractUtil;
import com.playtech.utils.services.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FontFixerFactory implements Util {

    enum FileType {
        FNT, XML
    }

    @Autowired
    private NgmFontFixer ngmFontFixer;

    @Autowired
    private GpasFontFixer gpasFontFixer;

    public AbstractFontFixer getFontFixer() {
        FileType fileType = FileType.valueOf(AbstractUtil.getFileName().substring(AbstractUtil.getFileName().indexOf(".") + 1).toUpperCase());
        return fileType.equals(FileType.FNT) ? ngmFontFixer : gpasFontFixer;
    }

    @Override
    public void run() {
        getFontFixer().run();
    }
}
