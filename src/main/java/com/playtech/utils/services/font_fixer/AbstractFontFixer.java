package com.playtech.utils.services.font_fixer;

import com.playtech.utils.services.AbstractUtil;

import java.util.ArrayList;

/**
 * Tool for bulk change of xoffset/yoffset/xadvance parameters of bitmap font
 */
public abstract class AbstractFontFixer extends AbstractUtil {

    String configLineBeginning;
    String parametersSeparator;

    private AbstractFontParameters fontParameters;

    public AbstractFontFixer(AbstractFontParameters fontParameters) {
        this.fontParameters = fontParameters;
    }

    public void setXOffset(int delta) {
        fontParameters.setParameterValue(AbstractFontParameters.ParameterType.X_OFFSET, delta);
    }

    public void setYOffset(int delta) {
        fontParameters.setParameterValue(AbstractFontParameters.ParameterType.Y_OFFSET, delta);
    }

    public void setXAdvanced(int delta) {
        fontParameters.setParameterValue(AbstractFontParameters.ParameterType.X_ADVANCE, delta);
    }

    public abstract void setConfigLineBeginning(String configLineBeginning);

    public abstract void setParametersSeparator(String parametersSeparator);

    @Override
    protected void performActions() {
        for (AbstractFontParameters.ParameterType parameterType : AbstractFontParameters.ParameterType.values()) {
            updateParameter(fontParameters.getParameterKey(parameterType), fontParameters.getParameterValue(parameterType));
        }
    }

    private void updateParameter(String parameterPattern, int delta) {
        setResultedLines(new ArrayList<>(getParsedLines()));
        for (int lineIndex = 0; lineIndex < getParsedLines().size(); lineIndex++) {
            if (getParsedLines().get(lineIndex).trim().startsWith(configLineBeginning)) {
                String[] splitLine = getParsedLines().get(lineIndex).split(parameterPattern);
                String endOfLine = splitLine[1].substring(splitLine[1].indexOf(parametersSeparator));
                int number = Integer.parseInt(splitLine[1].substring(0, splitLine[1].indexOf(parametersSeparator)));
                getResultedLines().set(lineIndex, splitLine[0] + parameterPattern + (number + delta) + endOfLine);
            }
        }
    }
}
