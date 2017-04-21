package com.demand.well_family.well_family.family.manage.adapter.owner;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.family.manage.presenter.ManageFamilyPresenter;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Dev-0 on 2017-04-20.
 */

public class FamilyForOwnerAdapter extends RecyclerView.Adapter<FamilyForOwnerAdapter.FamilyForOwnerViewHolder> {
    private ManageFamilyPresenter manageFamilyPresenter;

    private ArrayList<Family> familyList;
    private int layout;
    private Context context;

    public FamilyForOwnerAdapter(ArrayList<Family> familyList, int layout, Context context, ManageFamilyPresenter manageFamilyPresenter) {
        this.familyList = familyList;
        this.layout = layout;
        this.context = context;
        this.manageFamilyPresenter = manageFamilyPresenter;
    }

    @Override
    public FamilyForOwnerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FamilyForOwnerViewHolder viewHolder = new FamilyForOwnerViewHolder(LayoutInflater.from(context).inflate(layout, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FamilyForOwnerViewHolder holder, final int position) {
        holder.tv_manage_family_owner.setText(familyList.get(position).getName());
        Glide.with(context).load(context.getString(R.string.cloud_front_family_avatar) + familyList.get(position).getAvatar()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_manage_family_owner);
    }

    @Override
    public int getItemCount() {
        return familyList.size();
    }

    public void setFamilyForOwnerDelete(int position){
        familyList.remove(position);
    }


    public class FamilyForOwnerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CircleImageView iv_manage_family_owner;
        private TextView tv_manage_family_owner;

        public FamilyForOwnerViewHolder(View itemView) {
            super(itemView);
            iv_manage_family_owner = (CircleImageView) itemView.findViewById(R.id.iv_manage_family_owner);
            tv_manage_family_owner = (TextView) itemView.findViewById(R.id.tv_manage_family_owner);

            iv_manage_family_owner.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_manage_family_owner:

                    int position  = getAdapterPosition();
                    Family family = familyList.get(position);
                    family.setPosition(position);

                    manageFamilyPresenter.onClickFamilyForOwner(family);
                    break;
            }
        }
    }
}
