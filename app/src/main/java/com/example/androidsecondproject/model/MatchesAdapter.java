package com.example.androidsecondproject.model;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.androidsecondproject.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MatchesAdapter extends RecyclerView.Adapter<MatchesAdapter.MatchesViewHolder> implements Filterable {
    final long DAY = 24 * 60 * 60 * 1000;

    private Map<String,Profile> mProfilesMap;
    private Context mContext;
    private String newMatchUid;
    private List<Chat> mChats;
    private List<Chat> mAllChats;
    private List<Profile> mMatches;
    private boolean isLtr;



    public MatchesAdapter(List<Profile> profiles,Context context,List<Chat> chats,String newMatchUid,boolean isLtr) {
        this.mContext = context;
        this.newMatchUid=newMatchUid;
        this.mChats=chats;
        mProfilesMap=new HashMap<>();
        for(Profile profile:profiles){
           mProfilesMap.put(profile.getUid(),profile);
        }
        this.mAllChats = new ArrayList<>(chats);
        this.isLtr = isLtr;
        this.mMatches=profiles;
    }

    private MatchInterface matchClickListener;

    public void sortChats() {

        Collections.sort(mChats, new Comparator<Chat>() {
            @Override
            public int compare(Chat o1, Chat o2) {
                Log.d("cmp_date",o1.getLastMessage().getMessageDate().compareTo(o2.getLastMessage().getMessageDate())+"");
                return (o1.getLastMessage().getMessageDate().compareTo(o2.getLastMessage().getMessageDate()))*(-1);
            }
        });
        this.mAllChats = new ArrayList<>(mChats);
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    private Filter mFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Chat> filteredList = new ArrayList<>();
            if(constraint.toString().isEmpty()){
                filteredList.addAll(mAllChats);
            }
            else {
                for(Chat chat : mAllChats){
                    Profile currProfile;
                    if(mProfilesMap.containsKey(chat.getFirstUid())){
                        currProfile=mProfilesMap.get(chat.getFirstUid());
                    }
                    else {
                        currProfile=mProfilesMap.get(chat.getSecondUid());
                    }
                    String fullName = currProfile.getFirstName()+" "+currProfile.getLastName();
                    if(fullName.toLowerCase().contains(constraint.toString().toLowerCase())){
                        filteredList.add(chat);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mChats.clear();
            mChats.addAll((Collection<? extends Chat>) results.values);
            notifyDataSetChanged();
        }
    };

    public Profile getMatchProfile(int position) {
        Chat chat=mChats.get(position);
        if(mProfilesMap.containsKey(chat.getSecondUid())){
            return mProfilesMap.get(chat.getSecondUid());
        }
        else{
            return mProfilesMap.get(chat.getFirstUid());
        }

    }


    public interface MatchInterface
    {
        void onChatClickedListener(Profile otherProfile);
    }
    public void setListener(MatchInterface listener)
    {
        this.matchClickListener = listener;
    }

    @NonNull
    @Override
    public MatchesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_chat_card,parent,false);
        MatchesViewHolder matchesViewHolder = new MatchesViewHolder(view);
        return matchesViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MatchesViewHolder holder, int position) {

       Chat currChat=mChats.get(position);
        Profile currProfile;
       if(mProfilesMap.containsKey(currChat.getFirstUid())){
           currProfile=mProfilesMap.get(currChat.getFirstUid());
       }
       else {
           currProfile=mProfilesMap.get(currChat.getSecondUid());
       }
        holder.mProfileNameTv.setText(currProfile.getFirstName()+" "+currProfile.getLastName());
        Glide.with(mContext).load(currProfile.getProfilePictureUri()).error(R.drawable.man_profile).into(holder.mProfileIv);
        if(newMatchUid!=null&&newMatchUid.equals(currProfile.getUid()))
        {
            holder.mProfileNameTv.setTextColor(Color.RED);
        }
        else{
            holder.mProfileNameTv.setTextColor(Color.BLACK);
        }
        if(currChat.getLastMessage()!=null) {
            if(!currChat.getLastMessage().getText().equals(""))
            {
                holder.mNewMatchIv.setVisibility(View.GONE);
                holder.mLastMessageTv.setText(currChat.getLastMessage().getText());
                holder.mDateTv.setText(currChat.getLastMessage().getFormattedDate());
                if(currChat.getLastMessage().getText().length()>15)
                {
                    String subLastMessage = currChat.getLastMessage().getText().substring(0,15) + "...";
                    holder.mLastMessageTv.setText(subLastMessage);
                }
            }
            else if(inLastDay(currChat.getLastMessage().getMessageDate()))
            {
                holder.mNewMatchIv.setVisibility(View.VISIBLE);
                if(!isLtr){
                    holder.mNewMatchIv.setRotation(-90);
                }
            }
            else {
                holder.mNewMatchIv.setVisibility(View.GONE);
            }
        }

    }


    @Override
    public int getItemCount() {
        return mChats.size();
    }

    public class MatchesViewHolder extends  RecyclerView.ViewHolder{
        TextView mProfileNameTv,mLastMessageTv,mDateTv;
        CircleImageView mProfileIv;
        ImageView mNewMatchIv;

        public MatchesViewHolder(@NonNull View itemView) {
            super(itemView);
            mProfileNameTv = itemView.findViewById(R.id.profile_name_chat_card);
            mProfileIv  =itemView.findViewById(R.id.profile_iv_chat_card);
            mLastMessageTv= itemView.findViewById(R.id.last_message_tv_chat_card);
            mDateTv = itemView.findViewById(R.id.date_tv_chat_card);
            mNewMatchIv = itemView.findViewById(R.id.new_match_iv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(matchClickListener !=null)
                    {
                        Chat currChat=mChats.get(getAdapterPosition());
                        Profile currProfile;
                        if(mProfilesMap.containsKey(currChat.getFirstUid())){
                            currProfile=mProfilesMap.get(currChat.getFirstUid());
                        }
                        else {
                            currProfile=mProfilesMap.get(currChat.getSecondUid());
                        }
                        matchClickListener.onChatClickedListener(currProfile);
                    }

                }
            });

        }
    }

    public boolean inLastDay(Date aDate) {
        return aDate.getTime() > System.currentTimeMillis() - DAY;
    }
}
