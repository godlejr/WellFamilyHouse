package com.demand.well_family.well_family.notification.falldiagnosis.interactor.impl;

import com.demand.well_family.well_family.notification.falldiagnosis.interactor.NotificationFallDiagnosisStoryInteractor;
import com.demand.well_family.well_family.notification.falldiagnosis.presenter.NotificationFallDiagnosisStoryPresenter;

/**
 * Created by ㅇㅇ on 2017-06-13.
 */

public class NotificationFallDiagnosisStoryInteractorImpl implements NotificationFallDiagnosisStoryInteractor {
    private NotificationFallDiagnosisStoryPresenter notificationFallDiagnosisStoryPresenter;

    public NotificationFallDiagnosisStoryInteractorImpl(NotificationFallDiagnosisStoryPresenter notificationFallDiagnosisStoryPresenter) {
        this.notificationFallDiagnosisStoryPresenter = notificationFallDiagnosisStoryPresenter;
    }
}
