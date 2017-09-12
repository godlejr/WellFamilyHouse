package com.demand.well_family.well_family.falldiagnosisstory.detail.adapter.environment.content;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dto.EnvironmentEvaluationCategory;
import com.demand.well_family.well_family.falldiagnosisstory.detail.presenter.FallDiagnosisStoryDetailPresenter;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-06-08.
 */

public class FallDiagnosisStoryDetailEnvEvaluationCategoryAdapter extends RecyclerView.Adapter<FallDiagnosisStoryDetailEnvEvaluationCategoryAdapter.FallDiagnosisStoryDetailEnvEvaluationCategoryViewHolder> {
    private FallDiagnosisStoryDetailPresenter fallDiagnosisStoryDetailPresenter;
    private Context context;
    private ArrayList<EnvironmentEvaluationCategory> environmentEvaluationCategoryList;

    public FallDiagnosisStoryDetailEnvEvaluationCategoryAdapter(Context context, ArrayList<EnvironmentEvaluationCategory> environmentEvaluationCategoryList, FallDiagnosisStoryDetailPresenter fallDiagnosisStoryDetailPresenter) {
        this.fallDiagnosisStoryDetailPresenter = fallDiagnosisStoryDetailPresenter;
        this.context = context;
        this.environmentEvaluationCategoryList = environmentEvaluationCategoryList;
    }

    @Override
    public FallDiagnosisStoryDetailEnvEvaluationCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FallDiagnosisStoryDetailEnvEvaluationCategoryViewHolder holder = new FallDiagnosisStoryDetailEnvEvaluationCategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.item_falldiagnosisstorydetail_content, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(FallDiagnosisStoryDetailEnvEvaluationCategoryViewHolder holder, int position) {
        EnvironmentEvaluationCategory environmentEvaluationCategory = environmentEvaluationCategoryList.get(position);
        holder.tv_item_falldiagnosisstorydetail_selfdiagnosis.setText(environmentEvaluationCategory.getName());
    }

    @Override
    public int getItemCount() {
        return environmentEvaluationCategoryList.size();
    }

    public class FallDiagnosisStoryDetailEnvEvaluationCategoryViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_item_falldiagnosisstorydetail_selfdiagnosis;

        public FallDiagnosisStoryDetailEnvEvaluationCategoryViewHolder(View itemView) {
            super(itemView);
            tv_item_falldiagnosisstorydetail_selfdiagnosis = (TextView) itemView.findViewById(R.id.tv_item_falldiagnosisstorydetail_selfdiagnosis);
        }
    }
}
