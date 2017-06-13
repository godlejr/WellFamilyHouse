package com.demand.well_family.well_family.falldiagnosistory.detail.adapter.selfdiagnosis;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dto.FallDiagnosisContentCategory;
import com.demand.well_family.well_family.falldiagnosistory.detail.presenter.FallDiagnosisStoryDetailPresenter;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-06-08.
 */

public class FallDiagnosisStoryDetailSelfDiagnosisAdapter extends RecyclerView.Adapter<FallDiagnosisStoryDetailSelfDiagnosisAdapter.FallDiagnosisStoryDetailSelfDiagnosisViewHolder> {
    private FallDiagnosisStoryDetailPresenter fallDiagnosisStoryDetailPresenter;
    private Context context;
    private ArrayList<FallDiagnosisContentCategory> fallDiagnosisContentCategoryList;

    public FallDiagnosisStoryDetailSelfDiagnosisAdapter(Context context, ArrayList<FallDiagnosisContentCategory> fallDiagnosisContentCategoryList, FallDiagnosisStoryDetailPresenter fallDiagnosisStoryDetailPresenter) {
        this.context = context;
        this.fallDiagnosisContentCategoryList = fallDiagnosisContentCategoryList;
        this.fallDiagnosisStoryDetailPresenter = fallDiagnosisStoryDetailPresenter;
    }

    @Override
    public FallDiagnosisStoryDetailSelfDiagnosisViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FallDiagnosisStoryDetailSelfDiagnosisViewHolder fallDiagnosisStoryDetailSelfDiagnosisViewHolder = new FallDiagnosisStoryDetailSelfDiagnosisViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_falldiagnosisstorydetail_content, parent, false));
        return fallDiagnosisStoryDetailSelfDiagnosisViewHolder;
    }

    @Override
    public void onBindViewHolder(FallDiagnosisStoryDetailSelfDiagnosisViewHolder holder, int position) {
        FallDiagnosisContentCategory fallDiagnosisContentCategory = fallDiagnosisContentCategoryList.get(position);
        String content = fallDiagnosisContentCategory.getId() + ". " + fallDiagnosisContentCategory.getName();
        holder.tv_item_falldiagnosisstorydetail_selfdiagnosis.setText(content);
    }

    @Override
    public int getItemCount() {
        return fallDiagnosisContentCategoryList.size();
    }

    public class FallDiagnosisStoryDetailSelfDiagnosisViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_item_falldiagnosisstorydetail_selfdiagnosis;

        public FallDiagnosisStoryDetailSelfDiagnosisViewHolder(View itemView) {
            super(itemView);
            tv_item_falldiagnosisstorydetail_selfdiagnosis = (TextView) itemView.findViewById(R.id.tv_item_falldiagnosisstorydetail_selfdiagnosis);
        }
    }

}
