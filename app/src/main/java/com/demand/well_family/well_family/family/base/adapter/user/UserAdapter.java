package com.demand.well_family.well_family.family.base.adapter.user;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.family.base.presenter.FamilyPresenter;

import java.util.ArrayList;

/**
 * Created by Dev-0 on 2017-04-18.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private FamilyPresenter familyPresenter;
    private Context context;
    private ArrayList<User> userList;
    private int layout;

    public UserAdapter(Context context, ArrayList<User> userList, int layout, FamilyPresenter familyPresenter) {
        this.context = context;
        this.userList = userList;
        this.layout = layout;
        this.familyPresenter = familyPresenter;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        UserViewHolder userViewHolder = new UserViewHolder(LayoutInflater.from(context).inflate(layout, parent, false));
        return userViewHolder;
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        Glide.with(context).load(context.getString(R.string.cloud_front_user_avatar) + userList.get(position).getAvatar()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_user_item);
        holder.tv_user_name.setText(familyPresenter.validateUserIdentification(userList.get(position)));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }


    public class UserViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_user_item;
        private TextView tv_user_name;

        public UserViewHolder(View itemView) {
            super(itemView);
            iv_user_item = (ImageView) itemView.findViewById(R.id.iv_family_item);
            tv_user_name = (TextView) itemView.findViewById(R.id.tv_family_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User user = new User();
                    user.setId(userList.get(getAdapterPosition()).getId());
                    user.setEmail(userList.get(getAdapterPosition()).getEmail());
                    user.setName(userList.get(getAdapterPosition()).getName());
                    user.setPhone(userList.get(getAdapterPosition()).getPhone());
                    user.setBirth(userList.get(getAdapterPosition()).getBirth());
                    user.setAvatar(userList.get(getAdapterPosition()).getAvatar());
                    user.setLevel(userList.get(getAdapterPosition()).getLevel());

                    familyPresenter.onClickUser(user);
                }
            });
        }
    }

}
