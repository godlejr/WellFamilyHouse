package com.demand.well_family.well_family.main.base.adapter.family.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demand.well_family.well_family.R;

/**
 * Created by Dev-0 on 2017-04-12.
 */

public class FamilyViewHolder extends RecyclerView.ViewHolder  {

    private LinearLayout ll_container;
    private ImageView iv_family_item;
    private TextView tv_family_name;

    public FamilyViewHolder(View itemView) {
        super(itemView);

        ll_container = (LinearLayout)itemView.findViewById(R.id.ll_container);
        iv_family_item = (ImageView) itemView.findViewById(R.id.iv_family_item);
        tv_family_name = (TextView) itemView.findViewById(R.id.tv_family_name);

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

    public LinearLayout getLl_container() {
        return ll_container;
    }

    public void setLl_container(LinearLayout ll_container) {
        this.ll_container = ll_container;
    }
}
