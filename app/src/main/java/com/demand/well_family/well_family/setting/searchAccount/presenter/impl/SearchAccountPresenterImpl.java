package com.demand.well_family.well_family.setting.searchAccount.presenter.impl;

import android.content.Context;
import android.view.View;

import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.setting.searchAccount.interactor.SearchAccountInteractor;
import com.demand.well_family.well_family.setting.searchAccount.interactor.impl.SearchAccountInteractorImpl;
import com.demand.well_family.well_family.setting.searchAccount.presenter.SearchAccountPresenter;
import com.demand.well_family.well_family.setting.searchAccount.view.FindAccountView;
import com.demand.well_family.well_family.util.APIErrorUtil;


/**
 * Created by ㅇㅇ on 2017-04-18.
 */

public class SearchAccountPresenterImpl implements SearchAccountPresenter {
    private SearchAccountInteractor findAccountInteractor;
    private FindAccountView findAccountView;

    public SearchAccountPresenterImpl(Context context) {
        this.findAccountInteractor = new SearchAccountInteractorImpl(this);
        this.findAccountView = (FindAccountView) context;
    }

    @Override
    public void onCreate() {
        findAccountView.init();

        View decorView = findAccountView.getDecorView();
        findAccountView.setToolbar(decorView);
        findAccountView.showToolbarTitle("계정 찾기");
    }

    @Override
    public void getUserInfoFromEmail(String email) {
        findAccountInteractor.getUserInfoFromEmail(email);
    }

    @Override
    public void onSuccessGetUserInfo(User user) {
        if (user == null) {
            findAccountView.showMessage("이메일을 확인해주세요.");
        } else {
            findAccountView.navigateToSendPasswordActivity(user);
            findAccountView.navigateToBack();
        }
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
    public void onTextChanged(CharSequence password) {
        if (password.length() != 0) {
            findAccountView.showPasswordResetButton();
        } else {
            findAccountView.gonePasswordResetButton();
        }
    }
}
