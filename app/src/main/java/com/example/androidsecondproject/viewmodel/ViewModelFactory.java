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
            case Setup:
                if (modelClass.isAssignableFrom(AccountSetupViewModel.class)) {
                    objectToReturn= (T) (new AccountSetupViewModel(mApplication));
                }
                break;
            case ProfilePhoto:
                if (modelClass.isAssignableFrom(ProfilePhotoViewModel.class)) {
                    objectToReturn= (T) (new ProfilePhotoViewModel(mApplication));
                }
                break;
            case Preferences:
                if (modelClass.isAssignableFrom(PreferencesViewModel.class)) {
                    objectToReturn= (T) (new PreferencesViewModel(mApplication));
                }
                break;
            case Main:
                if (modelClass.isAssignableFrom(MainViewModel.class)) {
                    objectToReturn= (T) (new MainViewModel(mApplication));
                }
                break;
            case ProfileFragment:
                if (modelClass.isAssignableFrom(ProfileViewModel.class)) {
                    objectToReturn= (T) (new ProfileViewModel(mApplication));
                }
                break;
            case Swipe:
                if (modelClass.isAssignableFrom(SwipeViewModel.class)) {
                    objectToReturn= (T) (new SwipeViewModel(mApplication));
                }
                break;
            case Questions:
                if (modelClass.isAssignableFrom(QuestionsViewModel.class)) {
                    objectToReturn= (T) (new QuestionsViewModel(mApplication));
                }
                break;
            case Matches:
                if (modelClass.isAssignableFrom(MatchesViewModel.class)) {
                    objectToReturn= (T) (new MatchesViewModel(mApplication));
                }
                break;
            case Chat:
                if (modelClass.isAssignableFrom(ChatViewModel.class)) {
                    objectToReturn= (T) (new ChatViewModel(mApplication));
                }
                break;





        }
        return objectToReturn;
    }
}