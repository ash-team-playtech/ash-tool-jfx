package com.playtech.utils.services;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DuplicatesRemover extends AbstractParser implements Util {

    private static final String PATH = "C:\\Work\\Temp\\localization\\charset_white_bitmap_font.txt";

    private Set<Character> charList = new HashSet<>();

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
        for (char c : line.toCharArray()) {
            charList.add(c);
        }
    }

    protected void printResults() {
        StringBuilder builder = new StringBuilder();
        for (Character c : charList) {
            builder.append(c);
        }
        System.out.println(builder.toString());
    }
}