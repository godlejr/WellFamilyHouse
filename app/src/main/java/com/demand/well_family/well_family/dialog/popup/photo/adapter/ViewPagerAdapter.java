package com.demand.well_family.well_family.dialog.popup.photo.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dialog.popup.photo.activity.PhotoPopupActivity;
import com.demand.well_family.well_family.dialog.popup.photo.presenter.PhotoPopupPresenter;
import com.demand.well_family.well_family.dto.Photo;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-04-19.
 */

public class ViewPagerAdapter extends PagerAdapter implements View.OnTouchListener{
    private PhotoPopupPresenter photoPopupPresenter;

    private LayoutInflater inflater;
    private TextView tv_viewPager_indicator;
    private ArrayList<Photo> photoList;

    public ViewPagerAdapter(LayoutInflater inflater, ArrayList<Photo> photoList, PhotoPopupPresenter photoPopupPresenter) {
        this.inflater = inflater;
        this.photoPopupPresenter = photoPopupPresenter;
        this.photoList = photoList;
    }

    @Override
    public int getCount() {
        return photoList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = inflater.inflate(R.layout.viewpager_childview, null);
        ImageView iv_viewPager_childView = (ImageView) view.findViewById(R.id.iv_viewPager_childView);
        tv_viewPager_indicator = (TextView) view.findViewById(R.id.tv_viewPager_position);

        iv_viewPager_childView.setOnTouchListener(this);
        container.addView(view);


        String imageURL = photoPopupPresenter.getImageURL(position);
        Glide.with(inflater.getContext()).load(imageURL).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_viewPager_childView);


        photoPopupPresenter.setViewPagerIndicator(position);

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
        photoPopupPresenter.setPopupTitleBar();
        return true;
    }

    public void setViewPagerIndicator(String position) {
        tv_viewPager_indicator.setText(position);
    }

}
