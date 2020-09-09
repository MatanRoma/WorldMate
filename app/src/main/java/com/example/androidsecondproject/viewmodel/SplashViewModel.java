package com.example.androidsecondproject.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.androidsecondproject.repository.AuthRepository;

public class SplashViewModel extends AndroidViewModel {

    // Create a LiveData with a String
 //   private MutableLiveData<Boolean> mIsAuth;
    private AuthRepository mRepository;

    public SplashViewModel(@NonNull Application application) {
        super(application);
        mRepository=new AuthRepository(application.getApplicationContext());
    }

    public String getUserUid(){
        return mRepository.getCurrentUserUid();
    }

   /* public MutableLiveData<Boolean> getIsAuth() {
        if (mIsAuth == null) {
            mIsAuth = new MutableLiveData<>();
            mIsAuth.setValue(mRepository.checkIfAuth());
        }
        return mIsAuth;
    }*/
    public boolean checkIfAuth(){
        return mRepository.checkIfAuth();
    }

}
