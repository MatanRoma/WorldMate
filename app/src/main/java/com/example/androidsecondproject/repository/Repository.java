package com.example.androidsecondproject.repository;

import android.content.Context;
import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import com.example.androidsecondproject.model.Chat;
import com.example.androidsecondproject.model.ChatAndMessages;
import com.example.androidsecondproject.model.Match;
import com.example.androidsecondproject.model.Message;
import com.example.androidsecondproject.model.Preferences;
import com.example.androidsecondproject.model.Profile;
import com.example.androidsecondproject.model.Question;
import com.example.androidsecondproject.model.TranslateString;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Repository {

    private final String PROFILE_TABLE = "profiles";
    private final String QUESTIONS_TABLE = "questions_table";
    private final String CHATS_TABLE = "chats_table";
    private FirebaseDatabase database;
    private AuthRepository mAuthRepository;
    private StorageRepository mStorageRepository;
    private DatabaseReference mProfilesTable;
    private DatabaseReference mQuestionsTable;
    private DatabaseReference mChatsTable;
    private ProfileListener mProfileListener;
    private ProfilesListener mProfilesListener;
    private MessageListener mMessageListener;
    private ChatListener mChatListener;
    private ProfilesForGuestListener mProfilesForGuestListener;
    private ValueEventListener mMyProfileValueEventListener;
    private ValueEventListener mIsOnlineEventListener;
    private LikesListener mLikesListener;
    private static Repository repository;
    private QuestionsListener mQuestionsListener;
    private ReadOtherProfileListener mReadOtherProfileListener;
    private MatchesListener mMatchesListener;
    private Context mContext;
    private OnlineListener mOnlineListener;

    private Repository(Context context) {
        database=FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(false);
        mProfilesTable =database.getReference(PROFILE_TABLE);
        mQuestionsTable =database.getReference(QUESTIONS_TABLE);
        mChatsTable = database.getReference(CHATS_TABLE);
        mChatsTable.keepSynced(true);
        mAuthRepository =AuthRepository.getInstance(context);
        mStorageRepository =StorageRepository.getInstance();
        mContext = context;

    }
    public static Repository getInstance(Context context){
        if(repository==null){
            repository=new Repository(context);
        }
        return repository;
    }

    public void readProfile(String uid){
        if(mMyProfileValueEventListener!=null)
            mProfilesTable.child(uid).removeEventListener(mMyProfileValueEventListener);
        mMyProfileValueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Profile profile=snapshot.getValue(Profile.class);
                    if(mProfileListener !=null) {
                        mProfileListener.onProfileDataChangeSuccess(profile);
                    }
                }
                else {
                    if(mProfileListener !=null)
                        mProfileListener.onProfileDataChangeFail("not_exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if(mProfileListener !=null)
                    mProfileListener.onProfileDataChangeFail(error.getMessage());
                //TODO
            }
        };
        mProfilesTable.child(uid).addValueEventListener(mMyProfileValueEventListener);
    }

    public void readProfiles(final Profile myProfile){

        mProfilesTable.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String myUid=myProfile.getUid();
                    List<Profile> profiles=new ArrayList<>();
                    Set<String> matchesSet=generateMatchSet(myProfile);
                    for(DataSnapshot currSnapshot:snapshot.getChildren()){
                        if(!currSnapshot.getKey().equals(myUid)) {
                            Profile profile = currSnapshot.getValue(Profile.class);
                            if(checkCompatibility(myProfile,profile,matchesSet)) {
                                profiles.add(profile);
                            }
                        }
                    }
                    if(mProfilesListener !=null) {
                        Collections.shuffle(profiles);
                        mProfilesListener.onProfilesDataChangeSuccess(profiles);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private Set<String> generateMatchSet(Profile myProfile) {
        Set<String> matchSet=new HashSet<>();
        for(Match match:myProfile.getMatches()){
            matchSet.add(match.getOtherUid());
        }
        return matchSet;
    }

    public void readMatches(final Profile myProfile){

        mProfilesTable.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    String myUid=getCurrentUserId();
                    List<Profile> profiles=new ArrayList<>();
                    List<Match> matches=myProfile.getMatches();
                    for(DataSnapshot currSnapshot:snapshot.getChildren()){
                        String otherUid=currSnapshot.getKey();
                        if(!otherUid.equals(myUid)) {
                            Profile profile = currSnapshot.getValue(Profile.class);
                            for(Match match:matches){
                                if(match.getOtherUid().equals(otherUid))
                                    profiles.add(profile);
                            }
                            ;
                        }
                    }
                    mMatchesListener.onMatchesDataChangeSuccess(profiles);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private boolean checkCompatibility(Profile myProfile, Profile otherProfile,Set<String> myMatchesSet) {
        if(!otherProfile.isDiscovery()){
            return false;
        }
        else if(otherProfile.getLikes().contains(myProfile.getUid())||otherProfile.getDisLikes().contains(myProfile.getUid())||myMatchesSet.contains(otherProfile.getUid())){
            return false;
        }
       else if(checkCompatibilityHelper(myProfile,otherProfile)&&checkCompatibilityHelper(otherProfile,myProfile)){
            return true;
        }
        return false;
    }


    public void removeSpecificChat(String chatId){
        mChatsTable.child(chatId).removeValue();
    }

    private boolean checkCompatibilityHelper(Profile profile, Profile otherProfile) {
        int myAge = profile.calculateCurrentAge();
        String myGender = profile.getGender();
        Preferences otherPreferences = otherProfile.getPreferences();
        Preferences myPreferences=profile.getPreferences();

        if (myAge < otherPreferences.getMinAge() || myAge > otherPreferences.getMaxAge()) {
            return false;
        } else if (!((otherPreferences.isLookingForMen() && TranslateString.checkMale(myGender)) || (otherPreferences.isLookingForWomen() && TranslateString.checkFemale(myGender)))) {
            return false;
        } else if (profile.getCity() == null && otherProfile.getCity() != null) {
            return false;
        }
        else if(profile.getLocation()!=null&&otherProfile.getLocation()!=null){
            if(profile.getLocation().calculateDistance(otherProfile.getLocation())>myPreferences.getMaxDistance())
                return false;
        }
        return true;
    }

    public void writeMyProfile(Profile profile){
        mProfilesTable.child(profile.getUid()).setValue(profile);
        //TODO
    }

    public void writeOtherProfile(Profile profile){
        mProfilesTable.child((profile.getUid())).setValue(profile);
    }

    public void updateProfile(String uid, String key, Object objectToUpdate){
        Map<String,Object> map =new HashMap<>();
        map.put(key,objectToUpdate);
        mProfilesTable.child((uid)).updateChildren(map);
    }

    public String getCurrentUserId(){
        return mAuthRepository.getCurrentUserUid();
    }

    public void setDownloadProfilePicListener(StorageRepository.StorageDownloadProfilePicListener downloadListener){
        mStorageRepository.setDownloadListener(downloadListener);
    }

    public void setDownloadMainPicListener(StorageRepository.StorageDownloadMainPicListener downloadMainPicListener){
        mStorageRepository.setDownloadMainPicListener(downloadMainPicListener);
    }

    public void setUploadListener(StorageRepository.StorageUploadPicListener uploadListener){
        mStorageRepository.setUploadListener(uploadListener);
    }

    public void writePictureToStorage(Bitmap bitmap){
        mStorageRepository.writePictureToStorage(bitmap, mAuthRepository.getCurrentUserUid());
    }

    public void readMyProfilePictureFromStorage(){
        mStorageRepository.readPictureFromStorage(mAuthRepository.getCurrentUserUid());
    }

    public void readPictureFromStorage(String uid){
        mStorageRepository.readPictureFromStorage(uid);
    }

    public boolean checkIfAuth() {
        return mAuthRepository.checkIfAuth();
    }

    public String getCurrenUserEmail() {
        return mAuthRepository.getCurrentUserEmail();
    }

    public Query readAllMessages(String chatId) {
        return mChatsTable.child(chatId).child("Messages");
    }

    public void writeMessage(String chatId, final Message message){
        mChatsTable.child(chatId).child("chat").setValue(new Chat(chatId,message.getRecipientUid(),message.getSenderUid(),message));
        mChatsTable.child(chatId).child("Messages").push().setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if(mMessageListener !=null)
                    mMessageListener.onMessageSentSuccess(message);
            }
        });
    }
    private String calculateChatUid(String myUid,String otherUid){
        if(myUid.compareTo(otherUid)>0){
            return myUid+otherUid;
        }
        else {
            return otherUid + myUid;
        }
    }
    public void readChats(final Profile profile) {
        final Set<String> chatIds=new HashSet<>();
        for(Match match:profile.getMatches()){
            chatIds.add(match.getId());
        }
        mChatsTable.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Chat> chats=new ArrayList<>();

                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    ChatAndMessages chatAndMessages=snapshot.getValue(ChatAndMessages.class);
                    if(chatIds.contains(chatAndMessages.getChat().getId()))
                        chats.add(chatAndMessages.getChat());
                }
                if(mChatListener !=null){
                    mChatListener.onChatDataChanged(chats);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void writeChat(Chat chat) {
        chat.setLastMessage(new Message(chat.getFirstUid(),"",chat.getSecondUid()));
        mChatsTable.child(chat.getId()).child("chat").setValue(chat);
    }

    public void readProfilesForGuest() {
        mProfilesTable.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    final int MAX_PROFILES_TO_SHOW=3;
                    int i=0;
                    List<Profile> profiles=new ArrayList<>();
                    for(DataSnapshot currSnapshot:snapshot.getChildren()){
                        if(i==MAX_PROFILES_TO_SHOW)
                            break;
                        profiles.add(currSnapshot.getValue(Profile.class));
                        i++;
                    }
                    if(mProfilesForGuestListener !=null)
                        mProfilesForGuestListener.onGuestProfilesChangedSuccess(profiles);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void remveProfileListener(String uid) {
        if (mMyProfileValueEventListener!=null){
            mProfilesTable.child(uid).removeEventListener(mMyProfileValueEventListener);
        }
    }

    public interface  ProfilesForGuestListener{
        void onGuestProfilesChangedSuccess(List<Profile> guestProfiles);
    }
    public void setProfileGuestListener(ProfilesForGuestListener profileGuestListener){
        this.mProfilesForGuestListener =profileGuestListener;
    }


    public interface MessageListener{
        void onMessageSentSuccess(Message message);
    }


    public interface ProfileListener {
        void onProfileDataChangeSuccess(Profile profile);
        void onProfileDataChangeFail(String error);
    }
    public void setProfileListener(ProfileListener profileListener) {
        this.mProfileListener = profileListener;
    }
    public interface ProfilesListener{
        void onProfilesDataChangeSuccess(List<Profile> profiles);
        void onProfilesDataChangeFail(String error);
    }

    public void setProfilesListener(ProfilesListener profilesListener) {
        this.mProfilesListener = profilesListener;
    }
    public void setMatchesListener(MatchesListener matchesListener){
        this.mMatchesListener =matchesListener;
    }

    public interface  MatchesListener{
        void onMatchesDataChangeSuccess(List<Profile> matches);
        void onMatchesDataChangeFail(String error);
    }

    public interface QuestionsListener{
        void onQuestionsDataChangeSuccess(List<Question> questions);
        void onQuestionsDataChangeFail(String error);
    }
    public interface ChatListener{
        void onChatDataChanged(List<Chat> chats);
    }
    public void setQuestionsListener(QuestionsListener questionsListener) {
        this.mQuestionsListener = questionsListener;
    }
    public void logout(){
        mAuthRepository.logoutUser();
    }

    public void readQuestions(String language){

        mQuestionsTable.child(language).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    List<Question> questions=new ArrayList<>();
                    for(DataSnapshot currSnapshot:snapshot.getChildren()){
                        Question question=currSnapshot.getValue(Question.class);
                        questions.add(question);
                    }
                    if(mQuestionsListener !=null)
                        mQuestionsListener.onQuestionsDataChangeSuccess(questions);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {



            }
        });
    }

    public MessageListener getMessageListener() {
        return mMessageListener;
    }

    public void setMessageListener(MessageListener messageListener) {
        this.mMessageListener = messageListener;
    }

    public interface ReadOtherProfileListener{
        void onOtherProfileChange(Profile profile);
    }
    public void setOtherProfileListener(ReadOtherProfileListener readOtherProfileListener){
        this.mReadOtherProfileListener =readOtherProfileListener;
    }
    public void readOtherProfile(String uid) {

        mProfilesTable.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Profile profile = snapshot.getValue(Profile.class);
                    if (mProfileListener != null) {
                        mReadOtherProfileListener.onOtherProfileChange(profile);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (mProfileListener != null)
                    mProfileListener.onProfileDataChangeFail(error.getMessage());
            }
        });
    }
    public void uploadAndDownload(Bitmap bitmap,boolean isProfilePic){
        mStorageRepository.uploadAndDownload(bitmap,isProfilePic);

    }
    public void setChatListener(ChatListener chatListener){
        this.mChatListener =chatListener;
    }

    public void deletePhotoFromStorage(String url){
        mStorageRepository.deletePhotoFromStorage(url);
    }
    public void readLikedProfiles(final Profile myProfile){
        mProfilesTable.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String myUid=myProfile.getUid();
                    List<Profile> profiles=new ArrayList<>();
                    for(DataSnapshot currSnapshot:snapshot.getChildren()){
                        if(!currSnapshot.getKey().equals(myUid)) {
                            Profile otherProfile = currSnapshot.getValue(Profile.class);
                            if(otherProfile.getLikes().contains(myUid)) {
                                profiles.add(otherProfile);

                            }
                        }
                    }
                    if(mLikesListener!=null) {
                        mLikesListener.onProfileLikedDataChangeSuccess(profiles);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public interface LikesListener{
        void onProfileLikedDataChangeSuccess(List<Profile> profiles);
        void onProfileLikedDataChangeFail(String error);
    }
    public void setLikesListener(LikesListener likesListener){
        this.mLikesListener=likesListener;
    }

    public interface OnlineListener{
        void onOnlineChangeSuccess(boolean isOnline);
    }

    public void setOnlineListener(OnlineListener onlineListener){
        this.mOnlineListener = onlineListener;
    }



    public void readProfileIsOnline(String uid)
    {
        if(mIsOnlineEventListener!=null)
            mProfilesTable.child(uid).child("online").removeEventListener(mIsOnlineEventListener);
        mIsOnlineEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    if(mOnlineListener != null)
                    {
                        mOnlineListener.onOnlineChangeSuccess((Boolean) snapshot.getValue());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mProfilesTable.child(uid).child("online").addValueEventListener(mIsOnlineEventListener);

    }
}
