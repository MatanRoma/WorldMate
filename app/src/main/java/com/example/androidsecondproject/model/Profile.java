package com.example.androidsecondproject.model;

import android.net.Uri;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.Date;

public class Profile implements Serializable {

    private String uid;
    private String fullName;
    private Uri imageUri;
    private String description;
    private float age;
    private boolean gender;  // male=true
    private String hobbies;

    public Profile(String uid, String fullName, Uri imageUri, String description, float age, boolean gender, String hobbies) {
        this.uid = uid;
        this.fullName = fullName;
        this.imageUri = imageUri;
        this.description = description;
        this.age = age;
        this.gender = gender;
        this.hobbies = hobbies;
    }

    public Profile() {
    }
    @Exclude
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    @Exclude
    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
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

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getHobbies() {
        return hobbies;
    }




    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }
}

