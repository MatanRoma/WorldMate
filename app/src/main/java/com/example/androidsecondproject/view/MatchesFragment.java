package com.example.androidsecondproject.view;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidsecondproject.R;
import com.example.androidsecondproject.model.Chat;
import com.example.androidsecondproject.model.MatchesAdapter;
import com.example.androidsecondproject.model.Profile;
import com.example.androidsecondproject.model.eViewModels;
import com.example.androidsecondproject.viewmodel.MatchesViewModel;
import com.example.androidsecondproject.viewmodel.ViewModelFactory;

import java.util.List;

public class MatchesFragment extends Fragment  {
    private MatchesViewModel mViewModel;
    private MatchesAdapter mMatchesAdapter;
    private OnMoveToChat moveToChatListener;

    public interface OnMoveToChat{
        public void OnClickMoveToChat(Profile myProfile,Profile otherProfile,String chatid);
    }

    public static MatchesFragment newInstance(String matherUid)
    {
        Bundle bundle=new Bundle();
        bundle.putString("matcher_uid",matherUid);
        MatchesFragment matchesFragment=new MatchesFragment();
        matchesFragment.setArguments(bundle);
        return matchesFragment;
    }
    public static MatchesFragment newInstance()
    {
        return new MatchesFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        moveToChatListener=(OnMoveToChat)getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.matches_fragment,container,false);
        mViewModel =new ViewModelProvider(this,new ViewModelFactory(getActivity().getApplication(), eViewModels.Matches)).get(MatchesViewModel.class);
      //  mViewModel.setProfile((Profile) getArguments().getSerializable("profile"));
        if(getArguments()!=null)
            mViewModel.setNewMatchUid(getArguments().getString("matcher_uid"));


        final RecyclerView matchesRecycler = rootView.findViewById(R.id.matches_recycler);
        matchesRecycler.setHasFixedSize(true);
        matchesRecycler.setLayoutManager(new GridLayoutManager(getContext(),1));


        Observer<Profile> myProfileSuccessObserver=new Observer<Profile>() {
            @Override
            public void onChanged(Profile profile) {
                mViewModel.readMatches();
            }
        };


        Observer<List<Profile>> profileSuccessObserver =new Observer<List<Profile>>() {
            @Override
            public void onChanged(List<Profile> profiles) {
            mMatchesAdapter = new MatchesAdapter(mViewModel.getMatches(),getContext(),mViewModel.getMyProfile(),mViewModel.getNewMatchUid());
            mMatchesAdapter.setListener(new MatchesAdapter.MatchInterface() {
                @Override
                public void onChatClickedListener(int position) {
                    Profile otherProfile=mViewModel.getMatches().get(position);
                    String chatid=mViewModel.getChatId(otherProfile.getUid());
                    moveToChat(mViewModel.getMyProfile(),otherProfile,chatid);

                }
            });
            matchesRecycler.setAdapter(mMatchesAdapter);

            }
        };
        mViewModel.getMyProfileResultSuccess().observe(this,myProfileSuccessObserver);
        mViewModel.getProfilesResultSuccess().observe(this, profileSuccessObserver);
        mViewModel.readMyProfile();
        return rootView;
    }



    public void moveToChat(Profile myProfile,Profile otherProfile,String chatid)
    {
        moveToChatListener.OnClickMoveToChat(myProfile,otherProfile,chatid);
    }


}
