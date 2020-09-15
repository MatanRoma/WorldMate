package com.example.androidsecondproject.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.androidsecondproject.model.Profile;
import com.example.androidsecondproject.repository.Repository;

import java.util.GregorianCalendar;

public class AccountSetupViewModel extends AndroidViewModel {
   private Profile profile;
   private Repository repository;

    public AccountSetupViewModel(@NonNull Application application) {
        super(application);
        repository=Repository.getInstance(application.getApplicationContext());
        profile=new Profile();
    }

    public void writeProfileToDatabase (){
        repository.writeMyProfile(profile);
    }
    public Profile getProfile(){
        return profile;
    }

    public void setDate(GregorianCalendar date) {
        profile.calculateBirthday(date);
    }

    public void setGender(String gender) {
        profile.setGender(gender);
    }

    public void setFirstName(String firstName) {
        profile.setFirstName(firstName);
    }

    public void setLastName(String lastName) {
        profile.setLastName(lastName);
    }

    public void setEmail() {
        profile.setEmail(repository.getCurrenUserEmail());
    }

    public void setUid() {
       profile.setUid(repository.getCurrentUserId());
    }
}
