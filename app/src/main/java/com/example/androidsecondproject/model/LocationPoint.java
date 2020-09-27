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
        return calculateDistance(x,y,x2,y2);
    }
        private int calculateDistance(double latA, double longA, double latB, double longB) {

        double theDistance = (Math.sin(Math.toRadians(latA)) *
                Math.sin(Math.toRadians(latB)) +
                Math.cos(Math.toRadians(latA)) *
                  Math.cos(Math.toRadians(latB)) *
                        Math.cos(Math.toRadians(longA - longB)));

        return (int)(Math.toDegrees(Math.acos(theDistance) * 69.09)*1.6);
    }
}
