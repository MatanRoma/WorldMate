package com.example.androidsecondproject.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.CheckBoxPreference;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SeekBarPreference;
import androidx.preference.SwitchPreference;

import com.example.androidsecondproject.R;
import com.example.androidsecondproject.model.Profile;
import com.example.androidsecondproject.model.eViewModels;
import com.example.androidsecondproject.viewmodel.SettingsViewModel;
import com.example.androidsecondproject.viewmodel.SwipeViewModel;
import com.example.androidsecondproject.viewmodel.ViewModelFactory;

import java.util.Set;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private SettingsViewModel mViewModel;
    CheckBoxPreference discoveryCb;
    MultiSelectListPreference lookingForMsl;
    SeekBarPreference maxDistanceSk;
    SeekBarPreference minAgeSk;
    SeekBarPreference maxAgeSk;
    SwitchPreference matchSw;
    SwitchPreference messageSw;
    SwitchPreference vibrateSw;

    public static SettingsFragment newInstance(Profile profile)
    {
        Bundle bundle=new Bundle();
        bundle.putSerializable("profile",profile);
        SettingsFragment settingsFragment=new SettingsFragment();
        settingsFragment.setArguments(bundle);
        return settingsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mViewModel=new ViewModelProvider(this,new ViewModelFactory(getActivity().getApplication(), eViewModels.Settings)).get(SettingsViewModel.class);
        mViewModel.setmProfile((Profile)getArguments().getSerializable("profile"));

    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.removeItem(R.id.filter_id);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=super.onCreateView(inflater, container, savedInstanceState);
        assert rootView != null;
        rootView.setBackgroundColor(Color.WHITE);

        discoveryCb.setChecked(mViewModel.getmProfile().isDiscovery());
        lookingForMsl.setValues(mViewModel.getLookingFor());
        maxDistanceSk.setValue(mViewModel.getmProfile().getPreferences().getMaxDistance());
        minAgeSk.setValue(mViewModel.getmProfile().getPreferences().getMinAge());
        maxAgeSk.setValue(mViewModel.getmProfile().getPreferences().getMaxAge());




        return rootView;

    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preference_settings,rootKey);
        discoveryCb = getPreferenceScreen().findPreference("discovery_cb_pref");
        lookingForMsl = getPreferenceScreen().findPreference("looking_for_pref");
        maxDistanceSk = getPreferenceScreen().findPreference("max_distance_pref");
        matchSw = getPreferenceScreen().findPreference("match_pref");
        messageSw = getPreferenceScreen().findPreference("message_pref");
        vibrateSw = getPreferenceScreen().findPreference("vibrate_pref");
        minAgeSk = getPreferenceScreen().findPreference("min_age_pref1");
        maxAgeSk = getPreferenceScreen().findPreference("max_age_pref1");



    }
    @Override
    public void onResume() {
        super.onResume();
// Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
// Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }


    //SharedPreference sp = PreferenceManager.getDefaultSharedPreferences(requireContext())

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        final int MIN_AGE=Integer.parseInt(String.valueOf(sharedPreferences.getInt("min_age_pref1",18)));
        final int MAX_AGE=Integer.parseInt(String.valueOf(sharedPreferences.getInt("max_age_pref1",18)));
        switch (key){
            case "discovery_cb_pref":

                boolean isDiscovery=sharedPreferences.getBoolean("discovery_cb_pref",true);
                discoveryCb.setChecked(isDiscovery);
                mViewModel.updateDiscovery(isDiscovery);
                break;
            case "looking_for_pref":

                final String MALE="male",FEMALE="female";
                Set<String> gender = sharedPreferences.getStringSet("looking_for_pref",null);

                if (gender.isEmpty()){
                    gender.add(MALE);
                }
                mViewModel.updateLookingFor(gender);
                lookingForMsl.setValues(gender);
                break;
            case "max_distance_pref":
                final int MAX_DISTANCE=Integer.parseInt(String.valueOf(sharedPreferences.getInt("max_distance_pref",0)));
                mViewModel.updateMaxDistance(MAX_DISTANCE);
                maxDistanceSk.setValue(MAX_DISTANCE);
                break;
            case "min_age_pref1":
                calculateAge(MIN_AGE,MAX_AGE);
                break;
            case "max_age_pref1":
                calculateAge(MIN_AGE,MAX_AGE);
                break;
            case "match_pref":
                matchSw.setChecked(sharedPreferences.getBoolean("match_pref",true));
                break;
            case "message_pref":
                messageSw.setChecked(sharedPreferences.getBoolean("message_pref",true));
                break;
            case "vibrate_pref":
                vibrateSw.setChecked(sharedPreferences.getBoolean("vibrate_pref",false));
                break;
        }


    }
    public void calculateAge(int min,int max){
        if(min>max){
            maxAgeSk.setValue(min);
            minAgeSk.setValue(max);
            mViewModel.updateMinMaxAge(max,min);
        }
        else{
            maxAgeSk.setValue(max);
            minAgeSk.setValue(min);
            mViewModel.updateMinMaxAge(min,max);
        }


    }
}