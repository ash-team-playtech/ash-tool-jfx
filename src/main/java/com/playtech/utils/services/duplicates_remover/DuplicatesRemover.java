package com.playtech.utils.services.duplicates_remover;

import com.playtech.utils.services.AbstractUtil;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Tool for removing duplicated chars (e.g. font char sequences)
 */
@Component
public class DuplicatesRemover extends AbstractUtil {

    @Override
    protected void performActions() {
        Set<Character> charList = new HashSet<>();
        for (String parsedLine : getParsedLines()) {
            for (char c : parsedLine.toCharArray()) {
                charList.add(c);
            }
        }
        getResultedLines().add(charList.stream()
                .map(String::valueOf)
                .collect(Collectors.joining()));
    }
}