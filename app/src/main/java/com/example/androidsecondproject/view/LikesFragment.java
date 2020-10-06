package com.example.androidsecondproject.view;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidsecondproject.R;
import com.example.androidsecondproject.model.CompabilityCalculator;
import com.example.androidsecondproject.model.LikesAdapter;
import com.example.androidsecondproject.model.Profile;
import com.example.androidsecondproject.model.eViewModels;
import com.example.androidsecondproject.viewmodel.LikesViewModel;
import com.example.androidsecondproject.viewmodel.ViewModelFactory;
import com.github.ybq.android.spinkit.SpinKitView;

import java.util.ArrayList;
import java.util.List;

public class LikesFragment extends Fragment {
    private LikesViewModel mViewModel;

    private SpinKitView mLoadingAnimation;
    private LikesAdapter mLikesAdapter;
    private RecyclerView mLikesRecycler;
    private LikesFragmentInterface mLikesFragmentInteface;
    private SearchView mSearchView;

    public interface LikesFragmentInterface{
        public void OnMoveToProfilePreviewFromLikes(Profile myProfile,int compatability);
    }

    public static LikesFragment newInstance(Profile myProfile)
    {
        Bundle bundle=new Bundle();
        bundle.putSerializable("my_profile",myProfile);
        LikesFragment likesFragment=new LikesFragment();
        likesFragment.setArguments(bundle);
        return likesFragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mLikesFragmentInteface =(LikesFragmentInterface) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.likes_fragment,container,false);
        mViewModel =new ViewModelProvider(this,new ViewModelFactory(getActivity().getApplication(), eViewModels.Likes)).get(LikesViewModel.class);
        mViewModel.setMyProfile((Profile) getArguments().get("my_profile"));

        mSearchView = rootView.findViewById(R.id.search_view);
        mLikesRecycler = rootView.findViewById(R.id.likes_recycler);
        mLikesRecycler.setHasFixedSize(true);
        mLikesRecycler.setLayoutManager(new GridLayoutManager(getContext(),2));

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mLikesAdapter.getFilter().filter(newText);
                return false;
            }
        });

        Observer<List<Profile>> likesObserver=new Observer<List<Profile>>() {
            @Override
            public void onChanged(List<Profile> profiles) {
                Log.d("likes",mLikesAdapter+"");
                if(mLikesAdapter==null) {
                    mLoadingAnimation.setVisibility(View.GONE);
                    mSearchView.setVisibility(View.VISIBLE);
                    mLikesAdapter = new LikesAdapter(profiles, mViewModel.getMyProfile(),getContext());
                    mLikesAdapter.setProfilePressedListener(new LikesAdapter.LikedProfilePressedListener() {
                        @Override
                        public void OnLikedProfiledPressedListener(Profile likedProfile) {
                            List<String> categories=new ArrayList<>();
                            addCategories(categories);
                            int compability = CompabilityCalculator.caculateCompability(categories,likedProfile.getQuestionResponds(),mViewModel.getMyProfile().getQuestionResponds());
                            mLikesFragmentInteface.OnMoveToProfilePreviewFromLikes(likedProfile,compability);
                        }
                    });
                    mLikesRecycler.setAdapter(mLikesAdapter);


                }
            }
        };


        mLoadingAnimation=rootView.findViewById(R.id.spin_kit);
        mLoadingAnimation.setVisibility(View.VISIBLE);




        mViewModel.getProfilesResultSuccess().observe(this,likesObserver);
        mViewModel.readLikes();



        return rootView;
    }

    private void addCategories(List<String> categories) {
        categories.add("sport");
        categories.add("food");
        categories.add("culture");
        categories.add("music");
        categories.add("religion");
        categories.add("travel");
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
