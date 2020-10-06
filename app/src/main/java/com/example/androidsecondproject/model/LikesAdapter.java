package com.example.androidsecondproject.model;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LikesAdapter extends RecyclerView.Adapter<LikesAdapter.LikesViewHolder> {
    private List<Profile> mProfiles;

    @NonNull
    @Override
    public LikesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull LikesViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mProfiles.size();
    }

    public class LikesViewHolder extends RecyclerView.ViewHolder {
    public LikesViewHolder(@NonNull View itemView) {
        super(itemView);
    }
}

}
