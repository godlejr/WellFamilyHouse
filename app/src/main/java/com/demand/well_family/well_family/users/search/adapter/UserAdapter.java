package com.demand.well_family.well_family.users.search.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dto.UserInfoForFamilyJoin;
import com.demand.well_family.well_family.users.base.activity.UserActivity;
import com.demand.well_family.well_family.users.search.presenter.SearchUserPresenter;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-05-23.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private SearchUserPresenter searchUserPresenter;

    private ArrayList<UserInfoForFamilyJoin> userList;
    private Context context;
    private int layout;

    public UserAdapter(ArrayList<UserInfoForFamilyJoin> userList, Context context, int layout, SearchUserPresenter searchUserPresenter) {
        this.userList = userList;
        this.context = context;
        this.layout = layout;
        this.searchUserPresenter = searchUserPresenter;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        UserViewHolder userViewHolder = new UserViewHolder(LayoutInflater.from(context).inflate(layout, parent, false));
        return userViewHolder;
    }

    @Override
    public void onBindViewHolder(final UserViewHolder holder, final int position) {
        UserInfoForFamilyJoin userFound = userList.get(position);
        Glide.with(context).load(context.getString(R.string.cloud_front_user_avatar) + userFound.getAvatar()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_search_user_avatar);
        holder.tv_search_user_name.setText(userFound.getName());
        holder.tv_search_user_birth.setText(userFound.getBirth());
        int joinFlag = userFound.getJoin_flag();

        searchUserPresenter.setUserStateButton(holder, userFound,joinFlag);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_search_user_name;
        TextView tv_search_user_birth;
        ImageView iv_search_user_avatar;
        Button btn_search_user;

        public UserViewHolder(View itemView) {
            super(itemView);

            tv_search_user_name = (TextView) itemView.findViewById(R.id.tv_search_user_name);
            tv_search_user_birth = (TextView) itemView.findViewById(R.id.tv_search_user_birth);

            iv_search_user_avatar = (ImageView) itemView.findViewById(R.id.iv_search_user_avatar);
            btn_search_user = (Button) itemView.findViewById(R.id.btn_search_user);

            tv_search_user_name.setOnClickListener(this);
            iv_search_user_avatar.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_search_user_name:
                case R.id.iv_search_user_avatar:
                    Intent intent = new Intent(context, UserActivity.class);

                    //userinfo
                    intent.putExtra("story_user_id", userList.get(getAdapterPosition()).getId());
                    intent.putExtra("story_user_email", userList.get(getAdapterPosition()).getEmail());
                    intent.putExtra("story_user_birth", userList.get(getAdapterPosition()).getBirth());
                    intent.putExtra("story_user_phone", userList.get(getAdapterPosition()).getPhone());
                    intent.putExtra("story_user_name", userList.get(getAdapterPosition()).getName());
                    intent.putExtra("story_user_level", userList.get(getAdapterPosition()).getLevel());
                    intent.putExtra("story_user_avatar", userList.get(getAdapterPosition()).getAvatar());

                    context.startActivity(intent);
                    break;
            }
        }
    }

    public void setUserStateButtonForFamily(UserViewHolder holder) {
        holder.btn_search_user.setText("가족");
        holder.btn_search_user.setTextColor(Color.parseColor("#999999"));
        holder.btn_search_user.setBackgroundResource(R.drawable.round_corner_border_gray_r30);
    }

    public void setUserStateButtonForJoin(UserViewHolder holder, final UserInfoForFamilyJoin user) {
        holder.btn_search_user.setText("가입 승인");
        holder.btn_search_user.setTextColor(Color.parseColor("#999999"));
        holder.btn_search_user.setBackgroundResource(R.drawable.round_corner_border_gray_r30);
        holder.btn_search_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchUserPresenter.onClickSetUserJoinFamily(user);
            }
        });
    }
    public void setUserStateButtonForStay(UserViewHolder holder){
        holder.btn_search_user.setText("요청 대기");
        holder.btn_search_user.setTextColor(Color.parseColor("#999999"));
        holder.btn_search_user.setBackgroundResource(R.drawable.round_corner_border_gray_r30);
        holder.btn_search_user.setClickable(false);
    }

    public void setUserStateButtonForMe(UserViewHolder holder) {
        holder.btn_search_user.setText("나");
        holder.btn_search_user.setTextColor(Color.parseColor("#542920"));
        holder.btn_search_user.setBackgroundResource(R.drawable.round_corner_border_gray_r30);
    }

    public void setUserStateButtonForInvite(final UserViewHolder holder, final UserInfoForFamilyJoin user) {
        holder.btn_search_user.setText("초대하기");
        holder.btn_search_user.setTextColor(Color.parseColor("#542920"));
        holder.btn_search_user.setBackgroundResource(R.drawable.round_corner_border_brown_r30);
        holder.btn_search_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchUserPresenter.onClickSetUserInvited(user, holder);
            }
        });
    }



}

