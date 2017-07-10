package com.demand.well_family.well_family.exercisestory.base.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dto.ExerciseStory;
import com.demand.well_family.well_family.exercisestory.base.presenter.ExerciseStoryPresenter;
import com.demand.well_family.well_family.util.CalculateDateUtil;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-06-27.
 */

public class ExerciseStoryAdapter extends RecyclerView.Adapter<ExerciseStoryAdapter.ExerciseStoryViewHolder> implements Serializable {
    private ExerciseStoryPresenter exerciseStoryPresenter;
    private ArrayList<ExerciseStory> exerciseStoryList;
    private Context context;
    private int layout;

    public ExerciseStoryAdapter(ExerciseStoryPresenter exerciseStoryPresenter, ArrayList<ExerciseStory> exerciseStoryList, Context context, int layout) {
        this.exerciseStoryPresenter = exerciseStoryPresenter;
        this.exerciseStoryList = exerciseStoryList;
        this.context = context;
        this.layout = layout;
    }

    @Override
    public ExerciseStoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ExerciseStoryViewHolder exerciseStoryViewHolder = new ExerciseStoryViewHolder(LayoutInflater.from(context).inflate(layout, parent, false));
        return exerciseStoryViewHolder;
    }

    @Override
    public void onBindViewHolder(ExerciseStoryViewHolder holder, int position) {
        ExerciseStory exerciseStory = exerciseStoryList.get(position);
        exerciseStory.setPosition(position);
        exerciseStory.setFirstChecked(false);

        String date = exerciseStory.getCreated_at();
        String title = exerciseStory.getTitle();
        String scoreMessage = exerciseStory.getScore() + "점";
        String dateMessage = CalculateDateUtil.calculateDate(date);

        holder.tv_exercisestory_result.setText(scoreMessage);
        holder.tv_exercisestory_date.setText(dateMessage);
        holder.tv_exercisestory_title.setText(title);

        exerciseStoryPresenter.onLoadContent(holder, exerciseStory);
    }

    @Override
    public int getItemCount() {
        return exerciseStoryList.size();
    }


    public void showExerciseStoryAdapterCommentCount(ExerciseStoryViewHolder holder, String count) {
        holder.tv_exercisestory_commentcount.setText(count);
    }

    public void showExerciseStoryAdapterLikeCount(ExerciseStoryViewHolder holder, String count) {
        holder.tv_exercisestory_likecount.setText(count);
    }

    public void setLikeChange(int position, Boolean likeCheck) {
        exerciseStoryList.get(position).setChecked(likeCheck);
    }

    public void setContentDelete(int position) {
        exerciseStoryList.remove(position);
    }

    public void setExerciseStoryAdapterLikeIsChecked(ExerciseStoryViewHolder holder, int position) {
        holder.cb_exercisestory_likebtn.setChecked(true);
        exerciseStoryList.get(position).setFirstChecked(true);
        exerciseStoryList.get(position).setChecked(true);
    }

    public void setExerciseStoryAdapterLikeIsUnChecked(ExerciseStoryViewHolder holder, int position) {
        holder.cb_exercisestory_likebtn.setChecked(false);
        exerciseStoryList.get(position).setFirstChecked(true);
        exerciseStoryList.get(position).setChecked(false);
    }

    public void setExerciseStoryAdapterLikeUnChecked(ExerciseStoryViewHolder holder, int position) {
        holder.tv_exercisestory_likecount.setText(String.valueOf(Integer.parseInt(holder.tv_exercisestory_likecount.getText().toString()) - 1));
        holder.cb_exercisestory_likebtn.setChecked(false);
        exerciseStoryList.get(position).setChecked(false);
    }

    public void setExerciseStoryAdapterLikeChecked(ExerciseStoryViewHolder holder, int position) {
        holder.tv_exercisestory_likecount.setText(String.valueOf(Integer.parseInt(holder.tv_exercisestory_likecount.getText().toString()) + 1));
        holder.cb_exercisestory_likebtn.setChecked(true);
        exerciseStoryList.get(position).setChecked(true);
    }


    public class ExerciseStoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
        private LinearLayout ll_exercisestory;
        private TextView tv_exercisestory_title;
        private TextView tv_exercisestory_date;
        private TextView tv_exercisestory_result;
        private TextView tv_exercisestory_likecount;
        private TextView tv_exercisestory_commentcount;
        private CheckBox cb_exercisestory_likebtn;
        private ImageView iv_exercisestory_commentbtn;

        public ExerciseStoryViewHolder(View itemView) {
            super(itemView);

            ll_exercisestory = (LinearLayout) itemView.findViewById(R.id.ll_exercisestory);
            tv_exercisestory_title = (TextView) itemView.findViewById(R.id.tv_exercisestory_title);
            tv_exercisestory_date = (TextView) itemView.findViewById(R.id.tv_exercisestory_date);
            tv_exercisestory_likecount = (TextView) itemView.findViewById(R.id.tv_exercisestory_likecount);
            tv_exercisestory_commentcount = (TextView) itemView.findViewById(R.id.tv_exercisestory_commentcount);
            tv_exercisestory_result = (TextView) itemView.findViewById(R.id.tv_exercisestory_result);
            cb_exercisestory_likebtn = (CheckBox) itemView.findViewById(R.id.cb_exercisestory_likebtn);
            iv_exercisestory_commentbtn = (ImageView) itemView.findViewById(R.id.iv_exercisestory_commentbtn);

            cb_exercisestory_likebtn.setOnCheckedChangeListener(this);
            ll_exercisestory.setOnClickListener(this);
            iv_exercisestory_commentbtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_exercisestory:
                case R.id.iv_exercisestory_commentbtn:
                    int position = getAdapterPosition();
                    ExerciseStory exerciseStory = exerciseStoryList.get(position);
                    exerciseStory.setPosition(position);
                    exerciseStory.setFirstChecked(false);

                    exerciseStoryPresenter.onClickContentBody(exerciseStory);

                    break;
            }
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int position = getAdapterPosition();
            ExerciseStory exerciseStory = exerciseStoryList.get(position);
            exerciseStory.setPosition(position);
            exerciseStoryPresenter.onCheckedChangeForLike(this, exerciseStory, isChecked);
        }
    }
}
