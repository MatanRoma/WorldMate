package com.example.androidsecondproject.repository;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.androidsecondproject.model.Chat;
import com.example.androidsecondproject.model.Match;
import com.example.androidsecondproject.model.Message;
import com.example.androidsecondproject.model.Preferences;
import com.example.androidsecondproject.model.Profile;
import com.example.androidsecondproject.model.Question;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Repository {

    private final String PROFILE_TABLE = "profiles";
    private final String QUESTIONS_TABLE = "questions_table";
    private final String CHATS_TABLE = "chats_table";
    private FirebaseDatabase database;
    private AuthRepository authRepository;
    private StorageRepository storageRepository;
    private DatabaseReference profilesTable;
    private DatabaseReference questionsTable;
    private DatabaseReference chatsTable;
    private ProfileListener profileListener;
    private ProfilesListener profilesListener;

    private static Repository repository;
    private QuestionsListener questionsListener;
    private ChatListener chatListener;


    private Repository(Context context) {
        database=FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);
        profilesTable=database.getReference(PROFILE_TABLE);
        questionsTable=database.getReference(QUESTIONS_TABLE);
        chatsTable = database.getReference(CHATS_TABLE);
        chatsTable.keepSynced(true);
        authRepository=AuthRepository.getInstance(context);
        storageRepository=StorageRepository.getInstance();

    }
    public static Repository getInstance(Context context){
        if(repository==null){
            repository=new Repository(context);
        }
        return repository;
    }

/*    public void addChat()
    {
        String key = FirebaseDatabase.getInstance().getReference().child("chat").push().getKey();

    }*/

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

    public void readMatches(final Profile myProfile){

        profilesTable.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    String myUid=getCurrentUserId();
                    List<Profile> profiles=new ArrayList<>();
                    List<Match> matches=myProfile.getMatches();
                    for(DataSnapshot currSnapshot:snapshot.getChildren()){
                        String otherUid=currSnapshot.getKey();
                        Log.d("uid",otherUid);
                        if(!otherUid.equals(myUid)) {
                            Profile profile = currSnapshot.getValue(Profile.class);
                            for(Match match:matches){
                                if(match.getOtherUid().equals(otherUid))
                                    profiles.add(profile);
                            }
                            ;
                        }
                    }
                    Log.d("size",profiles.size()+"");
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

    public void writeChat(String chatid)
    {
        Log.d("chat",chatid);
        chatsTable.child(chatid).push().setValue(null);
    }

    public void readChat(String chatId){
        profilesTable.child(chatId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Chat chat=snapshot.getValue(Chat.class);
                    if(chatListener!=null) {
                        chatListener.onChatDataChangeSuccess(chat);
                    }
                }
                else {
                    if(chatListener!=null)
                        chatListener.onChatDataChangeFail("not_exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if(chatListener!=null)
                    chatListener.onChatDataChangeFail(error.getMessage());
                //TODO
            }
        });

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

    public Query readAllMessages(String chatId) {
        return chatsTable.child(chatId);
    }
    public void writeMessage(String chatId, Message message){
        chatsTable.child(chatId).push().setValue(message);
    }

    public interface ChatListener{
        void onChatDataChangeSuccess(Chat chat);
        void onChatDataChangeFail(String error);
    }
    public void setChatListener(ChatListener chatListener) {
        this.chatListener = chatListener;
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
