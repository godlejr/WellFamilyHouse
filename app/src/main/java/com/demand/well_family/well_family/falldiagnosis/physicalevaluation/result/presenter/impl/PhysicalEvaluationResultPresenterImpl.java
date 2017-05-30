package com.demand.well_family.well_family.falldiagnosis.physicalevaluation.result.presenter.impl;

import android.content.Context;
import android.view.View;

import com.demand.well_family.well_family.dto.FallDiagnosisCategory;
import com.demand.well_family.well_family.dto.FallDiagnosisStory;
import com.demand.well_family.well_family.dto.PhysicalEvaluation;
import com.demand.well_family.well_family.dto.PhysicalEvaluationCategory;
import com.demand.well_family.well_family.dto.PhysicalEvaluationScore;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.falldiagnosis.physicalevaluation.result.flag.PhysicalEvaluationResultCodeFlag;
import com.demand.well_family.well_family.falldiagnosis.physicalevaluation.result.interactor.PhysicalEvaluationResultInteractor;
import com.demand.well_family.well_family.falldiagnosis.physicalevaluation.result.interactor.impl.PhysicalEvaluationResultInteractorImpl;
import com.demand.well_family.well_family.falldiagnosis.physicalevaluation.result.presenter.PhysicalEvaluationResultPresenter;
import com.demand.well_family.well_family.falldiagnosis.physicalevaluation.result.view.PhysicalEvaluationResultView;
import com.demand.well_family.well_family.util.APIErrorUtil;
import com.demand.well_family.well_family.util.PreferenceUtil;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-05-26.
 */

public class PhysicalEvaluationResultPresenterImpl implements PhysicalEvaluationResultPresenter {
    private PhysicalEvaluationResultInteractor physicalEvaluationResultInteractor;
    private PhysicalEvaluationResultView physicalEvaluationResultView;
    private PreferenceUtil preferenceUtil;

    public PhysicalEvaluationResultPresenterImpl(Context context) {
        this.physicalEvaluationResultInteractor = new PhysicalEvaluationResultInteractorImpl(this);
        this.physicalEvaluationResultView = (PhysicalEvaluationResultView) context;
        this.preferenceUtil = new PreferenceUtil(context);
    }

    @Override
    public void onCreate(FallDiagnosisCategory fallDiagnosisCategory, ArrayList<PhysicalEvaluationCategory> physicalEvaluationCategoryList, ArrayList<PhysicalEvaluation> physicalEvaluationList) {
        User user = preferenceUtil.getUserInfo();
        physicalEvaluationResultInteractor.setUser(user);
        physicalEvaluationResultInteractor.setFallDiagnosisCategory(fallDiagnosisCategory);
        physicalEvaluationResultInteractor.setPhysicalEvaluationCategoryList(physicalEvaluationCategoryList);
        physicalEvaluationResultInteractor.setPhysicalEvaluationList(physicalEvaluationList);
        physicalEvaluationResultView.init();

        View decorView = physicalEvaluationResultView.getDecorView();
        physicalEvaluationResultView.setToolbar(decorView);
        physicalEvaluationResultView.showToolbarTitle("신체능력 평가결과");
    }

    @Override
    public void onNetworkError(APIErrorUtil apiErrorUtil) {
        if (apiErrorUtil == null) {
            physicalEvaluationResultView.showMessage("네트워크 불안정합니다. 다시 시도하세요.");
        } else {
            physicalEvaluationResultView.showMessage(apiErrorUtil.message());
        }
    }

    @Override
    public void onClickSendResult() {
        User user = physicalEvaluationResultInteractor.getUser();
        FallDiagnosisCategory fallDiagnosisCategory = physicalEvaluationResultInteractor.getFallDiagnosisCategory();

        int userId = user.getId();
        int fallDiagnosisCategoryId = fallDiagnosisCategory.getId();

        FallDiagnosisStory fallDiagnosisStory = new FallDiagnosisStory();
        fallDiagnosisStory.setUser_id(userId);
        fallDiagnosisStory.setFall_diagnosis_category_id(fallDiagnosisCategoryId);

        physicalEvaluationResultInteractor.setFallDiagnosisStory(fallDiagnosisStory);
        physicalEvaluationResultInteractor.setStoryAdded();
    }


