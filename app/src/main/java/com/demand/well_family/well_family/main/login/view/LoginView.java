package com.demand.well_family.well_family.main.login.view;

import com.demand.well_family.well_family.dto.User;
import com.nhn.android.naverlogin.OAuthLogin;

/**
 * Created by Dev-0 on 2017-04-14.
 */

public interface LoginView {

    void initFacebookLogin();

    void initNaverLogin(OAuthLogin oAuthLogin, String naverClientId, String naverClientSecret, String naverClientName);


    void init();

    String getDeviceId();

    String getFireBaseToken();

    void setLoginSmoothScrollTo(int x, int y);

    void showMessage(String message);

    void navigateToSearchAccountActivity();

    void navigateToMainActivity();

    void navigateToAgreementActivity();


    void navigateToJoinFromSNSActivity(User user);


    String getOAuthLoginResponse(OAuthLogin oAuthLogin, String url);

    void isKakaoLogin();

    void showProgressDialog();

    void goneProgressDialog();
}
