package com.demand.well_family.well_family.main.login.presenter.impl;

import android.content.Context;

import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.main.login.interactor.LoginInteracter;
import com.demand.well_family.well_family.main.login.interactor.impl.LoginInteractorImpl;
import com.demand.well_family.well_family.main.login.presenter.LoginPresenter;
import com.demand.well_family.well_family.main.login.view.LoginView;
import com.demand.well_family.well_family.util.APIErrorUtil;
import com.demand.well_family.well_family.util.PreferenceUtil;
import com.facebook.CallbackManager;
import com.kakao.usermgmt.response.model.UserProfile;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;

/**
 * Created by Dev-0 on 2017-04-14.
 */

public class LoginPresenterImpl implements LoginPresenter {
    private LoginView loginView;
    private LoginInteracter loginInteracter;
    private PreferenceUtil preferenceUtil;

    public LoginPresenterImpl(Context context) {
        this.loginView = (LoginView) context;
        this.loginInteracter = new LoginInteractorImpl(this);
        this.preferenceUtil = new PreferenceUtil(context);
    }

    @Override
    public void onBeforeCreate() {
        //facebook login init
        loginView.initFacebookLogin();
        loginInteracter.setFacebookCallbackManager();

        //naver login init
        loginInteracter.setNaverOAuthLogin();
        OAuthLogin oAuthLogin = loginInteracter.getNaverOAuthLogin();
        String naverClientId = loginInteracter.getNaverClientId();
        String naverClientSecret = loginInteracter.getNaverClientSecret();
        String naverClientName = loginInteracter.getNaverClientName();

        loginView.initNaverLogin(oAuthLogin, naverClientId, naverClientSecret, naverClientName);

    }

    @Override
    public void onCreate() {
        loginView.init();
    }

    @Override
    public void onClickLogin(String email, String password) {
        loginInteracter.setLogin(email,password);
    }

    @Override
    public void onClickFacebookLogin() {
        loginInteracter.setFacebookLogin();
    }

    @Override
    public void onSuccessSNSLogin(User userFromLogin, User userForLogin) {
        if (userFromLogin == null) {
            loginView.navigateToJoinFromSNSActivity(userForLogin);
        } else {
            String deviceId = loginView.getDeviceId();
            String firebaseToken = loginView.getFireBaseToken();

            loginInteracter.setDeviceIdAndToken(userFromLogin, deviceId, firebaseToken);
        }

        loginView.goneProgressDialog();
    }

    @Override
    public void onSuccessLogin(User user) {
        if (user == null) {
            loginView.showMessage("ID / 패스워드를 확인해주세요.");
        } else {
            String deviceId = loginView.getDeviceId();
            String firebaseToken = loginView.getFireBaseToken();

            loginInteracter.setDeviceIdAndToken(user, deviceId, firebaseToken);
        }
    }

    @Override
    public void onSuccessSetDeviceIdAndToken(User user, String deviceId, String firebaseToken,String accessToken) {
        preferenceUtil.setUserInfo(user, deviceId, firebaseToken,accessToken);
        loginView.navigateToMainActivity();
    }


    @Override
    public void onLoginFocusChange(boolean hasFocus) {
        if (hasFocus) {
            loginInteracter.setLoginFocusHandler();
        }
    }

    @Override
    public void onSuccessLoginFocusChange() {
        loginView.setLoginSmoothScrollTo(0, 450);
    }

    @Override
    public OAuthLoginHandler getOAuthLoginHandler() {
        return loginInteracter.getOAuthLoginHandler();
    }

    @Override
    public OAuthLogin getNaverOAuthLogin() {
        return loginInteracter.getNaverOAuthLogin();
    }

    @Override
    public CallbackManager getCallbackManager() {
        return loginInteracter.getCallbackManager();
    }

    @Override
    public String getOAuthLoginResponse(OAuthLogin oAuthLogin, String url) {
        return loginView.getOAuthLoginResponse(oAuthLogin, url);
    }

    @Override
    public void onNetworkError(APIErrorUtil apiErrorUtil) {
        if (apiErrorUtil == null) {
            loginView.showMessage("네트워크 불안정합니다. 다시 시도하세요.");
        } else {
            loginView.showMessage(apiErrorUtil.message());
        }
    }

    @Override
    public void onClickKakaoLogin() {
        loginView.showProgressDialog();
        loginView.isKakaoLogin();
    }

    @Override
    public void onSuccessKakaoLogin(UserProfile userProfile) {
        loginInteracter.setKakaoLogin(userProfile);
    }


}
