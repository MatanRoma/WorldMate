package com.example.androidsecondproject.viewmodel;

import android.app.Application;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.androidsecondproject.model.Profile;
import com.example.androidsecondproject.repository.Repository;
import com.example.androidsecondproject.repository.StorageRepository;

public class ProfileViewModel extends AndroidViewModel {

    private Repository mRepository;
    private Profile profile;
    private Uri imageUri;
    private MutableLiveData<Boolean> mPictureUploadSuccess;
    private MutableLiveData<Uri> mPictureDownloadSuccess;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        mRepository =Repository.getInstance(application.getApplicationContext());
    }


    public void writeProfile(){
        mRepository.writeMyProfile(profile);
    }

    public void writePicture(Bitmap bitmap){
     //   repository.
        mRepository.writePictureToStorage(bitmap);
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


    public void downloadPicture(){
        mRepository.readMyProfilePictureFromStorage();
    }
    public void uploadPicture(Bitmap bitmap){
        mRepository.writePictureToStorage(bitmap);
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public void setImageUri(Uri imageUri) {
        profile.setProfilePictureUri(imageUri.toString());
    }

    public Profile getProfile()
    {
        return profile;
    }

    public Uri getImageUri()
    {
        return Uri.parse(profile.getProfilePictureUri()) ;
    }


}
