package com.demand.well_family.well_family.family.search.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dto.FamilyInfoForFamilyJoin;
import com.demand.well_family.well_family.family.base.activity.FamilyActivity;
import com.demand.well_family.well_family.family.search.presenter.SearchFamilyPresenter;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ㅇㅇ on 2017-05-24.
 */

public class FamilyAdapter extends RecyclerView.Adapter<FamilyAdapter.FamilyViewHolder> {
    private SearchFamilyPresenter searchFamilyPresenter;

    private ArrayList<FamilyInfoForFamilyJoin> familyList;
    private int layout;
    private Context context;

    public FamilyAdapter(ArrayList<FamilyInfoForFamilyJoin> familyList, int layout, Context context, SearchFamilyPresenter searchFamilyPresenter) {
        this.searchFamilyPresenter = searchFamilyPresenter;
        this.familyList = familyList;
        this.layout = layout;
        this.context = context;
    }

    @Override
    public FamilyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FamilyViewHolder viewHolder = new FamilyViewHolder(LayoutInflater.from(context).inflate(layout, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final FamilyViewHolder holder, final int position) {
        FamilyInfoForFamilyJoin family = familyList.get(position);
        Glide.with(context).load(context.getString(R.string.cloud_front_family_avatar) + family.getAvatar()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_search_family_avatar);
        holder.tv_search_family_name.setText(family.getName());
        holder.tv_search_family_content.setText(family.getContent());
        int joinFlag = family.getJoin_flag();

        searchFamilyPresenter.setFamilyStateButton(holder, family, joinFlag);
    }

    @Override
    public int getItemCount() {
        return familyList.size();
    }


    public class FamilyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private LinearLayout ll_search_family;
        private TextView tv_search_family_name;
        private TextView tv_search_family_content;
        public Button btn_search_family_commit;
        private CircleImageView iv_search_family_avatar;

        public FamilyViewHolder(View itemView) {
            super(itemView);

            iv_search_family_avatar = (CircleImageView) itemView.findViewById(R.id.iv_search_family_avatar);
            tv_search_family_name = (TextView) itemView.findViewById(R.id.tv_search_family_name);
            tv_search_family_content = (TextView) itemView.findViewById(R.id.tv_search_family_content);
            btn_search_family_commit = (Button) itemView.findViewById(R.id.btn_search_family_commit);
            ll_search_family = (LinearLayout) itemView.findViewById(R.id.ll_search_family);

            ll_search_family.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_search_family:
                    Intent intent = new Intent(v.getContext(), FamilyActivity.class);

                    intent.putExtra("family_id", familyList.get(getAdapterPosition()).getId());
                    intent.putExtra("family_name", familyList.get(getAdapterPosition()).getName());
                    intent.putExtra("family_content", familyList.get(getAdapterPosition()).getContent());
                    intent.putExtra("family_avatar", familyList.get(getAdapterPosition()).getAvatar());
                    intent.putExtra("family_user_id", familyList.get(getAdapterPosition()).getUser_id());
                    intent.putExtra("family_created_at", familyList.get(getAdapterPosition()).getCreated_at());

                    context.startActivity(intent);
                    break;
            }
        }
    }

    public void setFamilyStateButtonForMe(FamilyAdapter.FamilyViewHolder holder) {
        holder.btn_search_family_commit.setText("가족");
        holder.btn_search_family_commit.setBackgroundResource(R.drawable.round_corner_green_r30);
        holder.btn_search_family_commit.setTextColor(Color.parseColor("#ffffff"));
    }


    public void setFamilyStateButtonForStay(FamilyAdapter.FamilyViewHolder holder) {
        holder.btn_search_family_commit.setText("승인 대기");
        holder.btn_search_family_commit.setBackgroundResource(R.drawable.round_corner_border_gray_r30);
        holder.btn_search_family_commit.setTextColor(Color.parseColor("#999999"));
        holder.btn_search_family_commit.setClickable(false);
    }


    public void setFamilyStateButtonForJoin(final FamilyAdapter.FamilyViewHolder holder, final FamilyInfoForFamilyJoin familyFound) {
        holder.btn_search_family_commit.setText("가입하기");
        holder.btn_search_family_commit.setBackgroundResource(R.drawable.round_corner_border_green_r30);
        holder.btn_search_family_commit.setTextColor(Color.parseColor("#51BD86"));

        holder.btn_search_family_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchFamilyPresenter.onClickSetUserJoinFamily(familyFound, holder);
            }
        });
    }
}