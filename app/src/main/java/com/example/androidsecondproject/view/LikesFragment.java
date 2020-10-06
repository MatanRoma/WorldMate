package com.example.androidsecondproject.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidsecondproject.R;
import com.example.androidsecondproject.model.Profile;
import com.example.androidsecondproject.model.eViewModels;
import com.example.androidsecondproject.viewmodel.LikesViewModel;
import com.example.androidsecondproject.viewmodel.ViewModelFactory;
import com.github.ybq.android.spinkit.SpinKitView;

public class LikesFragment extends Fragment {
    private ViewModel mViewModel;

    private SpinKitView mLoadingAnimation;

    public static LikesFragment newInstance(Profile myProfile)
    {
        Bundle bundle=new Bundle();
        bundle.putSerializable("my_profile",myProfile);
        LikesFragment likesFragment=new LikesFragment();
        likesFragment.setArguments(bundle);
        return likesFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.likes_fragment,container,false);
        mViewModel =new ViewModelProvider(this,new ViewModelFactory(getActivity().getApplication(), eViewModels.Likes)).get(LikesViewModel.class);


        mLoadingAnimation=rootView.findViewById(R.id.spin_kit);
        mLoadingAnimation.setVisibility(View.VISIBLE);

        final RecyclerView likesRecycler = rootView.findViewById(R.id.likes_recycler);
        likesRecycler.setHasFixedSize(true);
        likesRecycler.setLayoutManager(new GridLayoutManager(getContext(),2));


        //mViewModel.readLikes();




        return rootView;
    }
}
