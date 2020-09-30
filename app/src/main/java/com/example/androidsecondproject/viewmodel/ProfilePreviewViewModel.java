package com.example.androidsecondproject.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.androidsecondproject.model.Profile;

public class ProfilePreviewViewModel extends AndroidViewModel {
    Profile otherProfile;

    public ProfilePreviewViewModel(@NonNull Application application) {
        super(application);
    }

    public Profile getOtherProfile() {
        return otherProfile;
    }

    public void setOtherProfile(Profile otherProfile) {
        this.otherProfile = otherProfile;
    }
}
