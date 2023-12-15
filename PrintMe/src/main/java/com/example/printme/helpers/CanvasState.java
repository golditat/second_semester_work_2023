package com.example.printme.helpers;

import javafx.scene.paint.Color;

import java.io.Serializable;

public class CanvasState implements Serializable {
    private double size;
    private double x;
    private double y;
    private boolean isEraser;
    private ColorSerial color;

    public CanvasState(double size, double x, double y, boolean isEraser, ColorSerial color) {
        this.size = size;
        this.x = x;
        this.y = y;
        this.isEraser = isEraser;
        this.color = color;
    }

    public ColorSerial getColor() {
        return color;
    }

    public void setColor(ColorSerial color) {
        this.color = color;
    }

    public double getSize() {
        return size;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public boolean isEraser() {
        return isEraser;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setEraser(boolean eraser) {
        isEraser = eraser;
    }
}