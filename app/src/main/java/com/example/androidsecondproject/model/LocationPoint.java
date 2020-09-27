package com.example.androidsecondproject.model;

import java.io.Serializable;

public class LocationPoint implements Serializable {
    private double x;
    private double y;

    public LocationPoint() {
    }

    public LocationPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
    public int calculateDistance(LocationPoint otherLocation){
        double x2,y2;
        x2=otherLocation.x;
        y2=otherLocation.y;
        return (int)Math.sqrt(Math.pow(x2-x,2)+Math.pow(y2-y,2));
    }
}
