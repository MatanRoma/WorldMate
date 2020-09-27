package com.example.androidsecondproject.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class Profile implements Serializable {


    private String firstName;
    private String lastName;
    private String description;
    private float age;
    private String birthday;
    private String gender;
    private String messageToken;
    private String hobbies;
    private String lookingFor;
    private boolean discovery;
    private String profilePictureUri;
    private String email;
    private Preferences preferences;
    private List<String> likes;
    private List<String> disLikes;
    private List<Match> matches;
    private String uid;
    private String city;
    private LocationPoint location;
    private List<QuestionRespond> questionResponds;



    public Profile() {
        description="";
        hobbies="";
        profilePictureUri="";
        lookingFor="";
        likes=new ArrayList<>();
        disLikes=new ArrayList<>();
        matches=new ArrayList<>();
        questionResponds = new ArrayList<>();
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

    public List<String> getLikes() {
        return likes;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setLikes(List<String> likes) {
        this.likes = likes;
    }

    public List<String> getDisLikes() {
        return disLikes;
    }

    public void setDisLikes(List<String> disLikes) {
        this.disLikes = disLikes;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

    public List<QuestionRespond> getQuestionResponds() {
        return questionResponds;
    }

    public void setQuestionResponds(List<QuestionRespond> questionResponds) {
        this.questionResponds = questionResponds;
    }

    public String getMessageToken() {
        return messageToken;
    }

    public void setMessageToken(String messageToken) {
        this.messageToken = messageToken;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public LocationPoint getLocation() {
        return location;
    }

    public void setLocation(LocationPoint location) {
        this.location = location;
    }
}

