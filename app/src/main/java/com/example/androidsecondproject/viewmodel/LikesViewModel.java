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
    private MutableLiveData<List<Profile>> mLikesMutableLiveData;

    public LikesViewModel(@NonNull Application application) {
        super(application);
        mRepository =Repository.getInstance(application.getApplicationContext());
    }

    public MutableLiveData<List<Profile>> getProfilesResultSuccess(){
        if (mLikesMutableLiveData == null) {
            mLikesMutableLiveData = new MutableLiveData<>();
            //loadProfilesData();
        }
        return mLikesMutableLiveData;
    }

   /* private void loadProfilesData() {
        mRepository.setLikesListener(new Repository.LikesListener() {
            @Override
            public void onLikesDataChangeSuccess(List<Profile> Likes) {
                mLikesMutableLiveData.setValue(likes);
            }

            @Override
            public void onLikesDataChangeFail(String error) {

            }
        });
    }

    public void readLikes(){
        mRepository.readLikes(mProfile);
    }*/
}
