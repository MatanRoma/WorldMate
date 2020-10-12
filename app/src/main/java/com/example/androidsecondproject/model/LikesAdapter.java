package com.example.androidsecondproject.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.androidsecondproject.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class LikesAdapter extends RecyclerView.Adapter<LikesAdapter.LikesViewHolder> implements Filterable {
    private List<Profile> mLikes;
    private List<Profile> mAllLikes;
    private Profile mMyProfile;
    private Context mContext;
    private LikedProfilePressedListener profilePressedListener;

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    public interface LikedProfilePressedListener
    {
        void OnLikedProfiledPressedListener(Profile likedProfile);
    }

    public void setProfilePressedListener(LikedProfilePressedListener profilePressedListener) {
        this.profilePressedListener = profilePressedListener;
    }

    public LikesAdapter(List<Profile> mLikes, Profile myProfile, Context context) {
        this.mLikes = mLikes;
        this.mMyProfile=myProfile;
        this.mContext=context;
        this.mAllLikes = new ArrayList<>(mLikes);
    }



    private Filter mFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Profile> filteredList = new ArrayList<>();
            if(constraint.toString().isEmpty()){
                filteredList.addAll(mAllLikes);
            }
            else {
                for(Profile profile : mAllLikes){
                    if(profile.getFirstName().toLowerCase().contains(constraint.toString().toLowerCase())){
                        filteredList.add(profile);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mLikes.clear();
            mLikes.addAll((Collection<? extends Profile>) results.values);
            notifyDataSetChanged();
        }
    };

    @NonNull
    @Override
    public LikesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_card,parent,false);
        LikesViewHolder likesViewHolder=new LikesViewHolder(view);
        return likesViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LikesViewHolder holder, int position) {
        Profile likedProfile=mLikes.get(position);
        holder.mNameTv.setText(likedProfile.getFirstName()+", "+likedProfile.calculateCurrentAge());
        //holder.mAgeTv.setText(", "+likedProfile.calculateCurrentAge()+"");
        holder.mCityTv.setText(likedProfile.getCity());
        Glide.with(mContext).load(likedProfile.getProfilePictureUri()).error(TranslateString.checkMale(likedProfile.getGender())?R.drawable.man_profile:R.drawable.woman_profile).
                into(holder.mProfileIv);
    }

    @Override
    public int getItemCount() {
        return mLikes.size();
    }

    public class LikesViewHolder extends RecyclerView.ViewHolder {
        TextView mNameTv;
        CircleImageView mProfileIv;
        TextView mCompabilityTv;
        TextView mCityTv;
        TextView mAgeTv;
        int mCompability;



    public LikesViewHolder(@NonNull View itemView) {
        super(itemView);
        mNameTv=itemView.findViewById(R.id.card_name_tv);
        mProfileIv=itemView.findViewById(R.id.card_profile_iv);
        mCompabilityTv = itemView.findViewById(R.id.compability_tv);
        mCityTv=itemView.findViewById(R.id.location_tv);
        mAgeTv=itemView.findViewById(R.id.card_age_tv);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profilePressedListener.OnLikedProfiledPressedListener(mLikes.get(getAdapterPosition()));
            }
        });

    }
}

}
