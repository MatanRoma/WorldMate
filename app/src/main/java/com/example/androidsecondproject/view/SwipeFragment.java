package com.example.androidsecondproject.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

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
import com.example.androidsecondproject.model.eViewModels;
import com.example.androidsecondproject.viewmodel.SwipeViewModel;
import com.example.androidsecondproject.viewmodel.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class SwipeFragment extends Fragment {

    private SwipeAdapter mSwipeAdapter;
    private SwipeViewModel mViewModel;
    RecyclerView mRecyclerView;

    public SwipeViewModel getmViewModel() {
        return mViewModel;
    }

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

        mRecyclerView=rootView.findViewById(R.id.swipe_recycle_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));

        mViewModel=new ViewModelProvider(this,new ViewModelFactory(getActivity().getApplication(), eViewModels.Swipe)).get(SwipeViewModel.class);

        Observer<List<Profile>> profileSuccessObserver =new Observer<List<Profile>>() {
            @Override
            public void onChanged(List<Profile> profiles) {
                List<String> categories = new ArrayList<>();
                categories.add("sport");
                categories.add("food");
                mSwipeAdapter=new SwipeAdapter(profiles,getContext(),mViewModel.getProfile(),categories);
                mRecyclerView.setAdapter(mSwipeAdapter);
            }
        };

        mViewModel.getProfilesResultSuccess().observe(this, profileSuccessObserver);
        mViewModel.setUserProfile((Profile)getArguments().getSerializable("profile"));
        setTouchHelper();
        mViewModel.readProfiles();

        final CheckBox sportCb = rootView.findViewById(R.id.sport_cb);
        final CheckBox foodCb = rootView.findViewById(R.id.food_cb);

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



        return rootView;
    }

    private void setTouchHelper() {
        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.DOWN,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final int position=viewHolder.getAdapterPosition();
/*                List<String> tstList = new ArrayList<>();
                tstList.add("sport");
                CompabilityCalculator compabilityCalculator = new CompabilityCalculator(tstList,mViewModel.getProfile().getQuestionResponds(),mSwipeAdapter.getmProfiles().get(position).getQuestionResponds());
                Toast.makeText(getContext(), compabilityCalculator.getCompability()+"", Toast.LENGTH_SHORT).show();*/
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
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
        };

        private void profileLiked(final int position){
            mViewModel.addLikedProfile(position);


            if(mViewModel.checkIfMatch(position)){
                mViewModel.updateMatch(position);
                mViewModel.writeOtherProfile(position);
            }
            mViewModel.test();
            mViewModel.removeProfile(position);
            mSwipeAdapter.notifyItemRemoved(position);
            mViewModel.writeMyProfile();




        }
        private  void profileDisliked(final int position){


        }


}

