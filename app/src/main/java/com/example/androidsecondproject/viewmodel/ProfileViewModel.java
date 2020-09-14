package com.example.androidsecondproject.viewmodel;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.androidsecondproject.model.Profile;
import com.example.androidsecondproject.repository.Repository;

public class ProfileViewModel extends AndroidViewModel {

    private Repository repository;
    private Profile profile;
    private Uri imageUri;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        repository=Repository.getInstance(application.getApplicationContext());
    }


    public void writeProfile(){
        repository.writeProfile(profile);
    }

    public void writePicture(){
     //   repository.
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }
}
