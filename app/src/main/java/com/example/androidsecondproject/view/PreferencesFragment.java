package com.example.androidsecondproject.view;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;
import com.example.androidsecondproject.R;
import com.example.androidsecondproject.model.Preferences;
import com.example.androidsecondproject.model.Profile;
import com.example.androidsecondproject.model.eViewModels;
import com.example.androidsecondproject.viewmodel.PreferencesViewModel;
import com.example.androidsecondproject.viewmodel.ViewModelFactory;

public class PreferencesFragment extends androidx.fragment.app.DialogFragment {

    private int minAge;
    private int maxAge;
    private PreferencesViewModel mViewModel;


    private PreferencesFragmentInterface mListener;

    interface PreferencesFragmentInterface{
        void OnClickContinueToApp();
    }

    public  static PreferencesFragment newInstance()
    {

        PreferencesFragment preferencesFragment = new PreferencesFragment();
        return  preferencesFragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener =(PreferencesFragmentInterface) getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.preference_fragment,container,false);

        final CrystalSeekbar distanceSb = rootView.findViewById(R.id.distance_seekbar);
        final TextView distanceResTv = rootView.findViewById(R.id.distance_res_tv);
        final CheckBox menCb=rootView.findViewById(R.id.men_cb);
        final CheckBox womenCb=rootView.findViewById(R.id.women_cb);
        final CheckBox discoveryCb=rootView.findViewById(R.id.discovery_cb);
        mViewModel=new ViewModelProvider(this,new ViewModelFactory(getActivity().getApplication(), eViewModels.Preferences)).get(PreferencesViewModel.class);

        Observer<Profile> profileObserver=new Observer<Profile>() {
            @Override
            public void onChanged(Profile profile) {
                Preferences preferences=new Preferences();
                preferences.setLookingForMen(menCb.isChecked());
                preferences.setLookingForWomen(womenCb.isChecked());
                preferences.setMaxAge(maxAge);
                preferences.setMinAge(minAge);
                preferences.setMaxDistance(distanceSb.getSelectedMinValue().intValue());
                profile.setPreferences(preferences);
                profile.setDiscovery(discoveryCb.isChecked());
                Log.d("prof","pref");
                mViewModel.writeProfile(profile);
                mListener.OnClickContinueToApp();


            }
        };
        mViewModel.getProfileResultSuccess().observe(this, profileObserver);

        distanceSb.setMinStartValue(20);
        distanceSb.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                distanceResTv.setText(value + " Km");
            }
        });

        final CrystalRangeSeekbar ageSb = rootView.findViewById(R.id.age_range_seekbar);
        ageSb.setMinStartValue(17);
        final TextView ageResTv = rootView.findViewById(R.id.age_res_tv);
        ageSb.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                minAge = minValue.intValue();
                maxAge = maxValue.intValue();
                ageResTv.setText(minValue + " - " + maxValue);
            }
        });

        Button continueBtn = rootView.findViewById(R.id.continue_btn);
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(menCb.isChecked()||womenCb.isChecked()){
                    mViewModel.readProfile();
                }

            }
        });

        setCancelable(false);
        return rootView;
    }


}