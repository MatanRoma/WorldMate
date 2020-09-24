package com.example.androidsecondproject.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidsecondproject.R;

import java.util.List;

public class MatchesAdapter extends RecyclerView.Adapter<MatchesAdapter.MatchesViewHolder> {
    private List<Profile> mProfiles;
    private Context mContext;
    private Profile mMyProfile;

    public MatchesAdapter(List<Profile> mProfiles,Context context,Profile profile) {
        this.mProfiles = mProfiles;
        this.mContext = context;
        this.mMyProfile = profile;

    }

    private MatchInterface matchClickListener;

    public interface MatchInterface
    {
        void onChatClickedListener(String chatId);
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
        Profile curProfile = mProfiles.get(position);
        holder.mProfileNameTv.setText(curProfile.getFirstName());
        for (Match match: curProfile.getMatches()) {
            //Toast.makeText(mContext, mMyProfile.getEmail()+"", Toast.LENGTH_SHORT).show();
            if(match.getEmail().equals(mMyProfile.getEmail()))
            {
                holder.mChatId = match.getId();
            }
        }
    }

    @Override
    public int getItemCount() {
        return mProfiles.size();
    }

    public class MatchesViewHolder extends  RecyclerView.ViewHolder{
        TextView mProfileNameTv;
        String mChatId;
        public MatchesViewHolder(@NonNull View itemView) {
            super(itemView);
            mProfileNameTv = itemView.findViewById(R.id.profile_name_chat_card);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "set adapter listener", Toast.LENGTH_SHORT).show();
                    if(matchClickListener !=null)
                    {
                        matchClickListener.onChatClickedListener(mChatId);
                    }

                }
            });

        }
    }
}
