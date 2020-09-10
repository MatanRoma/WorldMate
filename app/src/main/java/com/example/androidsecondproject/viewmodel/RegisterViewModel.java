package com.example.androidsecondproject.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.androidsecondproject.repository.AuthRepository;

public class RegisterViewModel extends ViewModel {
    private MutableLiveData<String> mRegisterDataSuccess;
    private MutableLiveData<String> mRegisterDataFailed;
    private AuthRepository mRepository;
    private String mEmail;
    private String mPassword;
    private String mNickname;

    public RegisterViewModel(Context context) {
        mRepository=new AuthRepository(context);
    }

    public MutableLiveData<String> getRegisterResultSuccess(){
        if (mRegisterDataSuccess == null) {
            mRegisterDataSuccess = new MutableLiveData<>();
            loadRegisterData();
        }
        return mRegisterDataSuccess;
    }
    public MutableLiveData<String> getRegisterResultFailed(){
        if (mRegisterDataFailed == null) {
            mRegisterDataFailed = new MutableLiveData<>();
            loadRegisterData();
        }
        return mRegisterDataFailed;
    }


    private void loadRegisterData(){
        mRepository.setRegisterLister(new AuthRepository.RepoRegisterInterface() {
            @Override
            public void onSuccessRegister(String uid) {
                mRegisterDataSuccess.setValue(uid);
            }

            @Override
            public void onFailedRegister(String error) {
                mRegisterDataFailed.setValue(error);
            }
        });
    }
    public void registerUser(){

        mRepository.registerUser(mEmail, mPassword, mNickname);
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        if(!email.equals(""))
            this.mEmail = email;
        else
            this.mEmail =" ";
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        if(!password.equals(""))
            this.mPassword = password;
        else
            this.mPassword =" ";
    }

    public String getNickname() {
        return mNickname;
    }

    public void setNickname(String nickname) {
        this.mNickname = nickname;
    }

}
