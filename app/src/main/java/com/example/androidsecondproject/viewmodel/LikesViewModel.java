package com.example.androidsecondproject.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.androidsecondproject.model.Profile;
import com.example.androidsecondproject.repository.Repository;

import java.util.List;

public class LikesViewModel extends AndroidViewModel {

    private Repository mRepository;
    private Profile mMyProfile;
    private MutableLiveData<List<Profile>> mLikesMutableLiveData;

    public LikesViewModel(@NonNull Application application) {
        super(application);
        mRepository =Repository.getInstance(application.getApplicationContext());
    }

    public MutableLiveData<List<Profile>> getProfilesResultSuccess(){
        if (mLikesMutableLiveData == null) {
            mLikesMutableLiveData = new MutableLiveData<>();
            loadLikesData();
        }
        return mLikesMutableLiveData;
    }

    private void loadLikesData() {
        mRepository.setLikesListener(new Repository.LikesListener() {
            @Override
            public void onProfileLikedDataChangeSuccess(List<Profile> profiles) {
                mLikesMutableLiveData.setValue(profiles);
            }

            @Override
            public void onProfileLikedDataChangeFail(String error) {

            }
        });
    }

    public void readLikes(){
        mRepository.readLikedProfiles(mMyProfile);
    }

    public Profile getMyProfile() {
        return mMyProfile;
    }

    public void setMyProfile(Profile mMyProfile) {
        this.mMyProfile = mMyProfile;
    }
}
