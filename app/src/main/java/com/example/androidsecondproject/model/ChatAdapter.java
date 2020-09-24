package com.example.androidsecondproject.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.androidsecondproject.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatVH> {

    List<Profile> mProfiles;
    Profile mProfile;
    Context mContext;


    interface ChatListener{
        void OnProfileChatClick(View view, int position);
        void OnProfileChatLongClick(View view, int position);
    }

    private ChatListener chatListener;

    public void setListener(ChatListener chatListener) {
        this.chatListener=chatListener;
    }

    public ChatAdapter(List<Profile> mProfiles, Profile mProfile,Context mContext){
        this.mContext=mContext;
        this.mProfiles=mProfiles;
        this.mProfile=mProfile;
    }


    public class ChatVH extends RecyclerView.ViewHolder{

        CircleImageView profileIv;
        TextView profileName;

        public ChatVH(@NonNull View itemView) {
            super(itemView);

            profileIv = itemView.findViewById(R.id.profile_iv_chat_card);
            profileName = itemView.findViewById(R.id.profile_name_chat_card);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(chatListener!=null)
                    {
                        chatListener.OnProfileChatClick(v,getAdapterPosition());
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(chatListener!=null)
                        chatListener.OnProfileChatLongClick(v,getAdapterPosition());
                    return false;
                }
            });

        }

    }


    @NonNull
    @Override
    public ChatAdapter.ChatVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_chat_card,parent,false);
        ChatVH chatVH = new ChatVH(view);
        return chatVH;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ChatVH holder, int position) {
        Profile currentProfile=mProfiles.get(position);
        Glide.with(mContext).load(currentProfile.getProfilePictureUri()).error(R.drawable.man_profile).into(holder.profileIv);
        holder.profileName.setText(currentProfile.getFirstName()+" "+currentProfile.getLastName());
    }

    @Override
    public int getItemCount() {
        return mProfiles.size();
    }

}
