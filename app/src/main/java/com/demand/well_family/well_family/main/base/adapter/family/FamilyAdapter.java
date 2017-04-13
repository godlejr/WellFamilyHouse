package com.demand.well_family.well_family.main.base.adapter.family;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.main.base.adapter.family.holder.FamilyViewHolder;

import java.util.ArrayList;

/**
 * Created by Dev-0 on 2017-04-12.
 */

public class FamilyAdapter extends RecyclerView.Adapter<FamilyViewHolder> {
    private Context context;
    private ArrayList<Family> familyList;
    private int layout;

    public FamilyAdapter(Context context, ArrayList<Family> familyList, int layout) {
        this.context = context;
        this.familyList = familyList;
        this.layout = layout;
    }

    @Override
    public FamilyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FamilyViewHolder familyViewHolder = new FamilyViewHolder(LayoutInflater.from(context).inflate(layout, parent, false), context, familyList);
        return familyViewHolder;
    }

    @Override
    public void onBindViewHolder(FamilyViewHolder holder, int position) {
        ImageView family_avatar = holder.getIv_family_item();
        TextView family_name = holder.getTv_family_name();

        Glide.with(context).load(context.getString(R.string.cloud_front_family_avatar) + familyList.get(position).getAvatar()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(family_avatar);
        family_name.setText(familyList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return familyList.size();
    }
}