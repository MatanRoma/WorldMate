package com.example.androidsecondproject.view;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;

import android.widget.RelativeLayout;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
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
import com.github.ybq.android.spinkit.SpinKitView;

import java.util.List;

public class MatchesFragment extends Fragment  {
    private MatchesViewModel mViewModel;
    private MatchesAdapter mMatchesAdapter;
    private OnMoveToChat moveToChatListener;
    private SpinKitView mLoadingAnimation;
    private LinearLayout noMatchesLayout;


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
        mLoadingAnimation=rootView.findViewById(R.id.spin_kit);
        mLoadingAnimation.setVisibility(View.VISIBLE);

        noMatchesLayout = rootView.findViewById(R.id.no_matches_layout);

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
                SearchView searchView = rootView.findViewById(R.id.search_view);
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        mMatchesAdapter.getFilter().filter(newText);
                        return false;
                    }
                });
            }
        };

        Observer<List<Profile>> profileSuccessObserver =new Observer<List<Profile>>() {
            @Override
            public void onChanged(List<Profile> profiles) {
            /*mMatchesAdapter = new MatchesAdapter(mViewModel.getMatches(),getContext(),mViewModel.getMyProfile(),mViewModel.getNewMatchUid());
            mMatchesAdapter.setListener(new MatchesAdapter.MatchInterface() {
                @Override
                public void onChatClickedListener(int position) {
                    Profile otherProfile=mViewModel.getMatches().get(position);
                    String chatid=mViewModel.getChatId(otherProfile.getUid());
                    moveToChat(mViewModel.getMyProfile(),otherProfile,chatid);

                }
            });
            matchesRecycler.setAdapter(mMatchesAdapter);
            mLoadingAnimation.setVisibility(View.GONE);

*/
            mViewModel.readChats();
            }
        };
        Observer<List<Chat>> chatDataChangedObserver= new Observer<List<Chat>>() {
            @Override
            public void onChanged(List<Chat> chats) {

                checkIfNoMatches();

                if (mMatchesAdapter == null){
                   boolean isLtr = checkDirection();
                    mMatchesAdapter = new MatchesAdapter(mViewModel.getMatches(), getContext(),mViewModel.getChats(), mViewModel.getNewMatchUid(),isLtr);
                mMatchesAdapter.setListener(new MatchesAdapter.MatchInterface() {
                    @Override
                    public void onChatClickedListener(Profile otherProfile) {
                        String chatid = mViewModel.getChatId(otherProfile.getUid());
                        moveToChat(mViewModel.getMyProfile(), otherProfile, chatid);

                    }
                });

                mMatchesAdapter.sortChats();
                    Log.d("heree","ger1");
                matchesRecycler.setAdapter(mMatchesAdapter);
                mLoadingAnimation.setVisibility(View.GONE);
            }
                else{
                    Log.d("heree","ger2");
                  //  mMatchesAdapter.setChats();
                    mMatchesAdapter.sortChats();
                    mMatchesAdapter.notifyDataSetChanged();

                }


            }
        };



        mViewModel.getChatDataChange().observe(this,chatDataChangedObserver);
        mViewModel.getMyProfileResultSuccess().observe(this,myProfileSuccessObserver);
        mViewModel.getProfilesResultSuccess().observe(this, profileSuccessObserver);
        mViewModel.readMyProfile();
        return rootView;
    }

    private boolean checkDirection() {
        boolean isLtr;
        Configuration config = getResources().getConfiguration();
        if(config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            isLtr = false;
        }
        else {
            isLtr = true;
        }
        return  isLtr;
    }

    void checkIfNoMatches()
    {
        if(mViewModel.getChats().size()==0)
        {
            noMatchesLayout.setVisibility(View.VISIBLE);
        }
        else
        {
            noMatchesLayout.setVisibility(View.GONE);
        }
    }


    public void moveToChat(Profile myProfile,Profile otherProfile,String chatid)
    {
        moveToChatListener.OnClickMoveToChat(myProfile,otherProfile,chatid);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.removeItem(R.id.filter_id);
    }


}
