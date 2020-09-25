package com.example.androidsecondproject.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidsecondproject.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends FirebaseRecyclerAdapter< Message, ChatAdapter.ChatViewHolder> {
   /* List<Profile> mProfiles;
    Profile mProfile;
    Context mContext;*/
   private String myUid;
   private final int TYPE_SEND=1,TYPE_RECEIVE=2;


    interface ChatListener{
        void OnProfileChatClick(View view, int position);
        void OnProfileChatLongClick(View view, int position);
    }

    public ChatAdapter(@NonNull FirebaseRecyclerOptions<Message> options, String myUid) {
        super(options);
        this.myUid = myUid;
    }

    private ChatListener chatListener;

    public void setListener(ChatListener chatListener) {
        this.chatListener=chatListener;
    }

/*
    public ChatAdapter(List<Profile> mProfiles, Profile mProfile,Context mContext){
        this.mContext=mContext;
        this.mProfiles=mProfiles;
        this.mProfile=mProfile;
    }
*/


    public class ChatViewHolder extends RecyclerView.ViewHolder{


        TextView dateTv;
        TextView contentTv;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);

            dateTv=itemView.findViewById(R.id.message_time);
            contentTv=itemView.findViewById(R.id.message_content);



            /*itemView.setOnClickListener(new View.OnClickListener() {
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
            });*/

        }

    }

    @Override
    public int getItemViewType(int position) {
        Message message=getItem(position);
        if(message.getSenderUid().equals(myUid)){
            return TYPE_SEND;
        }
        return TYPE_RECEIVE;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType==TYPE_RECEIVE){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_message_receive_card,parent,false);
        }
        else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_message_send_card,parent,false);
        }

        return new ChatViewHolder(view);
    }

 /*   @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ChatVH holder, int position) {
        Profile currentProfile=mProfiles.get(position);
        Glide.with(mContext).load(currentProfile.getProfilePictureUri()).error(R.drawable.man_profile).into(holder.profileIv);
        holder.profileName.setText(currentProfile.getFirstName()+" "+currentProfile.getLastName());
    }*/

    @Override
    protected void onBindViewHolder(@NonNull ChatViewHolder holder, int position, @NonNull Message message) {
        holder.contentTv.setText(message.getText());
        holder.dateTv.setText(message.getFormattedDate());
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }
}
