package com.example.androidsecondproject.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.androidsecondproject.R;
import com.example.androidsecondproject.model.Profile;
import com.example.androidsecondproject.model.eViewModels;
import com.example.androidsecondproject.viewmodel.ProfileViewModel;
import com.example.androidsecondproject.viewmodel.ViewModelFactory;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends androidx.fragment.app.DialogFragment {

    final int GALLERY_PICTURE = 1;
    final int CAMERA_REQUEST = 2;

    Uri imageUri;
    File file;

    private ProfileViewModel mProfileViewModel;

    public interface UpdateDrawerFromProfileFragment
    {
        public void onUpdateProfile(Profile profile);
    }

    private UpdateDrawerFromProfileFragment mUpdateDrawerListener;



    public static ProfileFragment newInstance(Profile profile,String uri)
    {
        Bundle bundle=new Bundle();
        bundle.putSerializable("profile",profile);
        bundle.putString("profile_picture",uri);
        ProfileFragment profileFragment = new ProfileFragment();
        profileFragment.setArguments(bundle);
        return profileFragment;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mUpdateDrawerListener = (UpdateDrawerFromProfileFragment)getActivity();
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
        final TextView firstNameTv = rootView.findViewById(R.id.first_name_tv);
        final TextView lastNameTv = rootView.findViewById(R.id.last_name_tv);
        final ImageButton confirmNameBtn = rootView.findViewById(R.id.name_confirm_iv);
        final EditText firstNameEt = rootView.findViewById(R.id.first_name_et);
        final EditText lastNameEt = rootView.findViewById(R.id.last_name_et);
        final CircleImageView profilePicture = rootView.findViewById(R.id.profile_image);

        mProfileViewModel=new ViewModelProvider(this,new ViewModelFactory(getActivity().getApplication(), eViewModels.ProfileFragment)).get(ProfileViewModel.class);
        Bundle bundle=getArguments();
        mProfileViewModel.setProfile((Profile)bundle.getSerializable("profile"));
        mProfileViewModel.setImageUri(Uri.parse(bundle.getString("profile_picture")));

        firstNameTv.setText(mProfileViewModel.getProfile().getFirstName());
        lastNameTv.setText(mProfileViewModel.getProfile().getLastName());
        Glide.with(this).load(mProfileViewModel.getImageUri()).into(profilePicture);

        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
                        getActivity());
                myAlertDialog.setTitle("Upload Pictures Option");
                myAlertDialog.setMessage("How do you want to set your picture?");

                myAlertDialog.setPositiveButton("Gallery",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent, GALLERY_PICTURE);

                            }
                        });

                myAlertDialog.setNegativeButton("Camera",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {


                                file = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),   "profile.jpg");

                                imageUri = FileProvider.getUriForFile(
                                        getContext(),
                                        "com.example.androidsecondproject.provider", //(use your app signature + ".provider" )
                                        file);

                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                startActivityForResult(intent, CAMERA_REQUEST);

                            }
                        });
                myAlertDialog.show();


            }
        });
        
        editNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstNameTv.setVisibility(View.GONE);
                firstNameEt.setVisibility(View.VISIBLE);
                firstNameEt.setText(firstNameTv.getText().toString());
                editNameBtn.setVisibility(View.GONE);
                confirmNameBtn.setVisibility(View.VISIBLE);
                lastNameTv.setVisibility(View.GONE);
                lastNameEt.setVisibility(View.VISIBLE);
                lastNameEt.setText(lastNameTv.getText().toString());
            }
        });

        confirmNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstNameTv.setVisibility(View.VISIBLE);
                firstNameEt.setVisibility(View.GONE);
                firstNameTv.setText(firstNameEt.getText().toString());
                editNameBtn.setVisibility(View.VISIBLE);
                confirmNameBtn.setVisibility(View.GONE);
                lastNameTv.setVisibility(View.VISIBLE);
                lastNameEt.setVisibility(View.GONE);
                lastNameTv.setText(lastNameEt.getText().toString());

                mProfileViewModel.getProfile().setFirstName(firstNameEt.getText().toString());
                mProfileViewModel.getProfile().setLastName(lastNameEt.getText().toString());
                mUpdateDrawerListener.onUpdateProfile(mProfileViewModel.getProfile());
                mProfileViewModel.writeProfile();
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

                    writeToUi(itemSpinner.getItemAtPosition(position).toString(),descriptionTv);

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
                            readFromUi(itemTitleTv.getText().toString(),descriptionEt);


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

    public void writeToUi(String title,TextView descriptionTv)
        {
        switch (title){
            case "About myself":
                descriptionTv.setText(mProfileViewModel.getProfile().getFirstName());
                break;
            case "Looking for":
                descriptionTv.setText(mProfileViewModel.getProfile().getLookingFor());
                break;
            case "My hobbies":
                descriptionTv.setText(mProfileViewModel.getProfile().getHobbies());
                break;
        }

    }

    public void readFromUi(String title, EditText descriptionEt)
    {
        switch (title){
            case "About myself":
                mProfileViewModel.getProfile().setDescription(descriptionEt.getText().toString());
                mProfileViewModel.writeProfile();
                break;
            case "Looking for":
                mProfileViewModel.getProfile().setLookingFor(descriptionEt.getText().toString());
                mProfileViewModel.writeProfile();
                break;
            case "My hobbies":
                mProfileViewModel.getProfile().setHobbies(descriptionEt.getText().toString());
                mProfileViewModel.writeProfile();
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {

            if (requestCode == CAMERA_REQUEST) {
                // mViewModel.uploadPicture(imageUri);
            }
            if (requestCode == GALLERY_PICTURE) {
                imageUri = data.getData();
            }

        }
    }
}