package com.example.androidsecondproject.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidsecondproject.R;
import com.example.androidsecondproject.model.ChatAdapter;
import com.example.androidsecondproject.model.Profile;
import com.example.androidsecondproject.viewmodel.ChatViewModel;

public class ChatFragment extends Fragment {
    private ChatAdapter mChatAdapter;
    private ChatViewModel mViewModel;
    RecyclerView mRecyclerView;
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
        View rootView = inflater.inflate(R.layout.chat_conversations_fragment,container,false);

        mRecyclerView=rootView.findViewById(R.id.messaging_recycler);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));



        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
