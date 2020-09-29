package com.example.androidsecondproject.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidsecondproject.R;
import com.example.androidsecondproject.model.Profile;
import com.example.androidsecondproject.model.SwipeAdapter;
import com.example.androidsecondproject.model.SwipeFlingAdapter;
import com.example.androidsecondproject.model.eViewModels;
import com.example.androidsecondproject.viewmodel.SwipeViewModel;
import com.example.androidsecondproject.viewmodel.ViewModelFactory;
import com.github.ybq.android.spinkit.SpinKitView;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

import swipeable.com.layoutmanager.OnItemSwiped;
import swipeable.com.layoutmanager.SwipeableLayoutManager;
import swipeable.com.layoutmanager.SwipeableTouchHelperCallback;

public class SwipeFragment extends Fragment {

    private SwipeAdapter mSwipeAdapter;
    private SwipeViewModel mViewModel;
    private RecyclerView mRecyclerView;
    private SpinKitView mLoadingAnimation;
    SwipeFlingAdapterView swipeProfile;

    private SwipeFlingAdapter mSwipeFlingAdapter;

    ImageView mMatchAnimation;



    public static SwipeFragment newInstance(Profile profile)
    {
        Bundle bundle=new Bundle();
        bundle.putSerializable("profile",profile);
        SwipeFragment swipeFragment  = new SwipeFragment();
        swipeFragment.setArguments(bundle);
        return swipeFragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View rootView = inflater.inflate(R.layout.swipe_fragment,container,false);

        mLoadingAnimation=rootView.findViewById(R.id.spin_kit);
        mLoadingAnimation.setVisibility(View.VISIBLE);
        mMatchAnimation = rootView.findViewById(R.id.match_anim);

      //  swipeProfile=rootView.findViewById(R.id.swipe_fling);
        mRecyclerView=rootView.findViewById(R.id.swipe_recycle_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));

        mViewModel=new ViewModelProvider(this,new ViewModelFactory(getActivity().getApplication(), eViewModels.Swipe)).get(SwipeViewModel.class);
        mViewModel.setContext(getContext());
        final List<String> categories = new ArrayList<>();
        categories.add("sport");
        categories.add("food");
        categories.add("culture");
        Observer<List<Profile>> profileSuccessObserver =new Observer<List<Profile>>() {
            @Override
            public void onChanged(List<Profile> profiles) {
                Log.d("testtt","testt1");
                if (mSwipeAdapter == null) {
                    Log.d("testtt","testt2");
                    mSwipeAdapter=new SwipeAdapter(profiles,getContext(),mViewModel.getProfile(),categories);
                 //   mSwipeFlingAdapter=new SwipeFlingAdapter(profiles,mViewModel.getProfile(),getContext());
                      mRecyclerView.setAdapter(mSwipeAdapter);


                /*    swipeProfile.setAdapter(mSwipeFlingAdapter);
                    swipeProfile.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {

            }

            @Override
            public void onLeftCardExit(Object o) {

            }

            @Override
            public void onRightCardExit(Object o) {

            }

            @Override
            public void onAdapterAboutToEmpty(int i) {

            }

            @Override
            public void onScroll(float v) {

            }
        });*/


/*
        swipeProfile.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int i, Object o) {

            }
        });*/

                    mLoadingAnimation.setVisibility(View.GONE);
                }
                else {
                    mSwipeAdapter.setmProfiles(profiles);
                    mSwipeAdapter.notifyDataSetChanged();
                }

            }
        };

        mViewModel.getProfilesResultSuccess().observe(this, profileSuccessObserver);
        mViewModel.setUserProfile((Profile)getArguments().getSerializable("profile"));
        setTouchHelper();
        Log.d("testtt","testt0");
        mViewModel.readProfiles();






        final CheckBox sportCb = rootView.findViewById(R.id.sport_cb);
        final CheckBox foodCb = rootView.findViewById(R.id.food_cb);
        final CheckBox cultureCb = rootView.findViewById(R.id.culture_cb);

        sportCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String title = sportCb.getText().toString();
                mSwipeAdapter.updateFilter(title,isChecked);
                mSwipeAdapter.notifyDataSetChanged();
            }
        });

        foodCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String title = foodCb.getText().toString();
                mSwipeAdapter.updateFilter(title,isChecked);
                mSwipeAdapter.notifyDataSetChanged();
            }
        });

        cultureCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String title = cultureCb.getText().toString();
                mSwipeAdapter.updateFilter(title,isChecked);
                mSwipeAdapter.notifyDataSetChanged();
            }
        });



        return rootView;
    }

    private void setTouchHelper() {
        /*ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.DOWN,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final int position=viewHolder.getAdapterPosition();
*//*                List<String> tstList = new ArrayList<>();
                tstList.add("sport");
                CompabilityCalculator compabilityCalculator = new CompabilityCalculator(tstList,mViewModel.getProfile().getQuestionResponds(),mSwipeAdapter.getmProfiles().get(position).getQuestionResponds());
                Toast.makeText(getContext(), compabilityCalculator.getCompability()+"", Toast.LENGTH_SHORT).show();*//*
                    if(direction==ItemTouchHelper.RIGHT)
                    {
                        Toast.makeText(getContext(), "gothere", Toast.LENGTH_SHORT).show();
                        profileLiked(position);
                    }
                    else if(direction==ItemTouchHelper.LEFT){
                        profileDisliked(position);
                    }


                }
            };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);*/
        SwipeableTouchHelperCallback swipeableTouchHelperCallback =
                new SwipeableTouchHelperCallback(new OnItemSwiped() {
                    @Override public void onItemSwiped() {
                        mSwipeAdapter.removeTopItem();
                    }

                    @Override public void onItemSwipedLeft() {
                        profileDisliked(0);
                    }

                    @Override public void onItemSwipedRight() {
                        profileLiked(0);
                    }

                    @Override public void onItemSwipedUp() {
                        Log.e("SWIPE", "UP");
                    }

                    @Override public void onItemSwipedDown() {
                        Log.e("SWIPE", "DOWN");
                    }
                }) {
                    @Override
                    public int getAllowedSwipeDirectionsMovementFlags(RecyclerView.ViewHolder viewHolder) {
                        return ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                    }
                };

        final swipeable.com.layoutmanager.touchelper.ItemTouchHelper itemTouchHelper = new swipeable.com.layoutmanager.touchelper.ItemTouchHelper(swipeableTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.setLayoutManager(new SwipeableLayoutManager().setAngle(10)
                .setAnimationDuratuion(450)
                .setMaxShowCount(3)
                .setScaleGap(0.1f)
                .setTransYGap(0));



        };

        private void profileLiked(final int position){
            mViewModel.addLikedProfile(position);


            if(mViewModel.checkIfMatch(position)){
                mMatchAnimation.setVisibility(View.VISIBLE);
                AnimationDrawable animationDrawable = (AnimationDrawable) mMatchAnimation.getDrawable();
                animationDrawable.start();
                MediaPlayer matchSound = MediaPlayer.create(getActivity(),R.raw.match);
                matchSound.start();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mMatchAnimation.setVisibility(View.GONE);
                    }
                },3000);
                mViewModel.updateMatch(position);
                mViewModel.writeOtherProfile(position);
            }
           
        //    mViewModel.removeProfile(position);
          //  mSwipeAdapter.notifyItemRemoved(position);
            mViewModel.writeMyProfile();
        }

        private  void profileDisliked(final int position){
            mViewModel.addDislikedProfile(position);
            mViewModel.writeMyProfile();

        }


}

