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

import java.util.List;

public class ProfileViewModel extends AndroidViewModel {

    private Repository mRepository;
    private Profile mProfile;
    private MutableLiveData<Boolean> mPictureUploadSuccess;
    private MutableLiveData<Uri> mPictureProfileDownloadSuccess;
    private MutableLiveData<Uri> mPictureMainDownloadSuccess;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        mRepository =Repository.getInstance(application.getApplicationContext());
    }


    public void writeProfile(){
        mRepository.writeMyProfile(mProfile);
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
    public MutableLiveData<Uri> getDownloadProfileResultSuccess(){
        if (mPictureProfileDownloadSuccess == null) {
            mPictureProfileDownloadSuccess = new MutableLiveData<>();
            setDownloadProfilePicListener();
        }
        return mPictureProfileDownloadSuccess;
    }

    public MutableLiveData<Uri> getDownloadMainResultSuccess(){
        if (mPictureMainDownloadSuccess == null) {
            mPictureMainDownloadSuccess = new MutableLiveData<>();
            setDownloadMainPicListener();
        }
        return mPictureMainDownloadSuccess;
    }

    private void setDownloadMainPicListener() {
        mRepository.setDownloadMainPicListener(new StorageRepository.StorageDownloadMainPicListener() {
            @Override
            public void onSuccessDownloadMainPic(Uri uri) {
                mPictureMainDownloadSuccess.setValue(uri);
            }

            @Override
            public void onFailedDownloadMainPic(String error) {

            }
        });
    }

    private void setDownloadProfilePicListener() {
        mRepository.setDownloadProfilePicListener(new StorageRepository.StorageDownloadProfilePicListener() {
            @Override
            public void onSuccessDownloadProfilePic(Uri uri) {
                mPictureProfileDownloadSuccess.setValue(uri);
            }

            @Override
            public void onFailedDownloadProfilePic(String error) {

            }
        });
    }


    public void downloadPicture(){
        mRepository.readMyProfilePictureFromStorage();
    }
    public void uploadPicture(Bitmap bitmap,boolean isProfilePic){
       // mRepository.writePictureToStorage(bitmap);
        mRepository.uploadAndDownload(bitmap,isProfilePic);
    }

    public void setmProfile(Profile mProfile) {
        this.mProfile = mProfile;
    }

    public void setImageUri(Uri imageUri) {
        mProfile.setProfilePictureUri(imageUri.toString());
    }

    public Profile getmProfile()
    {
        return mProfile;
    }

    public Uri getImageUri()
    {
        return Uri.parse(mProfile.getProfilePictureUri()) ;
    }


    public void readProfiles() {
        mRepository.readProfiles(mProfile);
    }

    public List<String> getPictures() {
        return mProfile.getPictures();

    }

    public void updateProfileMainPictures() {
        mRepository.updateProfile(mRepository.getCurrentUserId(),"pictures",mProfile.getPictures());
    }

    public void updateDataBaseProfilePic(String s) {
        mRepository.updateProfile(mRepository.getCurrentUserId(),"profilePictureUri",s);
    }
}
