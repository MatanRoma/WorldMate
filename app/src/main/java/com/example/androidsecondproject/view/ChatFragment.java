package com.example.androidsecondproject.view;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.androidsecondproject.R;
import com.example.androidsecondproject.model.ChatAdapter;
import com.example.androidsecondproject.model.CompabilityCalculator;
import com.example.androidsecondproject.model.Message;
import com.example.androidsecondproject.model.Profile;
import com.example.androidsecondproject.model.eViewModels;
import com.example.androidsecondproject.viewmodel.ChatViewModel;
import com.example.androidsecondproject.viewmodel.ViewModelFactory;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Observable;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatFragment extends Fragment {

    private ChatAdapter mChatAdapter;
    private ChatViewModel mViewModel;
    private LinearLayoutManager mLinearLayoutManager;
    public static String chatId;
    private RecyclerView mRecyclerView;

    public OnMoveToProfilePreviewFromChat onMoveToProfilePreviewFromChat;

    public interface OnMoveToProfilePreviewFromChat
    {
        void onClickMoveToProfilePreviewFromChat(Profile otherProfile,int compability);
    }

    public static ChatFragment newInstance(Profile profile, Profile otherProfile, String chatId)
    {
        Bundle bundle=new Bundle();
        bundle.putSerializable("profile",profile);
        bundle.putSerializable("other_profile",otherProfile);
        bundle.putString("chat_id",chatId);
        ChatFragment chatFragment=new ChatFragment();
        chatFragment.setArguments(bundle);
        return chatFragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onMoveToProfilePreviewFromChat = (OnMoveToProfilePreviewFromChat)getActivity();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.chat_layout,container,false);

        mViewModel=new ViewModelProvider(this,new ViewModelFactory(getActivity().getApplication(), eViewModels.Chat)).get(ChatViewModel.class);

        mRecyclerView=rootView.findViewById(R.id.messaging_recycler);
        mRecyclerView.setHasFixedSize(true);
        mLinearLayoutManager=new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);


        ImageButton backToAllChats = rootView.findViewById(R.id.back_to_chats_ib);
        ImageButton sendButton=rootView.findViewById(R.id.send_message);
        final EditText chatEt=rootView.findViewById(R.id.text_chat_et);
        TextView nameTv=rootView.findViewById(R.id.profile_name_chat);
        CircleImageView profileImage=rootView.findViewById(R.id.profile_image_chat);
        final TextView isOnlineTv = rootView.findViewById(R.id.is_online_tv);

        RelativeLayout chatHeader = rootView.findViewById(R.id.chat_rl_layout);

        mViewModel.setChatId(getArguments().getString("chat_id"));
        mViewModel.setMyProfile((Profile)getArguments().getSerializable("profile"));
        mViewModel.setOtherProfile((Profile)getArguments().getSerializable("other_profile"));
        mViewModel.setContext(getContext());
        FirebaseRecyclerOptions<Message> recyclerOptions=new FirebaseRecyclerOptions.Builder<Message>().setQuery(mViewModel.readAllMessages(),Message.class).build();

        chatHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int compability = checkComapability(mViewModel.getMyProfile(),mViewModel.getOtherProfile());
                onMoveToProfilePreviewFromChat.onClickMoveToProfilePreviewFromChat(mViewModel.getOtherProfile(),compability);
            }
        });

        backToAllChats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getActivity()).onBackPressed();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text=chatEt.getText().toString();
                if(text.length()>0){
                    mViewModel.writeMessage(text);
                    chatEt.setText("");
                }
            }
        });

        mChatAdapter=new ChatAdapter(recyclerOptions,mViewModel.getMyUid());
        mRecyclerView.setAdapter(mChatAdapter);
        mRecyclerView.scrollToPosition(mChatAdapter.getItemCount());
        if(!checkDirection()){
            sendButton.setRotation(180);
        }

        mRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if(oldBottom>bottom){
                    mRecyclerView.smoothScrollToPosition(mChatAdapter.getItemCount());
                    mLinearLayoutManager.setStackFromEnd(true);
                    mRecyclerView.setLayoutManager(mLinearLayoutManager);
                }
                mLinearLayoutManager.setStackFromEnd(false);
                mRecyclerView.setLayoutManager(mLinearLayoutManager);
            }
        });

        mChatAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int messagesCount = mChatAdapter.getItemCount();
                int lastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisiblePosition == -1 || (positionStart >= (messagesCount - 1)) && lastVisiblePosition == (positionStart - 1)) {
                    mRecyclerView.scrollToPosition(positionStart);
                }
            }
        });

        Profile otherProfile =(Profile)getArguments().getSerializable("other_profile");
        nameTv.setText(otherProfile.getFirstName() + " " + otherProfile.getLastName());
        Glide.with(getContext()).load(otherProfile.getProfilePictureUri()).error(R.drawable.man_profile).into(profileImage);

        Observer<Boolean> isOnlineObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                isOnlineTv.setVisibility(View.VISIBLE);
                isOnlineTv.setText(aBoolean?getString(R.string.online_str):getString(R.string.offline_str));
            }
        };
        mViewModel.getIsOnline().observe(this,isOnlineObserver);

        mViewModel.readIsOnline();
        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        mChatAdapter.stopListening();
        chatId=null;
    }

    @Override
    public void onStart() {
        super.onStart();
        mChatAdapter.startListening();
        chatId=mViewModel.getChatId();
    }

    public int checkComapability(Profile myProfile, Profile otherProfile)
    {
        List<String> categories = new ArrayList<String>() {{
            add("sport");
            add("food");
            add("culture");
            add("music");
            add("religion");
            add("travel");
        }};
        return CompabilityCalculator.caculateCompability(categories,otherProfile.getQuestionResponds(),myProfile.getQuestionResponds());
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.removeItem(R.id.filter_id);

    }

    @Override
    public void onPause() {
        super.onPause();
       // getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }
}
