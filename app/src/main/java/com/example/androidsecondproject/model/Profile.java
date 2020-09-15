package com.example.androidsecondproject.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Profile implements Serializable {


    private String firstName;
    private String lastName;
    private String description="about myself";
    private float age;
    private String birthday;
    private String gender;
    private String hobbies="my hobbies";
    private String lookingFor="looking for";
    private boolean discovery;
    private String profilePictureUri="";
    private String email;
    private Preferences preferences;
/*
    public Profile(String firstName, String lastName, float age, String gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.description = " ";
        this.hobbies=" ";
        this.age = age;
        this.gender = gender;
    }
    */


    public Profile() {
    }


    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void calculateBirthday(GregorianCalendar date) {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int birthYear=date.get(Calendar.YEAR);
        this.age=currentYear-birthYear;

        int birthMonth=date.get(Calendar.MONTH);
        int birthDay=date.get(Calendar.DAY_OF_MONTH);
        this.birthday=birthDay+"/"+birthMonth+"/"+birthYear;
    }



    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getAge() {
        return age;
    }

    public void setAge(float age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHobbies() {
        return hobbies;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    public boolean isDiscovery() {
        return discovery;
    }

    public Preferences getPreferences() {
        return preferences;
    }

    public void setPreferences(Preferences preferences) {
        this.preferences = preferences;
    }

    public String getProfilePictureUri() {
        return profilePictureUri;
    }

    public void setProfilePictureUri(String profilePictureUri) {
        this.profilePictureUri = profilePictureUri;
    }

    public void setDiscovery(boolean discovery) {
        this.discovery = discovery;
    }

    public String getLookingFor() {
        return lookingFor;
    }

    public void setLookingFor(String lookingFor) {
        this.lookingFor = lookingFor;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

