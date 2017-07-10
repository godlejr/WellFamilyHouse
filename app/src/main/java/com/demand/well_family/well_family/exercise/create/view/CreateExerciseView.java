package com.demand.well_family.well_family.exercise.create.view;

import android.view.View;

import com.demand.well_family.well_family.dto.ExerciseCategory;

import java.io.IOException;

/**
 * Created by ㅇㅇ on 2017-06-26.
 */

public interface CreateExerciseView {
    void init();

    void setVideoItem(String video) throws IOException;

    View getDecorView();

    void showToolbarTitle(String message);

    void setToolbar(View decorView);

    void showMessage(String message);

    void navigateToExercisePopupActivity(ExerciseCategory exerciseCategory);

    void showPauseButton();

    void showPlayButton();

    void setMediaPlayerStart();

    void setMediaPlayerPause();

    void navigateToBack();

    void setProgressBar();
}
