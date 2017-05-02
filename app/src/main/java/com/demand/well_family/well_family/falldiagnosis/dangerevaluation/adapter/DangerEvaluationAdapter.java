package com.demand.well_family.well_family.falldiagnosis.dangerevaluation.adapter;

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
import com.demand.well_family.well_family.dto.Evaluation;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-04-24.
 */

public class DangerEvaluationAdapter extends RecyclerView.Adapter<DangerEvaluationAdapter.DangerEvaluationViewHolder> {
    private ArrayList<Evaluation> dangerEvaluationList;
    private Context context;

    public DangerEvaluationAdapter(ArrayList<Evaluation> dangerEvaluationList, Context context) {
        this.dangerEvaluationList = dangerEvaluationList;
        this.context = context;
    }

    @Override
    public DangerEvaluationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DangerEvaluationViewHolder dangerEvaluationViewHolder = new DangerEvaluationViewHolder(LayoutInflater.from(context).inflate(R.layout.item_danger_evaluation, parent, false));
        return dangerEvaluationViewHolder;
    }

    @Override
    public void onBindViewHolder(DangerEvaluationViewHolder holder, int position) {
        holder.tv_item_danger_evaluation.setText(dangerEvaluationList.get(position).getTitle());
        Glide.with(context).load(dangerEvaluationList.get(position).getImage()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_item_danger_evaluation);
        holder.iv_item_danger_evaluation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return dangerEvaluationList.size();
    }

    public class DangerEvaluationViewHolder extends RecyclerView.ViewHolder{
        private ImageView iv_item_danger_evaluation;
        private TextView tv_item_danger_evaluation;

        public DangerEvaluationViewHolder(View itemView) {
            super(itemView);
            iv_item_danger_evaluation = (ImageView)itemView.findViewById(R.id.iv_item_danger_evaluation);
            tv_item_danger_evaluation = (TextView)itemView.findViewById(R.id.tv_item_danger_evaluation);
        }
    }
}
