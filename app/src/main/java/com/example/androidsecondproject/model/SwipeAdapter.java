package com.example.androidsecondproject.model;

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

    public interface ProfilePressedListener
    {
        void OnProfiledPressedListener(Profile otherProfile,int compability);
    }

    private ProfilePressedListener mProfilePressedListener;

    public void setProfiledPressedListener(ProfilePressedListener likeDislikeItemListener)
    {
        this.mProfilePressedListener = likeDislikeItemListener;
    }

    public void removeTopItem() {
        mProfiles.remove(0);
        notifyItemRemoved(0);

    }

    public class SwipeViewHolder extends RecyclerView.ViewHolder {
        TextView mNameTv;
        ImageView mProfileIv;
        TextView mCompabilityTv;
        TextView mCityTv;
        TextView mAgeTv;

        int mCompability;
        /*        ImageButton likeBtn;
                ImageButton dislikeBtn;*/
        View mItemView;
        boolean isSwiped;

        public SwipeViewHolder(@NonNull View itemView) {
            super(itemView);
            mNameTv=itemView.findViewById(R.id.card_name_tv);
            mProfileIv=itemView.findViewById(R.id.card_profile_iv);
            mCompabilityTv = itemView.findViewById(R.id.compability_tv);
            mCityTv=itemView.findViewById(R.id.location_tv);
            mAgeTv=itemView.findViewById(R.id.card_age_tv);
/*            likeBtn = itemView.findViewById(R.id.like_ib);
            dislikeBtn = itemView.findViewById(R.id.dislike_ib);*/
            mItemView = itemView;

            mItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mProfilePressedListener.OnProfiledPressedListener(mProfiles.get(getAdapterPosition()),mCompability);               }
            });

/*            likeBtn.setOnClickListener(new View.OnClickListener() {
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
            });*/

        }
    }

    private List<Profile> mProfiles;
    private List<String> mCategories;
    private  Profile mMyProfile;
    private Context mContext;
    private boolean mIsGuestLogin;

    public List<String> getmCategories() {
        return mCategories;
    }

    public void setmCategories(List<String> mCategories) {
        this.mCategories = mCategories;
    }

    public SwipeAdapter(List<Profile> profiles, Context context, Profile profile, List<String> categories){
        this.mProfiles=profiles;
        this.mContext=context;
        this.mMyProfile = profile;
        this.mCategories = categories;
    }
    public SwipeAdapter(List<Profile> profiles, Context context, List<String> categories){
        this.mProfiles=profiles;
        this.mContext=context;
        mIsGuestLogin=true;
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
        //View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_card,parent,false);
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.swipe_profile_card,parent,false);
        SwipeViewHolder swipeViewHolder=new SwipeViewHolder(view);
        return swipeViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final SwipeViewHolder holder, int position) {
        Profile currentProfile=mProfiles.get(position);
        if(TranslateString.checkMale(currentProfile.getGender()))
            {
            Glide.with(mContext).load(currentProfile.getProfilePictureUri()).error(R.drawable.man_profile).into(holder.mProfileIv);
        }
        else
        {
            Glide.with(mContext).load(currentProfile.getProfilePictureUri()).error(R.drawable.woman_profile_strech).into(holder.mProfileIv);
        }
        holder.mNameTv.setText(currentProfile.getFirstName()+", "+(int)currentProfile.calculateCurrentAge());
        //holder.mAgeTv.setText((int)currentProfile.calculateCurrentAge()+"");
        if(currentProfile.getCity()!=null)
            holder.mCityTv.setText(currentProfile.getCity());


        if(mCategories.size() != 0&&!mIsGuestLogin)
        {

            CompabilityCalculator compabilityCalculator = new CompabilityCalculator(mCategories, mMyProfile.getQuestionResponds(),getmProfiles().get(position).getQuestionResponds());
            holder.mCompability = compabilityCalculator.getCompability();
            if(holder.mCompability != 0)
            {
                holder.mCompabilityTv.setText(holder.mCompability+"%");
                holder.mCompabilityTv.setVisibility(View.VISIBLE);
                holder.mCompabilityTv.setVisibility(View.VISIBLE);
            }

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
