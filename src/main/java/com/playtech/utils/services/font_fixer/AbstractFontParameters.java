package com.playtech.utils.services.font_fixer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public abstract class AbstractFontParameters {

    public enum ParameterType {
        X_OFFSET, Y_OFFSET, X_ADVANCE
    }

    protected Map<ParameterType, LinkedList<String>> parameters = new HashMap<>();

    public String getParameterKey(ParameterType parameterType) {
        return parameters.get(parameterType).getFirst();
    }

    public int getParameterValue(ParameterType parameterType) {
        return Integer.parseInt(parameters.get(parameterType).getLast());
    }

    protected abstract void setXOffsetData(String xOffsetPattern, String xOffsetDelta);

    protected abstract void setYOffsetData(String yOffsetPattern, String yOffsetDelta);

    protected abstract void setXAdvanceData(String xAdvancePattern, String xAdvanceDelta);
}
