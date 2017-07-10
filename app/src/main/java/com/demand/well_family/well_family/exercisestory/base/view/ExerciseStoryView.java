package com.demand.well_family.well_family.exercisestory.base.view;

import android.view.View;

import com.demand.well_family.well_family.dto.ExerciseStory;
import com.demand.well_family.well_family.exercisestory.base.adapter.ExerciseStoryAdapter;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-06-27.
 */

public interface ExerciseStoryView {
    void showMessage(String message);

    View getDecorView();

    void showToolbarTitle(String message);

    void setToolbar(View decorView);

    void init();

    void setExerciseStoryAdapterItem(ArrayList<ExerciseStory>  exerciseStoryList);

    void showExerciseStoryAdapterCommentCount(ExerciseStoryAdapter.ExerciseStoryViewHolder holder, String count);

    void showExerciseStoryAdapterLikeCount(ExerciseStoryAdapter.ExerciseStoryViewHolder holder, String count);

    void showProgressDialog();

    void showExerciseStoryAdapterNotifyItemChanged(int position);

    void goneProgressDialog();

    void setExerciseStoryAdapter(ExerciseStoryAdapter fallDiagnosisStoryAdapter);

    ExerciseStoryAdapter getExerciseStoryAdapter();

    void navigateToExerciseStoryDetailActivity(ExerciseStory exerciseStory);

    void setExerciseStoryAdapterLikeDown(ExerciseStoryAdapter.ExerciseStoryViewHolder holder, int position);

    void setExerciseStoryAdapterLikeUp(ExerciseStoryAdapter.ExerciseStoryViewHolder holder, int position);

    void setExerciseStoryAdapterLikeIsUnChecked(ExerciseStoryAdapter.ExerciseStoryViewHolder holder, int position);

    void setExerciseStoryAdapterLikeIsChecked(ExerciseStoryAdapter.ExerciseStoryViewHolder holder, int position);
}
