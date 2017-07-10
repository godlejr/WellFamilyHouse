package com.demand.well_family.well_family.exercise.base.view;

import android.view.View;

import com.demand.well_family.well_family.dto.ExerciseCategory;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-06-23.
 */

public interface ExerciseView {
    void init();

    void showMessage(String message);


    View getDecorView();

    void showToolbarTitle(String message);

    void setToolbar(View decorView);

    void navigateToCreateExerciseActivity(ExerciseCategory exerciseCategory);

    void setExerciseAdapterItem(ArrayList<ExerciseCategory> exerciseCategoryList);
}
