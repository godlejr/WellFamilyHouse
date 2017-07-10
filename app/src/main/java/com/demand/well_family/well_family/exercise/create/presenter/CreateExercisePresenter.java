package com.demand.well_family.well_family.exercise.create.presenter;

import com.demand.well_family.well_family.dto.ExerciseCategory;
import com.demand.well_family.well_family.util.APIErrorUtil;

/**
 * Created by ㅇㅇ on 2017-06-26.
 */

public interface CreateExercisePresenter {
    void onNetworkError(APIErrorUtil apiErrorUtil);

    void onCreate(ExerciseCategory exerciseCategory);


    void surfaceCreated();

    void onClickPlay(boolean isPlaying);

    void onComplete();

    void onBackPressed();

    void setSeekBarStart();

    void setProgress();
}
