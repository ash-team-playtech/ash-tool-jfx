package com.playtech.utils.services;

public enum UtilType {
    LINES_PARSER(0),
    FONT_FIXER(1),
    DUPLICATES_REMOVER(2);

    private int utilID;

    UtilType(int utilID) {
        this.utilID = utilID;
    }

    public int getUtilID() {
        return utilID;
    }

    public static UtilType getTypeById(int id) {
        for (UtilType utilType : values()) {
            if (utilType.getUtilID() == id) {
                return utilType;
            }
        }
        return null;
    }
}
