package com.playtech.utils.services.font_fixer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NgmFontParameters extends AbstractFontParameters {

    @Override
    @Value("${ngm.font-fixer.xoffset.pattern}")
    public void setXOffsetData(String xOffsetPattern, @Value("${ngm.font-fixer.xoffset.delta}") int xOffsetDelta) {
        parameters.put(ParameterType.X_OFFSET, getParameterDataMap(xOffsetPattern, xOffsetDelta));
    }

    @Override
    @Value("${ngm.font-fixer.yoffset.pattern}")
    public void setYOffsetData(String yOffsetPattern, @Value("${ngm.font-fixer.yoffset.delta}") int yOffsetDelta) {
        parameters.put(ParameterType.Y_OFFSET, getParameterDataMap(yOffsetPattern, yOffsetDelta));
    }

    @Override
    @Value("${ngm.font-fixer.xadvance.pattern}")
    public void setXAdvanceData(String xAdvancePattern, @Value("${ngm.font-fixer.xadvance.delta}") int xAdvanceDelta) {
        parameters.put(ParameterType.X_ADVANCE, getParameterDataMap(xAdvancePattern, xAdvanceDelta));
    }
}
