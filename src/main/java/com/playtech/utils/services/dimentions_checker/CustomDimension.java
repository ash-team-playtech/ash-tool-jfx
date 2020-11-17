package com.playtech.utils.services.dimentions_checker;

import java.awt.*;

public class CustomDimension extends Dimension {

    public CustomDimension(int width, int height) {
        super(width, height);
    }

    @Override
    public String toString() {
        return "[" + width + "x" + height + "]";
    }
}
