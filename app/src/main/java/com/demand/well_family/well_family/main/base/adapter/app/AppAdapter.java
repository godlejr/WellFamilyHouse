package com.demand.well_family.well_family.main.base.adapter.app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dto.App;
import com.demand.well_family.well_family.main.base.presenter.MainPresenter;

import java.util.ArrayList;

/**
 * Created by Dev-0 on 2017-04-12.
 */

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.AppViewHolder> {
    private MainPresenter mainPresenter;

    private ArrayList<App> appList;
    private Context context;
    private int layout;

    public AppAdapter(ArrayList<App> appList, Context context, int layout, MainPresenter mainPresenter) {
        this.appList = appList;
        this.context = context;
        this.layout = layout;
        this.mainPresenter = mainPresenter;
    }

    @Override
    public AppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AppViewHolder appsViewHolder = new AppViewHolder(LayoutInflater.from(parent.getContext()).inflate(layout, parent, false));
        return appsViewHolder;
    }

    @Override
    public void onBindViewHolder(AppViewHolder holder, final int position) {
        TextView tv_app_name = holder.tv_app_name;
        ImageView iv_app_img = holder.iv_app_img;

        tv_app_name.setText(appList.get(position).getName());
        iv_app_img.setImageResource(appList.get(position).getImage());

        iv_app_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == 0) {
                    mainPresenter.onClickSongMain();
                } else if (position == 1) {
                    //셀핏
                } else {
                    mainPresenter.onClickAppGames(appList.get(position).getPackageName());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }


    public class AppViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_app_name;
        private ImageView iv_app_img;


        public AppViewHolder(View itemView) {
            super(itemView);
            tv_app_name = (TextView) itemView.findViewById(R.id.tv_app_name);
            iv_app_img = (ImageView) itemView.findViewById(R.id.iv_app_img);
        }

    }
}
