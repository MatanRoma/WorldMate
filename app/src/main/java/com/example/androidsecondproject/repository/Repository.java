package com.example.androidsecondproject.repository;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.androidsecondproject.model.Profile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Repository {

    private final String PROFILE_TABLE = "profiles";
    private FirebaseDatabase database;
    private AuthRepository authRepository;
    private StorageRepository storageRepository;
    private DatabaseReference profilesTable;
    private ProfileListener profileListener;

    private static Repository repository;


    private Repository(Context context) {
        database=FirebaseDatabase.getInstance();
        profilesTable=database.getReference(PROFILE_TABLE);
        authRepository=AuthRepository.getInstance(context);
        storageRepository=StorageRepository.getInstance();

    }
    public static Repository getInstance(Context context){
        if(repository==null){
            repository=new Repository(context);
        }
        return repository;
    }

    public void readProfile(String uid){
                profilesTable.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                                Profile profile=snapshot.getValue(Profile.class);
                            Log.d("prof","tst2");
                                if(profileListener!=null)
                                    Log.d("prof","tst3");
                                    profileListener.onProfileDataChangeSuccess(profile);
                        }
                    }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if(profileListener!=null)
                    profileListener.onProfileDataChangeFail(error.getMessage());
                //TODO
            }
        });
    }
    public void writeProfile(Profile profile){
        profilesTable.child(authRepository.getCurrentUserUid()).setValue(profile);
        //TODO
    }

    public String getCurrentUserId(){
       return authRepository.getCurrentUserUid();
    }
    public void setProfileListener(ProfileListener profileListener) {
        this.profileListener = profileListener;
    }
    public void setDownloadListener(StorageRepository.StorageDownloadPicListener downloadListener){
        storageRepository.setDownloadListener(downloadListener);
    }
    public void setUploadListener(StorageRepository.StorageUploadPicListener uploadListener){
        storageRepository.setUploadListener(uploadListener);
    }
    public void writePictureToStorage(Uri imageUri){
        storageRepository.writePictureToStorage(imageUri,authRepository.getCurrentUserUid());
    }
    public void readMyProfilePictureFromStorage(){
        storageRepository.readPictureFromStorage(authRepository.getCurrentUserUid());
    }
    public void readPictureFromStorage(String uid){
        storageRepository.readPictureFromStorage(uid);
    }

    public interface ProfileListener {
        void onProfileDataChangeSuccess(Profile profile);
        void onProfileDataChangeFail(String error);
    }
    public void logout(){
        authRepository.logoutUser();
    }

}
