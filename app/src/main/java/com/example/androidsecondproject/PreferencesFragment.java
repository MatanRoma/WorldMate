package com.example.androidsecondproject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;

public class PreferencesFragment extends androidx.fragment.app.DialogFragment {
    private PreferencesFragmentInterface mListener;

    interface PreferencesFragmentInterface{
        void OnClickContinue();
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
        View rootView = inflater.inflate(R.layout.preferance_fragment,container,false);

        CrystalSeekbar distanceSb = rootView.findViewById(R.id.distance_seekbar);
        final TextView distanceResTv = rootView.findViewById(R.id.distance_res_tv);

        distanceSb.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                distanceResTv.setText(value + " Km");
            }
        });

        CrystalRangeSeekbar ageSb = rootView.findViewById(R.id.age_range_seekbar);
        final TextView ageResTv = rootView.findViewById(R.id.age_res_tv);
        ageSb.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                ageResTv.setText(minValue + " - " + maxValue);
            }
        });

        setCancelable(false);
        return rootView;
    }
}
