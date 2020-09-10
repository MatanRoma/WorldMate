package com.example.androidsecondproject.model;

import android.net.Uri;
import android.util.Log;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Profile implements Serializable {


    private String firstName;
    private String lastName;
    private String description;
    private float age;
    private String birthday;
    private String gender;
    private String hobbies;

    public Profile(String firstName, String lastName, float age, String gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.description = " ";
        this.hobbies=" ";
        this.age = age;
        this.gender = gender;
    }

    public Profile() {
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(GregorianCalendar date) {
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
}

