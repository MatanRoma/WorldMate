package com.example.androidsecondproject.view;

import android.os.Bundle;
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



    public static MatchesFragment newInstance(Profile profile)
    {
        Bundle bundle=new Bundle();
        bundle.putSerializable("profile",profile);
        MatchesFragment matchesFragment=new MatchesFragment();
        matchesFragment.setArguments(bundle);
        return matchesFragment;
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
        Observer<List<Profile>> profileSuccessObserver =new Observer<List<Profile>>() {
            @Override
            public void onChanged(List<Profile> profiles) {
            mViewModel.setmProfiles(profiles);
            mViewModel.calculateMatches();
            mMatchesAdapter = new MatchesAdapter(mViewModel.getMatches(),getContext(),mViewModel.getProfile());
            mMatchesAdapter.setListener(new MatchesAdapter.MatchInterface() {
                @Override
                public void onChatClickedListener(String chatId) {
                    readChat(chatId);
                }
            });
            matchesRecycler.setAdapter(mMatchesAdapter);
            Toast.makeText(getContext(), mViewModel.getMatches().size()+"", Toast.LENGTH_SHORT).show();
            }
        };
        mViewModel.getProfilesResultSuccess().observe(this, profileSuccessObserver);
        mViewModel.readProfiles();

        return rootView;
    }

    public void readChat(final String chatId)
    {
        Observer<Chat> chatSuccessObserver = new Observer<Chat>() {
            @Override
            public void onChanged(Chat chat) {
                moveToChat(chatId);
            }
        };

        mViewModel.readChat(chatId);

        Toast.makeText(getContext(), chatId, Toast.LENGTH_SHORT).show();
    }

    public void moveToChat(String chatId)
    {

    }


}
