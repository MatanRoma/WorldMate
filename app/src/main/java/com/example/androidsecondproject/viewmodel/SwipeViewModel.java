package com.example.androidsecondproject.viewmodel;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.androidsecondproject.model.Chat;
import com.example.androidsecondproject.model.Match;
import com.example.androidsecondproject.model.Profile;
import com.example.androidsecondproject.repository.Repository;

import java.util.List;

public class SwipeViewModel extends AndroidViewModel {

    private Repository mRepository;
    private Profile mProfile;
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
         mProfile.getLikes().add((mProfilesMutableLiveData.getValue().get(position).getEmail()));
    }

    public void removeProfile(int position) {
        mProfilesMutableLiveData.getValue().remove(position);
    }

    public boolean checkIfMatch(int position) {
        String myEmail=mProfile.getEmail();
        List<String> likeEmails=mProfilesMutableLiveData.getValue().get(position).getLikes();
        return likeEmails.contains(myEmail);
    }

    public void updateMatch(int position) {
        Profile otherPofile=mProfilesMutableLiveData.getValue().get(position);
        String key = mProfile.getEmail()+otherPofile.getEmail();
        key = key.replace('.',' ');
        key = key.replace('#',' ');
        key = key.replace('$',' ');
        key = key.replace('[',' ');
        key = key.replace(']',' ');
        key = key.trim();
        Log.d("chat",key);
        Match myMatch = new Match(otherPofile.getEmail(),key);
        //match.setEmail(otherPofile.getEmail());

        mProfile.getMatches().add(myMatch);
        Toast.makeText(getApplication(), otherPofile.getEmail()+"", Toast.LENGTH_SHORT).show();
        Match otherMatch = new Match(mProfile.getEmail(),key);
        otherPofile.getMatches().add(otherMatch);
        mRepository.writeChat(new Chat(key,mProfile.getEmail(),otherPofile.getEmail()));
    }
    public void test(){
        for(String str:mProfile.getLikes()){
            Log.d("like",str);
        }
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
}
