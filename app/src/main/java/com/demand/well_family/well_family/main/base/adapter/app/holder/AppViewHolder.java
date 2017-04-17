package com.demand.well_family.well_family.main.base.adapter.app.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.demand.well_family.well_family.R;

/**
 * Created by Dev-0 on 2017-04-12.
 */

public class AppViewHolder extends RecyclerView.ViewHolder{
    private TextView tv_app_name;
    private ImageView iv_app_img;


    public AppViewHolder(View itemView) {
        super(itemView);
        tv_app_name = (TextView) itemView.findViewById(R.id.tv_app_name);
        iv_app_img = (ImageView) itemView.findViewById(R.id.iv_app_img);
    }

    public TextView getTv_app_name() {
        return tv_app_name;
    }

    public void setTv_app_name(TextView tv_app_name) {
        this.tv_app_name = tv_app_name;
    }

    public ImageView getIv_app_img() {
        return iv_app_img;
    }

    public void setIv_app_img(ImageView iv_app_img) {
        this.iv_app_img = iv_app_img;
    }

}