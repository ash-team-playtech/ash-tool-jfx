package com.playtech.utils.services;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LinesParser extends AbstractParser implements Util {

    private static final String PATH = "C:\\Work\\Temp\\LWSS\\Forest Prince Profile 1.4.xml";
    private static final String CONFIG_PATTERN = "<Winline ";

    private List<String> linesConfig = new ArrayList<>();

    @Override
    public void execute() {
        init();
        parseConfigFile();
        printResults();
    }

    @Override
    protected void init() {
        setPath(PATH);
    }

    @Override
    protected void parseLine(String line) {
        if (line.contains(CONFIG_PATTERN)) {
            String [] lineParams = line.substring(line.indexOf("<"), line.indexOf("/")).split(" ");
            String value = lineParams[1].replace("\"", "");
            linesConfig.add(formatLine(value.substring(value.indexOf("=") + 1)));
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

    protected void printResults() {
        for (String line : linesConfig) {
            System.out.println(line);
        }
    }
}