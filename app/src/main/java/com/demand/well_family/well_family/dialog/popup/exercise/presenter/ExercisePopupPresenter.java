package com.demand.well_family.well_family.dialog.popup.exercise.presenter;

import com.demand.well_family.well_family.dto.ExerciseCategory;
import com.demand.well_family.well_family.dto.ExerciseStory;
import com.demand.well_family.well_family.util.APIErrorUtil;

/**
 * Created by ㅇㅇ on 2017-06-26.
 */

public interface ExercisePopupPresenter {
    void onCreate(ExerciseCategory exerciseCategory);

    void onNetworkError(APIErrorUtil apiErrorUtil);

    void onClickSubmit(ExerciseStory exerciseStory);


    void onSuccessSetExerciseStoryAdded(int isSuccess);

    void onRatingChanged(float rating);

    void onBackPressed();
}
