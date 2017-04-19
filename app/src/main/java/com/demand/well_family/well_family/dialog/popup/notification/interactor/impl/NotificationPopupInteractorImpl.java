package com.demand.well_family.well_family.dialog.popup.notification.interactor.impl;

import com.demand.well_family.well_family.dialog.popup.notification.interactor.NotificationPopupInteractor;
import com.demand.well_family.well_family.dialog.popup.notification.presenter.NotificationPopupPresenter;

/**
 * Created by ㅇㅇ on 2017-04-19.
 */

public class NotificationPopupInteractorImpl implements NotificationPopupInteractor{
    private NotificationPopupPresenter notificationPopupPresenter;

    public NotificationPopupInteractorImpl(NotificationPopupPresenter notificationPopupPresenter) {
        this.notificationPopupPresenter = notificationPopupPresenter;
    }
}
