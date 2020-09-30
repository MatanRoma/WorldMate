package com.example.androidsecondproject.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.androidsecondproject.R;
import com.example.androidsecondproject.model.Profile;
import com.example.androidsecondproject.model.ProfilePicturesPagerAdapter;
import com.example.androidsecondproject.model.eViewModels;
import com.example.androidsecondproject.viewmodel.ProfilePreviewViewModel;
import com.example.androidsecondproject.viewmodel.ViewModelFactory;

public class ProfilePreviewFragment extends Fragment {
    ProfilePreviewViewModel mViewModel;
    ProfilePicturesPagerAdapter mViewPagerAdapter;
    ViewPager mViewPager;

    public interface OnMoveToPhotoPreview
    {
        void onClickMoveToPhotoPreviewListener(String uri);
    }
    private OnMoveToPhotoPreview onMoveToPhotoPreview;

    public static ProfilePreviewFragment newInstance(Profile otherProfile,int compability)
    {
        Bundle bundle=new Bundle();
        bundle.putSerializable("other_profile",otherProfile);
        bundle.putInt("compability",compability);
        ProfilePreviewFragment profilePreviewFragment=new ProfilePreviewFragment();
        profilePreviewFragment.setArguments(bundle);
        return profilePreviewFragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.onMoveToPhotoPreview = (OnMoveToPhotoPreview)getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.profile_preview_layout,container,false);

        mViewModel =new ViewModelProvider(this,new ViewModelFactory(getActivity().getApplication(), eViewModels.ProfilePreview)).get(ProfilePreviewViewModel.class);
        mViewModel.setOtherProfile((Profile)getArguments().getSerializable("other_profile"));

        TextView nameTv = rootView.findViewById(R.id.profile_name_tv);
        TextView ageTv = rootView.findViewById(R.id.profile_age_tv);
        TextView genderTv = rootView.findViewById(R.id.profile_gender_tv);
        TextView cityTv = rootView.findViewById(R.id.profile_location_tv);
        TextView aboutMyselfDesTv = rootView.findViewById(R.id.about_myself_des_tv);
        TextView lookingForfDesTv = rootView.findViewById(R.id.looking_for_des_tv);
        TextView myHobbiesDesTv = rootView.findViewById(R.id.my_hobbies_des_tv);
        TextView compabilityTv = rootView.findViewById(R.id.compability_tv);

        nameTv.setText(mViewModel.getOtherProfile().getFirstName()+" "+mViewModel.getOtherProfile().getLastName());
        ageTv.setText((int)mViewModel.getOtherProfile().getAge()+"");
        genderTv.setText(mViewModel.getOtherProfile().getGender()+"");
        cityTv.setText(mViewModel.getOtherProfile().getCity()+"");
        int compability = getArguments().getInt("compability");
        if(compability !=0)
        {
            compabilityTv.setText(compability+"%");
            compabilityTv.setVisibility(View.VISIBLE);
        }

        if(!mViewModel.getOtherProfile().getDescription().equals(""))
        {
            aboutMyselfDesTv.setText(mViewModel.getOtherProfile().getDescription());
            aboutMyselfDesTv.setTextColor(getResources().getColor(R.color.black));
        }
        if(!mViewModel.getOtherProfile().getLookingFor().equals(""))
        {
            lookingForfDesTv.setText(mViewModel.getOtherProfile().getLookingFor());
            lookingForfDesTv.setTextColor(getResources().getColor(R.color.black));
        }
        if(!mViewModel.getOtherProfile().getHobbies().equals(""))
        {
            myHobbiesDesTv.setText(mViewModel.getOtherProfile().getHobbies());
            myHobbiesDesTv.setTextColor(getResources().getColor(R.color.black));
        }
        //test uri array
        String images[] = {"android.resource://com.example.androidsecondproject/" + R.drawable.bob_dylan1,"android.resource://com.example.androidsecondproject/" + R.drawable.bob_dylan2,"android.resource://com.example.androidsecondproject/" + R.drawable.bob_dylan3};

        mViewPager = rootView.findViewById(R.id.pictures_pager);
        mViewPagerAdapter = new ProfilePicturesPagerAdapter(getContext(),images);
        mViewPager.setAdapter(mViewPagerAdapter);

        mViewPagerAdapter.setProfilePagerClick(new ProfilePicturesPagerAdapter.ProfilePagerClickListener() {
            @Override
            public void onRightClickListener(int position) {
                moveToRightTab(position);
            }

            @Override
            public void onLeftClickListener(int position) {
                moveLeftTab(position);
            }

            @Override
            public void onPictureLongClickListener(String uri) {
            onMoveToPhotoPreview.onClickMoveToPhotoPreviewListener(uri);
            }
        });

        return rootView;
    }

    private void moveLeftTab(int position)
    {
        if(position != 0)
        {
            mViewPager.setCurrentItem(position-1,true);
        }
    }

    private void moveToRightTab(int position)
    {
        if(position != mViewPagerAdapter.getCount()-1)
        {
            mViewPager.setCurrentItem(position+1,true);
        }
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
