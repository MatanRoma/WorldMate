package com.example.androidsecondproject.repository;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class StorageRepository {

    private StorageReference mStorageRef;
    private StorageDownloadProfilePicListener mDownloadProfilePicListener;
    private static StorageRepository mStorageRepository;
    private StorageUploadPicListener mUploadListener;
    private StorageDownloadMainPicListener mDownloadMainPicListener;



    private StorageRepository() {
        this.mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    public static StorageRepository getInstance(){
        if(mStorageRepository==null){
            mStorageRepository=new StorageRepository();
        }
        return mStorageRepository;
}

    public void uploadAndDownload(Bitmap bitmap,final boolean isProfilePic){
        String uniqueID = UUID.randomUUID().toString();
        final StorageReference imagesRef = mStorageRef.child("images/"+uniqueID+".jpg");



        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 25, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask2 = imagesRef.putBytes(data);
        uploadTask2.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("uri",uri+"");
                        if (isProfilePic)
                            mDownloadProfilePicListener.onSuccessDownloadProfilePic(uri);
                        else
                            mDownloadMainPicListener.onSuccessDownloadMainPic(uri);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        if(isProfilePic)
                            mDownloadProfilePicListener.onFailedDownloadProfilePic(exception.getMessage());
                        else{
                            mDownloadMainPicListener.onFailedDownloadMainPic(exception.getMessage());
                        }
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
              //  mUploadListener.onFailedUploadPic(e.getMessage());
            }
        });

    }

    public void writePictureToStorage(Bitmap bitmap,String uid){
        StorageReference imagesRef = mStorageRef.child("images/"+uid+".jpg");


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 25, baos);
        byte[] data = baos.toByteArray();
        //uploading the image
        UploadTask uploadTask2 = imagesRef.putBytes(data);
        uploadTask2.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                mUploadListener.onSuccessUploadPic(true);
           //     Log.d("imgurl",taskSnapshot.+"");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mUploadListener.onFailedUploadPic(e.getMessage());
            }
        });

    }
    public void readPictureFromStorage(String uid){

        mStorageRef.child("images/"+uid+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'

                Log.d("uri",uri+"");
                mDownloadProfilePicListener.onSuccessDownloadProfilePic(uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
               mDownloadProfilePicListener.onFailedDownloadProfilePic(exception.getMessage());
            }
        });
    }
    public interface StorageDownloadProfilePicListener {
        void onSuccessDownloadProfilePic(Uri uri);
        void onFailedDownloadProfilePic(String error);
    }
    public interface StorageDownloadMainPicListener {
        void onSuccessDownloadMainPic(Uri uri);
        void onFailedDownloadMainPic(String error);
    }
    public interface StorageUploadPicListener{
        void onSuccessUploadPic(boolean isSuccess );
        void onFailedUploadPic(String error);
    }
    public void setDownloadListener(StorageDownloadProfilePicListener downloadListener){
        this.mDownloadProfilePicListener =downloadListener;
    }
    public void setDownloadMainPicListener(StorageDownloadMainPicListener downloadListener){
        this.mDownloadMainPicListener =downloadListener;
    }
    public void setUploadListener(StorageUploadPicListener uploadListener){
        this.mUploadListener=uploadListener;
    }
}
