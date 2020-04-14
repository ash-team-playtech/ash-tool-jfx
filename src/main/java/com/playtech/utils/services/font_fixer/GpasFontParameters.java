package com.playtech.utils.services.font_fixer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.LinkedList;

@Component
public class GpasFontParameters extends AbstractFontParameters {

    @Override
    @Value("${gpas.font-fixer.xoffset.pattern}")
    public void setXOffsetData(String xOffsetPattern, @Value("${gpas.font-fixer.xoffset.delta}") String xOffsetDelta) {
        parameters.put(ParameterType.X_OFFSET, new LinkedList<>(Arrays.asList(xOffsetPattern, xOffsetDelta)));
    }

    @Override
    @Value("${gpas.font-fixer.yoffset.pattern}")
    public void setYOffsetData(String yOffsetPattern, @Value("${gpas.font-fixer.yoffset.delta}") String yOffsetDelta) {
        parameters.put(ParameterType.Y_OFFSET, new LinkedList<>(Arrays.asList(yOffsetPattern, yOffsetDelta)));
    }

    @Override
    @Value("${gpas.font-fixer.xadvance.pattern}")
    public void setXAdvanceData(String xAdvancePattern, @Value("${gpas.font-fixer.xadvance.delta}") String xAdvanceDelta) {
        parameters.put(ParameterType.X_ADVANCE, new LinkedList<>(Arrays.asList(xAdvancePattern, xAdvanceDelta)));
    }
}
