package com.example.androidsecondproject.repository;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

public class StorageRepository {

    private StorageReference mStorageRef;
    private StorageDownloadPicListener mDownloadListener;
    private static StorageRepository mStorageRepository;
    private StorageUploadPicListener mUploadListener;



    private StorageRepository() {
        this.mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    public static StorageRepository getInstance(){
        if(mStorageRepository==null){
            mStorageRepository=new StorageRepository();
        }
        return mStorageRepository;
    }

    public void writePictureToStorage(Uri imageUri,String uid){
        StorageReference imagesRef = mStorageRef.child("images/"+uid+".jpg");
       // riversRef.child("users/me/profile.png").getDownloadUrl().
        imagesRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                      //  Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        mUploadListener.onSuccessUploadPic(true);


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });

    }
    public void readPictureFromStorage(String uid){


       /*final StorageReference ref = mStorageRef.child("images/"+uid+".jpg");
        try {

            final File localFile = File.createTempFile("Images", "bmp");
            ref.getFile(localFile).addOnSuccessListener(new OnSuccessListener< FileDownloadTask.TaskSnapshot >() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap my_image;
                    my_image = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    mDownloadListener.onSuccessDownloadPic(ref.getDownloadUrl().getResult());

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }*/


        mStorageRef.child("images/"+uid+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'

                Log.d("uri",uri+"");
                mDownloadListener.onSuccessDownloadPic(uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.d("uri","blabla");
            }
        });
    }
    public interface StorageDownloadPicListener{
        void onSuccessDownloadPic(Uri uri);
        void onFailedDownloadPic(String error);
    }
    public interface StorageUploadPicListener{
        void onSuccessUploadPic(boolean isSuccess );
        void onFailedUploadPic(String error);
    }
    public void setDownloadListener(StorageDownloadPicListener downloadListener){
        this.mDownloadListener=downloadListener;
    }
    public void setUploadListener(StorageUploadPicListener uploadListener){
        this.mUploadListener=uploadListener;
    }
}
