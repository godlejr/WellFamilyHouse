package com.demand.well_family.well_family.main.base.adapter.app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.demand.well_family.well_family.dto.App;
import com.demand.well_family.well_family.main.base.adapter.app.holder.AppViewHolder;

import java.util.ArrayList;

/**
 * Created by Dev-0 on 2017-04-12.
 */

public class AppAdapter extends RecyclerView.Adapter<AppViewHolder> {
    private ArrayList<App> appList;
    private Context context;
    private int layout;

    public AppAdapter(ArrayList<App> appList, Context context, int layout) {
        this.appList = appList;
        this.context = context;
        this.layout = layout;
    }

    @Override
    public AppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AppViewHolder appsViewHolder = new AppViewHolder(LayoutInflater.from(context).inflate(layout, parent, false), context, appList);
        return appsViewHolder;
    }

    @Override
    public void onBindViewHolder(AppViewHolder holder, int position) {
        holder.getTv_app_name().setText(appList.get(position).getName());
        holder.getIv_app_img().setImageResource(appList.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }
}
