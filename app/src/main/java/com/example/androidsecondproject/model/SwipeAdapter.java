package com.example.androidsecondproject.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.androidsecondproject.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SwipeAdapter extends RecyclerView.Adapter<SwipeAdapter.SwipeViewHolder>  {

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

    public void removeItemPosition(int position) {
        mProfiles.remove(position);
        notifyItemRemoved(position);
    }

    public interface LikeDislikeItemListener
    {
        void OnLikeListener(View view,int position);
        void OnDislikeListener(View view,int position);
    }

    private LikeDislikeItemListener mLikeDislikeListener;

    public void setLikeDislikeListener(LikeDislikeItemListener likeDislikeItemListener)
    {
        this.mLikeDislikeListener = likeDislikeItemListener;
    }

    public void removeTopItem() {
        mProfiles.remove(0);
        notifyItemRemoved(0);

    }

    public class SwipeViewHolder extends RecyclerView.ViewHolder {
        TextView mNameTv;
        CircleImageView mProfileIv;
        TextView mCompabilityTv;
        TextView mCityTv;
        TextView mAgeTv;
        ImageButton likeBtn;
        ImageButton dislikeBtn;
        View mItemView;
        boolean isSwiped;

        public SwipeViewHolder(@NonNull View itemView) {
            super(itemView);
            mNameTv=itemView.findViewById(R.id.card_name_tv);
            mProfileIv=itemView.findViewById(R.id.card_profile_iv);
            mCompabilityTv = itemView.findViewById(R.id.compability_tv);
            mCityTv=itemView.findViewById(R.id.location_tv);
            mAgeTv=itemView.findViewById(R.id.card_age_tv);
            likeBtn = itemView.findViewById(R.id.like_ib);
            dislikeBtn = itemView.findViewById(R.id.dislike_ib);
            mItemView = itemView;

            likeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!isSwiped) {
                        mLikeDislikeListener.OnLikeListener(mItemView,getAdapterPosition());
                    }
                    isSwiped=true;
                }
            });
            dislikeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        mLikeDislikeListener.OnDislikeListener(mItemView,getAdapterPosition());

                }
            });

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
    public void onBindViewHolder(@NonNull final SwipeViewHolder holder, int position) {
        Profile currentProfile=mProfiles.get(position);
        Glide.with(mContext).load(currentProfile.getProfilePictureUri()).error(R.drawable.man_profile).into(holder.mProfileIv);
        holder.mNameTv.setText(currentProfile.getFirstName()+",");
        holder.mAgeTv.setText((int)currentProfile.getAge()+"");
        if(currentProfile.getCity()!=null)
            holder.mCityTv.setText(currentProfile.getCity());


        if(mCategories.size() != 0)
        {
            CompabilityCalculator compabilityCalculator = new CompabilityCalculator(mCategories,mProfile.getQuestionResponds(),getmProfiles().get(position).getQuestionResponds());
            holder.mCompabilityTv.setText(compabilityCalculator.getCompability()+"%");
            holder.mCompabilityTv.setVisibility(View.VISIBLE);
        }
        else {
            holder.mCompabilityTv.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mProfiles.size();
    }
}
