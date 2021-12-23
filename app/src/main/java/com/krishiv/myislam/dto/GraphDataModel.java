package com.krishiv.myislam.dto;

public class GraphDataModel {
    int pinPointX;
    int pinPointY;
    int month;

    public GraphDataModel(int pinPointX, int pinPointY, int month) {
        this.pinPointX = pinPointX;
        this.pinPointY = pinPointY;
        this.month = month;
    }

    public int getPinPointX() {
        return pinPointX;
    }

    public void setPinPointX(int pinPointX) {
        this.pinPointX = pinPointX;
    }

    public int getPinPointY() {
        return pinPointY;
    }

    public void setPinPointY(int pinPointY) {
        this.pinPointY = pinPointY;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }
}
