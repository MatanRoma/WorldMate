package com.example.androidsecondproject.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.androidsecondproject.R;

public class ProfilePicturesPagerAdapter extends PagerAdapter {
    Context context;
    String images[];
    LayoutInflater layoutInflater;

    private ProfilePagerClickListener profilePagerClick;

    public interface ProfilePagerClickListener
    {
        void onRightClickListener(int position);
        void onLeftClickListener(int position);
        void onPictureLongClickListener(String uri);
    }

    public void setProfilePagerClick(ProfilePagerClickListener profilePagerClick) {
        this.profilePagerClick = profilePagerClick;
    }

    public ProfilePicturesPagerAdapter(Context context, String[] images) {
        this.context = context;
        this.images = images;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.picture_pager_item,container,false);
        ImageView imageView = (ImageView)itemView.findViewById(R.id.profile_image_item);
        Glide.with(context).load(images[position]).into(imageView);
        Button leftBtn = itemView.findViewById(R.id.left_btn);
        Button rightBtn = itemView.findViewById(R.id.right_btn);

        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profilePagerClick.onLeftClickListener(position);
            }
        });

        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "click right", Toast.LENGTH_SHORT).show();
                profilePagerClick.onRightClickListener(position);
            }
        });

        rightBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                profilePagerClick.onPictureLongClickListener(images[position]);
                return true;
            }
        });
        leftBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                profilePagerClick.onPictureLongClickListener(images[position]);
                return true;
            }
        });
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ((ViewPager) container).removeView((View) object);
    }
}
