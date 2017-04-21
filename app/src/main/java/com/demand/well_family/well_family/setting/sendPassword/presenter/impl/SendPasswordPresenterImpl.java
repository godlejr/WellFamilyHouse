package com.demand.well_family.well_family.setting.sendPassword.presenter.impl;

import android.content.Context;
import android.view.View;

import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.setting.sendPassword.interactor.SendPasswordInteractor;
import com.demand.well_family.well_family.setting.sendPassword.interactor.impl.SendPasswordInteractorImpl;
import com.demand.well_family.well_family.setting.sendPassword.presenter.SendPasswordPresenter;
import com.demand.well_family.well_family.setting.sendPassword.view.SendPasswordView;
import com.demand.well_family.well_family.util.APIErrorUtil;
import com.demand.well_family.well_family.util.PreferenceUtil;

/**
 * Created by ㅇㅇ on 2017-04-17.
 */

public class SendPasswordPresenterImpl implements SendPasswordPresenter {
    private SendPasswordView findAccountView;
    private SendPasswordInteractor findAccountInteractor;

    public SendPasswordPresenterImpl(Context context) {
        this.findAccountView = (SendPasswordView) context;
        this.findAccountInteractor = new SendPasswordInteractorImpl(this);
    }

    @Override
    public void onCreate(User user) {
        findAccountInteractor.setUser(user);
        findAccountView.init();

        // toolbar
        View decorView = findAccountView.getDecorView();
        findAccountView.setToolbar(decorView);
        findAccountView.showToolbarTitle("계정 확인");

        findAccountView.setUserInfo(user);
    }

    @Override
    public void sendEmail() {
        findAccountInteractor.sendEmail();
    }

    @Override
    public void onNetworkError(APIErrorUtil apiErrorUtil) {
        if (apiErrorUtil == null) {
            findAccountView.showMessage("네트워크 불안정합니다. 다시 시도하세요.");
        } else {
            findAccountView.showMessage(apiErrorUtil.message());
        }
    }

    @Override
    public void onSuccessSendEmail(String email) {
        findAccountView.showMessage(email + "로 임시 비밀번호가 발송되었습니다.");
        findAccountView.navigateToBack();
    }

}
