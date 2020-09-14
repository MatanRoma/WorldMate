package com.example.androidsecondproject.view;

import android.Manifest;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.androidsecondproject.R;
import com.example.androidsecondproject.model.eViewModels;
import com.example.androidsecondproject.repository.StorageRepository;
import com.example.androidsecondproject.viewmodel.ProfilePhotoViewModel;
import com.example.androidsecondproject.viewmodel.RegisterViewModel;
import com.example.androidsecondproject.viewmodel.ViewModelFactory;
import com.github.ybq.android.spinkit.SpinKitView;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class ProfilePhotoFragment extends androidx.fragment.app.DialogFragment {
    final int CAMERA_REQUEST = 1;
    final int WRITE_PERMISSION_REQUEST = 1;
    final int RESULT_LOAD_IMAGE = 2;

    File file;
    String imageUrl = null;
    Uri imageUri;
    ImageView resultIv;
    ProfilePhotoViewModel mViewModel;
    SpinKitView loadingAnimation;
    private PhotoFragmentInterface mListener;

    interface PhotoFragmentInterface {
        public void OnClickContinueToPreferences();
    }

    public static ProfilePhotoFragment newInstance() {

        ProfilePhotoFragment profilePhotoFragment = new ProfilePhotoFragment();
        return profilePhotoFragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (PhotoFragmentInterface) getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.photo_profile_fragment, container, false);

        resultIv = rootView.findViewById(R.id.selected_iv);
        loadingAnimation = rootView.findViewById(R.id.spin_kit);
        Button continueButton = rootView.findViewById(R.id.continue_btn_photo);
        mViewModel = new ViewModelProvider(this, new ViewModelFactory(getActivity().getApplication(), eViewModels.ProfilePhoto)).get(ProfilePhotoViewModel.class);
        final Observer<Uri> downloadObserverSuccess = new Observer<Uri>() {
            @Override
            public void onChanged(Uri uri) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadingAnimation.setVisibility(View.GONE);
                        resultIv.setVisibility(View.VISIBLE);
                    }
                }, 1500);
                Glide.with(ProfilePhotoFragment.this).load(uri).into(resultIv);

            }
        };
        final Observer<Boolean> uploadObserverSuccess = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                loadingAnimation.setVisibility(View.GONE);
                resultIv.setVisibility(View.VISIBLE);
                Glide.with(ProfilePhotoFragment.this).load(imageUri).into(resultIv);

            }
        };
        mViewModel.getUploadResultSuccess().observe(this, uploadObserverSuccess);
        mViewModel.getDownloadResultSuccess().observe(this, downloadObserverSuccess);

        ImageButton cameraBtn = rootView.findViewById(R.id.take_picture);
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date currentTime = Calendar.getInstance().getTime();
                Long miliTime = currentTime.getTime();

                file = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), miliTime + ".jpg");

                imageUri = FileProvider.getUriForFile(
                        getContext(),
                        "com.example.androidsecondproject.provider", //(use your app signature + ".provider" )
                        file);

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, CAMERA_REQUEST);
            }
        });

        ImageButton galleryBtn = rootView.findViewById(R.id.from_gallery);
        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
            }
        });

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.OnClickContinueToPreferences();
            }
        });
        return rootView;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            resultIv.setVisibility(View.GONE);
            loadingAnimation.setVisibility(View.VISIBLE);
            if (requestCode == CAMERA_REQUEST) {
                // mViewModel.uploadPicture(imageUri);
            }
            if (requestCode == RESULT_LOAD_IMAGE) {
                imageUri = data.getData();
                //     mViewModel.uploadPicture(imageUri);
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

