package com.example.androidsecondproject.viewmodel;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.androidsecondproject.model.Chat;
import com.example.androidsecondproject.model.Match;
import com.example.androidsecondproject.model.NotificationManager;
import com.example.androidsecondproject.model.Profile;
import com.example.androidsecondproject.repository.Repository;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class SwipeViewModel extends AndroidViewModel {

    private Context context;
    private Repository mRepository;
    private Profile mProfile;
    private boolean isFirstTime=true;
    MutableLiveData<List<Profile>> mProfilesMutableLiveData;

    public SwipeViewModel(@NonNull Application application) {
        super(application);
        mRepository=Repository.getInstance(application.getApplicationContext());
    }
    public MutableLiveData<List<Profile>> getProfilesResultSuccess(){
        if (mProfilesMutableLiveData == null) {
            mProfilesMutableLiveData = new MutableLiveData<>();
            loadProfilesData();
        }
        return mProfilesMutableLiveData;
    }
    public void readProfiles(){
        mRepository.readProfiles(mProfile);
    }
    public void setUserProfile(Profile profile){
        this.mProfile=profile;
    }

    private void loadProfilesData() {
        mRepository.setProfilesListener(new Repository.ProfilesListener() {
            @Override
            public void onProfilesDataChangeSuccess(List<Profile> profiles) {
                mProfilesMutableLiveData.setValue(profiles);
            }

            @Override
            public void onProfilesDataChangeFail(String error) {

            }
        });
    }

    public void addLikedProfile(int position) {
         mProfile.getLikes().add((mProfilesMutableLiveData.getValue().get(position).getUid()));
    }

    public void removeProfile(int position) {
        mProfilesMutableLiveData.getValue().remove(position);
    }

    public boolean checkIfMatch(int position) {
        String myUid=mProfile.getUid();
        List<String> likeUids=mProfilesMutableLiveData.getValue().get(position).getLikes();
        return likeUids.contains(myUid);
    }

    public void updateMatch(int position) {
        Profile otherPofile=mProfilesMutableLiveData.getValue().get(position);
        String key = mProfile.getUid()+otherPofile.getUid();
        /*key = key.replace('.',' ');
        key = key.replace('#',' ');
        key = key.replace('$',' ');
        key = key.replace('[',' ');
        key = key.replace(']',' ');
        key = key.trim();
        Log.d("chat",key);*/
        Match myMatch = new Match(otherPofile.getUid(),key);
        //match.setEmail(otherPofile.getEmail());

        mProfile.getMatches().add(myMatch);
        Toast.makeText(getApplication(), otherPofile.getUid()+"", Toast.LENGTH_SHORT).show();
        Match otherMatch = new Match(mProfile.getUid(),key);
        otherPofile.getMatches().add(otherMatch);
     //   notifyOtherProfile(otherPofile.getMessageToken());
        notifyOtherProfile(otherPofile.getMessageToken()); // only for test
      //  mRepository.writeChat(mProfile.getUid()+otherPofile.getUid());
    }

    public void writeMyProfile() {
        mRepository.writeMyProfile(mProfile);
    }

    public void writeOtherProfile(int position) {
        mRepository.writeOtherProfile(mProfilesMutableLiveData.getValue().get(position));
    }
    public Profile getProfile()
    {
        return mProfile;
    }

    public void notifyOtherProfile(String to) {
        JSONObject rootObject=new JSONObject();
        JSONObject dataObject=new JSONObject();
        try {
            rootObject.put("to",to);
            dataObject.put("match_uid",mProfile.getUid());
            dataObject.put("sender",mProfile.getFirstName());
            rootObject.put("data",dataObject);
            NotificationManager.sendNotification(context,rootObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void setContext(Context context) {
        this.context=context;
    }

    public boolean isFirstTime() {
        return isFirstTime;
    }

    public void setFirstTime(boolean firstTime) {
        isFirstTime = firstTime;
    }
}
