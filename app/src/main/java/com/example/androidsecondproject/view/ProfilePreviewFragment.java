package com.example.androidsecondproject.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.androidsecondproject.R;
import com.example.androidsecondproject.model.Profile;
import com.example.androidsecondproject.model.ProfilePicturesPagerAdapter;
import com.example.androidsecondproject.model.TranslateString;
import com.example.androidsecondproject.model.eViewModels;
import com.example.androidsecondproject.viewmodel.ProfilePreviewViewModel;
import com.example.androidsecondproject.viewmodel.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class ProfilePreviewFragment extends Fragment {
    ProfilePreviewViewModel mViewModel;
    ProfilePicturesPagerAdapter mViewPagerAdapter;
    ViewPager mViewPager;
    private List<ImageView> mAllCircles;
    private LinearLayout mCirclesLayout;

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
        ageTv.setText((int)mViewModel.getOtherProfile().calculateCurrentAge()+"");
        if(TranslateString.checkMale(mViewModel.getOtherProfile().getGender()))
        {
            genderTv.setText(getString(R.string.men));
        }
        else {
            genderTv.setText(getString(R.string.women));
        }
        //genderTv.setText(mViewModel.getOtherProfile().getGender()+"");
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
         //{"android.resource://com.example.androidsecondproject/" + R.drawable.bob_dylan1,"android.resource://com.example.androidsecondproject/" + R.drawable.bob_dylan2,"android.resource://com.example.androidsecondproject/" + R.drawable.bob_dylan3};
        initializePager();

        mViewPager = rootView.findViewById(R.id.pictures_pager);

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

        mCirclesLayout = rootView.findViewById(R.id.pictures_circles_layout);

        mAllCircles = new ArrayList<>();
        ImageView circle1 = rootView.findViewById(R.id.cicle_1);
        ImageView circle2 = rootView.findViewById(R.id.cicle_2);
        ImageView circle3 = rootView.findViewById(R.id.cicle_3);
        ImageView circle4 = rootView.findViewById(R.id.cicle_4);
        ImageView circle5 = rootView.findViewById(R.id.cicle_5);
        ImageView circle6 = rootView.findViewById(R.id.cicle_6);
        ImageView circle7 = rootView.findViewById(R.id.cicle_7);
        mAllCircles.add(circle1);
        mAllCircles.add(circle2);
        mAllCircles.add(circle3);
        mAllCircles.add(circle4);
        mAllCircles.add(circle5);
        mAllCircles.add(circle6);
        mAllCircles.add(circle7);

        loadCircles();

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for(int i = 0; i < mViewPagerAdapter.getCount(); i++)
                {
                    mViewModel.getmCirclesIv().get(i).setSelected(false);
                }
                mViewModel.getmCirclesIv().get(position).setSelected(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return rootView;
    }

    private void moveLeftTab(int position)
    {
        if(position != 0)
        {
            Toast.makeText(getContext(), position+"", Toast.LENGTH_SHORT).show();
            mViewPager.setCurrentItem(position-1,true);
/*            mViewModel.getmCirclesIv().get(position).setSelected(false);
            mViewModel.getmCirclesIv().get(position-1).setSelected(true);*/
        }
    }

    private void moveToRightTab(int position)
    {
        if(position != mViewPagerAdapter.getCount()-1)
        {
            Toast.makeText(getContext(), position+"", Toast.LENGTH_SHORT).show();
            mViewPager.setCurrentItem(position+1,true);
/*            mViewModel.getmCirclesIv().get(position).setSelected(false);
            mViewModel.getmCirclesIv().get(position+1).setSelected(true);*/
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

    private void initializePager()
    {
        List<String> pictures = mViewModel.getOtherProfile().getPictures();
        int i;
        String[] images;
        if(!mViewModel.getOtherProfile().getProfilePictureUri().equals(""))
        {
            images = new String[pictures.size()+1];
            images[0] = mViewModel.getOtherProfile().getProfilePictureUri();
            i =1;
        }
        else
        {
            if(pictures.size() == 0)
            {
                images = new String[]{"android.resource://com.example.androidsecondproject/" + R.drawable.man_profile};
                if(TranslateString.checkFemale(mViewModel.getOtherProfile().getGender()))
                {
                    images[0] = "android.resource://com.example.androidsecondproject/" + R.drawable.woman_profile;
                }
                
            }
            else
            {
                images = new String[pictures.size()];

            }

            i =0;
        }
        for(String picture : pictures)
        {
            images[i] = picture;
            i++;
        }
        mViewPagerAdapter = new ProfilePicturesPagerAdapter(getContext(),images);
    }

    public void loadCircles()
    {
        int size = mViewPagerAdapter.getCount();
        for(int i = 0; i < size; i++)
        {
            mViewModel.getmCirclesIv().add(mAllCircles.get(i));
            mAllCircles.get(i).setVisibility(View.VISIBLE);
            mAllCircles.get(i).setSelected(false);

        }
        mAllCircles.get(0).setSelected(true);
        if(size==1)
        {
            mCirclesLayout.setVisibility(View.GONE);
        }


    }

}
