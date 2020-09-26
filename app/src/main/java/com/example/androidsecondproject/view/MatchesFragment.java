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

    public static MatchesFragment newInstance(Profile profile)
    {
        Bundle bundle=new Bundle();
        bundle.putSerializable("profile",profile);
        MatchesFragment matchesFragment=new MatchesFragment();
        matchesFragment.setArguments(bundle);
        return matchesFragment;
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
        mViewModel.setProfile((Profile) getArguments().getSerializable("profile"));


        final RecyclerView matchesRecycler = rootView.findViewById(R.id.matches_recycler);
        matchesRecycler.setHasFixedSize(true);
        matchesRecycler.setLayoutManager(new GridLayoutManager(getContext(),1));

        
        //mViewModel.setmMatchesAdapter(new MatchesAdapter(mViewModel.getMatches(),getContext()));
        Observer<Chat> chatSuccessObserver = new Observer<Chat>() {
            @Override
            public void onChanged(Chat chat) {
             //   moveToChat(chatId);

            }
        };
        Observer<List<Profile>> profileSuccessObserver =new Observer<List<Profile>>() {
            @Override
            public void onChanged(List<Profile> profiles) {
            mMatchesAdapter = new MatchesAdapter(mViewModel.getMatches(),getContext(),mViewModel.getProfile());
            mMatchesAdapter.setListener(new MatchesAdapter.MatchInterface() {
                @Override
                public void onChatClickedListener(int position) {
                    Profile otherProfile=mViewModel.getMatches().get(position);
                    String chatid=mViewModel.getChatId(otherProfile.getUid());
                    moveToChat(mViewModel.getProfile(),otherProfile,chatid);

                }
            });
            matchesRecycler.setAdapter(mMatchesAdapter);
            Toast.makeText(getContext(), mViewModel.getMatches().size()+"", Toast.LENGTH_SHORT).show();
            }
        };
        mViewModel.getChatResultSuccess().observe(this,chatSuccessObserver);
        mViewModel.getProfilesResultSuccess().observe(this, profileSuccessObserver);
        mViewModel.readMatches();

        return rootView;
    }



    public void moveToChat(Profile myProfile,Profile otherProfile,String chatid)
    {
        moveToChatListener.OnClickMoveToChat(myProfile,otherProfile,chatid);
    }


}
