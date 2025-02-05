package com.playtech.utils.services;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractParsingUtil implements Util {

    private static final SimpleDateFormat TIME_STAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_");

    private static String filePath;
    private static String fileName;
    private static boolean override;

    private List<String> parsedLines = new ArrayList<>();
    private List<String> resultedLines = new ArrayList<>();

    public static String getFilePath() {
        return filePath;
    }

    public static void setFilePath(String filePath) {
        AbstractParsingUtil.filePath = filePath;
    }

    public static String getFileName() {
        return fileName;
    }

    public static void setFileName(String fileName) {
        AbstractParsingUtil.fileName = fileName;
    }

    public static boolean isOverride() {
        return override;
    }

    public static void setOverride(boolean override) {
        AbstractParsingUtil.override = override;
    }

    public List<String> getParsedLines() {
        return parsedLines;
    }

    public void setParsedLines(List<String> parsedLines) {
        this.parsedLines = parsedLines;
    }

    public List<String> getResultedLines() {
        return resultedLines;
    }

    public void setResultedLines(List<String> resultedLines) {
        this.resultedLines = resultedLines;
    }

    public <T> List<T> run() {
        parseConfigFile();
        performActions();
        saveResults();
        return Collections.emptyList();
    }

    protected void parseConfigFile() {
        try (Stream<String> lines = Files.lines(Paths.get(filePath + fileName), Charset.defaultCharset())) {
            parsedLines.addAll(lines.collect(Collectors.toList()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void saveResults() {
        String path = override ? filePath + fileName : filePath + TIME_STAMP_FORMAT.format(new Timestamp(System.currentTimeMillis())) + fileName;
        try {
            Files.write(Paths.get(path), resultedLines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void reset() {
        filePath = null;
        fileName = null;
        override = false;
        parsedLines.clear();
        resultedLines.clear();
    }

    protected abstract void performActions();
}
