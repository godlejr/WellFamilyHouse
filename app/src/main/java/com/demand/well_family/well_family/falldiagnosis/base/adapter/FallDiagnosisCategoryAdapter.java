package com.demand.well_family.well_family.falldiagnosis.base.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dto.Category;
import com.demand.well_family.well_family.falldiagnosis.base.presenter.FallDiagnosisMainPresenter;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-05-24.
 */

public class FallDiagnosisCategoryAdapter extends RecyclerView.Adapter<FallDiagnosisCategoryAdapter.FallDiagnosisCategoryViewHolder> {
    private Context context;
    private ArrayList<Category> fallDiagnosisCategoryList;
    private FallDiagnosisMainPresenter fallDiagnosisMainPresenter;

    public FallDiagnosisCategoryAdapter(Context context, ArrayList<Category> fallDiagnosisCategoryList, FallDiagnosisMainPresenter fallDiagnosisMainPresenter) {
        this.context = context;
        this.fallDiagnosisCategoryList = fallDiagnosisCategoryList;
        this.fallDiagnosisMainPresenter = fallDiagnosisMainPresenter;
    }

    @Override
    public FallDiagnosisCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_fall_diagnosis_main_category, parent, false);
        int height = parent.getMeasuredHeight() / 3;
        view.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));

        FallDiagnosisCategoryViewHolder fallDiagnosisCategoryViewHolder = new FallDiagnosisCategoryViewHolder(view);
        return fallDiagnosisCategoryViewHolder;
    }


    @Override
    public void onBindViewHolder(FallDiagnosisCategoryViewHolder holder, int position) {
        Category category = fallDiagnosisCategoryList.get(position);

        String title = category.getName();
        String content = fallDiagnosisMainPresenter.setCategoryContent(holder, category.getId());

        holder.tv_title.setText(title);
        holder.tv_content.setText(content);
    }

    @Override
    public int getItemCount() {
        return fallDiagnosisCategoryList.size();
    }

    public class FallDiagnosisCategoryViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_title;
        public TextView tv_content;
        public LinearLayout ll_category;

        public FallDiagnosisCategoryViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_falldiagnosis_category_title);
            tv_content = (TextView) itemView.findViewById(R.id.tv_falldiagnosis_category_content);
            ll_category = (LinearLayout)itemView.findViewById(R.id.ll_falldiagnosis_category);
        }
    }

    public void setBackgroundColorBeige(FallDiagnosisCategoryViewHolder holder) {
        holder.ll_category.setBackgroundResource(R.drawable.round_corner_beige_r10);
    }

    public void setBackgroundColorBeigegray(FallDiagnosisCategoryViewHolder holder) {
        holder.ll_category.setBackgroundResource(R.drawable.round_corner_beigegray_r10);
    }

    public void setBackgroundColorIndipink(FallDiagnosisCategoryViewHolder holder) {
        holder.ll_category.setBackgroundResource(R.drawable.round_corner_indipink_r10);
    }
}
