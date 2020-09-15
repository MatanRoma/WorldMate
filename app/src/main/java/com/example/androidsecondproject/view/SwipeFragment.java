package com.example.androidsecondproject.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidsecondproject.R;
import com.example.androidsecondproject.model.Profile;
import com.example.androidsecondproject.model.SwipeAdapter;
import com.example.androidsecondproject.model.eViewModels;
import com.example.androidsecondproject.repository.Repository;
import com.example.androidsecondproject.viewmodel.RegisterViewModel;
import com.example.androidsecondproject.viewmodel.SwipeViewModel;
import com.example.androidsecondproject.viewmodel.ViewModelFactory;

import java.util.List;

public class SwipeFragment extends Fragment {

    private SwipeAdapter mSwipeAdapter;
    private SwipeViewModel mViewModel;
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

        final RecyclerView recyclerView=rootView.findViewById(R.id.swipe_recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));

        mViewModel=new ViewModelProvider(this,new ViewModelFactory(getActivity().getApplication(), eViewModels.Swipe)).get(SwipeViewModel.class);

        Observer<List<Profile>> profileSuccessObserver =new Observer<List<Profile>>() {
            @Override
            public void onChanged(List<Profile> profiles) {
                mSwipeAdapter=new SwipeAdapter(profiles,getContext());
                recyclerView.setAdapter(mSwipeAdapter);
            }
        };

        mViewModel.getProfilesResultSuccess().observe(this, profileSuccessObserver);
        mViewModel.setUserProfile((Profile)getArguments().getSerializable("profile"));
        mViewModel.readProfiles();




        return rootView;
    }
}
