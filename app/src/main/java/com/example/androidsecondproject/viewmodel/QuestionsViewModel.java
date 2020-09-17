package com.example.androidsecondproject.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.androidsecondproject.model.Profile;
import com.example.androidsecondproject.repository.Repository;

public class QuestionsViewModel extends AndroidViewModel {

    private Repository mRepository;
    private Profile profile;


    public QuestionsViewModel(@NonNull Application application) {
        super(application);
        mRepository =Repository.getInstance(application.getApplicationContext());
    }

    public void writeProfile(){
        mRepository.writeMyProfile(profile);
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Profile getProfile()
    {
        return profile;
    }
}
