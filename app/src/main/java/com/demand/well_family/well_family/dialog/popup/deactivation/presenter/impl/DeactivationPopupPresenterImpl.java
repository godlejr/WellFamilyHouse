package com.demand.well_family.well_family.dialog.popup.deactivation.presenter.impl;

import android.content.Context;

import com.demand.well_family.well_family.dialog.popup.deactivation.interactor.DeactivationPopupInteractor;
import com.demand.well_family.well_family.dialog.popup.deactivation.interactor.impl.DeactivationPopupInteractorImpl;
import com.demand.well_family.well_family.dialog.popup.deactivation.presenter.DeactivationPopupPresenter;
import com.demand.well_family.well_family.dialog.popup.deactivation.view.DeactivationPopupView;
import com.demand.well_family.well_family.util.PreferenceUtil;


/**
 * Created by ㅇㅇ on 2017-04-18.
 */

public class DeactivationPopupPresenterImpl implements DeactivationPopupPresenter {
    private DeactivationPopupView deactivationPopupView;
    private DeactivationPopupInteractor deactivationPopupInteractor;
    private PreferenceUtil preferenceUtil;

    public DeactivationPopupPresenterImpl(Context context) {
        this.deactivationPopupView = (DeactivationPopupView) context;
        this.deactivationPopupInteractor = new DeactivationPopupInteractorImpl(this);
        this.preferenceUtil = new PreferenceUtil(context);
    }

    @Override
    public void onCreate() {
        deactivationPopupView.setDisplay();
        deactivationPopupView.init();

        deactivationPopupView.setPopupContent("계정을 비활성화하시면 더이상 가족들의 소식을 받아보실 수 없습니다.\n정말 계정을 비활성화 하시겠습니까?");
    }

    @Override
    public void onClickSetDeactivation() {
        preferenceUtil.removeUserInfo();

        deactivationPopupView.showMessage("계정이 비활성화 되었습니다.");
        deactivationPopupView.navigateToBack();
        deactivationPopupView.navigateToLoginActivity();
    }
}
