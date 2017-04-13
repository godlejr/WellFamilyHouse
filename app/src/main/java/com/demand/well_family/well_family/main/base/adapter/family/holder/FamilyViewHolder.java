package com.demand.well_family.well_family.main.base.adapter.family.holder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.family.FamilyActivity;

import java.util.ArrayList;

/**
 * Created by Dev-0 on 2017-04-12.
 */

public class FamilyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private Context context;
    private ArrayList<Family> familyList;

    private ImageView iv_family_item;
    private TextView tv_family_name;

    public FamilyViewHolder(View itemView, Context context, ArrayList<Family> familyList) {
        super(itemView);
        this.context = context;
        this.familyList = familyList;

        iv_family_item = (ImageView) itemView.findViewById(R.id.iv_family_item);
        tv_family_name = (TextView) itemView.findViewById(R.id.tv_family_name);
        itemView.setOnClickListener(this);
    }

    public ImageView getIv_family_item() {
        return iv_family_item;
    }

    public void setIv_family_item(ImageView iv_family_item) {
        this.iv_family_item = iv_family_item;
    }

    public TextView getTv_family_name() {
        return tv_family_name;
    }

    public void setTv_family_name(TextView tv_family_name) {
        this.tv_family_name = tv_family_name;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, FamilyActivity.class);

        //family info
        intent.putExtra("family_id", familyList.get(getAdapterPosition()).getId());
        intent.putExtra("family_name", familyList.get(getAdapterPosition()).getName());
        intent.putExtra("family_content", familyList.get(getAdapterPosition()).getContent());
        intent.putExtra("family_avatar", familyList.get(getAdapterPosition()).getAvatar());
        intent.putExtra("family_user_id", familyList.get(getAdapterPosition()).getUser_id());
        intent.putExtra("family_created_at", familyList.get(getAdapterPosition()).getCreated_at());

        context.startActivity(intent);
    }
}
