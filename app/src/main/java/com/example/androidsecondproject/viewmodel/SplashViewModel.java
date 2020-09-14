package com.example.androidsecondproject.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.androidsecondproject.model.Profile;
import com.example.androidsecondproject.repository.AuthRepository;
import com.example.androidsecondproject.repository.Repository;

public class SplashViewModel extends AndroidViewModel {

    // Create a LiveData with a String
 //   private MutableLiveData<Boolean> mIsAuth;
    private Repository mRepository;


    public SplashViewModel(@NonNull Application application) {
        super(application);
        mRepository=Repository.getInstance(application.getApplicationContext());
    }



    public boolean checkIfAuth(){
        return mRepository.checkIfAuth();
    }

}
