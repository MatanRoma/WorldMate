package com.example.androidsecondproject.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.androidsecondproject.R;
import com.example.androidsecondproject.model.Profile;
import com.example.androidsecondproject.model.eViewModels;
import com.example.androidsecondproject.viewmodel.ProfileViewModel;
import com.example.androidsecondproject.viewmodel.ViewModelFactory;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends androidx.fragment.app.DialogFragment {

    final int GALLERY_PICTURE = 1;
    final int CAMERA_REQUEST = 2;
    CircleImageView profilePicture;
    Button changePicBtn;
    Uri imageUri;
    File file;
    //private float mAge;


    private ProfileViewModel mViewModel;

    public interface UpdateDrawerFromProfileFragment
    {
        public void onUpdateProfile(Profile profile);
    }

    private UpdateDrawerFromProfileFragment mUpdateDrawerListener;



    public static ProfileFragment newInstance(Profile profile)
    {
        Bundle bundle=new Bundle();
        bundle.putSerializable("profile",profile);
        bundle.putString("profile_picture",profile.getProfilePictureUri());
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
        setHasOptionsMenu(true);

    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.removeItem(R.id.filter_id);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.profile_fragment,container,false);

        //final Button editDescriptionBtn = rootView.findViewById(R.id.edit_btn);
        //final TextView descriptionTv = rootView.findViewById(R.id.description_tv);
        //final EditText descriptionEt = rootView.findViewById(R.id.description_et);
        //final ImageButton confirmNameBtn = rootView.findViewById(R.id.name_confirm_iv);
        //final EditText firstNameEt = rootView.findViewById(R.id.first_name_et);
        //final EditText lastNameEt = rootView.findViewById(R.id.last_name_et);
        final Button saveBtn = rootView.findViewById(R.id.save_btn);
        final TextInputEditText firstName = rootView.findViewById(R.id.first_name_edit_profile_et);
        final TextInputEditText lastName = rootView.findViewById(R.id.last_name_edit_profile_et);
        final TextInputEditText descriptionAboutMe = rootView.findViewById(R.id.about_me_edit_profile_et);
        final TextInputEditText myHobbies = rootView.findViewById(R.id.hobbies_edit_profile_et);
        final TextInputEditText lookingFor = rootView.findViewById(R.id.looking_for_edit_profile_et);
        final RadioButton menRb = rootView.findViewById(R.id.men_rb);
        final RadioButton womenRb = rootView.findViewById(R.id.women_rb);
        profilePicture = rootView.findViewById(R.id.profile_image);
        changePicBtn = rootView.findViewById(R.id.change_pic_btn);





        mViewModel =new ViewModelProvider(this,new ViewModelFactory(getActivity().getApplication(), eViewModels.ProfileFragment)).get(ProfileViewModel.class);


        final Observer<Boolean> uploadObserverSuccess = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(file!=null)
                    file.delete();
                mViewModel.downloadPicture();
            }
        };
        final Observer<Uri> downloadObserverSuccess = new Observer<Uri>() {
            @Override
            public void onChanged(Uri uri) {
                Glide.with(getContext()).load(uri).into(profilePicture);
                // mViewModel.setImageUri(uri);
                mViewModel.getProfile().setProfilePictureUri(uri.toString());
                mUpdateDrawerListener.onUpdateProfile(mViewModel.getProfile());
            }
        };
        mViewModel.getUploadResultSuccess().observe(this, uploadObserverSuccess);
        mViewModel.getDownloadResultSuccess().observe(this, downloadObserverSuccess);

        Bundle bundle=getArguments();
        //Initialize my profile fragment
        mViewModel.setProfile((Profile)bundle.getSerializable("profile"));
        mViewModel.setImageUri(Uri.parse(bundle.getString("profile_picture")));
        firstName.setText(mViewModel.getProfile().getFirstName());
        lastName.setText(mViewModel.getProfile().getLastName());
        descriptionAboutMe.setText(mViewModel.getProfile().getDescription());
        myHobbies.setText(mViewModel.getProfile().getHobbies());
        lookingFor.setText(mViewModel.getProfile().getLookingFor());
        Toast.makeText(getContext(), mViewModel.getImageUri()+"", Toast.LENGTH_SHORT).show();
        Glide.with(this).load(mViewModel.getImageUri()).error(R.drawable.man_profile).into(profilePicture);
        if(mViewModel.getProfile().getGender().equals("male"))
        {
            menRb.setChecked(true);
        }
        else
        {
            womenRb.setChecked(true);
        }


        changePicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePicture();
            }
        });

        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePicture();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.getProfile().setFirstName(Objects.requireNonNull(firstName.getText()).toString());
                mViewModel.getProfile().setLastName(Objects.requireNonNull(lastName.getText()).toString());
                mViewModel.getProfile().setHobbies(Objects.requireNonNull(myHobbies.getText()).toString());
                mViewModel.getProfile().setLookingFor(Objects.requireNonNull(lookingFor.getText()).toString());
                mViewModel.getProfile().setDescription(Objects.requireNonNull(descriptionAboutMe.getText()).toString());
                if(menRb.isChecked()){
                    mViewModel.getProfile().setGender("male");
                }
                else if(womenRb.isChecked()){
                    mViewModel.getProfile().setGender("female");
                }
                mViewModel.writeProfile();
                mViewModel.readProfiles();
                mUpdateDrawerListener.onUpdateProfile(mViewModel.getProfile());
            }
        });

        return rootView;


        /*ArrayList<String> genders = new ArrayList<String >();
        genders.add("Male");
        genders.add("Female");
        final ArrayAdapter<String> genderArrayAdapter = new ArrayAdapter<String>(getContext(),R.layout.spinner_item,genders);
        genderSpinner.setAdapter(genderArrayAdapter);*/

        //ageTv.setText(((int)mViewModel.getProfile().getAge())+"");


        /*menRb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(menRb.isChecked())
                {
                    mViewModel.getProfile().setGender("male");
                    mViewModel.writeProfile();
                }

            }
        });

        womenRb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(womenRb.isChecked())
                {
                    mViewModel.getProfile().setGender("female");
                    mViewModel.writeProfile();
                }

            }
        });*/


        //ageSb.setMinValue(mViewModel.getProfile().getAge()).apply();
        //ageSb.setMinStartValue(mViewModel.getProfile().getAge()).apply();
        //menRb.setText(mViewModel.getProfile().getGender());
        //Toast.makeText(getContext(), mViewModel.getProfile().getAge()+"", Toast.LENGTH_SHORT).show();
        //ageTv.setText("My age  "+mViewModel.getProfile().getAge());

