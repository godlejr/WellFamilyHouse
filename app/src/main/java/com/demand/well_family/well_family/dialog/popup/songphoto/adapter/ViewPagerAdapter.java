package com.demand.well_family.well_family.dialog.popup.songphoto.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dialog.popup.songphoto.presenter.SongPhotoPresenter;
import com.demand.well_family.well_family.dto.SongPhoto;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-05-22.
 */

public class ViewPagerAdapter extends PagerAdapter implements View.OnTouchListener {
    private SongPhotoPresenter songPhotoPresenter;
    private LayoutInflater inflater;
    private ArrayList<SongPhoto> photoList;

    public ViewPagerAdapter(LayoutInflater inflater, ArrayList<SongPhoto> photoList, SongPhotoPresenter songPhotoPresenter) {
        this.photoList = photoList;
        this.inflater = inflater;
        this.songPhotoPresenter = songPhotoPresenter;
    }

    @Override
    public int getCount() {
        return photoList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = inflater.inflate(R.layout.viewpager_childview, null);
        ImageView iv_viewPager_childView = (ImageView) view.findViewById(R.id.iv_viewPager_childView);
        TextView tv_viewPager_position = (TextView) view.findViewById(R.id.tv_viewPager_position);

        iv_viewPager_childView.setOnTouchListener(this);

        Context context = container.getContext();
        String imageURL = context.getString(R.string.cloud_front_song_stories_images) + photoList.get(position).getName() + "." + photoList.get(position).getExt();
        Glide.with(context).load(imageURL).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_viewPager_childView);


        int photoListSize = photoList.size();
        String viewPager_position = (position + 1) + " / " + photoListSize;
        tv_viewPager_position.setText(viewPager_position);

        container.addView(view);

        return view;
    }

    @Override
    public boolean isViewFromObject(View v, Object obj) {
        return v == obj;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.iv_viewPager_childView:
                songPhotoPresenter.onTouch(event);
                break;
        }

        return true;

    }
}
