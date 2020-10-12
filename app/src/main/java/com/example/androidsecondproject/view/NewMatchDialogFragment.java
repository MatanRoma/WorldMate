package com.example.androidsecondproject.view;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.androidsecondproject.R;
import com.example.androidsecondproject.model.Profile;
import com.example.androidsecondproject.model.eViewModels;
import com.example.androidsecondproject.viewmodel.NewMatchDialogViewModel;
import com.example.androidsecondproject.viewmodel.ViewModelFactory;
import com.google.android.material.textfield.TextInputEditText;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewMatchDialogFragment extends DialogFragment {
    private AlertDialog mShow;
    private SwipeFragment mParentFragment;
    private NewMatchDialogViewModel mViewModel;


    public interface OnNewMatchDialogFragmentDialofListener
    {
        void onMoveToNewChat(Profile profile,String chatId,Profile otherProfile);
    }

    private OnNewMatchDialogFragmentDialofListener mListener;

    public NewMatchDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static NewMatchDialogFragment newInstance(Profile otherProfile,Profile myProfile) {
        NewMatchDialogFragment frag = new NewMatchDialogFragment();
        Bundle args = new Bundle();
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

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel =new ViewModelProvider(this,new ViewModelFactory(getActivity().getApplication(), eViewModels.NewMatchDialog)).get(NewMatchDialogViewModel.class);

        setCancelable(false);
        mViewModel.setmOtherProfile((Profile) getArguments().getSerializable("other_profile"));
        mViewModel.setmMyProfile((Profile) getArguments().getSerializable("my_profile"));
        mViewModel.setmChatId();
        mViewModel.setContext(getContext());
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final RelativeLayout matchLayout = view.findViewById(R.id.new_match_layout);
        Button chatBtn = view.findViewById(R.id.chat_btn);
        Button closeBtn = view.findViewById(R.id.close_btn);



            //view.setBackground(getContext().getResources().getDrawable(R.drawable.transparent_gradient));
            TextView titleTv = view.findViewById(R.id.new_match_title);
            titleTv.setText(titleTv.getText()+" "+mViewModel.getmOtherProfile().getFirstName());
            final ImageView matchAnimation = view.findViewById(R.id.match_anim);
            CircleImageView profileImage = view.findViewById(R.id.profile_new_match_image);
            final TextInputEditText messageEt = view.findViewById(R.id.message_et);
            ImageButton sendBtn = view.findViewById(R.id.send_btn);
            if(!checkDirection()){
                sendBtn.setRotation(180);
            }
            //profileImage.setImageURI(Uri.parse(otherProfile.getProfilePictureUri()));
            Glide.with(getContext()).load(mViewModel.getmOtherProfile().getProfilePictureUri()).error(R.drawable.man_profile).into(profileImage);
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
                    matchAnimation.setVisibility(View.GONE);
                    matchLayout.setVisibility(View.VISIBLE);
                    matchLayout.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimaryDark));
                }
            },2200);
            //mListener.onUpdateMatch(getArguments().getInt("position"));
            //mShow =myAlertDialog.show();
        //((SwipeFragment)getParentFragment()).updateMatch(getArguments().getInt("position"));

        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onMoveToNewChat(mViewModel.getmMyProfile(),mViewModel.getmChatId(),mViewModel.getmOtherProfile());
                getDialog().dismiss();
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!messageEt.getText().toString().equals(""))
                {
                    mViewModel.writeMessage(messageEt.getText().toString());
                    getDialog().dismiss();
                }

            }
        });

    }


    private boolean checkDirection() {
        boolean isLtr;
        Configuration config = getResources().getConfiguration();
        if(config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            isLtr = false;
        }
        else {
            isLtr = true;
        }
        return  isLtr;
    }


}
