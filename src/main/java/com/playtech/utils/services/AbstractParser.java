package com.playtech.utils.services;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public abstract class AbstractParser {

    private String path;

    public void setPath(String path) {
        this.path = path;
    }

    protected void parseConfigFile() {
        try (Stream<String> lines = Files.lines(Paths.get(path), Charset.defaultCharset())) {
            lines.forEachOrdered(this::parseLine);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected abstract void init();

    protected abstract void parseLine(String line);
}