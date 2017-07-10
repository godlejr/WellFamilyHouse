package com.demand.well_family.well_family.dialog.list.exercise.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dialog.list.exercise.presenter.ExerciseStoryDialogPresenter;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-07-07.
 */

public class ExerciseStoryDialogAdapter extends RecyclerView.Adapter<ExerciseStoryDialogAdapter.ExerciseStoryDialogViewHolder> {
    private ExerciseStoryDialogPresenter exerciseStoryDialogPresenter;
    private Context context;
    private ArrayList<String> exerciseStoryDialogList;

    public ExerciseStoryDialogAdapter(Context context, ArrayList<String> exerciseStoryDialogList, ExerciseStoryDialogPresenter exerciseStoryDialogPresenter) {
        this.context = context;
        this.exerciseStoryDialogPresenter = exerciseStoryDialogPresenter;
        this.exerciseStoryDialogList = exerciseStoryDialogList;
    }

    @Override
    public  ExerciseStoryDialogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ExerciseStoryDialogViewHolder exerciseStoryDialogViewHolder = new ExerciseStoryDialogViewHolder(LayoutInflater.from(context).inflate(R.layout.item_textview, parent, false));
        return exerciseStoryDialogViewHolder;
    }

    @Override
    public void onBindViewHolder(ExerciseStoryDialogViewHolder holder, int position) {
        holder.tv_popup_list.setText(exerciseStoryDialogList.get(position));
    }

    @Override
    public int getItemCount() {
        return exerciseStoryDialogList.size();
    }

    public class ExerciseStoryDialogViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tv_popup_list;

        public ExerciseStoryDialogViewHolder(View itemView) {
            super(itemView);
            tv_popup_list = (TextView) itemView.findViewById(R.id.tv_popup_list);
            tv_popup_list.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            exerciseStoryDialogPresenter.onClickDialog(getAdapterPosition());
        }
    }

}
