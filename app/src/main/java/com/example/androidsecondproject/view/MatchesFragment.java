package com.example.androidsecondproject.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidsecondproject.R;
import com.example.androidsecondproject.model.Chat;
import com.example.androidsecondproject.model.MatchesAdapter;
import com.example.androidsecondproject.model.Profile;
import com.example.androidsecondproject.model.eViewModels;
import com.example.androidsecondproject.viewmodel.MatchesViewModel;
import com.example.androidsecondproject.viewmodel.ViewModelFactory;
import com.github.ybq.android.spinkit.SpinKitView;

import java.util.ArrayList;
import java.util.List;

public class MatchesFragment extends Fragment  {
    private MatchesViewModel mViewModel;
    private MatchesAdapter mMatchesAdapter;
    private OnMoveToChat moveToChatListener;
    private SpinKitView mLoadingAnimation;
    private LinearLayout mNoMatchesLayout;
    private SearchView mSearchView;


    public interface OnMoveToChat{
        public void OnClickMoveToChat(Profile myProfile,Profile otherProfile,String chatid);
    }

    public static MatchesFragment newInstance(Profile myProfile)
    {
        Bundle bundle=new Bundle();
        bundle.putSerializable("my_profile",myProfile);
        MatchesFragment matchesFragment=new MatchesFragment();
        matchesFragment.setArguments(bundle);
        return matchesFragment;
    }
  /*  public static MatchesFragment newInstance()
    {
        return new MatchesFragment();
    }*/

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
        mSearchView = rootView.findViewById(R.id.search_view);
        mNoMatchesLayout = rootView.findViewById(R.id.no_matches_layout);


        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

        mViewModel =new ViewModelProvider(this,new ViewModelFactory(getActivity().getApplication(), eViewModels.Matches)).get(MatchesViewModel.class);
        mViewModel.setProfile((Profile)getArguments().getSerializable("my_profile"));
        //  mViewModel.setProfile((Profile) getArguments().getSerializable("profile"));


        final RecyclerView matchesRecycler = rootView.findViewById(R.id.matches_recycler);
        matchesRecycler.setHasFixedSize(true);
        matchesRecycler.setLayoutManager(new GridLayoutManager(getContext(),1));
        setTouchHelper(matchesRecycler);

    /*    Observer<Profile> myProfileSuccessObserver=new Observer<Profile>() {
            @Override
            public void onChanged(Profile profile) {
                mViewModel.readMatches();
            }
        };*/

        Observer<List<Profile>> profileSuccessObserver =new Observer<List<Profile>>() {
            @Override
            public void onChanged(List<Profile> profiles) {
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
                    mSearchView.setVisibility(View.VISIBLE);
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
        // mViewModel.getMyProfileResultSuccess().observe(this,myProfileSuccessObserver);
        mViewModel.getProfilesResultSuccess().observe(this, profileSuccessObserver);
        mViewModel.readMatches();



        return rootView;
    }

    private void setTouchHelper(RecyclerView matchesRecycler){
        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.DOWN,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final int position=viewHolder.getAdapterPosition();
                final Chat currChat=mViewModel.getChats().get(position);
                final Profile matcherProfile=mMatchesAdapter.getMatchProfile(position);
                if(direction==ItemTouchHelper.RIGHT||direction==ItemTouchHelper.LEFT)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Are you sure you want to unmatch with "+matcherProfile.getFirstName()+"?" )
                            .setCancelable(false).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mMatchesAdapter.notifyDataSetChanged();
                        }
                    })
                            .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    mViewModel.updateMatches(matcherProfile,currChat.getId());
                                    mViewModel.updateLikes(matcherProfile);
                                    //    mMatchesAdapter.removeMatcherFrom(int chatPosition);
                                    mViewModel.removeChat(currChat.getId());
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();


                }

            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(matchesRecycler);
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
            mNoMatchesLayout.setVisibility(View.VISIBLE);
        }
        else
        {
            mNoMatchesLayout.setVisibility(View.GONE);
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
