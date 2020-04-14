package com.playtech.utils.services;

import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class Renamer implements Util {

    private static final String PATH = "c:\\Work\\Temp\\BONUS_TRAINS\\popups\\ok_button-assets\\";

    private static final String PATH_0 = "c:\\Work\\Temp\\BONUS_TRAINS\\bottombar\\autoplay_button-assets\\";
    private static final String PATH_1 = "c:\\Work\\Temp\\BONUS_TRAINS\\bottombar\\clicktostart_button-assets\\";
    private static final String PATH_2 = "c:\\Work\\Temp\\BONUS_TRAINS\\bottombar\\info_button-assets\\";
    private static final String PATH_3 = "c:\\Work\\Temp\\BONUS_TRAINS\\bottombar\\largeclicktostart_button-assets\\";
    private static final String PATH_4 = "c:\\Work\\Temp\\BONUS_TRAINS\\bottombar\\largeclicktostart_noturbo_noautoplay_button-assets\\";
    private static final String PATH_5 = "c:\\Work\\Temp\\BONUS_TRAINS\\bottombar\\largespin_button-assets\\";
    private static final String PATH_6 = "c:\\Work\\Temp\\BONUS_TRAINS\\bottombar\\largespin_noturbo_noautoplay_button-assets\\";
    private static final String PATH_7 = "c:\\Work\\Temp\\BONUS_TRAINS\\bottombar\\largestop_button-assets\\";
    private static final String PATH_8 = "c:\\Work\\Temp\\BONUS_TRAINS\\bottombar\\largestop_noturbo_noautoplay_button-assets\\";
    private static final String PATH_9 = "c:\\Work\\Temp\\BONUS_TRAINS\\bottombar\\spin_button-assets\\";
    private static final String PATH_10 = "c:\\Work\\Temp\\BONUS_TRAINS\\bottombar\\stop_button@-assets\\";
    private static final String PATH_11 = "c:\\Work\\Temp\\BONUS_TRAINS\\bottombar\\stopautoplay_button-assets\\";
    private static final String PATH_12 = "c:\\Work\\Temp\\BONUS_TRAINS\\bottombar\\turbomode_button_m-assets\\";
    private static final String PATH_13 = "c:\\Work\\Temp\\BONUS_TRAINS\\bottombar\\turbomode_button-assets\\";
    private static final String PATH_14 = "c:\\Work\\Temp\\BONUS_TRAINS\\bottombar\\turbomodeon_button-assets\\";
    private static final String PATH_15 = "c:\\Work\\Temp\\BONUS_TRAINS\\bottombar\\turboon_button_m-assets\\";

    private static final List<String> paths = Arrays.asList(PATH_0,PATH_1,PATH_2,PATH_3,PATH_4,PATH_5,PATH_6,PATH_7,PATH_8,PATH_9,PATH_10,PATH_11,PATH_12,PATH_13,PATH_14,PATH_15);
    private static final List<String> path = Collections.singletonList(PATH);

    private static final boolean isSingleDirectory = true;

    @Override
    public void execute() {
        renameFiles(isSingleDirectory ? path : paths);
    }

    private void renameFiles(List<String> paths) {
        for (String path : paths) {
            File rootFolder = new File(path);
            File[] files = rootFolder.listFiles();

            for (File file : files) {
                if (file.getName().contains("@")) {
                    String oldName = file.getName();
                    String firstPart = oldName.substring(0, oldName.indexOf("@"));
                    String langSuffix = oldName.substring(oldName.lastIndexOf("_"), oldName.indexOf("."));
                    String lastPart = oldName.substring(oldName.indexOf("@"));
                    String newName = firstPart.concat(langSuffix.toLowerCase()).concat(lastPart.replace(langSuffix, ""));
                    file.renameTo(new File(path + newName));
                }
            }
        }
    }
}