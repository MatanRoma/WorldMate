package com.example.androidsecondproject.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.androidsecondproject.model.eViewModels;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private Application mApplication;
    private Context activityContext;
    private eViewModels mEViewModel;

    public ViewModelFactory(@NonNull Application Application,eViewModels eViewModels) {
        this.mApplication = Application;
        this.mEViewModel=eViewModels;

    }
    public ViewModelFactory(Context context, eViewModels eViewModels) {
        this.activityContext=context;
        this.mEViewModel=eViewModels;

    }

    public <T extends ViewModel> T create(Class<T> modelClass) {
        T objectToReturn=null;
        switch (mEViewModel){
            case Splash:
                if (modelClass.isAssignableFrom(SplashViewModel.class)) {
                    objectToReturn= (T) (new SplashViewModel(mApplication));
                }
                break;
            case Register:
                if (modelClass.isAssignableFrom(RegisterViewModel.class)) {
                    objectToReturn= (T) (new RegisterViewModel(activityContext));
                }
                break;
            case Login:
                if (modelClass.isAssignableFrom(LoginViewModel.class)) {
                    objectToReturn= (T) (new LoginViewModel(activityContext));
                }
                break;


        }
        return objectToReturn;
    }
}