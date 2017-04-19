package com.demand.well_family.well_family.main.base.adapter.family;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.main.base.presenter.MainPresenter;

import java.util.ArrayList;

/**
 * Created by Dev-0 on 2017-04-12.
 */

public class FamilyAdapter extends RecyclerView.Adapter<FamilyAdapter.FamilyViewHolder> {
    private MainPresenter mainPresenter;
    private Context context;
    private ArrayList<Family> familyList;
    private int layout;

    public FamilyAdapter(Context context, ArrayList<Family> familyList, int layout, MainPresenter mainPresenter) {
        this.context = context;
        this.familyList = familyList;
        this.layout = layout;
        this.mainPresenter = mainPresenter;
    }

    @Override
    public FamilyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FamilyViewHolder familyViewHolder = new FamilyViewHolder(LayoutInflater.from(context).inflate(layout, parent, false));
        return familyViewHolder;
    }

    @Override
    public void onBindViewHolder(FamilyViewHolder holder, final int position) {
        ImageView familyAvatar = holder.iv_family_item;
        TextView familyName = holder.tv_family_name;

        Glide.with(context).load(context.getString(R.string.cloud_front_family_avatar) + familyList.get(position).getAvatar()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(familyAvatar);
        familyName.setText(familyList.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return familyList.size();
    }

    public class FamilyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private LinearLayout ll_container;
        private ImageView iv_family_item;
        private TextView tv_family_name;

        public FamilyViewHolder(View itemView) {
            super(itemView);

            ll_container = (LinearLayout) itemView.findViewById(R.id.ll_container);
            iv_family_item = (ImageView) itemView.findViewById(R.id.iv_family_item);
            tv_family_name = (TextView) itemView.findViewById(R.id.tv_family_name);

            ll_container.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_container:
                    int position = getAdapterPosition();
                    Family family = new Family();

                    family.setId(familyList.get(position).getId());
                    family.setName(familyList.get(position).getName());
                    family.setContent(familyList.get(position).getContent());
                    family.setAvatar(familyList.get(position).getAvatar());
                    family.setUser_id(familyList.get(position).getUser_id());
                    family.setCreated_at(familyList.get(position).getCreated_at());

                    mainPresenter.onClickFamily(family);
                    break;
            }
        }
    }
}