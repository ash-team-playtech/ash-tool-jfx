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
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Checks dimensions of the images to fit max allowed limits.
 * Additional features:
 * 1) auto detection of mobile/desktop assets
 * 2) possibility to exclude some folders
 * 3) TODO: detection of paytable assets and check of their equality
 */
@Component
public class DimensionsChecker implements Util {

    private static final Logger logger = LoggerFactory.getLogger(DimensionsChecker.class);

    @Value("${dimensions-checker.excluded.folders}")
    private List<String> excludedFolders;

    @Value("${dimensions-checker.mobile.folders}")
    private List<String> mobileAssetsFolders;

    private final CustomDimension mobileDimensionLimit;
    private final CustomDimension desktopDimensionLimit;

    private String projectPath;
    private List<ProblematicImage> problematicImages = new ArrayList<>();

    @Autowired
    public DimensionsChecker(@Value("${dimensions-checker.mob.x}") int mobileDimensionX, @Value("${dimensions-checker.mob.y}") int mobileDimensionY,
                             @Value("${dimensions-checker.desk.x}") int desktopDimensionX, @Value("${dimensions-checker.desk.y}") int desktopDimensionY) {
        mobileDimensionLimit = new CustomDimension(mobileDimensionX, mobileDimensionY);
        desktopDimensionLimit = new CustomDimension(desktopDimensionX, desktopDimensionY);
    }

    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }

    @Override
    public <T> List<T> run() {
        checkOverallDimensions();
        checkTranslationsDimensions();
        return (List<T>) problematicImages;
    }

    private void checkOverallDimensions() {
        try (Stream<Path> walk = Files.walk(Paths.get(projectPath))) {
            walk.filter(path -> !Files.isDirectory(path) && excludedFolders.stream().noneMatch(path.toString()::contains))
                    .forEach(path -> {
                        CustomDimension imageDim = getImageDim(path.toString());
                        CustomDimension maxAllowedDimension = mobileAssetsFolders.stream().anyMatch(path.toString()::contains) ? mobileDimensionLimit : desktopDimensionLimit;
                        if (imageDim != null && imageDim.width > maxAllowedDimension.width && imageDim.height > maxAllowedDimension.height) {
                            problematicImages.add(new ProblematicImage(path, maxAllowedDimension, imageDim));
                        }
                    });
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void checkTranslationsDimensions() {
        Set<Path> uniqueAssetFolders = getUniqueTranslatedAssetsFolders();
        uniqueAssetFolders.forEach(uniqueAssetFolder -> {
            try {
                Path engAssetPath = Files.find(uniqueAssetFolder, 1, (path, basicFileAttributes) -> path.toFile().getName().matches(".+_en[_@.].+"))
                        .findFirst().orElse(null);
                if (engAssetPath != null) {
                    CustomDimension engDimension = getImageDim(engAssetPath.toString());
                    Files.walk(uniqueAssetFolder).filter(path -> !Files.isDirectory(path))
                            .forEach(path -> {
                                CustomDimension imageDim = getImageDim(path.toString());
                                if (imageDim != null && (imageDim.width != engDimension.width || imageDim.height != engDimension.height)) {
                                    problematicImages.add(new ProblematicImage(path, engDimension, imageDim));
                                }
                            });
                }
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        });
    }

    private Set<Path> getUniqueTranslatedAssetsFolders() {
        try (Stream<Path> walk = Files.walk(Paths.get(projectPath))) {
            return walk.filter(Files::isDirectory)
                    .filter(dir -> Stream.of(dir)
                            .map(Path::toString)
                            .filter(d -> excludedFolders.stream().noneMatch(d::contains))
                            .anyMatch(p -> p.contains("translations")))
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return Collections.emptySet();
    }

    private CustomDimension getImageDim(final String path) {
        CustomDimension result = null;
        String suffix = getFileSuffix(path);
        Iterator<ImageReader> iter = ImageIO.getImageReadersBySuffix(suffix);
        if (iter.hasNext()) {
            ImageReader reader = iter.next();
            try {
                ImageInputStream stream = new FileImageInputStream(new File(path));
                reader.setInput(stream);
                int width = reader.getWidth(reader.getMinIndex());
                int height = reader.getHeight(reader.getMinIndex());
                result = new CustomDimension(width, height);
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
