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

import android.widget.ImageButton;
import android.widget.ImageView;

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
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.IOException;

import java.util.List;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends androidx.fragment.app.DialogFragment implements View.OnClickListener {

    final int GALLERY_PICTURE = 1;
    final int CAMERA_REQUEST = 2;
    CircleImageView profilePicture;
    Button changePicBtn;
    Uri imageUri;
    File file;
    private boolean mIsfromProfile;
    private ImageView[] mImageViews;
    private ImageButton[] mImageCloseBtns;
    private SpinKitView[] mLoadingAnimations;
    private SpinKitView mLoadingProfileAnimation;
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

        final Button saveBtn = rootView.findViewById(R.id.save_btn);
        final TextInputEditText firstName = rootView.findViewById(R.id.first_name_edit_profile_et);
        final TextInputEditText lastName = rootView.findViewById(R.id.last_name_edit_profile_et);
        final TextInputEditText descriptionAboutMe = rootView.findViewById(R.id.about_me_edit_profile_et);
        final TextInputEditText myHobbies = rootView.findViewById(R.id.hobbies_edit_profile_et);
        final TextInputEditText lookingFor = rootView.findViewById(R.id.looking_for_edit_profile_et);
        final RadioButton menRb = rootView.findViewById(R.id.men_rb);
        final RadioButton womenRb = rootView.findViewById(R.id.women_rb);
        final ImageButton galleryBtn=rootView.findViewById(R.id.gallery_btn);
        final ImageButton cameraBtn=rootView.findViewById(R.id.camera_btn);

        mViewModel =new ViewModelProvider(this,new ViewModelFactory(getActivity().getApplication(), eViewModels.ProfileFragment)).get(ProfileViewModel.class);

        Bundle bundle=getArguments();
        mViewModel.setmProfile((Profile)bundle.getSerializable("profile"));
        mViewModel.setImageUri(Uri.parse(bundle.getString("profile_picture")));

        profilePicture = rootView.findViewById(R.id.profile_image);
        changePicBtn = rootView.findViewById(R.id.change_pic_btn);
        mImageViews= new ImageView[]{rootView.findViewById(R.id.picture0_iv),rootView.findViewById(R.id.picture1_iv),rootView.findViewById(R.id.picture2_iv),
                rootView.findViewById(R.id.picture3_iv),rootView.findViewById(R.id.picture4_iv),rootView.findViewById(R.id.picture5_iv)};
        mImageCloseBtns=new ImageButton[]{rootView.findViewById(R.id.picture0_close),rootView.findViewById(R.id.picture1_close),rootView.findViewById(R.id.picture2_close),
                rootView.findViewById(R.id.picture3_close),rootView.findViewById(R.id.picture4_close),rootView.findViewById(R.id.picture5_close)};
        mLoadingAnimations=new SpinKitView[]{rootView.findViewById(R.id.spin_kit_loading0),rootView.findViewById(R.id.spin_kit_loading1),rootView.findViewById(R.id.spin_kit_loading2),
                rootView.findViewById(R.id.spin_kit_loading3),rootView.findViewById(R.id.spin_kit_loading4),rootView.findViewById(R.id.spin_kit_loading5)};
        mLoadingProfileAnimation=rootView.findViewById(R.id.spin_kit_loading_profile_pic);

        initializePictures();
        pictureButtonListeners(cameraBtn,galleryBtn);
        closeButtonsListeners();








    /*    final Observer<Boolean> uploadObserverSuccess = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(file!=null)
                    file.delete();
                mViewModel.downloadPicture();
            }
        };*/
        final Observer<Uri> downloadObserverMainPicSuccess = new Observer<Uri>() {
            @Override
            public void onChanged(Uri uri) {
                if(file!=null)
                    file.delete();
                String stringUri=uri.toString();
                List<String> pictures= mViewModel.getPictures();
                pictures.add(stringUri);
                Glide.with(getContext()).load(uri).into(mImageViews[pictures.size()-1]);
                mImageCloseBtns[pictures.size()-1].setVisibility(View.VISIBLE);
                mLoadingAnimations[pictures.size()-1].setVisibility(View.GONE);
                mViewModel.updateProfileMainPictures();


        }
        };

        final Observer<Uri> downloadObserverProfilePicSuccess = new Observer<Uri>() {
            @Override
            public void onChanged(Uri uri) {
                if(file!=null)
                    file.delete();
                Glide.with(getContext()).load(uri).into(profilePicture);
                mLoadingProfileAnimation.setVisibility(View.GONE);
                // mViewModel.setImageUri(uri);
                mViewModel.getmProfile().setProfilePictureUri(uri.toString());
                mUpdateDrawerListener.onUpdateProfile(mViewModel.getmProfile());
                mViewModel.updateDataBaseProfilePic();
            }
        };
       // mViewModel.getUploadResultSuccess().observe(this, uploadObserverSuccess);
        mViewModel.getDownloadMainResultSuccess().observe(this,downloadObserverMainPicSuccess);
        mViewModel.getDownloadProfileResultSuccess().observe(this, downloadObserverProfilePicSuccess);


        firstName.setText(mViewModel.getmProfile().getFirstName());
        lastName.setText(mViewModel.getmProfile().getLastName());
        descriptionAboutMe.setText(mViewModel.getmProfile().getDescription());
        myHobbies.setText(mViewModel.getmProfile().getHobbies());
        lookingFor.setText(mViewModel.getmProfile().getLookingFor());
        Toast.makeText(getContext(), mViewModel.getImageUri()+"", Toast.LENGTH_SHORT).show();
        Glide.with(this).load(mViewModel.getImageUri()).error(R.drawable.man_profile).into(profilePicture);
        if(mViewModel.getmProfile().getGender().equals("male"))
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
                mViewModel.getmProfile().setFirstName(Objects.requireNonNull(firstName.getText()).toString());
                mViewModel.getmProfile().setLastName(Objects.requireNonNull(lastName.getText()).toString());
                mViewModel.getmProfile().setHobbies(Objects.requireNonNull(myHobbies.getText()).toString());
                mViewModel.getmProfile().setLookingFor(Objects.requireNonNull(lookingFor.getText()).toString());
                mViewModel.getmProfile().setDescription(Objects.requireNonNull(descriptionAboutMe.getText()).toString());
                if(menRb.isChecked()){
                    mViewModel.getmProfile().setGender("male");
                }
                else if(womenRb.isChecked()){
                    mViewModel.getmProfile().setGender("female");
                }
                mViewModel.writeProfile();
              //  mViewModel.readProfiles();
                mUpdateDrawerListener.onUpdateProfile(mViewModel.getmProfile());
                Objects.requireNonNull(getActivity()).onBackPressed();
            }
        });

        return rootView;




    }

    private void closeButtonsListeners() {
        int i=0;
        for(ImageButton imageButton:mImageCloseBtns){
            imageButton.setOnClickListener(this);
            imageButton.setTag(i);
            i++;
        }

    }
    private void pictureButtonListeners(ImageButton cameraBtn,ImageButton galleryBtn) {
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mViewModel.getPictures().size()<=5){
                    file = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),    System.nanoTime()+"profile.jpg");

                    imageUri = FileProvider.getUriForFile(
                            getContext(),
                            "com.example.androidsecondproject.provider", //(use your app signature + ".provider" )
                            file);

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, CAMERA_REQUEST);
                }
            }
        });
        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mViewModel.getPictures().size()<=5){
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, GALLERY_PICTURE);
                }
            }
        });
    }

    private void initializePictures() {
        List<String> pictures=mViewModel.getPictures();
        for(int i=0;i<pictures.size();i++){
            Glide.with(getContext()).load(pictures.get(i)).into(mImageViews[i]);
            mImageCloseBtns[i].setVisibility(View.VISIBLE);
        }
        /*Glide.with(getContext()).load(android.R.color.transparent).into(mImageViews[1]);
        pictures.remove(1);
        for(int i=0;i<mImageViews.length;i++){
            if(i<pictures.size()){
                Glide.with(getContext()).load(pictures.get(i)).into(mImageViews[i]);
            }
            else {
                Glide.with(getContext()).load(android.R.color.transparent).into(mImageViews[i]);
            }
        }*/

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
                        mIsfromProfile=true;
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
                        mIsfromProfile=true;
                        startActivityForResult(intent, CAMERA_REQUEST);

                    }
                });
        myAlertDialog.show();

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

            uploadBitmap();

        }
        else {
            mIsfromProfile=false;
        }
    }
    private void uploadBitmap(){
        if(!mIsfromProfile){
            mLoadingAnimations[mViewModel.getPictures().size()].setVisibility(View.VISIBLE);
        }
        else{
          mLoadingProfileAnimation.setVisibility(View.VISIBLE);
        }

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
        mViewModel.uploadPicture(bitmap,mIsfromProfile);
        mIsfromProfile=false;
    }
    @Override
    public void onClick(View v) {
        int position= (int) v.getTag();
        List<String> pictures=mViewModel.getPictures();
        pictures.remove(position); //TODO delete picture from storage
        if(position==pictures.size()){
            mImageCloseBtns[position].setVisibility(View.INVISIBLE);
            Glide.with(getContext()).load(android.R.color.transparent).into(mImageViews[position]);
        }
        else{
            for(int i=0;i<mImageViews.length;i++){
                if(i<pictures.size()){
                    Glide.with(getContext()).load(pictures.get(i)).into(mImageViews[i]);
                    mImageCloseBtns[i].setVisibility(View.VISIBLE);
                }
                else {
                    Glide.with(getContext()).load(android.R.color.transparent).into(mImageViews[i]);
                    mImageCloseBtns[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        mViewModel.updateProfileMainPictures();


    }

}