package com.example.androidsecondproject.repository;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
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

import java.io.ByteArrayOutputStream;
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
                mDownloadListener.onSuccessDownloadPic(uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
               mDownloadListener.onFailedDownloadPic(exception.getMessage());
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
