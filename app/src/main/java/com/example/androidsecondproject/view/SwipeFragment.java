package com.example.androidsecondproject.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.androidsecondproject.R;
import com.example.androidsecondproject.model.Profile;
import com.example.androidsecondproject.model.SwipeAdapter;
import com.example.androidsecondproject.model.SwipeFlingAdapter;
import com.example.androidsecondproject.model.eViewModels;
import com.example.androidsecondproject.viewmodel.SwipeViewModel;
import com.example.androidsecondproject.viewmodel.ViewModelFactory;
import com.github.ybq.android.spinkit.SpinKitView;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.Duration;
import com.yuyakaido.android.cardstackview.RewindAnimationSetting;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SwipeFragment extends Fragment {

    private SwipeAdapter mSwipeAdapter;
    private SwipeViewModel mViewModel;
    private RecyclerView mRecyclerView;
    private CardStackView mCardStackView;
    private SpinKitView mLoadingAnimation;
    SwipeFlingAdapterView swipeProfile;
    Boolean isWhat;

    SwipeAnimationSetting mSettingRight, mSettingLeft;
    CardStackLayoutManager mCardStackLayoutManager;

    private SwipeFlingAdapter mSwipeFlingAdapter;

    private ImageView mMatchAnimation;

    private LinearLayout btnsLayout;
    private RelativeLayout mNoPeopleMainLayout, mNoPeopleGuestLayout;
    private ImageView rangeIv;
    private CircleImageView mNoPeopleLogo;
    //ImageView rewindBtn;

    private SwipeInterface mSwipeListener;

    public static Fragment newInstance() {
        // login as guest
        Bundle bundle=new Bundle();
        bundle.putSerializable("is_guest",true);
        SwipeFragment swipeFragment  = new SwipeFragment();
        swipeFragment.setArguments(bundle);
        return swipeFragment;
    }

    public interface SwipeInterface
    {
        void onClickMoveToProfilePreview(Profile otherProfile,int compability);
        void onShowToolbar();
        void onLogoutFromSwipeFragment();
    }



    public static SwipeFragment newInstance(Profile profile)
    {
        Bundle bundle=new Bundle();
        bundle.putSerializable("profile",profile);
        bundle.putSerializable("is_guest",false);
        SwipeFragment swipeFragment  = new SwipeFragment();
        swipeFragment.setArguments(bundle);
        return swipeFragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mSwipeListener = (SwipeInterface)getActivity();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_with_filter,menu);
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

/*        if(item.getItemId() == R.id.filter_id){
            Toast.makeText(getContext(), "Filter", Toast.LENGTH_SHORT).show();
        }*/
        return super.onOptionsItemSelected(item);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.swipe_fragment,container,false);
        mViewModel=new ViewModelProvider(this,new ViewModelFactory(getActivity().getApplication(), eViewModels.Swipe)).get(SwipeViewModel.class);
        mViewModel.setIsLoginAsGuest(getArguments().getBoolean("is_guest"));

        mLoadingAnimation=rootView.findViewById(R.id.spin_kit);
        mLoadingAnimation.setVisibility(View.VISIBLE);
        mMatchAnimation = rootView.findViewById(R.id.match_anim);
        mNoPeopleGuestLayout =rootView.findViewById(R.id.no_people_layout_guest);

        //((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);

        //  swipeProfile=rootView.findViewById(R.id.swipe_fling);
        mRecyclerView=rootView.findViewById(R.id.swipe_recycle_view);
        mCardStackView = rootView.findViewById(R.id.stack_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        //     mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));

        ImageView likeBtn = rootView.findViewById(R.id.like_ib);
        ImageView dislikeBtn = rootView.findViewById(R.id.dislike_ib);
        //rewindBtn = rootView.findViewById(R.id.rewind_ib);
        btnsLayout = rootView.findViewById(R.id.btns_layout);
        btnsLayout.setVisibility(View.GONE);

        mNoPeopleMainLayout = rootView.findViewById(R.id.no_people_layout);
        rangeIv = rootView.findViewById(R.id.range_iv);
        mNoPeopleLogo = rootView.findViewById(R.id.no_people_logo);

        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCardStackLayoutManager.setSwipeAnimationSetting(mSettingRight);
                mCardStackView.swipe();
            }
        });

        dislikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCardStackLayoutManager.setSwipeAnimationSetting(mSettingLeft);
                mCardStackView.swipe();
            }
        });

       /* rewindBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mViewModel.getProfile().getDisLikes().isEmpty())
                {

                    mViewModel.getProfile().getDisLikes().remove(mViewModel.getProfile().getDisLikes().size()-1);
                    mCardStackView.rewind();

                }
            }
        });*/




        mViewModel.setContext(getContext());
        final List<String> categories = new ArrayList<>();
        categories.add("sport");
        categories.add("food");
        categories.add("culture");

        if(!mViewModel.isLoginAsGuest()) {
            Observer<List<Profile>> profileSuccessObserver = new Observer<List<Profile>>() {
                @Override
                public void onChanged(List<Profile> profiles) {
                        if (mSwipeAdapter == null) {
                           initialAdapter(categories);
                        } else {
                            mSwipeAdapter.notifyDataSetChanged();

                        }
                        if(mSwipeAdapter.getItemCount()==0)
                            noMatchesAnimation();
                        else
                            mNoPeopleMainLayout.setVisibility(View.GONE);
                }
            };

            mViewModel.getProfilesResultSuccess().observe(this, profileSuccessObserver);
            mViewModel.setUserProfile((Profile) getArguments().getSerializable("profile"));
            Log.d("call","swipe");
            mViewModel.readProfiles();
        }
        else {
            Observer<List<Profile>> guestProfileSuccessObserver = new Observer<List<Profile>>() {
                @Override
                public void onChanged(List<Profile> profiles) {
                    initialAdapter(categories);
                    if(mSwipeAdapter.getItemCount()==0)
                        noMatchesAnimation();
                    else
                        mNoPeopleGuestLayout.setVisibility(View.GONE);
                }

            };
            Button guestNoPeopleBtn=rootView.findViewById(R.id.no_people_guest_btn);
            guestNoPeopleBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSwipeListener.onLogoutFromSwipeFragment();
                }
            });


            mViewModel.getGuestProfilesResultSuccess().observe(this, guestProfileSuccessObserver);
            mViewModel.readProfilesForGuest();
        }


        setTouchHelper();
        Log.d("testtt","testt0");





        /*final CheckBox sportCb = rootView.findViewById(R.id.sport_cb);
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
*/


        return rootView;
    }
    private void initialAdapter(List<String> categories){
        if(mViewModel.isLoginAsGuest()){
            mSwipeAdapter= new SwipeAdapter(mViewModel.getGuestProfiles(), getContext(), categories);
        }
        else{
            mSwipeAdapter = new SwipeAdapter(mViewModel.getProfiles(), getContext(), mViewModel.getProfile(), categories);
        }
        mSwipeListener.onShowToolbar();

        mSwipeAdapter.setProfiledPressedListener(new SwipeAdapter.ProfilePressedListener() {
            @Override
            public void OnProfiledPressedListener(Profile otherProfile, int compability) {
                moveToProfilePreview(otherProfile, compability);
            }
        });

        //    mRecyclerView.setAdapter(mSwipeAdapter);
        mCardStackView.setAdapter(mSwipeAdapter);
        mLoadingAnimation.setVisibility(View.GONE);
        btnsLayout.setVisibility(View.VISIBLE);
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

 /*       SwipeableTouchHelperCallback swipeableTouchHelperCallback =
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
        SwipeableLayoutManager swipeableLayoutManager= new SwipeableLayoutManager();
        mRecyclerView.setLayoutManager(swipeableLayoutManager.setAngle(10)
                .setAnimationDuratuion(450)
                .setMaxShowCount(100)
                .setScaleGap(0.1f)
                .setTransYGap(0));*/
        mCardStackLayoutManager = new CardStackLayoutManager(getContext(), new CardStackListener() {
            @Override
            public void onCardDragging(Direction direction, float ratio) {
                Log.d("stack_swipe","dragging");
            }

            @Override
            public void onCardSwiped(Direction direction) {
                if(direction == Direction.Right)
                {
                    if(!mViewModel.isLoginAsGuest()) {
                        profileLiked(0);
                    }

                    //rewindBtn.setVisibility(View.GONE);
                    Log.d("stack_swipe","swiped_right");
                }
                else if(direction ==Direction.Left){
                    if(!mViewModel.isLoginAsGuest()) {
                        profileDisliked(0);
                    }
                    //rewindBtn.setVisibility(View.VISIBLE);
                    Log.d("stack_swipe","swiped_left");
                }
                mSwipeAdapter.removeTopItem();
                if(mSwipeAdapter.getItemCount()==0)
                    noMatchesAnimation();
            }

            @Override
            public void onCardRewound() {
                Log.d("stack_swipe","rewind");
            }

            @Override
            public void onCardCanceled() {
                Log.d("stack_swipe","canceled");
            }

            @Override
            public void onCardAppeared(View view, int position) {
                Log.d("stack_swipe","apeared");
            }

            @Override
            public void onCardDisappeared(View view, int position) {
                Log.d("stack_swipe","disappeared");
            }
        });

        mCardStackView.setLayoutManager(mCardStackLayoutManager);
        mSettingRight= new SwipeAnimationSetting.Builder().setDirection(Direction.Right)
                .setDuration(Duration.Slow.duration)
                .build();
        mSettingLeft = new SwipeAnimationSetting.Builder().setDirection(Direction.Left)
                .setDuration(Duration.Slow.duration)
                .build();
        //mCardStackLayoutManager.setSwipeAnimationSetting(mSetting);

        RewindAnimationSetting rewindSetting = new RewindAnimationSetting.Builder()
                .setDirection(Direction.Bottom)
                .setDuration(Duration.Normal.duration)
                .build();
        mCardStackLayoutManager.setRewindAnimationSetting(rewindSetting);

        mCardStackLayoutManager.setMaxDegree(45.0f);
        mCardStackLayoutManager.setDirections(Direction.HORIZONTAL);
        mCardStackLayoutManager.setSwipeThreshold(0.5f);
        mCardStackLayoutManager.setCanScrollHorizontal(true);
        mCardStackLayoutManager.setCanScrollVertical(true);
        mCardStackLayoutManager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual);

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
            //   mViewModel.writeOtherProfile(position);
        }

        //    mViewModel.removeProfile(position);
        //  mSwipeAdapter.notifyItemRemoved(position);
        //   mViewModel.writeMyProfile();
        //   mViewModel.updateProfile();
    }

    private  void profileDisliked(final int position){
        mViewModel.addDislikedProfile(position);
        //  mViewModel.writeMyProfile();

    }


    private void moveToProfilePreview(Profile otherProfile, int compability) {
        mSwipeListener.onClickMoveToProfilePreview(otherProfile,compability);
    }


    public void updateCategories(boolean[] checkeds)
    {
        List<String> categories = mSwipeAdapter.getmCategories();
        String[] filterCategories = {"sport","food","culture","music"};
        categories.clear();
        for(int i = 0; i < checkeds.length;i++)
        {
            if(checkeds[i])
            {
                categories.add(filterCategories[i]);
            }
        }
        mSwipeAdapter.setmCategories(categories);
        mSwipeAdapter.notifyDataSetChanged();
    }


    public void noMatchesAnimation()
    {
        if(!mViewModel.isLoginAsGuest()) {
            mNoPeopleMainLayout.setVisibility(View.VISIBLE);
            Glide.with(getContext()).load(mViewModel.getProfile().getProfilePictureUri()).error(R.mipmap.ic_launcher).into(mNoPeopleLogo);
            Animation rangeAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.range_animation);
            rangeAnimation.setRepeatMode(Animation.REVERSE);
            rangeIv.startAnimation(rangeAnimation);
        }
        else{
            mNoPeopleGuestLayout.setVisibility(View.VISIBLE);
        }
    }


}

