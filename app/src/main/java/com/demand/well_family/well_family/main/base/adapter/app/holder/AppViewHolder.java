package com.demand.well_family.well_family.main.base.adapter.app.holder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dto.App;
import com.demand.well_family.well_family.memory_sound.SongMainActivity;

import java.util.ArrayList;

/**
 * Created by Dev-0 on 2017-04-12.
 */

public class AppViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView tv_app_name;
    private ImageView iv_app_img;
    private Context context;
    private ArrayList<App> appList;

    public AppViewHolder(View itemView, Context context, ArrayList<App> appList) {
        super(itemView);
        this.context = context;
        this.appList = appList;

        tv_app_name = (TextView) itemView.findViewById(R.id.tv_app_name);
        iv_app_img = (ImageView) itemView.findViewById(R.id.iv_app_img);

        iv_app_img.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        if (getAdapterPosition() == 0) {
            Intent intent = new Intent(context, SongMainActivity.class);
            context.startActivity(intent);
        } else if (getAdapterPosition() == 1) {
            //셀핏
        } else {
            Intent startLink = context.getPackageManager().getLaunchIntentForPackage(appList.get(getAdapterPosition()).getPackageName());
            if (startLink == null) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appList.get(getAdapterPosition()).getPackageName() + "")));
            } else {
                context.startActivity(startLink);
            }
        }
    }
}