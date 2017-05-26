package com.demand.well_family.well_family.falldiagnosis.physicalevaluation.base.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.falldiagnosis.physicalevaluation.base.presenter.PhysicalEvaluationPresenter;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-05-26.
 */

public class PhysicalEvaluationAdapter extends RecyclerView.Adapter<PhysicalEvaluationAdapter.PhysicalEvaluationViewHolder> {
    private Context context;
    private ArrayList<String> physicalEvaluationCategoryList;
    private PhysicalEvaluationPresenter physicalEvaluationPresenter;

    public PhysicalEvaluationAdapter(Context context, ArrayList<String> physicalEvaluationCategoryList, PhysicalEvaluationPresenter physicalEvaluationPresenter) {
        this.context = context;
        this.physicalEvaluationCategoryList = physicalEvaluationCategoryList;
        this.physicalEvaluationPresenter = physicalEvaluationPresenter;
    }

    @Override
    public PhysicalEvaluationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.item_physicalevaluation, parent, false);
//        int height = parent.getMeasuredHeight() / 3;
//        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams();
//        layoutParams.setMargins(10, 10, 10, 10);
//        view.setLayoutParams(layoutParams);

        PhysicalEvaluationViewHolder viewHolder = new PhysicalEvaluationViewHolder(LayoutInflater.from(context).inflate(R.layout.item_physicalevaluation, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PhysicalEvaluationViewHolder holder, int position) {
//        Glide.with(context).load(context.getString("cloud_front_") + physicalEvaluationCategoryList.get(position)).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_physicalevaluation_main);
    }

    @Override
    public int getItemCount() {
//        return physicalEvaluationCategoryList.size();
        return 3;
    }

    public class PhysicalEvaluationViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv_physicalevaluation_img;
        public TextView tv_physicalevaluation_content;

        public PhysicalEvaluationViewHolder(View itemView) {
            super(itemView);
            iv_physicalevaluation_img = (ImageView) itemView.findViewById(R.id.iv_physicalevaluation_img);
            tv_physicalevaluation_content = (TextView)itemView.findViewById(R.id.tv_physicalevaluation_content);
        }
    }


}
