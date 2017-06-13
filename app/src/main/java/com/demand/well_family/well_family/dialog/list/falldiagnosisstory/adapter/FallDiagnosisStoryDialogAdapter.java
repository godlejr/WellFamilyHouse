package com.demand.well_family.well_family.dialog.list.falldiagnosisstory.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dialog.list.falldiagnosisstory.presenter.FallDiagnosisStoryDialogPresenter;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-06-08.
 */

public class FallDiagnosisStoryDialogAdapter extends RecyclerView.Adapter<FallDiagnosisStoryDialogAdapter.FallDiagnosisStoryDialogViewHolder> {
    private FallDiagnosisStoryDialogPresenter fallDiagnosisStoryDialogPresenter;
    private Context context;
    private ArrayList<String> fallDiagnosisStoryDialogList;

    public FallDiagnosisStoryDialogAdapter(Context context, ArrayList<String> fallDiagnosisStoryDialogList,FallDiagnosisStoryDialogPresenter fallDiagnosisStoryDialogPresenter) {
        this.fallDiagnosisStoryDialogPresenter = fallDiagnosisStoryDialogPresenter;
        this.context = context;
        this.fallDiagnosisStoryDialogList = fallDiagnosisStoryDialogList;
    }

    @Override
    public FallDiagnosisStoryDialogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FallDiagnosisStoryDialogViewHolder fallDiagnosisStoryDialogViewHolder = new FallDiagnosisStoryDialogViewHolder(LayoutInflater.from(context).inflate(R.layout.item_textview, parent,false));
        return fallDiagnosisStoryDialogViewHolder;
    }

    @Override
    public void onBindViewHolder(FallDiagnosisStoryDialogViewHolder holder, int position) {
        holder.tv_popup_list.setText(fallDiagnosisStoryDialogList.get(position));
    }

    @Override
    public int getItemCount() {
        return fallDiagnosisStoryDialogList.size();
    }

    public class FallDiagnosisStoryDialogViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tv_popup_list;

        public FallDiagnosisStoryDialogViewHolder(View itemView) {
            super(itemView);
            tv_popup_list = (TextView)itemView.findViewById(R.id.tv_popup_list);
            tv_popup_list.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
          fallDiagnosisStoryDialogPresenter.onClickDialog(getAdapterPosition());
        }
    }

}
