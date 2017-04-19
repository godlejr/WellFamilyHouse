package com.demand.well_family.well_family.setting.resetPassword.presenter.impl;

import android.content.Context;
import android.view.View;

import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.setting.resetPassword.interactor.ResetPasswordInteractor;
import com.demand.well_family.well_family.setting.resetPassword.interactor.impl.ResetPasswordInteractorImpl;
import com.demand.well_family.well_family.setting.resetPassword.presenter.ResetPasswordPresenter;
import com.demand.well_family.well_family.setting.resetPassword.view.ResetPasswordView;
import com.demand.well_family.well_family.util.APIErrorUtil;
import com.demand.well_family.well_family.util.PreferenceUtil;

/**
 * Created by ㅇㅇ on 2017-04-18.
 */

public class ResetPasswordPresenterImpl implements ResetPasswordPresenter {
    private ResetPasswordInteractor resetPasswordInteractor;
    private ResetPasswordView resetPasswordView;
    private PreferenceUtil preferenceUtil;

    public ResetPasswordPresenterImpl(Context context) {
        this.resetPasswordView = (ResetPasswordView) context;
        this.resetPasswordInteractor = new ResetPasswordInteractorImpl(this);
        this.preferenceUtil = new PreferenceUtil(context);
    }

    @Override
    public void onCreate() {
        resetPasswordView.init();

        View decorView = resetPasswordView.getDecorView();
        resetPasswordView.setToolbar(decorView);
        resetPasswordView.showToolbarTitle("비밀번호 변경");

        User user = preferenceUtil.getUserInfo();
        resetPasswordInteractor.setUser(user);
    }

    @Override
    public void setPassword(String password1, String password2) {
        if (password1.length() == 0 || password2.length() == 0 || !password1.equals(password2)) {
            resetPasswordView.showMessage("비밀번호를 확인해주세요.");
        } else {
            resetPasswordInteractor.setPassword(password1);
        }
    }

    @Override
    public void onSuccessSetPassword() {
        resetPasswordView.showMessage("비밀번호 변경이 완료되었습니다.");
        resetPasswordView.navigateToBack();
    }

    @Override
    public void onTextChanged(CharSequence password) {
        if (password.length() != 0) {
            resetPasswordView.setButtonColorBrown();
        } else {
            resetPasswordView.setButtonColorGray();
        }
    }

    @Override
    public void onNetworkError(APIErrorUtil apiErrorUtil) {
        if(apiErrorUtil == null){
            resetPasswordView.showMessage("네트워크 불안정합니다. 다시 시도하세요.");
        }else{
            resetPasswordView.showMessage(apiErrorUtil.message());
        }
    }
}
