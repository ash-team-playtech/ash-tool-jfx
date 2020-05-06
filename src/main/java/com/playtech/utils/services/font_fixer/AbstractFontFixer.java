package com.playtech.utils.services.font_fixer;

import com.playtech.utils.services.AbstractParser;
import com.playtech.utils.services.Util;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Tool for bulk change of xoffset/yoffset/xadvance parameters of bitmap font
 */
public abstract class AbstractFontFixer extends AbstractParser implements Util {

    private static final SimpleDateFormat TIME_STAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_");

    private String fontFilePath;
    private String fontFileName;
    private boolean override;

    String configLineBeginning;
    String parametersSeparator;

    private AbstractFontParameters fontParameters;
    private List<String> lines = new ArrayList<>();

    public AbstractFontFixer(AbstractFontParameters fontParameters) {
        this.fontParameters = fontParameters;
    }

    public void setFontFilePath(String fontFilePath) {
        this.fontFilePath = fontFilePath;
    }

    public void setFontFileName(String fontFileName) {
        this.fontFileName = fontFileName;
    }

    public void setOverride(boolean override) {
        this.override = override;
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
    protected void init() {
        setPath(fontFilePath + fontFileName);
    }

    @Override
    protected void parseLine(String line) {
        lines.add(line);
    }

    @Override
    public void execute() {
        init();
        parseConfigFile();
        for (AbstractFontParameters.ParameterType parameterType : AbstractFontParameters.ParameterType.values()) {
            updateParameter(fontParameters.getParameterKey(parameterType), fontParameters.getParameterValue(parameterType));
        }
        saveResults();
    }

    private void updateParameter(String parameterPattern, int delta) {
        for (int lineIndex = 0; lineIndex < lines.size(); lineIndex++) {
            if (lines.get(lineIndex).trim().startsWith(configLineBeginning)) {
                String[] splitLine = lines.get(lineIndex).split(parameterPattern);
                String endOfLine = splitLine[1].substring(splitLine[1].indexOf(parametersSeparator));
                int number = Integer.parseInt(splitLine[1].substring(0, splitLine[1].indexOf(parametersSeparator)));
                lines.set(lineIndex, splitLine[0] + parameterPattern + (number + delta) + endOfLine);
            }
        }
    }

    private void saveResults() {
        String path = override ? fontFilePath + fontFileName : fontFilePath + TIME_STAMP_FORMAT.format(new Timestamp(System.currentTimeMillis())) + fontFileName;
        try {
            Files.write(Paths.get(path), lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
