package com.playtech.utils.services.dimentions_checker;

import java.nio.file.Path;

public class ProblematicImage {

    private Path filePath;
    private CustomDimension expectedDimension;
    private CustomDimension actualDimension;

    public ProblematicImage(Path filePath, CustomDimension expectedDimension, CustomDimension actualDimension) {
        this.filePath = filePath;
        this.expectedDimension = expectedDimension;
        this.actualDimension = actualDimension;
    }

    public Path getFilePath() {
        return filePath;
    }

    public void setFilePath(Path filePath) {
        this.filePath = filePath;
    }

    public CustomDimension getExpectedDimension() {
        return expectedDimension;
    }

    public void setExpectedDimension(CustomDimension expectedDimension) {
        this.expectedDimension = expectedDimension;
    }

    public CustomDimension getActualDimension() {
        return actualDimension;
    }

    public void setActualDimension(CustomDimension actualDimension) {
        this.actualDimension = actualDimension;
    }
}
