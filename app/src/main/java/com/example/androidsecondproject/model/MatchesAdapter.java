package com.example.androidsecondproject.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidsecondproject.R;

import java.util.List;

public class MatchesAdapter extends RecyclerView.Adapter<MatchesAdapter.MatchesViewHolder> {
    private List<Profile> mProfiles;
    private Context mContext;

    public MatchesAdapter(List<Profile> mProfiles,Context context) {
        this.mProfiles = mProfiles;
        this.mContext = context;
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
    }

    @Override
    public int getItemCount() {
        return mProfiles.size();
    }

    public class MatchesViewHolder extends  RecyclerView.ViewHolder{
        TextView mProfileNameTv;
        public MatchesViewHolder(@NonNull View itemView) {
            super(itemView);
            mProfileNameTv = itemView.findViewById(R.id.profile_name_tv);
        }
    }
}
