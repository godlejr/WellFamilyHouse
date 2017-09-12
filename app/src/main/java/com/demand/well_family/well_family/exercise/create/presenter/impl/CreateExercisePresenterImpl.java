package com.demand.well_family.well_family.exercise.create.presenter.impl;

import android.content.Context;
import android.view.View;

import com.demand.well_family.well_family.dto.ExerciseCategory;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.exercise.create.interactor.CreateExerciseInteractor;
import com.demand.well_family.well_family.exercise.create.interactor.impl.CreateExerciseInteractorImpl;
import com.demand.well_family.well_family.exercise.create.presenter.CreateExercisePresenter;
import com.demand.well_family.well_family.exercise.create.view.CreateExerciseView;
import com.demand.well_family.well_family.util.APIErrorUtil;
import com.demand.well_family.well_family.util.PreferenceUtil;

import java.io.IOException;

/**
 * Created by ㅇㅇ on 2017-06-26.
 */

public class CreateExercisePresenterImpl implements CreateExercisePresenter {
    private CreateExerciseInteractor createExerciseInteractor;
    private CreateExerciseView createExerciseView;
    private PreferenceUtil preferenceUtil;

    public CreateExercisePresenterImpl(Context context) {
        this.createExerciseInteractor = new CreateExerciseInteractorImpl(this);
        this.createExerciseView = (CreateExerciseView) context;
        this.preferenceUtil = new PreferenceUtil(context);
    }

    @Override
    public void onCreate(ExerciseCategory exerciseCategory) {
        User user = preferenceUtil.getUserInfo();
        createExerciseInteractor.setUser(user);
        createExerciseInteractor.setExerciseCategory(exerciseCategory);

        String name = exerciseCategory.getName();
        View decorView = createExerciseView.getDecorView();
        createExerciseView.setToolbar(decorView);
        createExerciseView.showToolbarTitle(name);

        createExerciseView.init();
    }

    @Override
    public void surfaceCreated() {
        ExerciseCategory exerciseCategory = createExerciseInteractor.getExerciseCategory();
        String video = exerciseCategory.getVideo();

        try {
            createExerciseView.setVideoItem(video);
        } catch (IOException e) {
            createExerciseInteractor.log(e);
            onNetworkError(null);
        }
    }

    @Override
    public void onClickPlay(boolean isPlaying) {
        if (isPlaying) {
            createExerciseView.setMediaPlayerPause();
            createExerciseView.showPlayButton();
        } else {
            createExerciseView.setMediaPlayerStart();
            createExerciseView.showPauseButton();
        }
    }

    @Override
    public void onComplete(){
        createExerciseInteractor.setPlaying(false);
        createExerciseView.navigateToBack();

        ExerciseCategory exerciseCategory = createExerciseInteractor.getExerciseCategory();
        createExerciseView.navigateToExercisePopupActivity(exerciseCategory);
    }




    @Override
    public void onBackPressed() {
        createExerciseInteractor.setPlaying(false);
        createExerciseView.navigateToBack();
    }

    @Override
    public void setSeekBarStart() {
        createExerciseInteractor.setSeekBarStart();
    }

    @Override
    public void setProgress() {
        createExerciseView.setProgressBar();
    }

    @Override
    public void onNetworkError(APIErrorUtil apiErrorUtil) {
        if (apiErrorUtil == null) {
            createExerciseView.showMessage("네트워크 불안정합니다. 다시 시도하세요.");
        } else {
            createExerciseView.showMessage(apiErrorUtil.message());
        }
    }


}
