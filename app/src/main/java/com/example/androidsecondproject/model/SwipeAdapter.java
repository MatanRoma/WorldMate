package com.example.androidsecondproject.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.androidsecondproject.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SwipeAdapter extends RecyclerView.Adapter<SwipeAdapter.SwipeViewHolder> {

    public class SwipeViewHolder extends RecyclerView.ViewHolder {
        TextView mNameTv;
        CircleImageView mProfileIv;

        public SwipeViewHolder(@NonNull View itemView) {
            super(itemView);
            mNameTv=itemView.findViewById(R.id.card_name_tv);
            mProfileIv=itemView.findViewById(R.id.card_profile_iv);

        }
    }

    List<Profile> mProfiles;
    Context mContext;
    public SwipeAdapter(List<Profile> profiles,Context context){
        this.mProfiles=profiles;
        this.mContext=context;
    }

    @NonNull
    @Override
    public SwipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_card,parent,false);
        SwipeViewHolder swipeViewHolder=new SwipeViewHolder(view);
        return swipeViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SwipeViewHolder holder, int position) {
        Profile currentProfile=mProfiles.get(position);
        Glide.with(mContext).load(currentProfile.getProfilePictureUri()).error(R.drawable.man_profile).into(holder.mProfileIv);
        holder.mNameTv.setText(currentProfile.getFirstName()+" "+currentProfile.getLastName());
    }

    @Override
    public int getItemCount() {
        return mProfiles.size();
    }
}
