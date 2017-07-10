package com.demand.well_family.well_family.exercise.base.presetner.impl;

import android.content.Context;
import android.view.View;

import com.demand.well_family.well_family.dto.ExerciseCategory;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.exercise.base.interactor.ExerciseInteractor;
import com.demand.well_family.well_family.exercise.base.interactor.impl.ExerciseInteractorImpl;
import com.demand.well_family.well_family.exercise.base.presetner.ExercisePresenter;
import com.demand.well_family.well_family.exercise.base.view.ExerciseView;
import com.demand.well_family.well_family.util.APIErrorUtil;
import com.demand.well_family.well_family.util.PreferenceUtil;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-06-23.
 */

public class ExercisePresenterImpl implements ExercisePresenter {
    private ExerciseInteractor exerciseInteractor;
    private ExerciseView exerciseView;
    private PreferenceUtil preferenceUtil;

    public ExercisePresenterImpl(Context context) {
        this.exerciseInteractor = new ExerciseInteractorImpl(this);
        this.exerciseView = (ExerciseView) context;
        this.preferenceUtil = new PreferenceUtil(context);
    }

    @Override
    public void onCreate() {
        User user = preferenceUtil.getUserInfo();
        exerciseInteractor.setUser(user);

        exerciseView.init();
        View decorView = exerciseView.getDecorView();
        exerciseView.setToolbar(decorView);
        exerciseView.showToolbarTitle("낙상예방운동");
    }

    @Override
    public void onLoadData(){
        exerciseInteractor.getExerciseCategoryList();
    }

    @Override
    public void onSuccessGetExerciseCategoryList(ArrayList<ExerciseCategory> exerciseCategoryList) {
        exerciseView.setExerciseAdapterItem(exerciseCategoryList);
    }

    @Override
    public void onClickContentBody(ExerciseCategory exerciseCategory) {
        exerciseView.navigateToCreateExerciseActivity(exerciseCategory);
    }

    @Override
    public void onNetworkError(APIErrorUtil apiErrorUtil) {
        if (apiErrorUtil == null) {
            exerciseView.showMessage("네트워크 불안정합니다. 다시 시도하세요.");
        } else {
            exerciseView.showMessage(apiErrorUtil.message());
        }
    }


}