/*        ageSb.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                mAge = value.floatValue();
                ageTv.setText("My age  "+(int)mAge);
            }
        });*/



       /* editNameBtn.setOnClickListener(new View.OnClickListener() {
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
                ageTv.setVisibility(View.GONE);
                //menRb.setVisibility(View.GONE);
               // genderSpinner.setVisibility(View.VISIBLE);
                ageEt.setVisibility(View.VISIBLE);
                //genderSpinner.setSelection(menRb.getText().toString().equals("Male")? 0:1);
            }
        });

        confirmNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.parseInt(ageEt.getText().toString())>=18 && Integer.parseInt(ageEt.getText().toString())<=100)
                {
                    firstNameTv.setVisibility(View.VISIBLE);
                    firstNameEt.setVisibility(View.GONE);
                    firstNameTv.setText(firstNameEt.getText().toString());
                    editNameBtn.setVisibility(View.VISIBLE);
                    confirmNameBtn.setVisibility(View.GONE);
                    lastNameTv.setVisibility(View.VISIBLE);
                    lastNameEt.setVisibility(View.GONE);
                    lastNameTv.setText(lastNameEt.getText().toString());

                    menRb.setVisibility(View.VISIBLE);
                    //genderSpinner.setVisibility(View.GONE);
                    ageEt.setVisibility(View.GONE);
                    ageTv.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), ageEt.getText().toString()+"", Toast.LENGTH_SHORT).show();
                    ageTv.setText(ageEt.getText().toString());
                    //menRb.setText(genderSpinner.getSelectedItem().toString());
                    mViewModel.getProfile().setFirstName(firstNameEt.getText().toString());
                    mViewModel.getProfile().setLastName(lastNameEt.getText().toString());
                    //mViewModel.getProfile().setGender(genderSpinner.getSelectedItem().toString());
                    mViewModel.getProfile().setAge(Float.parseFloat(ageTv.getText().toString()));
                    mViewModel.writeProfile();
                    mUpdateDrawerListener.onUpdateProfile(mViewModel.getProfile());
                }

            }
        });*/


        /*ArrayList<String> profileItems = new ArrayList<String >();
        profileItems.add("Choose a subject");
        profileItems.add("About myself");
        profileItems.add("Looking for");
        profileItems.add("My hobbies");*/

/*

        final Spinner itemSpinner = rootView.findViewById(R.id.profile_item_spinner);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(),R.layout.spinner_item,profileItems);

        itemSpinner.setAdapter(arrayAdapter);


        itemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

                if(itemSpinner.getItemAtPosition(position) != "Choose a subject")
                {
                    final LinearLayout itemsLayout = rootView.findViewById(R.id.items_layout);
                    final View child = getLayoutInflater().inflate(R.layout.profile_item_layout, itemsLayout, false);

                    final ImageButton editDescriptionBtn = child.findViewById(R.id.edit_btn);
                    final ImageButton saveBtn = child.findViewById(R.id.save_btn);
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
*/

            /*@Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/



        //itemSpinner.setPopupBackgroundDrawable(getResources().getDrawable(R.drawable.spinner_background));

    }

    public void changePicture(){
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


                        file = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),    System.nanoTime()+"profile.jpg");

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

    /*  public void writeToUi(String title,TextView descriptionTv)
          {
          switch (title){
              case "About myself":
                  descriptionTv.setText(mViewModel.getProfile().getFirstName());
                  break;
              case "Looking for":
                  descriptionTv.setText(mViewModel.getProfile().getLookingFor());
                  break;
              case "My hobbies":
                  descriptionTv.setText(mViewModel.getProfile().getHobbies());
                  break;
          }

      }

      public void readFromUi(String title, EditText descriptionEt)
      {
          switch (title){
              case "About myself":
                  mViewModel.getProfile().setDescription(descriptionEt.getText().toString());
                  mViewModel.writeProfile();
                  break;
              case "Looking for":
                  mViewModel.getProfile().setLookingFor(descriptionEt.getText().toString());
                  mViewModel.writeProfile();
                  break;
              case "My hobbies":
                  mViewModel.getProfile().setHobbies(descriptionEt.getText().toString());
                  mViewModel.writeProfile();
                  break;
          }
      }

  */
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

            uploadBitmap();

        }
    }
    private void uploadBitmap(){
        Bitmap bitmap=null;
        if(Build.VERSION.SDK_INT<28){
            try {
                bitmap= MediaStore.Images.Media.getBitmap(
                        getActivity().getContentResolver(),
                        imageUri
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            ImageDecoder.Source source = ImageDecoder.createSource(getActivity().getContentResolver(), imageUri);
            try {
                bitmap = ImageDecoder.decodeBitmap(source);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mViewModel.uploadPicture(bitmap);
    }
}