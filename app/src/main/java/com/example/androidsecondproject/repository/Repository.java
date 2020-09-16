package com.example.androidsecondproject.repository;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.androidsecondproject.model.Preferences;
import com.example.androidsecondproject.model.Profile;
import com.example.androidsecondproject.model.Question;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Repository {

    private final String PROFILE_TABLE = "profiles";
    private final String QUESTIONS_TABLE = "questions_table";
    private FirebaseDatabase database;
    private AuthRepository authRepository;
    private StorageRepository storageRepository;
    private DatabaseReference profilesTable;
    private DatabaseReference questionsTable;
    private ProfileListener profileListener;
    private ProfilesListener profilesListener;

    private static Repository repository;
    private QuestionsListener questionsListener;


    private Repository(Context context) {
        database=FirebaseDatabase.getInstance();
        profilesTable=database.getReference(PROFILE_TABLE);
        questionsTable=database.getReference(QUESTIONS_TABLE);
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
                                if(profileListener!=null) {
                                    Log.d("prof", "tst3");
                                    profileListener.onProfileDataChangeSuccess(profile);
                                }
                        }
                        else {
                            if(profileListener!=null)
                                profileListener.onProfileDataChangeFail("not_exist");
                        }
                    }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                        if(profileListener!=null)
                            profileListener.onProfileDataChangeFail(error.getMessage());
                        //TODO
                    }
        });
                /*
                profilesTable.child(uid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            Profile profile=snapshot.getValue(Profile.class);
                            Log.d("prof","tst2");
                            if(profileListener!=null) {
                                Log.d("prof", "tst3");
                                profileListener.onProfileDataChangeSuccess(profile);
                            }
                        }
                        else {
                            if(profileListener!=null)
                                profileListener.onProfileDataChangeFail("not_exist");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        if(profileListener!=null)
                            profileListener.onProfileDataChangeFail(error.getMessage());
                        //TODO
                    }
                });

                 */
    }

    public void readProfiles(final Profile myProfile){
        profilesTable.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    String myUid=getCurrentUserId();
                    List<Profile> profiles=new ArrayList<>();
                    for(DataSnapshot currSnapshot:snapshot.getChildren()){
                       if(!currSnapshot.getKey().equals(myUid)) {
                           Profile profile = currSnapshot.getValue(Profile.class);
                           if(checkCompatibility(myProfile,profile)) {
                               profiles.add(profile);
                           }
                       }
                    }
                    profilesListener.onProfilesDataChangeSuccess(profiles);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private boolean checkCompatibility(Profile myProfile, Profile otherProfile) {
        if(!otherProfile.isDiscovery()){
            return false;
        }
        if(checkCompatibilityHelper(myProfile,otherProfile.getPreferences())&&checkCompatibilityHelper(otherProfile,myProfile.getPreferences())){
            return true;
        }
        return false;
    }

    private boolean checkCompatibilityHelper(Profile profile, Preferences preferences) {
        float myAge=profile.getAge();
        String myGender=profile.getGender();
        /*if(myAge<preferences.getMinAge()||myAge>preferences.getMaxAge()){
            return false;
        }
        else if(!((preferences.isLookingForMen()&&myGender.equals("male"))||(preferences.isLookingForWomen()&&myGender.equals("female")))){
            return false;
        }*/
        return true;
    }

    public void writeMyProfile(Profile profile){
        Log.d("prof","tst1");
        profilesTable.child(authRepository.getCurrentUserUid()).setValue(profile);
        //TODO
    }
    public void writeOtherProfile(Profile profile){
        profilesTable.child((profile.getUid())).setValue(profile);

    }

    public String getCurrentUserId(){
       return authRepository.getCurrentUserUid();
    }
    public void setDownloadListener(StorageRepository.StorageDownloadPicListener downloadListener){
        storageRepository.setDownloadListener(downloadListener);
    }
    public void setUploadListener(StorageRepository.StorageUploadPicListener uploadListener){
        storageRepository.setUploadListener(uploadListener);
    }
    public void writePictureToStorage(Bitmap bitmap){
        storageRepository.writePictureToStorage(bitmap,authRepository.getCurrentUserUid());
    }
    public void readMyProfilePictureFromStorage(){
        storageRepository.readPictureFromStorage(authRepository.getCurrentUserUid());
    }
    public void readPictureFromStorage(String uid){
        storageRepository.readPictureFromStorage(uid);
    }

    public boolean checkIfAuth() {
        return authRepository.checkIfAuth();
    }

    public String getCurrenUserEmail() {
        return authRepository.getCurrentUserEmail();
    }


    public interface ProfileListener {
        void onProfileDataChangeSuccess(Profile profile);
        void onProfileDataChangeFail(String error);
    }
    public void setProfileListener(ProfileListener profileListener) {
        this.profileListener = profileListener;
    }
    public interface ProfilesListener{
        void onProfilesDataChangeSuccess(List<Profile> profiles);
        void onProfilesDataChangeFail(String error);
    }

    public void setProfilesListener(ProfilesListener profilesListener) {
        this.profilesListener = profilesListener;
    }
    public interface QuestionsListener{
        void onQuestionsDataChangeSuccess(List<Question> questions);
        void onQuestionsDataChangeFail(String error);
    }
    public void setQuestionsListener(QuestionsListener questionsListener) {
        this.questionsListener = questionsListener;
    }
    public void logout(){
        authRepository.logoutUser();
    }

    public void readQuestions(){
        questionsTable.child("english").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    List<Question> questions=new ArrayList<>();
                    for(DataSnapshot currSnapshot:snapshot.getChildren()){
                        Question question=currSnapshot.getValue(Question.class);
                        questions.add(question);
                    }
                    if(questionsListener!=null)
                         questionsListener.onQuestionsDataChangeSuccess(questions);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



}
