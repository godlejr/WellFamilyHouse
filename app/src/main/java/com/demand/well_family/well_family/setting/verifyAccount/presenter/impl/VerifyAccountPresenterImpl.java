package com.demand.well_family.well_family.setting.verifyAccount.presenter.impl;

import android.content.Context;
import android.view.View;

import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.setting.verifyAccount.interactor.VerifyAccountInteractor;
import com.demand.well_family.well_family.setting.verifyAccount.interactor.impl.VerifyAccountInteractorImpl;
import com.demand.well_family.well_family.setting.verifyAccount.presenter.VerifyAccountPresenter;
import com.demand.well_family.well_family.setting.verifyAccount.view.VerifyAccountView;
import com.demand.well_family.well_family.util.APIErrorUtil;

/**
 * Created by ㅇㅇ on 2017-04-18.
 */

public class VerifyAccountPresenterImpl implements VerifyAccountPresenter {
    private VerifyAccountInteractor verifyAccountInteractor;
    private VerifyAccountView verifyAccountView;

    public VerifyAccountPresenterImpl(Context context) {
        this.verifyAccountView = (VerifyAccountView) context;
        this.verifyAccountInteractor = new VerifyAccountInteractorImpl(this);
    }

    @Override
    public void onCreate() {
        verifyAccountView.init();

        View decorView = verifyAccountView.getDecorView();
        verifyAccountView.setToolbar(decorView);
        verifyAccountView.showToolbarTitle("비밀번호 확인");
    }

    @Override
    public void getUserInfo(User user) {
        String userEmail = user.getEmail();
        String userPassword = user.getPassword();

        int emailLength = userEmail.length();
        int passwordLength = userPassword.length();

        if (emailLength == 0 || passwordLength == 0) {
            verifyAccountView.showMessage("이메일과 비밀번호를 확인해주세요");
        } else {
            verifyAccountInteractor.getUserInfo(userEmail, userPassword);
        }
    }

    @Override
    public void onSuccessVerifyAccount(User user) {
        if (user != null) {
            verifyAccountView.navigateToResetPasswordActivity();
            verifyAccountView.navigateToBack();
        } else {
            verifyAccountView.showMessage("이메일과 비밀번호를 확인해주세요");
        }
    }

    @Override
    public void onTextChanged(CharSequence email) {
        if (email.length() != 0) {
            verifyAccountView.setButtonColorBrown();
        } else {
            verifyAccountView.setButtonColorGray();
        }
    }

    @Override
    public void onNetworkError(APIErrorUtil apiErrorUtil) {
        if (apiErrorUtil == null) {
            verifyAccountView.showMessage("네트워크 불안정합니다. 다시 시도하세요.");
        } else {
            verifyAccountView.showMessage(apiErrorUtil.message());
        }
    }

}
