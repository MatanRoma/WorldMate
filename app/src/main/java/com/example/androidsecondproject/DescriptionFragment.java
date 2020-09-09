package com.example.androidsecondproject;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DescriptionFragment extends androidx.fragment.app.DialogFragment {

    public  static DescriptionFragment newInstance()
    {
        DescriptionFragment descriptionFragment = new DescriptionFragment();
        return descriptionFragment;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.description_fragment,container,false);

        final Button editBtn = rootView.findViewById(R.id.edit_btn);
        final Button saveBtn = rootView.findViewById(R.id.save_btn);
        final TextView descriptionTv = rootView.findViewById(R.id.description_tv);
        final EditText descriptionEt = rootView.findViewById(R.id.description_et);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                descriptionTv.setVisibility(View.GONE);
                descriptionEt.setVisibility(View.VISIBLE);
                descriptionEt.setText(descriptionTv.getText().toString());
                editBtn.setVisibility(View.GONE);
                saveBtn.setVisibility(View.VISIBLE);

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                descriptionTv.setVisibility(View.VISIBLE);
                descriptionEt.setVisibility(View.GONE);
                descriptionTv.setText(descriptionEt.getText().toString());
                editBtn.setVisibility(View.VISIBLE);
                saveBtn.setVisibility(View.GONE);
            }
        });
        return rootView;
    }
}
