package com.example.androidsecondproject.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.androidsecondproject.model.Profile;
import com.example.androidsecondproject.model.Question;
import com.example.androidsecondproject.repository.Repository;

import java.util.List;

public class QuestionsViewModel extends AndroidViewModel {

    private Repository mRepository;
    private Profile profile;
    private MutableLiveData<List<Question>> mQuestionsSuccessLiveData;


    public QuestionsViewModel(@NonNull Application application) {
        super(application);
        mRepository =Repository.getInstance(application.getApplicationContext());
    }

    public MutableLiveData<List<Question>> getQuestionsResultSuccess(){
        Log.d("questions","set questions");
        if (mQuestionsSuccessLiveData == null) {
            mQuestionsSuccessLiveData = new MutableLiveData<>();
            loadQuestionsData();
            //      database.readProfileFrom();
        }
        return mQuestionsSuccessLiveData;
    }

    private void loadQuestionsData() {
        mRepository.setQuestionsListener(new Repository.QuestionsListener() {
            @Override
            public void onQuestionsDataChangeSuccess(List<Question> questions) {
                mQuestionsSuccessLiveData.setValue(questions);
            }

            @Override
            public void onQuestionsDataChangeFail(String error) {

            }
        });
    }

    public void updateQuestion(){
     //   mRepository.writeMyProfile(profile);
        mRepository.updateProfile(profile.getUid(),"questionResponds",profile.getQuestionResponds());
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Profile getProfile()
    {
        return profile;
    }

    public void readQuestions() {
        mRepository.readQuestions();
    }

    public List<Question> getQuestions() {
        return mQuestionsSuccessLiveData.getValue();
    }
}
