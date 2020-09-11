package com.example.androidsecondproject.viewmodel;

import android.app.Application;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.androidsecondproject.repository.Repository;
import com.example.androidsecondproject.repository.StorageRepository;

public class ProfilePhotoViewModel extends AndroidViewModel {
    Repository mRepository;
    private MutableLiveData<Boolean> mPictureUploadSuccess;
    private MutableLiveData<Uri> mPictureDownloadSuccess;


    public ProfilePhotoViewModel(@NonNull Application application) {
        super(application);
        mRepository=Repository.getInstance(application.getApplicationContext());
    }
    public MutableLiveData<Boolean> getUploadResultSuccess(){
        if (mPictureUploadSuccess == null) {
            mPictureUploadSuccess = new MutableLiveData<>();
            setUploadListener();
        }
        return mPictureUploadSuccess;
    }

    private void setUploadListener() {
        mRepository.setUploadListener(new StorageRepository.StorageUploadPicListener() {
            @Override
            public void onSuccessUploadPic(boolean isSuccess) {
                mPictureUploadSuccess.setValue(isSuccess);
            }

            @Override
            public void onFailedUploadPic(String error) {

            }
        });
    }

    public MutableLiveData<Uri> getDownloadResultSuccess(){
        if (mPictureDownloadSuccess == null) {
            mPictureDownloadSuccess = new MutableLiveData<>();
            setDownloadListener();
        }
        return mPictureDownloadSuccess;
    }
    private void setDownloadListener() {
        mRepository.setDownloadListener(new StorageRepository.StorageDownloadPicListener() {
            @Override
            public void onSuccessDownloadPic(Uri uri) {
                mPictureDownloadSuccess.setValue(uri);
            }

            @Override
            public void onFailedDownloadPic(String error) {

            }
        });
    }
    public void uploadPicture(Uri imageUri){
        mRepository.writePictureToStorage(imageUri);
    }
    public void downloadPicture(){
        mRepository.readMyProfilePictureFromStorage();
    }

}
