package com.demand.well_family.well_family.main.intro.presenter.impl;

import android.content.Context;

import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.main.intro.interactor.IntroInteractor;
import com.demand.well_family.well_family.main.intro.interactor.impl.IntroInteractorImpl;
import com.demand.well_family.well_family.main.intro.presenter.IntroPresenter;
import com.demand.well_family.well_family.main.intro.view.IntroView;
import com.demand.well_family.well_family.util.APIErrorUtil;
import com.demand.well_family.well_family.util.PreferenceUtil;

/**
 * Created by Dev-0 on 2017-04-13.
 */

public class IntroPresenterImpl implements IntroPresenter {
    private IntroView introView;
    private IntroInteractor introInteractor;
    private PreferenceUtil preferenceUtil;

    public IntroPresenterImpl(Context context) {
        this.introView = (IntroView) context;
        this.introInteractor = new IntroInteractorImpl(this);
        this.preferenceUtil = new PreferenceUtil(context);
    }

    @Override
    public void onCreate() {
        introView.init();
    }

    @Override
    public void validateUserExist() {
        User user = preferenceUtil.getUserInfo();
        int userId = user.getId();

        if (userId != 0) {
            introInteractor.getMutipleUserAccessValidation(user);
        } else {
            introView.navigateToLoginActivity();
        }
    }

    @Override
    public void onSuccessMultipleUserAccessValidation(int check) {
        if (check > 0) {
            introView.navigateToMainActivity();
        } else {
            introView.showMessage("다른 기기에서 접속중입니다. 로그인 시 다른기기에서 강제 로그아웃 됩니다.");
            preferenceUtil.removeUserInfo();
            introView.navigateToLoginActivity();
        }
    }

    @Override
    public void onNetworkError(APIErrorUtil apiErrorUtil) {
        if (apiErrorUtil == null) {
            introView.showMessage("네트워크 불안정합니다. 다시 시도하세요.");
        } else {
            introView.showMessage(apiErrorUtil.message());
        }
    }
}
