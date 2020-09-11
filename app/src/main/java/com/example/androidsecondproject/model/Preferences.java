package com.example.androidsecondproject.model;

public class Preferences {

    private boolean lookingForMen;
    private boolean lookingForWomen;
    private int maxDistance;
    private int minAge;
    private int maxAge;

    public Preferences(boolean lookingForMen, boolean lookingForWomen, int maxDistance, int minAge, int maxAge) {
        this.lookingForMen = lookingForMen;
        this.lookingForWomen = lookingForWomen;
        this.maxDistance = maxDistance;
        this.minAge = minAge;
        this.maxAge = maxAge;
    }
    public Preferences(){}

    public boolean isLookingForMen() {
        return lookingForMen;
    }

    public void setLookingForMen(boolean lookingForMen) {
        this.lookingForMen = lookingForMen;
    }

    public boolean isLookingForWomen() {
        return lookingForWomen;
    }

    public void setLookingForWomen(boolean lookingForWomen) {
        this.lookingForWomen = lookingForWomen;
    }

    public int getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(int maxDistance) {
        this.maxDistance = maxDistance;
    }

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }
}
