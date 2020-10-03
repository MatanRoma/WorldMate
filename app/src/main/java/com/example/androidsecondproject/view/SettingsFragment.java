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
        minAgeSk = getPreferenceScreen().findPreference("min_age_pref");
        maxAgeSk = getPreferenceScreen().findPreference("max_age_pref");
    }


    //SharedPreference sp = PreferenceManager.getDefaultSharedPreferences(requireContext())

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        switch (key){

            case "discovery_cb_pref":
                discoveryCb.setChecked(sharedPreferences.getBoolean("discovery_cb_pref",true));
               // mViewModel.updateDiscovery();
             //   mViewModel.readSwipeProfiles()
                break;
            case "looking_for_pref":
                Set<String> gender = sharedPreferences.getStringSet("looking_for_pref",null);
                lookingForMsl.setValues(gender);
                break;
            case "max_distance_pref":
                maxDistanceSk.setValue(Integer.parseInt(String.valueOf(sharedPreferences.getInt("max_distance_pref",0))));
                break;
            case "min_age_pref1":
                minAgeSk.setValue(Integer.parseInt(String.valueOf(sharedPreferences.getInt("min_age_pref1",18))));
                break;
            case "max_age_pref1":
                maxAgeSk.setValue(Integer.parseInt(String.valueOf(sharedPreferences.getInt("max_age_pref1",18))));
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
}