package com.demand.well_family.well_family.main.report.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dto.Category;
import com.demand.well_family.well_family.main.report.presenter.ReportPresenter;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-05-22.
 */

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {
    private ReportPresenter reportPresenter;

    private ArrayList<Category> reportCategoryList;
    private int layout;
    private Context context;

    public ReportAdapter(Context context, ArrayList<Category> reportCategoryList, int layout, ReportPresenter reportPresenter) {
        this.reportCategoryList = reportCategoryList;
        this.layout = layout;
        this.context = context;
        this.reportPresenter = reportPresenter;
    }

    @Override
    public ReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ReportViewHolder reportViewHolder = new ReportViewHolder(LayoutInflater.from(context).inflate(layout, parent, false));
        return reportViewHolder;
    }

    @Override
    public void onBindViewHolder(ReportViewHolder holder, int position) {
        holder.tv_item_report.setText(reportCategoryList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return reportCategoryList.size();
    }


    class ReportViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_item_report;

        public ReportViewHolder(View itemView) {
            super(itemView);

            tv_item_report = (TextView) itemView.findViewById(R.id.tv_item_report);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int reportCategoryId = getAdapterPosition() + 1;
                    reportPresenter.onClickReport(reportCategoryId);
                }
            });

        }
    }

}
