package com.demand.well_family.well_family.falldiagnosis.fall.result.presenter.impl;

import android.content.Context;
import android.view.View;

import com.demand.well_family.well_family.dto.FallDiagnosisCategory;
import com.demand.well_family.well_family.dto.FallDiagnosisStory;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.falldiagnosis.fall.result.interactor.SelfDiagnosisResultInteractor;
import com.demand.well_family.well_family.falldiagnosis.fall.result.interactor.impl.SelfDiagnosisResultInteractorImpl;
import com.demand.well_family.well_family.falldiagnosis.fall.result.presenter.SelfDiagnosisResultPresenter;
import com.demand.well_family.well_family.falldiagnosis.fall.result.view.SelfDiagnosisResultView;
import com.demand.well_family.well_family.util.APIErrorUtil;
import com.demand.well_family.well_family.util.PreferenceUtil;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-05-23.
 */

public class SelfDiagnosisResultPresenterImpl implements SelfDiagnosisResultPresenter {
    private SelfDiagnosisResultInteractor selfDiagnosisResultInteractor;
    private SelfDiagnosisResultView fallDiagnosisResultView;
    private PreferenceUtil preferenceUtil;

    public SelfDiagnosisResultPresenterImpl(Context context) {
        this.selfDiagnosisResultInteractor = new SelfDiagnosisResultInteractorImpl(this);
        this.fallDiagnosisResultView = (SelfDiagnosisResultView) context;
        this.preferenceUtil = new PreferenceUtil(context);
    }

    @Override
    public void onCreate(ArrayList<Boolean> answerList, FallDiagnosisCategory fallDiagnosisCategory) {
        User user = preferenceUtil.getUserInfo();
        selfDiagnosisResultInteractor.setUser(user);
        selfDiagnosisResultInteractor.setAnswerList(answerList);
        selfDiagnosisResultInteractor.setFallDiagnosisCategory(fallDiagnosisCategory);

        fallDiagnosisResultView.init();

        View decorView = fallDiagnosisResultView.getDecorView();
        fallDiagnosisResultView.setToolbar(decorView);
        fallDiagnosisResultView.showToolbarTitle("낙상위험 자가진단");
    }

    @Override
    public void onLoadData() {
        ArrayList<Boolean> answerList = selfDiagnosisResultInteractor.getAnswerList();
        ArrayList<Integer> trueList = selfDiagnosisResultInteractor.getTrueList();
        int answerSize = answerList.size();
        int trueSize = trueList.size();

        fallDiagnosisResultView.showTotalCount(answerSize);
        fallDiagnosisResultView.showScore(trueSize);
        fallDiagnosisResultView.showProgressbar(trueSize, answerSize);

        if (trueSize <= 3) {
            fallDiagnosisResultView.showResult("안전 단계");
            fallDiagnosisResultView.showProgressBarChangeColorWithSafe();
            fallDiagnosisResultView.showScoreTextChangeColorWithSafe();
            fallDiagnosisResultView.showTotalCountTextChangeColorWithSafe();

        } else if (trueSize <= 7) {
            fallDiagnosisResultView.showResult("주의 단계");
            fallDiagnosisResultView.showProgressBarChangeColorWithCaution();
            fallDiagnosisResultView.showScoreTextChangeColorWithCaution();
            fallDiagnosisResultView.showTotalCountTextChangeColorWithCaution();
        } else if (trueSize <= 11) {
            fallDiagnosisResultView.showResult("위험 단계");
            fallDiagnosisResultView.showProgressBarChangeColorWithRisk();
            fallDiagnosisResultView.showScoreTextChangeColorWithRisk();
            fallDiagnosisResultView.showTotalCountTextChangeColorWithRisk();
        }

    }

    @Override
    public void onClickSendResult() {
        User user = selfDiagnosisResultInteractor.getUser();
        FallDiagnosisCategory fallDiagnosisCategory = selfDiagnosisResultInteractor.getFallDiagnosisCategory();

        int userId = user.getId();
        int fallDiagnosisCategoryId = fallDiagnosisCategory.getId();

        FallDiagnosisStory fallDiagnosisStory = new FallDiagnosisStory();
        fallDiagnosisStory.setUser_id(userId);
        fallDiagnosisStory.setFall_diagnosis_category_id(fallDiagnosisCategoryId);

        selfDiagnosisResultInteractor.setFallDiagnosisStory(fallDiagnosisStory);
        selfDiagnosisResultInteractor.setStoryAdded();
    }


    @Override
    public void onNetworkError(APIErrorUtil apiErrorUtil) {
        if (apiErrorUtil == null) {
            fallDiagnosisResultView.showMessage("네트워크 불안정합니다. 다시 시도하세요.");
        } else {
            fallDiagnosisResultView.showMessage(apiErrorUtil.message());
        }
    }

    @Override
    public void onSuccessSetStoryAdded(int storyId) {
        ArrayList<Integer> trueList = selfDiagnosisResultInteractor.getTrueList();
        int trueSize = trueList.size();

        for (int i = 0; i < trueSize; i++) {
            selfDiagnosisResultInteractor.setSelfDiagnosisAdded(storyId, trueList.get(i));
        }
    }

    @Override
    public void onSuccessSetSelfDiagnosisAdded() {
        fallDiagnosisResultView.showMessage("낙상 자가진단기록이 등록되었습니다.");
        fallDiagnosisResultView.navigateToBack();
    }

}
