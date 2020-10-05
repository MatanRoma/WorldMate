package com.example.androidsecondproject.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.example.androidsecondproject.R;
import com.example.androidsecondproject.model.Profile;

public class NewMatchDialogFragment extends DialogFragment {
    private AlertDialog mShow;
    private SwipeFragment mParentFragment;


    public interface OnNewMatchDialogFragmentDialofListener
    {
        void onMoveToNewChat(Profile profile,String chatId);
    }

    private OnNewMatchDialogFragmentDialofListener mListener;

    public NewMatchDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static NewMatchDialogFragment newInstance(Profile otherProfile,int position,Profile myProfile) {
        NewMatchDialogFragment frag = new NewMatchDialogFragment();
        Bundle args = new Bundle();
        args.putInt("position",position);
        args.putSerializable("other_profile",otherProfile);
        args.putSerializable("my_profile",myProfile);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (OnNewMatchDialogFragmentDialofListener)getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.new_match_dialog,container);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setCancelable(false);
        final Profile otherProfile = (Profile) getArguments().getSerializable("other_profile");
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final LinearLayout matchLayout = view.findViewById(R.id.new_match_layout);
        Button chatBtn = view.findViewById(R.id.chat_btn);
        Button closeBtn = view.findViewById(R.id.close_btn);
        final Profile myProfile = (Profile) getArguments().getSerializable("my_profile");



            //view.setBackground(getContext().getResources().getDrawable(R.drawable.transparent_gradient));

            final ImageView matchAnimation = view.findViewById(R.id.match_anim);
            TextView nameTv = view.findViewById(R.id.new_match_name);
            ImageView profileImage = view.findViewById(R.id.profile_new_match_image);
            nameTv.setText(otherProfile.getFirstName());
            //profileImage.setImageURI(Uri.parse(otherProfile.getProfilePictureUri()));
            Glide.with(getContext()).load(getContext().getResources().getDrawable(R.drawable.woman_profile)).error(R.drawable.man_profile).load(profileImage);
            //profileImage.setImageDrawable(getContext().getResources().getDrawable(R.drawable.woman_profile));
            matchAnimation.setVisibility(View.VISIBLE);
            AnimationDrawable animationDrawable = (AnimationDrawable) matchAnimation.getDrawable();
            animationDrawable.start();
            MediaPlayer matchSound = MediaPlayer.create(getActivity(),R.raw.match);
            matchSound.start();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    matchAnimation.setVisibility(View.INVISIBLE);
                    matchLayout.setVisibility(View.VISIBLE);
                    matchLayout.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimaryDark));
                }
            },3000);
            //mListener.onUpdateMatch(getArguments().getInt("position"));
            //mShow =myAlertDialog.show();
        Toast.makeText(getContext(), getParentFragment()+"", Toast.LENGTH_SHORT).show();
        //((SwipeFragment)getParentFragment()).updateMatch(getArguments().getInt("position"));

        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onMoveToNewChat(otherProfile,getChatId(myProfile,otherProfile));
                getDialog().dismiss();
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

    }

    public String getChatId(Profile myProfile, Profile otherProfile)
    {
        String chatKeyId,myUid=myProfile.getUid(),otherUid=otherProfile.getUid();
        if(myUid.compareTo(otherUid)>0){
            chatKeyId=myUid+otherUid;
        }
        else{
            chatKeyId=otherUid+myUid;
        }
        return chatKeyId;
    }
}
