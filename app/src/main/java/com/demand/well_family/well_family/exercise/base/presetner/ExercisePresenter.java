package com.demand.well_family.well_family.exercise.base.presetner;

import com.demand.well_family.well_family.dto.ExerciseCategory;
import com.demand.well_family.well_family.util.APIErrorUtil;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-06-23.
 */

public interface ExercisePresenter {
    void onCreate();

    void onNetworkError(APIErrorUtil apiErrorUtil);

    void onLoadData();

    void onSuccessGetExerciseCategoryList(ArrayList<ExerciseCategory> exerciseCategoryList);

    void onClickContentBody(ExerciseCategory exerciseCategory);
}
