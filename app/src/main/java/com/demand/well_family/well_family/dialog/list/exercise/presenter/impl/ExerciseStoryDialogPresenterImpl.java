package com.demand.well_family.well_family.dialog.list.exercise.presenter.impl;

import android.content.Context;

import com.demand.well_family.well_family.dialog.list.exercise.interactor.ExerciseStoryDialogInteractor;
import com.demand.well_family.well_family.dialog.list.exercise.interactor.impl.ExerciseStoryDialogInteractorImpl;
import com.demand.well_family.well_family.dialog.list.exercise.presenter.ExerciseStoryDialogPresenter;
import com.demand.well_family.well_family.dialog.list.exercise.view.ExerciseStoryDialogView;
import com.demand.well_family.well_family.dialog.list.falldiagnosisstory.flag.FallDiagnosisStoryActFlag;
import com.demand.well_family.well_family.dto.ExerciseStory;
import com.demand.well_family.well_family.dto.FallDiagnosisStory;
import com.demand.well_family.well_family.dto.FallDiagnosisStoryInfo;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.setting.verifyAccount.interactor.VerifyAccountInteractor;
import com.demand.well_family.well_family.util.APIErrorUtil;
import com.demand.well_family.well_family.util.PreferenceUtil;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-07-07.
 */

public class ExerciseStoryDialogPresenterImpl implements ExerciseStoryDialogPresenter {
    private ExerciseStoryDialogInteractor exerciseStoryDialogInteractor;
    private ExerciseStoryDialogView exerciseStoryDialogView;
    private PreferenceUtil preferenceUtil;

    public ExerciseStoryDialogPresenterImpl(Context context) {
        this.exerciseStoryDialogInteractor = new ExerciseStoryDialogInteractorImpl(this);
        this.exerciseStoryDialogView = (ExerciseStoryDialogView) context;
        this.preferenceUtil = new PreferenceUtil(context);
    }


    @Override
    public void onCreate(ExerciseStory exerciseStory) {
        User user = preferenceUtil.getUserInfo();
        exerciseStoryDialogInteractor.setUser(user);
        exerciseStoryDialogInteractor.setExerciseStory(exerciseStory);

        exerciseStoryDialogView.init();
    }

    @Override
    public void onLoadData() {
        ArrayList<String> dialogList = new ArrayList<>();
        dialogList.add("삭제");
        exerciseStoryDialogView.setExerciseStoryDialogAdapterList(dialogList);
    }

    @Override
    public void onSuccessSetFallDiagnosisStoryDeleted() {
        ExerciseStory exerciseStory = exerciseStoryDialogInteractor.getExerciseStory();
        exerciseStoryDialogView.navigateToBackForResultOk(exerciseStory);
        exerciseStoryDialogView.showMessage("게시글이 삭제되었습니다.");
    }

    @Override
    public void onClickDialog(int position) {
        if (position == FallDiagnosisStoryActFlag.DELETE) {
            exerciseStoryDialogInteractor.setExerciseStoryDeleted();
        }
    }


    @Override
    public void onNetworkError(APIErrorUtil apiErrorUtil) {
        if (apiErrorUtil == null) {
            exerciseStoryDialogView.showMessage("네트워크 불안정합니다. 다시 시도하세요.");
        } else {
            exerciseStoryDialogView.showMessage(apiErrorUtil.message());
        }
    }
}
