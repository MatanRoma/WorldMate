package com.example.androidsecondproject.viewmodel;

import android.content.Context;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.androidsecondproject.repository.AuthRepository;

public class LoginViewModel extends ViewModel {

    private String mEmail;
    private String mPassword;
    private AuthRepository mRepository;
    private MutableLiveData<String> mLoginDataSuccess;
    private MutableLiveData<String> mLoginDataFailed;

    public LoginViewModel(Context context) {
        mRepository=new AuthRepository(context);
    }

    public MutableLiveData<String> getLoginDataSuccess(){
        if(mLoginDataSuccess==null){
            mLoginDataSuccess=new MutableLiveData<>();
            loadLoginData();
        }
        return mLoginDataSuccess;
    }
    public MutableLiveData<String> getLoginDataFailed(){
        if(mLoginDataFailed==null){
            mLoginDataFailed=new MutableLiveData<>();
            loadLoginData();
        }
        return mLoginDataFailed;
    }

    private void loadLoginData(){
        mRepository.setLoginLister(new AuthRepository.RepoLoginInterface() {
            @Override
            public void onSuccessLogin(String uid) {
                mLoginDataSuccess.setValue(uid);
            }

            @Override
            public void onFailedLogin(String error) {
                mLoginDataFailed.setValue(error);
            }
        });
    }

    public void loginUser(){
        mRepository.loginUser(mEmail,mPassword);
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        if(!email.equals(""))
            this.mEmail = email;
        else
            this.mEmail=" ";
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        if(!password.equals(""))
            this.mPassword = password;
        else
            this.mPassword=" ";
    }
}