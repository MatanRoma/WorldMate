package com.example.androidsecondproject.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.androidsecondproject.R;
import com.example.androidsecondproject.model.Profile;

public class ChatFragment extends Fragment {

    public static ChatFragment newInstance(Profile profile,Profile otherProfile)
    {
        Bundle bundle=new Bundle();
        bundle.putSerializable("profile",profile);
        bundle.putSerializable("other_profile",otherProfile);
        ChatFragment chatFragment=new ChatFragment();
        chatFragment.setArguments(bundle);
        return chatFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.chat_layout,container,false);


        return rootView;
    }
}
