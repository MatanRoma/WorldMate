package com.example.androidsecondproject.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.androidsecondproject.R;

public class PhotoPreviewFragment extends Fragment {
    public static PhotoPreviewFragment newInstance(String uri)
    {
        Bundle bundle=new Bundle();
        bundle.putString("uri",uri);
        PhotoPreviewFragment photoPreviewFragment=new PhotoPreviewFragment();
        photoPreviewFragment.setArguments(bundle);
        return photoPreviewFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.photo_preview_fragment,container,false);
        ImageView imageView = rootView.findViewById(R.id.image_preview);
        String imageUri = getArguments().getString("uri");
        Glide.with(getContext()).load(imageUri).into(imageView);

        return rootView;
    }
}
