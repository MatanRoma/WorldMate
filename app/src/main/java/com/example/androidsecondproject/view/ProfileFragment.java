package com.example.androidsecondproject.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.androidsecondproject.R;

import java.util.ArrayList;

public class ProfileFragment extends androidx.fragment.app.DialogFragment {

    public  static ProfileFragment newInstance()
    {
        ProfileFragment profileFragment = new ProfileFragment();
        return profileFragment;
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
        final View rootView = inflater.inflate(R.layout.profile_fragment,container,false);

        final Button editDescriptionBtn = rootView.findViewById(R.id.edit_btn);
        final Button saveBtn = rootView.findViewById(R.id.save_btn);
        final TextView descriptionTv = rootView.findViewById(R.id.description_tv);
        final EditText descriptionEt = rootView.findViewById(R.id.description_et);
        final ImageButton editNameBtn = rootView.findViewById(R.id.name_edit_iv);
        final TextView nameTv = rootView.findViewById(R.id.username_tv);
        final ImageButton confirmNameBtn = rootView.findViewById(R.id.name_confirm_iv);
        final EditText nameEt = rootView.findViewById(R.id.username_et);
        
        editNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameTv.setVisibility(View.GONE);
                nameEt.setVisibility(View.VISIBLE);
                nameEt.setText(nameTv.getText().toString());
                editNameBtn.setVisibility(View.GONE);
                confirmNameBtn.setVisibility(View.VISIBLE);
            }
        });

        confirmNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameTv.setVisibility(View.VISIBLE);
                nameEt.setVisibility(View.GONE);
                nameTv.setText(nameEt.getText().toString());
                editNameBtn.setVisibility(View.VISIBLE);
                confirmNameBtn.setVisibility(View.GONE);
            }
        });

        ArrayList<String> profileItems = new ArrayList<String >();
        profileItems.add("Choose a subject");
        profileItems.add("About myself");
        profileItems.add("Looking for");
        profileItems.add("My hobbies");

        final Spinner itemSpinner = rootView.findViewById(R.id.profile_item_spinner);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,profileItems);

        itemSpinner.setAdapter(arrayAdapter);


        itemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                Toast.makeText(getContext(), itemSpinner.getItemAtPosition(position)+"", Toast.LENGTH_SHORT).show();
                if(itemSpinner.getItemAtPosition(position) != "Choose a subject")
                {
                    final LinearLayout itemsLayout = rootView.findViewById(R.id.items_layout);
                    final View child = getLayoutInflater().inflate(R.layout.profile_item_layout, itemsLayout, false);

                    final Button editDescriptionBtn = child.findViewById(R.id.edit_btn);
                    final Button saveBtn = child.findViewById(R.id.save_btn);
                    final TextView descriptionTv = child.findViewById(R.id.description_tv);
                    final EditText descriptionEt = child.findViewById(R.id.description_et);
                    final TextView itemTitleTv = child.findViewById(R.id.item_title_tv);
                    final ImageButton closeBtnIv = child.findViewById(R.id.close_btn_iv);
                    itemTitleTv.setText(itemSpinner.getItemAtPosition(position).toString());

                    editDescriptionBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            descriptionTv.setVisibility(View.GONE);
                            descriptionEt.setVisibility(View.VISIBLE);
                            descriptionEt.setText(descriptionTv.getText().toString());
                            editDescriptionBtn.setVisibility(View.GONE);
                            saveBtn.setVisibility(View.VISIBLE);

                        }
                    });

                    saveBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            descriptionTv.setVisibility(View.VISIBLE);
                            descriptionEt.setVisibility(View.GONE);
                            descriptionTv.setText(descriptionEt.getText().toString());
                            editDescriptionBtn.setVisibility(View.VISIBLE);
                            saveBtn.setVisibility(View.GONE);

                        }
                    });

                    closeBtnIv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String title = itemTitleTv.getText().toString();
                            itemsLayout.removeView(child);
                            arrayAdapter.add(title);
                            arrayAdapter.notifyDataSetChanged();
                        }
                    });


                    itemsLayout.addView(child);
                    arrayAdapter.remove((String)itemSpinner.getSelectedItem());
                    arrayAdapter.notifyDataSetChanged();
                    itemSpinner.setSelection(0);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        //itemSpinner.setPopupBackgroundDrawable(getResources().getDrawable(R.drawable.spinner_background));



        return rootView;
    }
}