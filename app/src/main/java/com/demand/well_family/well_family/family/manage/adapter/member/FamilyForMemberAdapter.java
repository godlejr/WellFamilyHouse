package com.demand.well_family.well_family.family.manage.adapter.member;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dto.FamilyInfoForFamilyJoin;
import com.demand.well_family.well_family.family.manage.presenter.ManageFamilyPresenter;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Dev-0 on 2017-04-20.
 */

public class FamilyForMemberAdapter  extends RecyclerView.Adapter<FamilyForMemberAdapter.FamilyForMemberViewHolder> {
    private ManageFamilyPresenter manageFamilyPresenter;
    private ArrayList<FamilyInfoForFamilyJoin> familyInfoForFamilyJoinList;
    private int layout;
    private Context context;

    public FamilyForMemberAdapter(ArrayList<FamilyInfoForFamilyJoin> familyInfoForFamilyJoinList, int layout, Context context,ManageFamilyPresenter manageFamilyPresenter) {
        this.familyInfoForFamilyJoinList = familyInfoForFamilyJoinList;
        this.layout = layout;
        this.context = context;
        this.manageFamilyPresenter = manageFamilyPresenter;
    }

    @Override
    public FamilyForMemberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FamilyForMemberViewHolder viewHolder = new FamilyForMemberViewHolder(LayoutInflater.from(context).inflate(layout, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FamilyForMemberViewHolder holder, final int position) {
        holder.tv_manage_family_member.setText(familyInfoForFamilyJoinList.get(position).getName());
        Glide.with(context).load(context.getString(R.string.cloud_front_family_avatar) + familyInfoForFamilyJoinList.get(position).getAvatar()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_manage_family_member);

        manageFamilyPresenter.onLoadFamilyForMember(holder, familyInfoForFamilyJoinList.get(position));
    }

    @Override
    public int getItemCount() {
        return familyInfoForFamilyJoinList.size();
    }

    public void setFamilyForMemberDelete(int position){
        familyInfoForFamilyJoinList.remove(position);
    }

    public void setFamilyForMemberChangeForJoinFlag(int position, int JoinFlag){
        familyInfoForFamilyJoinList.get(position).setJoin_flag(JoinFlag);
    }

    public void setFamilyForMemberSecession(FamilyForMemberViewHolder holder,String message){
        holder.btn_manage_family_member.setBackgroundResource(R.drawable.round_corner_border_red_r30);
        holder.btn_manage_family_member.setText(message);
        holder.btn_manage_family_member.setTextColor(Color.parseColor("#FF0000"));
    }

    public void setFamilyForMemberHold(FamilyForMemberViewHolder holder,String message){
        holder.btn_manage_family_member.setBackgroundResource(R.drawable.round_corner_border_gray_r30);
        holder.btn_manage_family_member.setText(message);
        holder.btn_manage_family_member.setTextColor(Color.parseColor("#999999"));
    }

    public void setFamilyForMemberAgree(FamilyForMemberViewHolder holder,String message){
        holder.btn_manage_family_member.setBackgroundResource(R.drawable.round_corner_border_green_r30);
        holder.btn_manage_family_member.setText(message);
        holder.btn_manage_family_member.setTextColor(Color.parseColor("#1DDB16"));
    }


    public class FamilyForMemberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CircleImageView iv_manage_family_member;
        private TextView tv_manage_family_member;
        private Button btn_manage_family_member;

        public FamilyForMemberViewHolder(View itemView) {
            super(itemView);

            iv_manage_family_member = (CircleImageView) itemView.findViewById(R.id.iv_manage_family_member);
            tv_manage_family_member = (TextView) itemView.findViewById(R.id.tv_manage_family_member);
            btn_manage_family_member = (Button) itemView.findViewById(R.id.btn_manage_family_member);

            btn_manage_family_member.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_manage_family_member:
                    int position = getAdapterPosition();

                    FamilyInfoForFamilyJoin familyInfoForFamilyJoin = familyInfoForFamilyJoinList.get(position);
                    familyInfoForFamilyJoin.setPosition(position);

                    manageFamilyPresenter.onClickFamilyJoin(familyInfoForFamilyJoin);

                    break;
            }
        }
    }
}
