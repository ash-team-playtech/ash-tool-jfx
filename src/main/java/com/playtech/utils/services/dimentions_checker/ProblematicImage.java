package com.playtech.utils.services.dimentions_checker;

import java.nio.file.Path;

public class ProblematicImage {

    private Path filePath;
    private CustomDimension maxAllowedDimension;
    private CustomDimension actualDimension;

    public ProblematicImage(Path filePath, CustomDimension maxAllowedDimension, CustomDimension actualDimension) {
        this.filePath = filePath;
        this.maxAllowedDimension = maxAllowedDimension;
        this.actualDimension = actualDimension;
    }

    public Path getFilePath() {
        return filePath;
    }

    public void setFilePath(Path filePath) {
        this.filePath = filePath;
    }

    public CustomDimension getMaxAllowedDimension() {
        return maxAllowedDimension;
    }

    public void setMaxAllowedDimension(CustomDimension maxAllowedDimension) {
        this.maxAllowedDimension = maxAllowedDimension;
    }

    public CustomDimension getActualDimension() {
        return actualDimension;
    }

    public void setActualDimension(CustomDimension actualDimension) {
        this.actualDimension = actualDimension;
    }
}
