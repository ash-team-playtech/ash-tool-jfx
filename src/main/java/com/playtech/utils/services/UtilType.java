package com.playtech.utils.services;

public enum UtilType {
    LINES_PARSER("linesParser", "Lines Parser"),
    FONT_FIXER("fontFixer", "Font Fixer"),
    DUPLICATES_REMOVER("duplicatesRemover", "Duplicates Remover");

    private String utilId;
    private String utilName;

    UtilType(String utilId, String utilName) {
        this.utilId = utilId;
        this.utilName = utilName;
    }

    public String getUtilId() {
        return utilId;
    }

    public String getUtilName() {
        return utilName;
    }

    @Override
    public String toString() {
        return utilName;
    }
}
