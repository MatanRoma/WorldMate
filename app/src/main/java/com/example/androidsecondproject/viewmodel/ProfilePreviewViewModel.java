package com.example.androidsecondproject.viewmodel;

import android.app.Application;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.androidsecondproject.model.Profile;

import java.util.ArrayList;
import java.util.List;

public class ProfilePreviewViewModel extends AndroidViewModel {
    Profile otherProfile;
    private List<ImageView> mCirclesIv;

    public ProfilePreviewViewModel(@NonNull Application application) {
        super(application);
        this.mCirclesIv = new ArrayList<>();
    }

    public Profile getOtherProfile() {
        return otherProfile;
    }

    public void setOtherProfile(Profile otherProfile) {
        this.otherProfile = otherProfile;
    }

    public List<ImageView> getmCirclesIv() {
        return mCirclesIv;
    }

    public void setmCirclesIv(List<ImageView> mCirclesIv) {
        this.mCirclesIv = mCirclesIv;
    }
}