    @Override
    public void onLoadData() {
        ArrayList<PhysicalEvaluation> physicalEvaluationList = physicalEvaluationResultInteractor.getPhysicalEvaluationList();
        int physicalEvaluationSize = physicalEvaluationList.size();
        int balanceScore = 0;
        int movementSocre = 0;
        int legStrengthScore = 0;
        int totalScore = 0;

        for (int i = 0; i < physicalEvaluationSize; i++) {
            if (i == PhysicalEvaluationResultCodeFlag.STANDARD_POSE || i == PhysicalEvaluationResultCodeFlag.HALF_ROW_POSE) {
                int second = physicalEvaluationList.get(i).getSecond();
                if (second >= 10) {
                    balanceScore++;
                }
            }

            if (i == PhysicalEvaluationResultCodeFlag.ROW_POSE) {
                int second = physicalEvaluationList.get(i).getSecond();
                if (second >= 10) {
                    balanceScore += 2;
                } else if (second >= 3) {
                    balanceScore += 1;
                } else {
                    balanceScore += 0;
                }
            }

            if (i == PhysicalEvaluationResultCodeFlag.MOVEMENT) {
                float second = (float) physicalEvaluationList.get(i).getSecond();
                float millisecond = ((float) physicalEvaluationList.get(i).getMillisecond()) / (float) 100;
                float secondAndMillisecond = second + millisecond;

                if (secondAndMillisecond > 8.7) {
                    movementSocre++;
                } else if (secondAndMillisecond > 6.2) {
                    movementSocre += 2;
                } else if (secondAndMillisecond >= 4.82) {
                    movementSocre += 3;
                } else {
                    movementSocre += 4;
                }
            }

            if (i == PhysicalEvaluationResultCodeFlag.LEG_STRENGTH) {
                float second = (float) physicalEvaluationList.get(i).getSecond();
                float millisecond = ((float) physicalEvaluationList.get(i).getMillisecond()) / (float) 100;
                float secondAndMillisecond = second + millisecond;

                if (secondAndMillisecond > 16.7) {
                    legStrengthScore++;
                } else if (secondAndMillisecond > 13.7) {
                    legStrengthScore += 2;
                } else if (secondAndMillisecond >= 11.2) {
                    legStrengthScore += 3;
                } else {
                    legStrengthScore += 4;
                }
            }
        }
        totalScore = balanceScore + movementSocre + legStrengthScore;

        physicalEvaluationResultView.showTotalScore(String.valueOf(totalScore) + "점");
        physicalEvaluationResultView.showBalanceScore(String.valueOf(balanceScore) + "점");
        physicalEvaluationResultView.showLegStrengthScore(String.valueOf(legStrengthScore) + "점");
        physicalEvaluationResultView.showMovementScore(String.valueOf(movementSocre) + "점");
        physicalEvaluationResultView.showProgressbar(totalScore, PhysicalEvaluationResultCodeFlag.PERPECT_SCORE);

        if (totalScore >= 9) {
            physicalEvaluationResultView.showProgressBarChangeColorWithSafe();
            physicalEvaluationResultView.showTotalScoreTextChangeColorWithSafe();
            physicalEvaluationResultView.showResult("안전 단계");
        } else if (totalScore >= 5) {
            physicalEvaluationResultView.showProgressBarChangeColorWithCaution();
            physicalEvaluationResultView.showTotalScoreTextChangeColorWithCaution();
            physicalEvaluationResultView.showResult("주의 단계");
        } else {
            physicalEvaluationResultView.showProgressBarChangeColorWithRisk();
            physicalEvaluationResultView.showTotalScoreTextChangeColorWithRisk();
            physicalEvaluationResultView.showResult("위험 단계");
        }

        PhysicalEvaluationScore physicalEvaluationScore = new PhysicalEvaluationScore();
        physicalEvaluationScore.setBalance_score(balanceScore);
        physicalEvaluationScore.setMovement_score(movementSocre);
        physicalEvaluationScore.setLeg_strength_score(legStrengthScore);

        physicalEvaluationResultInteractor.setPhysicalEvaluationScore(physicalEvaluationScore);
    }

    @Override
    public void onSuccessSetStoryAdded(int storyId) {
        ArrayList<PhysicalEvaluation> physicalEvaluationList = physicalEvaluationResultInteractor.getPhysicalEvaluationList();
        int physicalEvaluationSize = physicalEvaluationList.size();

        for (int i = 0; i < physicalEvaluationSize; i++) {
            physicalEvaluationResultInteractor.setPhysicalEvaluationAdded(storyId, physicalEvaluationList.get(i));
        }
        physicalEvaluationResultInteractor.setPhysicalEvaluationScoreAdded(storyId);

    }

    @Override
    public void onSuccessSetPhysicalEvaluationScoreAdded() {
        physicalEvaluationResultView.showMessage("신체능력평가 기록이 등록되었습니다.");
        physicalEvaluationResultView.navigateToBack();
    }
}
