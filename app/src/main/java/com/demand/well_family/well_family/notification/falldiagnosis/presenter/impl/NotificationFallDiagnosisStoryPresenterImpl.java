package com.demand.well_family.well_family.notification.falldiagnosis.presenter.impl;

import android.content.Context;

import com.demand.well_family.well_family.dto.FallDiagnosisStory;
import com.demand.well_family.well_family.notification.falldiagnosis.interactor.NotificationFallDiagnosisStoryInteractor;
import com.demand.well_family.well_family.notification.falldiagnosis.interactor.impl.NotificationFallDiagnosisStoryInteractorImpl;
import com.demand.well_family.well_family.notification.falldiagnosis.presenter.NotificationFallDiagnosisStoryPresenter;
import com.demand.well_family.well_family.notification.falldiagnosis.view.NotificationFallDiagnosisStoryView;
import com.demand.well_family.well_family.util.APIErrorUtil;

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
    public void onNetworkError(APIErrorUtil apiErrorUtil) {
        if (apiErrorUtil == null) {
            notificationFallDiagnosisStoryView.showMessage("네트워크 불안정합니다. 다시 시도하세요.");
        } else {
            notificationFallDiagnosisStoryView.showMessage(apiErrorUtil.message());
        }
    }


}
