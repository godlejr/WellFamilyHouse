package com.demand.well_family.well_family.falldiagnosis.environment.result.presenter.impl;

import android.content.Context;
import android.net.Uri;
import android.view.View;

import com.demand.well_family.well_family.dto.EnvironmentEvaluationStatus;
import com.demand.well_family.well_family.dto.FallDiagnosisCategory;
import com.demand.well_family.well_family.dto.FallDiagnosisContentCategory;
import com.demand.well_family.well_family.dto.FallDiagnosisStory;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.falldiagnosis.environment.result.flag.EnvironmentEvaluationResultCodeFlag;
import com.demand.well_family.well_family.falldiagnosis.environment.result.interactor.EnvironmentEvaluationResultInteractor;
import com.demand.well_family.well_family.falldiagnosis.environment.result.interactor.impl.EnvironmentEvaluationResultInteractorImpl;
import com.demand.well_family.well_family.falldiagnosis.environment.result.presenter.EnvironmentEvaluationResultPresenter;
import com.demand.well_family.well_family.falldiagnosis.environment.result.view.EnvironmentEvaluationResultView;
import com.demand.well_family.well_family.util.APIErrorUtil;
import com.demand.well_family.well_family.util.FileToBase64Util;
import com.demand.well_family.well_family.util.PreferenceUtil;

import java.util.ArrayList;

/**
 * Created by Dev-0 on 2017-05-31.
 */

public class EnvironmentEvaluationResultPresenterImpl implements EnvironmentEvaluationResultPresenter {
    private EnvironmentEvaluationResultView environmentEvaluationResultView;
    private EnvironmentEvaluationResultInteractor environmentEvaluationResultInteractor;
    private PreferenceUtil preferenceUtil;
    private FileToBase64Util fileToBase64Util;

    public EnvironmentEvaluationResultPresenterImpl(Context context) {
        this.environmentEvaluationResultView = (EnvironmentEvaluationResultView) context;
        this.environmentEvaluationResultInteractor = new EnvironmentEvaluationResultInteractorImpl(this);
        this.preferenceUtil = new PreferenceUtil(context);
        this.fileToBase64Util = new FileToBase64Util(context);
    }

    @Override
    public void onCreate(FallDiagnosisCategory fallDiagnosisCategory, FallDiagnosisContentCategory fallDiagnosisContentCategory, ArrayList<Integer> answerList, int environmentEvaluationCategorySize, ArrayList<Uri> photoList, ArrayList<String> pathList) {
        User user = preferenceUtil.getUserInfo();
        environmentEvaluationResultInteractor.setUser(user);


        environmentEvaluationResultInteractor.setAnswerList(answerList);
        environmentEvaluationResultInteractor.setEnvironmentEvaluationCategorySize(environmentEvaluationCategorySize);

        environmentEvaluationResultInteractor.setFallDiagnosisCategory(fallDiagnosisCategory);
        environmentEvaluationResultInteractor.setFallDiagnosisContentCategory(fallDiagnosisContentCategory);
        environmentEvaluationResultInteractor.setPhotoList(photoList);
        environmentEvaluationResultInteractor.setPathList(pathList);

        environmentEvaluationResultView.init();

        View decorView = environmentEvaluationResultView.getDecorView();
        environmentEvaluationResultView.setToolbar(decorView);
        environmentEvaluationResultView.showToolbarTitle(fallDiagnosisContentCategory.getName() + " 위험평가 결과");
    }

