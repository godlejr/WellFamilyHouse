package com.demand.well_family.well_family.dialog.popup.notification.presenter.impl;

import android.content.Context;

import com.demand.well_family.well_family.dialog.popup.notification.interactor.NotificationPopupInteractor;
import com.demand.well_family.well_family.dialog.popup.notification.interactor.impl.NotificationPopupInteractorImpl;
import com.demand.well_family.well_family.dialog.popup.notification.presenter.NotificationPopupPresenter;
import com.demand.well_family.well_family.dialog.popup.notification.view.NotificationPopupView;
import com.demand.well_family.well_family.util.PreferenceUtil;

/**
 * Created by ㅇㅇ on 2017-04-19.
 */

public class NotificationPopupPresenterImpl implements NotificationPopupPresenter {
    private NotificationPopupView notificationPopupView;
    private NotificationPopupInteractor notificationPopupInteractor;
    private PreferenceUtil preferenceUtil;

    public NotificationPopupPresenterImpl(Context context) {
        this.notificationPopupView = (NotificationPopupView) context;
        this.notificationPopupInteractor = new NotificationPopupInteractorImpl(this);
        this.preferenceUtil = new PreferenceUtil(context);
    }

    @Override
    public void onCreate() {
        notificationPopupView.setDisplay();
        notificationPopupView.init();
        notificationPopupView.setNotificationContent();
    }

    @Override
    public void onClickLogout() {
        preferenceUtil.removeUserInfo();
        notificationPopupView.navigateToLoginActivity();
        notificationPopupView.navigateToBack();
    }

    @Override
    public void setNotificationContent() {
        notificationPopupView.setNotificationContent();
    }
}
