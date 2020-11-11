package com.playtech.utils.services.font_fixer;

import com.playtech.utils.services.AbstractUtil;
import com.playtech.utils.services.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

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
        FileType fileType = FileType.valueOf(getFileExtension(AbstractUtil.getFileName()));
        return fileType.equals(FileType.FNT) ? ngmFontFixer : gpasFontFixer;
    }

    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.indexOf(".") + 1).toUpperCase();
    }

    public boolean isValidFile(String fileName) {
        return Arrays.stream(FileType.values()).anyMatch(fileType -> fileType.name().equals(getFileExtension(fileName)));
    }

    public static String getSupportedFileTypes() {
        return Arrays.stream(FileType.values())
                .map(FileType::name)
                .collect(Collectors.joining(", "));
    }

    @Override
    public void run() {
        getFontFixer().run();
    }

    @Override
    public void reset() {
        getFontFixer().reset();
    }
}
