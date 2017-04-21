package com.demand.well_family.well_family.family.managedetail.adapter;

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
import com.demand.well_family.well_family.dto.UserInfoForFamilyJoin;
import com.demand.well_family.well_family.family.managedetail.presenter.ManageFamilyDetailPresenter;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Dev-0 on 2017-04-21.
 */

public class UserForFamilyJoinAdapter extends RecyclerView.Adapter<UserForFamilyJoinAdapter.UserForFamilyJoinViewHolder> {
    private ManageFamilyDetailPresenter manageFamilyDetailPresenter;

    private ArrayList<UserInfoForFamilyJoin> userList;
    private Context context;
    private int layout;

    public UserForFamilyJoinAdapter(ArrayList<UserInfoForFamilyJoin> userList, Context context, int layout, ManageFamilyDetailPresenter manageFamilyDetailPresenter) {
        this.userList = userList;
        this.context = context;
        this.layout = layout;
        this.manageFamilyDetailPresenter = manageFamilyDetailPresenter;
    }

    @Override
    public UserForFamilyJoinViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        UserForFamilyJoinViewHolder viewHolder = new UserForFamilyJoinViewHolder(LayoutInflater.from(context).inflate(layout, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(UserForFamilyJoinViewHolder holder, final int joiner_position) {
        Glide.with(context).load(context.getString(R.string.cloud_front_user_avatar) + userList.get(joiner_position).getAvatar()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_manage_family_join_avatar);
        holder.tv_manage_family_join_name.setText(userList.get(joiner_position).getName());

        manageFamilyDetailPresenter.onLoadUserForFamilyJoin(holder, userList.get(joiner_position));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void setUserForFamilyJoinDelete(int position){
        userList.remove(position);
    }

    public void setUserForFamilyJoinAgree(UserForFamilyJoinViewHolder holder, String message) {
        holder.btn_manage_family_join.setText(message);
        holder.btn_manage_family_join.setBackgroundResource(R.drawable.round_corner_border_green_r30);
        holder.btn_manage_family_join.setTextColor(Color.parseColor("#51BD86"));
    }

    public void setUserForFamilyJoinHold(UserForFamilyJoinViewHolder holder, String message) {
        holder.btn_manage_family_join.setText(message);
        holder.btn_manage_family_join.setBackgroundResource(R.drawable.round_corner_border_gray_r30);
        holder.btn_manage_family_join.setTextColor(Color.parseColor("#999999"));
    }

    public class UserForFamilyJoinViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CircleImageView iv_manage_family_join_avatar;
        private TextView tv_manage_family_join_name;
        private Button btn_manage_family_join;

        public UserForFamilyJoinViewHolder(View itemView) {
            super(itemView);

            iv_manage_family_join_avatar = (CircleImageView) itemView.findViewById(R.id.iv_manage_family_join_avatar);
            tv_manage_family_join_name = (TextView) itemView.findViewById(R.id.tv_manage_family_join_name);
            btn_manage_family_join = (Button) itemView.findViewById(R.id.btn_manage_family_join);

            btn_manage_family_join.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_manage_family_join:
                    int position = getAdapterPosition();
                    UserInfoForFamilyJoin userInfoForFamilyJoin = userList.get(position);
                    userInfoForFamilyJoin.setPosition(position);
                    manageFamilyDetailPresenter.onClickFamilyJoin(userInfoForFamilyJoin);
                    break;
            }
        }
    }
}
