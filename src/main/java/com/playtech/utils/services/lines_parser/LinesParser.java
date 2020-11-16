package com.playtech.utils.services.lines_parser;

import com.playtech.utils.services.AbstractParsingUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Tool for parsing winlines config and converting it to a usable format
 */
@Component
public class LinesParser extends AbstractParsingUtil {

    @Value("${lines-parser.pattern}")
    private String configPattern;

    @Override
    protected void performActions() {
        for (String parsedLine : getParsedLines()) {
            if (parsedLine.contains(configPattern)) {
                String [] lineParams = parsedLine.substring(parsedLine.indexOf("<"), parsedLine.indexOf("/")).split(" ");
                String value = lineParams[1].replace("\"", "");
                getResultedLines().add(formatLine(value.substring(value.indexOf("=") + 1)));
            }
        }
    }

    private String formatLine(String notFormattedLine) {
        String [] lineSlotsIds = notFormattedLine.split(",");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        for (int x = 0; x < lineSlotsIds.length; x++) {
            stringBuilder.append("[")
                    .append(x)
                    .append(", ")
                    .append(lineSlotsIds[x])
                    .append("]")
                    .append(((x != lineSlotsIds.length - 1) ? ", " : ""));
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}