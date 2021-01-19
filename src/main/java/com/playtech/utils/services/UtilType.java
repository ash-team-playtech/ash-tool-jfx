package com.playtech.utils.services;

public enum UtilType {
    FONT_FIXER("fontFixer", "Font Fixer"),
    DIMENSIONS_CHECKER("dimensionsChecker", "Dimensions Checker");

    private final String utilId;
    private final String utilName;

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