    @Override
    public void onLoadData() {
        FallDiagnosisContentCategory fallDiagnosisContentCategory = environmentEvaluationResultInteractor.getFallDiagnosisContentCategory();
        ArrayList<Integer> answerList = environmentEvaluationResultInteractor.getAnswerList();
        int environmentEvaluationCategorySize = environmentEvaluationResultInteractor.getEnvironmentEvaluationCategorySize();
        int fallDiagnosisContentCategoryId = fallDiagnosisContentCategory.getId();
        int answerSize = answerList.size();

        environmentEvaluationResultView.showTotalCount(environmentEvaluationCategorySize);
        environmentEvaluationResultView.showScore(answerSize);
        environmentEvaluationResultView.showProgressbar(answerSize, environmentEvaluationCategorySize);


        if (fallDiagnosisContentCategoryId == EnvironmentEvaluationResultCodeFlag.INNER_ROOM) {
            if (answerSize == 4) {
                environmentEvaluationResultView.showResult("위험 단계");
                environmentEvaluationResultView.showProgressBarChangeColorWithRisk();
                environmentEvaluationResultView.showScoreTextChangeColorWithRisk();
                environmentEvaluationResultView.showTotalCountTextChangeColorWithRisk();
                environmentEvaluationResultInteractor.setFallDiagnosisRiskCategoryId(EnvironmentEvaluationResultCodeFlag.RISK);
            } else if (answerSize >= 2) {
                environmentEvaluationResultView.showResult("주의 단계");
                environmentEvaluationResultView.showProgressBarChangeColorWithCaution();
                environmentEvaluationResultView.showScoreTextChangeColorWithCaution();
                environmentEvaluationResultView.showTotalCountTextChangeColorWithCaution();
                environmentEvaluationResultInteractor.setFallDiagnosisRiskCategoryId(EnvironmentEvaluationResultCodeFlag.CAUTION);

            } else {

                environmentEvaluationResultView.showResult("안전 단계");
                environmentEvaluationResultView.showProgressBarChangeColorWithSafe();
                environmentEvaluationResultView.showScoreTextChangeColorWithSafe();
                environmentEvaluationResultView.showTotalCountTextChangeColorWithSafe();
                environmentEvaluationResultInteractor.setFallDiagnosisRiskCategoryId(EnvironmentEvaluationResultCodeFlag.SAFE);
            }
        }

        if (fallDiagnosisContentCategoryId == EnvironmentEvaluationResultCodeFlag.BATH_ROOM) {
            if (answerSize == 9) {
                environmentEvaluationResultView.showResult("위험 단계");
                environmentEvaluationResultView.showProgressBarChangeColorWithRisk();
                environmentEvaluationResultView.showScoreTextChangeColorWithRisk();
                environmentEvaluationResultView.showTotalCountTextChangeColorWithRisk();
                environmentEvaluationResultInteractor.setFallDiagnosisRiskCategoryId(EnvironmentEvaluationResultCodeFlag.RISK);

            } else if (answerSize >= 6) {
                environmentEvaluationResultView.showResult("주의 단계");
                environmentEvaluationResultView.showProgressBarChangeColorWithCaution();
                environmentEvaluationResultView.showScoreTextChangeColorWithCaution();
                environmentEvaluationResultView.showTotalCountTextChangeColorWithCaution();
                environmentEvaluationResultInteractor.setFallDiagnosisRiskCategoryId(EnvironmentEvaluationResultCodeFlag.CAUTION);

            } else {

                environmentEvaluationResultView.showResult("안전 단계");
                environmentEvaluationResultView.showProgressBarChangeColorWithSafe();
                environmentEvaluationResultView.showScoreTextChangeColorWithSafe();
                environmentEvaluationResultView.showTotalCountTextChangeColorWithSafe();
                environmentEvaluationResultInteractor.setFallDiagnosisRiskCategoryId(EnvironmentEvaluationResultCodeFlag.SAFE);
            }
        }

        if (fallDiagnosisContentCategoryId == EnvironmentEvaluationResultCodeFlag.LIVING_ROOM) {
            if (answerSize == 6) {
                environmentEvaluationResultView.showResult("위험 단계");
                environmentEvaluationResultView.showProgressBarChangeColorWithRisk();
                environmentEvaluationResultView.showScoreTextChangeColorWithRisk();
                environmentEvaluationResultView.showTotalCountTextChangeColorWithRisk();
                environmentEvaluationResultInteractor.setFallDiagnosisRiskCategoryId(EnvironmentEvaluationResultCodeFlag.RISK);

            } else if (answerSize >= 4) {
                environmentEvaluationResultView.showResult("주의 단계");
                environmentEvaluationResultView.showProgressBarChangeColorWithCaution();
                environmentEvaluationResultView.showScoreTextChangeColorWithCaution();
                environmentEvaluationResultView.showTotalCountTextChangeColorWithCaution();
                environmentEvaluationResultInteractor.setFallDiagnosisRiskCategoryId(EnvironmentEvaluationResultCodeFlag.CAUTION);

            } else {

                environmentEvaluationResultView.showResult("안전 단계");
                environmentEvaluationResultView.showProgressBarChangeColorWithSafe();
                environmentEvaluationResultView.showScoreTextChangeColorWithSafe();
                environmentEvaluationResultView.showTotalCountTextChangeColorWithSafe();
                environmentEvaluationResultInteractor.setFallDiagnosisRiskCategoryId(EnvironmentEvaluationResultCodeFlag.SAFE);
            }
        }

        if (fallDiagnosisContentCategoryId == EnvironmentEvaluationResultCodeFlag.FRONT_DOOR) {
            if (answerSize == 6) {
                environmentEvaluationResultView.showResult("위험 단계");
                environmentEvaluationResultView.showProgressBarChangeColorWithRisk();
                environmentEvaluationResultView.showScoreTextChangeColorWithRisk();
                environmentEvaluationResultView.showTotalCountTextChangeColorWithRisk();
                environmentEvaluationResultInteractor.setFallDiagnosisRiskCategoryId(EnvironmentEvaluationResultCodeFlag.RISK);

            } else if (answerSize >= 4) {
                environmentEvaluationResultView.showResult("주의 단계");
                environmentEvaluationResultView.showProgressBarChangeColorWithCaution();
                environmentEvaluationResultView.showScoreTextChangeColorWithCaution();
                environmentEvaluationResultView.showTotalCountTextChangeColorWithCaution();
                environmentEvaluationResultInteractor.setFallDiagnosisRiskCategoryId(EnvironmentEvaluationResultCodeFlag.CAUTION);

            } else {

                environmentEvaluationResultView.showResult("안전 단계");
                environmentEvaluationResultView.showProgressBarChangeColorWithSafe();
                environmentEvaluationResultView.showScoreTextChangeColorWithSafe();
                environmentEvaluationResultView.showTotalCountTextChangeColorWithSafe();
                environmentEvaluationResultInteractor.setFallDiagnosisRiskCategoryId(EnvironmentEvaluationResultCodeFlag.SAFE);
            }
        }

        if (fallDiagnosisContentCategoryId == EnvironmentEvaluationResultCodeFlag.KITCHEN) {
            if (answerSize == 5) {
                environmentEvaluationResultView.showResult("위험 단계");
                environmentEvaluationResultView.showProgressBarChangeColorWithRisk();
                environmentEvaluationResultView.showScoreTextChangeColorWithRisk();
                environmentEvaluationResultView.showTotalCountTextChangeColorWithRisk();
                environmentEvaluationResultInteractor.setFallDiagnosisRiskCategoryId(EnvironmentEvaluationResultCodeFlag.RISK);

            } else if (answerSize >= 3) {
                environmentEvaluationResultView.showResult("주의 단계");
                environmentEvaluationResultView.showProgressBarChangeColorWithCaution();
                environmentEvaluationResultView.showScoreTextChangeColorWithCaution();
                environmentEvaluationResultView.showTotalCountTextChangeColorWithCaution();
                environmentEvaluationResultInteractor.setFallDiagnosisRiskCategoryId(EnvironmentEvaluationResultCodeFlag.CAUTION);

            } else {

                environmentEvaluationResultView.showResult("안전 단계");
                environmentEvaluationResultView.showProgressBarChangeColorWithSafe();
                environmentEvaluationResultView.showScoreTextChangeColorWithSafe();
                environmentEvaluationResultView.showTotalCountTextChangeColorWithSafe();
                environmentEvaluationResultInteractor.setFallDiagnosisRiskCategoryId(EnvironmentEvaluationResultCodeFlag.SAFE);
            }
        }

        if (fallDiagnosisContentCategoryId == EnvironmentEvaluationResultCodeFlag.BALCONY) {
            if (answerSize == 5) {
                environmentEvaluationResultView.showResult("위험 단계");
                environmentEvaluationResultView.showProgressBarChangeColorWithRisk();
                environmentEvaluationResultView.showScoreTextChangeColorWithRisk();
                environmentEvaluationResultView.showTotalCountTextChangeColorWithRisk();
                environmentEvaluationResultInteractor.setFallDiagnosisRiskCategoryId(EnvironmentEvaluationResultCodeFlag.RISK);

            } else if (answerSize >= 3) {
                environmentEvaluationResultView.showResult("주의 단계");
                environmentEvaluationResultView.showProgressBarChangeColorWithCaution();
                environmentEvaluationResultView.showScoreTextChangeColorWithCaution();
                environmentEvaluationResultView.showTotalCountTextChangeColorWithCaution();
                environmentEvaluationResultInteractor.setFallDiagnosisRiskCategoryId(EnvironmentEvaluationResultCodeFlag.CAUTION);

            } else {

                environmentEvaluationResultView.showResult("안전 단계");
                environmentEvaluationResultView.showProgressBarChangeColorWithSafe();
                environmentEvaluationResultView.showScoreTextChangeColorWithSafe();
                environmentEvaluationResultView.showTotalCountTextChangeColorWithSafe();
                environmentEvaluationResultInteractor.setFallDiagnosisRiskCategoryId(EnvironmentEvaluationResultCodeFlag.SAFE);
            }
        }

        if (fallDiagnosisContentCategoryId == EnvironmentEvaluationResultCodeFlag.STAIR) {
            if (answerSize == 7) {
                environmentEvaluationResultView.showResult("위험 단계");
                environmentEvaluationResultView.showProgressBarChangeColorWithRisk();
                environmentEvaluationResultView.showScoreTextChangeColorWithRisk();
                environmentEvaluationResultView.showTotalCountTextChangeColorWithRisk();
                environmentEvaluationResultInteractor.setFallDiagnosisRiskCategoryId(EnvironmentEvaluationResultCodeFlag.RISK);

            } else if (answerSize >= 5) {
                environmentEvaluationResultView.showResult("주의 단계");
                environmentEvaluationResultView.showProgressBarChangeColorWithCaution();
                environmentEvaluationResultView.showScoreTextChangeColorWithCaution();
                environmentEvaluationResultView.showTotalCountTextChangeColorWithCaution();
                environmentEvaluationResultInteractor.setFallDiagnosisRiskCategoryId(EnvironmentEvaluationResultCodeFlag.CAUTION);

            } else {

                environmentEvaluationResultView.showResult("안전 단계");
                environmentEvaluationResultView.showProgressBarChangeColorWithSafe();
                environmentEvaluationResultView.showScoreTextChangeColorWithSafe();
                environmentEvaluationResultView.showTotalCountTextChangeColorWithSafe();
                environmentEvaluationResultInteractor.setFallDiagnosisRiskCategoryId(EnvironmentEvaluationResultCodeFlag.SAFE);
            }
        }
    }

