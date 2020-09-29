package com.example.androidsecondproject.model;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.androidsecondproject.R;

import java.util.List;

public class SwipeFlingAdapter extends BaseAdapter {
    private List<Profile> mProfiles;
    private List<String> mCategories;
    private Profile mProfile;
    private Context mContext;

    public SwipeFlingAdapter(List<Profile> mProfiles, Profile mProfile,Context context) {
        this.mProfiles = mProfiles;
        this.mProfile = mProfile;
        this.mContext=context;

    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return mProfiles.size();
    }

    @Override
    public Object getItem(int position) {
        return mProfiles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Log.d("fling","test1");
        if(view ==null)
        {
            Log.d("fling","test2");
            LayoutInflater layoutInflater =(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.profile_card,parent,false);
        }

        TextView nameTv=view.findViewById(R.id.card_name_tv);
        ImageView profileIv=view.findViewById(R.id.card_profile_iv);
        TextView compabilityTv = view.findViewById(R.id.compability_tv);
        TextView cityTv=view.findViewById(R.id.location_tv);
        TextView ageTv=view.findViewById(R.id.card_age_tv);

        Profile currentProfile=mProfiles.get(position);

        Glide.with(mContext).load(currentProfile.getProfilePictureUri()).error(R.drawable.man_profile).into(profileIv);
        nameTv.setText(currentProfile.getFirstName()+",");
        ageTv.setText((int)currentProfile.getAge()+"");
        if(currentProfile.getCity()!=null)
            cityTv.setText(currentProfile.getCity());


        return view;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return mProfiles.isEmpty();
    }

}
