package com.playtech.utils.services.font_fixer;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractFontParameters {

    public enum ParameterType {
        X_OFFSET, Y_OFFSET, X_ADVANCE
    }

    protected Map<ParameterType, Map<String, Integer>> parameters = new HashMap<>();

    public String getParameterKey(ParameterType parameterType) {
        return parameters.get(parameterType).entrySet().iterator().next().getKey();
    }

    public int getParameterValue(ParameterType parameterType) {
        return parameters.get(parameterType).entrySet().iterator().next().getValue();
    }

    public void setParameterValue(ParameterType parameterType, int deltaValue) {
        parameters.get(parameterType).entrySet().iterator().next().setValue(deltaValue);
    }

    protected Map<String, Integer> getParameterDataMap(String pattern, int delta) {
        Map<String, Integer> data = new HashMap<>();
        data.put(pattern, delta);
        return data;
    }

    protected abstract void setXOffsetData(String xOffsetPattern, int xOffsetDelta);

    protected abstract void setYOffsetData(String yOffsetPattern, int yOffsetDelta);

    protected abstract void setXAdvanceData(String xAdvancePattern, int xAdvanceDelta);
}