    @Override
    public void onClickSendResult() {
        User user = environmentEvaluationResultInteractor.getUser();
        FallDiagnosisCategory fallDiagnosisCategory = environmentEvaluationResultInteractor.getFallDiagnosisCategory();

        int userId = user.getId();
        int fallDiagnosisCategoryId = fallDiagnosisCategory.getId();
        int fallDiagnosisRiskCategoryId = environmentEvaluationResultInteractor.getFallDiagnosisRiskCategoryId();

        FallDiagnosisStory fallDiagnosisStory = new FallDiagnosisStory();
        fallDiagnosisStory.setUser_id(userId);
        fallDiagnosisStory.setFall_diagnosis_category_id(fallDiagnosisCategoryId);
        fallDiagnosisStory.setFall_diagnosis_risk_category_id(fallDiagnosisRiskCategoryId);

        environmentEvaluationResultView.showProgressDialog();

        environmentEvaluationResultInteractor.setFallDiagnosisStory(fallDiagnosisStory);
        environmentEvaluationResultInteractor.setStoryAdded();
    }

    @Override
    public void onNetworkError(APIErrorUtil apiErrorUtil) {
        if (apiErrorUtil == null) {
            environmentEvaluationResultView.showMessage("네트워크 불안정합니다. 다시 시도하세요.");
        } else {
            environmentEvaluationResultView.showMessage(apiErrorUtil.message());
        }
    }

