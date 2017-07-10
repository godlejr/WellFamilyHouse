package com.demand.well_family.well_family.exercise.base.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dto.ExerciseCategory;
import com.demand.well_family.well_family.exercise.base.presetner.ExercisePresenter;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-06-26.
 */

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {
    private ExercisePresenter exercisePresenter;
    private ArrayList<ExerciseCategory> exerciseCategoryList;
    private Context context;

    public ExerciseAdapter(ExercisePresenter exercisePresenter, ArrayList<ExerciseCategory> exerciseCategoryList, Context context) {
        this.exercisePresenter = exercisePresenter;
        this.exerciseCategoryList = exerciseCategoryList;
        this.context = context;
    }

    @Override
    public ExerciseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ExerciseViewHolder exerciseViewHolder = new ExerciseViewHolder(LayoutInflater.from(context).inflate(R.layout.item_exercise, parent, false));
        return exerciseViewHolder;
    }

    @Override
    public void onBindViewHolder(ExerciseViewHolder holder, int position) {
        String name = exerciseCategoryList.get(position).getName();
        holder.tv_exercise.setText(name);
    }

    @Override
    public int getItemCount() {
        return exerciseCategoryList.size();
    }

    public class ExerciseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private LinearLayout ll_exercise;
        private TextView tv_exercise;

        public ExerciseViewHolder(View itemView) {
            super(itemView);
            ll_exercise = (LinearLayout) itemView.findViewById(R.id.ll_exercise);
            tv_exercise = (TextView) itemView.findViewById(R.id.tv_exercise);

            ll_exercise.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_exercise:
                    ExerciseCategory exerciseCategory = exerciseCategoryList.get(getAdapterPosition());
                    exercisePresenter.onClickContentBody(exerciseCategory);
            }
        }
    }
}
