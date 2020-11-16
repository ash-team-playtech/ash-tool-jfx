package com.playtech.utils.services.dimentions_checker;

import com.playtech.utils.services.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Checks dimensions of the images to fit max allowed limits.
 * Additional features:
 *  1) auto detection of mobile/desktop assets
 *  2) possibility to exclude some folders
 *  3) TODO: detection of paytable assets and check of their equality
 */
@Component
public class DimensionsChecker implements Util {

    private static final Logger logger = LoggerFactory.getLogger(DimensionsChecker.class);

    @Value("${dimensions-checker.excluded.folders}")
    private List<String> excludedFolders;

    @Value("${dimensions-checker.mobile.folders}")
    private List<String> mobileAssetsFolders;

    private String projectPath;

    private final Dimension mobileDimensionLimit;
    private final Dimension desktopDimensionLimit;

    @Autowired
    public DimensionsChecker(@Value("${dimensions-checker.mob.x}") int mobileDimensionX, @Value("${dimensions-checker.mob.y}") int mobileDimensionY,
                             @Value("${dimensions-checker.desk.x}") int desktopDimensionX, @Value("${dimensions-checker.desk.y}") int desktopDimensionY) {
        mobileDimensionLimit = new Dimension(mobileDimensionX, mobileDimensionY);
        desktopDimensionLimit = new Dimension(desktopDimensionX, desktopDimensionY);
    }

    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }

    @Override
    public <T> List<T> run() {
        List<String> problematicFiles = new ArrayList<>();
        try {
            Files.walk(Paths.get(projectPath)).map(Path::toString).forEach(path -> {
                if (excludedFolders.stream().noneMatch(path::contains)) {
                    Dimension imageDim = getImageDim(path);
                    Dimension maxAllowedDimension = mobileAssetsFolders.stream().anyMatch(path::contains) ? mobileDimensionLimit : desktopDimensionLimit;
                    if (imageDim != null && imageDim.width > maxAllowedDimension.width && imageDim.height > maxAllowedDimension.height) {
                        problematicFiles.add(path);
                    }
                }
            });
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return (List<T>) problematicFiles;
    }

    private Dimension getImageDim(final String path) {
        Dimension result = null;
        String suffix = getFileSuffix(path);
        Iterator<ImageReader> iter = ImageIO.getImageReadersBySuffix(suffix);
        if (iter.hasNext()) {
            ImageReader reader = iter.next();
            try {
                ImageInputStream stream = new FileImageInputStream(new File(path));
                reader.setInput(stream);
                int width = reader.getWidth(reader.getMinIndex());
                int height = reader.getHeight(reader.getMinIndex());
                result = new Dimension(width, height);
            } catch (IOException e) {
                logger.error(e.getMessage());
            } finally {
                reader.dispose();
            }
        } else {
            logger.debug("No reader found for given format: " + suffix);
        }
        return result;
    }

    private String getFileSuffix(final String path) {
        String result = null;
        if (path != null) {
            result = "";
            if (path.lastIndexOf('.') != -1) {
                result = path.substring(path.lastIndexOf('.'));
                if (result.startsWith(".")) {
                    result = result.substring(1);
                }
            }
        }
        return result;
    }

    @Override
    public void reset() {
        projectPath = null;
    }
}