    @Override
    public void onSuccessSetStoryAdded(int storyId) {
        ArrayList<Integer> answerList = environmentEvaluationResultInteractor.getAnswerList();
        ArrayList<Uri> photoList = environmentEvaluationResultInteractor.getPhotoList();
        ArrayList<String> pathList = environmentEvaluationResultInteractor.getPathList();
        int answerSize = answerList.size();

        FallDiagnosisContentCategory fallDiagnosisContentCategory = environmentEvaluationResultInteractor.getFallDiagnosisContentCategory();

        EnvironmentEvaluationStatus environmentEvaluationStatus = new EnvironmentEvaluationStatus();
        environmentEvaluationStatus.setFall_diagnosis_story_id(storyId);
        environmentEvaluationStatus.setFall_diagnosis_content_category_id(fallDiagnosisContentCategory.getId());

        environmentEvaluationResultInteractor.setEnvironmentEvaluationStatus(environmentEvaluationStatus);


        for (int i = 0; i < answerSize; i++) {
            environmentEvaluationResultInteractor.setEnvironmentEvaluationAdded(storyId, answerList.get(i));
        }

        if (photoList != null) {
            int photoSize = photoList.size();

            for (int i = 0; i < photoSize; i++) {
                environmentEvaluationResultView.setProgressDialog(i + 1);
                environmentEvaluationResultInteractor.setPhotoAdded(fileToBase64Util, storyId, photoList.get(i), pathList.get(i));
            }
        }

        environmentEvaluationResultView.goneProgressDialog();
        environmentEvaluationResultView.showMessage("낙상 위험환경 평가가 등록되었습니다.");
        environmentEvaluationResultView.navigateToBack();

    }

}
