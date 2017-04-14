package com.demand.well_family.well_family.main.login.presenter.impl;

import android.content.Context;

import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.main.login.interator.LoginInterater;
import com.demand.well_family.well_family.main.login.interator.impl.LoginInteratorImpl;
import com.demand.well_family.well_family.main.login.presenter.LoginPresenter;
import com.demand.well_family.well_family.main.login.view.LoginView;
import com.demand.well_family.well_family.util.APIError;
import com.demand.well_family.well_family.util.PreferenceUtil;
import com.facebook.CallbackManager;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;

/**
 * Created by Dev-0 on 2017-04-14.
 */

public class LoginPresenterImpl implements LoginPresenter {
    private LoginView loginView;
    private LoginInterater loginInterater;
    private PreferenceUtil preferenceUtil;

    public LoginPresenterImpl(Context context) {
        this.loginView = (LoginView) context;
        this.loginInterater = new LoginInteratorImpl(this);
        this.preferenceUtil = new PreferenceUtil(context);
    }

    @Override
    public void onBeforeCreate() {
        //facebook login init
        loginView.initFacebookLogin();
        loginInterater.setFacebookCallbackManager();

        //naver login init
        loginInterater.setNaverOAuthLogin();
        OAuthLogin oAuthLogin = loginInterater.getNaverOAuthLogin();
        String naverClientId = loginInterater.getNaverClientId();
        String naverClientSecret = loginInterater.getNaverClientSecret();
        String naverClientName = loginInterater.getNaverClientName();

        loginView.initNaverLogin(oAuthLogin, naverClientId, naverClientSecret, naverClientName);
    }

    @Override
    public void onCreate() {
        loginView.init();
    }

    @Override
    public void onClickLogin(String email, String password) {
        loginInterater.validateLogin(email,password);
    }

    @Override
    public void onClickFacebookLogin() {
        loginInterater.setFacebookLogin();
    }

    @Override
    public void onSuccessSNSLogin(User userFromLogin, User userForLogin) {
        if (userFromLogin == null) {
            loginView.navigateToJoinFromSNSActivity(userForLogin);
        } else {
            String deviceId = loginView.getDeviceId();
            String firebaseToken = loginView.getFireBaseToken();

            loginInterater.setLogin(userFromLogin, deviceId, firebaseToken);
        }
    }

    @Override
    public void onSuccessValidateLogin(User user) {
        if (user == null) {
            loginView.showMessage("ID / 패스워드를 확인해주세요.");
        } else {
            String deviceId = loginView.getDeviceId();
            String firebaseToken = loginView.getFireBaseToken();

            loginInterater.setLogin(user, deviceId, firebaseToken);
        }
    }

    @Override
    public void onSuccessLogin(User user, String deviceId, String firebaseToken) {
        preferenceUtil.setUserInfoForLogin(user, deviceId, firebaseToken);
        loginView.navigateToMainActivity();
    }


    @Override
    public void onLoginFocusChange(boolean hasFocus) {
        if (hasFocus) {
            loginInterater.setLoginFocusHandler();
        }
    }

    @Override
    public void onSuccessLoginFocusChange() {
        loginView.setLoginSmoothScrollTo(0, 450);
    }

    @Override
    public OAuthLoginHandler getOAuthLoginHandler() {
        return loginInterater.getOAuthLoginHandler();
    }

    @Override
    public OAuthLogin getNaverOAuthLogin() {
        return loginInterater.getNaverOAuthLogin();
    }

    @Override
    public CallbackManager getCallbackManager() {
        return loginInterater.getCallbackManager();
    }

    @Override
    public String getOAuthLoginResponse(OAuthLogin oAuthLogin, String url) {
        return loginView.getOAuthLoginResponse(oAuthLogin, url);
    }

    @Override
    public void onNetworkError(APIError apiError) {
        if (apiError == null) {
            loginView.showMessage("네트워크 불안정합니다. 다시 시도하세요.");
        } else {
            loginView.showMessage(apiError.message());
        }
    }


}
