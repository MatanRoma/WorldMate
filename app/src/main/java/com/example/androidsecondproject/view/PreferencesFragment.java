package com.example.androidsecondproject.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;
import com.example.androidsecondproject.R;

import java.util.ArrayList;

public class PreferencesFragment extends androidx.fragment.app.DialogFragment {
    String firstName;
    String lastName;
    String gender;
    ArrayList<Integer> date;

    int minAge;
    int maxAge;

    Bundle bundle;


    private PreferencesFragmentInterface mListener;

    interface PreferencesFragmentInterface{
        void OnClickContinueToPhoto(Bundle bundle);
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

        bundle = getArguments();

        firstName = bundle.getString("first_name");
        lastName = bundle.getString("last_name");
        gender = bundle.getString("gender");
        date = bundle.getIntegerArrayList("date");



        final CrystalSeekbar distanceSb = rootView.findViewById(R.id.distance_seekbar);
        final TextView distanceResTv = rootView.findViewById(R.id.distance_res_tv);

        distanceSb.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                distanceResTv.setText(value + " Km");
            }
        });

        final CrystalRangeSeekbar ageSb = rootView.findViewById(R.id.age_range_seekbar);
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
                CheckBox discovery = rootView.findViewById(R.id.discovery_cb);
                bundle.putBoolean("discovery",discovery.isChecked());
                boolean[] lookingFor = new boolean[2];
                CheckBox manCb = rootView.findViewById(R.id.men_cb);
                CheckBox womanCb = rootView.findViewById(R.id.women_cb);
                lookingFor[0] = manCb.isChecked();
                lookingFor[1] =womanCb.isChecked();
                bundle.putBooleanArray("looking_for",lookingFor);
                bundle.putInt("distance",(int)distanceSb.getMinValue());
                bundle.putInt("min_age", minAge);
                bundle.putInt("max_age", maxAge);

                mListener.OnClickContinueToPhoto(bundle);
            }
        });

        setCancelable(false);
        return rootView;
    }


}