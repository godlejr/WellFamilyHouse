package com.demand.well_family.well_family.dialog.popup.exercise.presenter.impl;

import android.content.Context;

import com.demand.well_family.well_family.dialog.popup.exercise.interactor.ExercisePopupInteractor;
import com.demand.well_family.well_family.dialog.popup.exercise.interactor.impl.ExercisePopupInteractorImpl;
import com.demand.well_family.well_family.dialog.popup.exercise.presenter.ExercisePopupPresenter;
import com.demand.well_family.well_family.dialog.popup.exercise.view.ExercisePopupView;
import com.demand.well_family.well_family.dto.ExerciseCategory;
import com.demand.well_family.well_family.dto.ExerciseStory;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.exercise.flag.ExerciseCodeFlag;
import com.demand.well_family.well_family.util.APIErrorUtil;
import com.demand.well_family.well_family.util.PreferenceUtil;

/**
 * Created by ㅇㅇ on 2017-06-26.
 */

public class ExercisePopupPresenterImpl implements ExercisePopupPresenter {
    private ExercisePopupInteractor exercisePopupInteractor;
    private ExercisePopupView exercisePopupView;
    private PreferenceUtil preferenceUtil;

    public ExercisePopupPresenterImpl(Context context) {
        this.exercisePopupInteractor = new ExercisePopupInteractorImpl(this);
        this.exercisePopupView = (ExercisePopupView) context;
        this.preferenceUtil = new PreferenceUtil(context);
    }

    @Override
    public void onCreate(ExerciseCategory exerciseCategory) {
        User user = preferenceUtil.getUserInfo();
        exercisePopupInteractor.setUser(user);
        exercisePopupInteractor.setExerciseCategory(exerciseCategory);

        exercisePopupView.init();

        String message = exerciseCategory.getName() + " 수행 완료!";
        exercisePopupView.showTitle(message);
    }

    @Override
    public void onNetworkError(APIErrorUtil apiErrorUtil) {
        if (apiErrorUtil == null) {
            exercisePopupView.showMessage("네트워크 불안정합니다. 다시 시도하세요.");
        } else {
            exercisePopupView.showMessage(apiErrorUtil.message());
        }
    }

    @Override
    public void onClickSubmit(ExerciseStory exerciseStory) {
        int score = exerciseStory.getScore();

        if(score > 0) {
            ExerciseCategory exerciseCategory = exercisePopupInteractor.getExerciseCategory();
            User user = exercisePopupInteractor.getUser();
            int exerciseCategoryId = exerciseCategory.getId();
            int userId = user.getId();

            exerciseStory.setExercise_category_id(exerciseCategoryId);
            exerciseStory.setUser_id(userId);
            exercisePopupInteractor.setExerciseStoryAdded(exerciseStory);
        } else {
            exercisePopupView.showMessage("점수를 선택해주세요.");
        }
    }

    @Override
    public void onSuccessSetExerciseStoryAdded(int isSuccess) {
        exercisePopupView.showMessage("운동 기록이 등록되었습니다.");
        exercisePopupView.navigateToBack();
    }

    @Override
    public void onRatingChanged(float rating) {
        if (rating == ExerciseCodeFlag.SCORE_ZERO) {
            exercisePopupView.setRating(ExerciseCodeFlag.SCORE_ONE);
        }
        if (rating == ExerciseCodeFlag.SCORE_ONE) {
            exercisePopupView.showSubtitle(ExerciseCodeFlag.SCORE_ONE_TITLE);
        }
        if (rating == ExerciseCodeFlag.SCORE_TWO) {
            exercisePopupView.showSubtitle(ExerciseCodeFlag.SCORE_TWO_TITLE);
        }
        if (rating == ExerciseCodeFlag.SCORE_THREE) {
            exercisePopupView.showSubtitle(ExerciseCodeFlag.SCORE_THREE_TITLE);
        }
        if (rating == ExerciseCodeFlag.SCORE_FOUR) {
            exercisePopupView.showSubtitle(ExerciseCodeFlag.SCORE_FOUR_TITLE);
        }
        if (rating == ExerciseCodeFlag.SCORE_FIVE) {
            exercisePopupView.showSubtitle(ExerciseCodeFlag.SCORE_FIVE_TITLE);
        }

    }

    @Override
    public void onBackPressed() {
        exercisePopupView.navigateToBack();
    }

}
