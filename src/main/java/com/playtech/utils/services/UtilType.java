package com.playtech.utils.services;

public enum UtilType {
    RENAMER(0),
    LINES_PARSER(1),
    FONT_FIXER(2),
    DUPLICATES_REMOVER(3);

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
