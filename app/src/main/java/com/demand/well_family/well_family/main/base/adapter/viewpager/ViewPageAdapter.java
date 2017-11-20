package com.demand.well_family.well_family.main.base.adapter.viewpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.main.base.presenter.MainPresenter;

/**
 * Created by Dev-0 on 2017-04-12.
 */

public class ViewPageAdapter extends PagerAdapter {
    private MainPresenter mainPresenter;
    private LayoutInflater inflater;
    private Context context;

    public ViewPageAdapter(LayoutInflater inflater, Context context, MainPresenter mainPresenter) {
        this.inflater = inflater;
        this.context = context;
        this.mainPresenter = mainPresenter;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view = inflater.inflate(R.layout.viewpager_childview, null);
        ImageView img = (ImageView) view.findViewById(R.id.iv_viewPager_childView);

        if (position == 0) {
            Glide.with(context).load(R.drawable.demand_banner0).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(img);
        } else if (position == 1) {
            Glide.with(context).load(R.drawable.demand_banner1).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(img);
        } else if (position == 2) {
            Glide.with(context).load(context.getString(R.string.cloud_front_banners) + "demand_banner2.jpg").thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(img);
        } else if (position == 3) {
            Glide.with(context).load(context.getString(R.string.cloud_front_banners) + "demand_banner3.jpg").thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(img);
        }


        TextView tv_viewPager_position = (TextView) view.findViewById(R.id.tv_viewPager_position);
        tv_viewPager_position.setText("");

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
}