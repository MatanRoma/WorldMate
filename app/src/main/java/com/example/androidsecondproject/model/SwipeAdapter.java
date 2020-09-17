package com.example.androidsecondproject.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.androidsecondproject.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SwipeAdapter extends RecyclerView.Adapter<SwipeAdapter.SwipeViewHolder> {

    public void updateFilter(String title,boolean isChecked) {
        if(isChecked)
        {
            mCategories.add(title);
        }
        else if(mCategories.contains(title))
        {
            mCategories.remove(title);
        }


    }

    public class SwipeViewHolder extends RecyclerView.ViewHolder {
        TextView mNameTv;
        CircleImageView mProfileIv;
        TextView mCompabilityTv;

        public SwipeViewHolder(@NonNull View itemView) {
            super(itemView);
            mNameTv=itemView.findViewById(R.id.card_name_tv);
            mProfileIv=itemView.findViewById(R.id.card_profile_iv);
            mCompabilityTv = itemView.findViewById(R.id.compability_tv);

        }
    }

    List<Profile> mProfiles;
    List<String> mCategories;
    Profile mProfile;
    Context mContext;

    public SwipeAdapter(List<Profile> profiles,Context context,Profile profile,List<String> categories){
        this.mProfiles=profiles;
        this.mContext=context;
        this.mProfile = profile;
        this.mCategories = categories;
    }

    public List<Profile> getmProfiles() {
        return mProfiles;
    }

    public void setmProfiles(List<Profile> mProfiles) {
        this.mProfiles = mProfiles;
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
        Toast.makeText(mContext, "categories size "+mCategories.size(), Toast.LENGTH_SHORT).show();
        if(mCategories.size() != 0)
        {
            CompabilityCalculator compabilityCalculator = new CompabilityCalculator(mCategories,mProfile.getQuestionResponds(),getmProfiles().get(position).getQuestionResponds());
            holder.mCompabilityTv.setText(compabilityCalculator.getCompability()+"%");
        }
        else {
            holder.mCompabilityTv.setText("0%");
        }
    }

    @Override
    public int getItemCount() {
        return mProfiles.size();
    }
}
