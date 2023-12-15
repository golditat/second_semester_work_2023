package com.example.printme.helpers;

import javafx.scene.paint.Color;

import java.io.Serializable;

public class ColorSerial implements Serializable {
    private double red;
    private double green;
    private double blue;
    private double opacity;

    public ColorSerial(Color color) {
        this.red = color.getRed();
        this.green = color.getGreen();
        this.blue = color.getBlue();
        this.opacity = color.getOpacity();
    }

    public double getRed() {
        return red;
    }

    public double getGreen() {
        return green;
    }

    public double getBlue() {
        return blue;
    }

    public double getOpacity() {
        return opacity;
    }
}
