package com.demand.well_family.well_family.notification.falldiagnosis.presenter.impl;

import android.content.Context;

import com.demand.well_family.well_family.dto.CommentInfo;
import com.demand.well_family.well_family.dto.EnvironmentEvaluationCategory;
import com.demand.well_family.well_family.dto.EnvironmentPhoto;
import com.demand.well_family.well_family.dto.FallDiagnosisContentCategory;
import com.demand.well_family.well_family.dto.FallDiagnosisStory;
import com.demand.well_family.well_family.dto.FallDiagnosisStoryComment;
import com.demand.well_family.well_family.dto.PhysicalEvaluationScore;
import com.demand.well_family.well_family.notification.falldiagnosis.interactor.NotificationFallDiagnosisStoryInteractor;
import com.demand.well_family.well_family.notification.falldiagnosis.interactor.impl.NotificationFallDiagnosisStoryInteractorImpl;
import com.demand.well_family.well_family.notification.falldiagnosis.presenter.NotificationFallDiagnosisStoryPresenter;
import com.demand.well_family.well_family.notification.falldiagnosis.view.NotificationFallDiagnosisStoryView;
import com.demand.well_family.well_family.util.APIErrorUtil;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-06-13.
 */

public class NotificationFallDiagnosisStoryPresenterImpl implements NotificationFallDiagnosisStoryPresenter {
    private NotificationFallDiagnosisStoryInteractor notificationFallDiagnosisStoryInteractor;
    private  NotificationFallDiagnosisStoryView notificationFallDiagnosisStoryView;

    public NotificationFallDiagnosisStoryPresenterImpl(Context context) {
        this.notificationFallDiagnosisStoryInteractor = new NotificationFallDiagnosisStoryInteractorImpl(this);
        this.notificationFallDiagnosisStoryView = (NotificationFallDiagnosisStoryView)context;
    }

    @Override
    public void onCreate(FallDiagnosisStory fallDiagnosisStory) {
        notificationFallDiagnosisStoryView.init();

    }

    @Override
    public void onLoadData() {

    }

    @Override
    public void onSuccessSetContentLikeUp() {

    }

    @Override
    public void onSuccessSetContentLikeDown() {

    }

    @Override
    public void onSuccessGetPhysicalEvaluationScore(PhysicalEvaluationScore physicalEvaluationScore) {

    }

    @Override
    public void onSuccessGetSelfDiagnosisList(ArrayList<FallDiagnosisContentCategory> fallDiagnosisContentCategoryList) {

    }

    @Override
    public void onSuccessGetEnvironmentPhoto(ArrayList<EnvironmentPhoto> environmentPhotoList) {

    }

    @Override
    public void onSuccessGetEnvironmentEvaluationList(ArrayList<EnvironmentEvaluationCategory> environmentEvaluationCategoryList) {

    }

    @Override
    public void onSuccessGetFallDiagnosisStoryCommentList(ArrayList<CommentInfo> commentInfoList) {

    }

    @Override
    public void onSuccessSetFallDiagnosisStoryComment(FallDiagnosisStoryComment fallDiagnosisStoryComment) {

    }

    @Override
    public void onSuccessGetFallDiagnosisStoryCommentCount(int commentCount) {

    }

    @Override
    public void onSuccessGetFallDiagnosisStoryLikeCount(int likeCount) {

    }

    @Override
    public void onNetworkError(APIErrorUtil apiErrorUtil) {
        if (apiErrorUtil == null) {
            notificationFallDiagnosisStoryView.showMessage("네트워크 불안정합니다. 다시 시도하세요.");
        } else {
            notificationFallDiagnosisStoryView.showMessage(apiErrorUtil.message());
        }
    }


}
