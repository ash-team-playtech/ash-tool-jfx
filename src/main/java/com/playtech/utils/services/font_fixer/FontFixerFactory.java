package com.playtech.utils.services.font_fixer;

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

    private FileType fileType;

    public void setFileType(String fileType) {
        this.fileType = FileType.valueOf(fileType.toUpperCase());
    }

    public AbstractFontFixer getFontFixer() {
        return fileType.equals(FileType.FNT) ? ngmFontFixer : gpasFontFixer;
    }

    @Override
    public void execute() {
        getFontFixer().execute();
    }
}
