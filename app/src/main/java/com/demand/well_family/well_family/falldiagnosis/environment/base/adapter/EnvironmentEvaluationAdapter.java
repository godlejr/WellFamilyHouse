package com.demand.well_family.well_family.falldiagnosis.environment.base.adapter;

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
import com.demand.well_family.well_family.dto.FallDiagnosisContentCategory;
import com.demand.well_family.well_family.falldiagnosis.environment.base.presenter.EnvironmentEvaluationPresenter;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-04-24.
 */

public class EnvironmentEvaluationAdapter extends RecyclerView.Adapter<EnvironmentEvaluationAdapter.DangerEvaluationViewHolder> {
    private EnvironmentEvaluationPresenter environmentEvaluationPresenter;
    private ArrayList<FallDiagnosisContentCategory> fallDiagnosisContentCategoryList;
    private Context context;

    public EnvironmentEvaluationAdapter(Context context, ArrayList<FallDiagnosisContentCategory> fallDiagnosisContentCategoryList, EnvironmentEvaluationPresenter environmentEvaluationPresenter) {
        this.environmentEvaluationPresenter = environmentEvaluationPresenter;
        this.fallDiagnosisContentCategoryList = fallDiagnosisContentCategoryList;
        this.context = context;
    }

    @Override
    public DangerEvaluationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DangerEvaluationViewHolder dangerEvaluationViewHolder = new DangerEvaluationViewHolder(LayoutInflater.from(context).inflate(R.layout.item_environment_evaluation, parent, false));
        return dangerEvaluationViewHolder;
    }

    @Override
    public void onBindViewHolder(DangerEvaluationViewHolder holder, int position) {
        final FallDiagnosisContentCategory fallDiagnosisContentCategory = fallDiagnosisContentCategoryList.get(position);

        holder.tv_item_environment_evaluation.setText(fallDiagnosisContentCategory.getName());
        Glide.with(context).load(context.getString(R.string.cloud_front_self_diagnosis) + fallDiagnosisContentCategory.getAvatar()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_item_environment_evaluation);

    }

    @Override
    public int getItemCount() {
        return fallDiagnosisContentCategoryList.size();
    }

    public class DangerEvaluationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView iv_item_environment_evaluation;
        private TextView tv_item_environment_evaluation;
        private LinearLayout ll_item_environment_evaluation;

        public DangerEvaluationViewHolder(View itemView) {
            super(itemView);
            iv_item_environment_evaluation = (ImageView) itemView.findViewById(R.id.iv_item_environment_evaluation);
            tv_item_environment_evaluation = (TextView) itemView.findViewById(R.id.tv_item_environment_evaluation);
            ll_item_environment_evaluation = (LinearLayout) itemView.findViewById(R.id.ll_item_environment_evaluation);

            ll_item_environment_evaluation.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_item_environment_evaluation:
                    int position = getAdapterPosition();
                    FallDiagnosisContentCategory fallDiagnosisContentCategory = fallDiagnosisContentCategoryList.get(position);
                    environmentEvaluationPresenter.onClickEnvironmentEvaluationItem(fallDiagnosisContentCategory);
                    break;

            }
        }
    }

}
