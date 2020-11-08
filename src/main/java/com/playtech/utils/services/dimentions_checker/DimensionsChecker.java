package com.playtech.utils.services.dimentions_checker;

import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;

@Component
public class DimensionsChecker {

    private final String rootPath = "c:\\WORK\\SVN\\ASH\\";
    private final Dimension mobileDimensionLimit = new Dimension(1024, 1024);
    private final Dimension desktopDimensionLimit = new Dimension(2048, 2048);

    public static void main(String[] args) {
        DimensionsChecker dimensionsChecker = new DimensionsChecker();
        dimensionsChecker.processImages();
    }

    private void processImages() {
        try {
            Files.walk(Paths.get(rootPath)).forEach(path -> {
                if (!path.toString().contains("target")) {
                    Dimension imageDim = getImageDim(path.toString());
                    Dimension maxAllowedDimension = path.toString().contains("mobile") || path.toString().contains("common") ? mobileDimensionLimit : desktopDimensionLimit;
                    if (imageDim != null && imageDim.width > maxAllowedDimension.width && imageDim.height > maxAllowedDimension.height) {
                        System.out.println(path.toString().replace(rootPath, ""));
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
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
//                log(e.getMessage());
            } finally {
                reader.dispose();
            }
        } else {
//            log("No reader found for given format: " + suffix);
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
}
