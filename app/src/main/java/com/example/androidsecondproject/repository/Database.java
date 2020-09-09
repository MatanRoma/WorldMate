package com.example.androidsecondproject.repository;

import androidx.annotation.NonNull;

import com.example.androidsecondproject.model.Profile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Database {
    private final String profileTable = "profiles";
    private FirebaseDatabase database;
    private DatabaseReference profilesTable;
    private ProfileListener profileListener;


    public Database() {
        database=FirebaseDatabase.getInstance();
        profilesTable=database.getReference(profileTable);
    }

    public void readProfile(String uid){
                profilesTable.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            for(DataSnapshot data:snapshot.getChildren()){
                                Profile profile=data.getValue(Profile.class);
                                if(profileListener!=null)
                                    profileListener.onProfileDataChangeSuccess(profile);
                            }
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
    public void writeProfile(String uid,Profile profile){
        profilesTable.child(uid).setValue(profile);
        //TODO
    }



    public void setProfileListener(ProfileListener profileListener) {
        this.profileListener = profileListener;
    }

    public interface ProfileListener {
        void onProfileDataChangeSuccess(Profile profile);
        void onProfileDataChangeFail(String error);
    }

}
