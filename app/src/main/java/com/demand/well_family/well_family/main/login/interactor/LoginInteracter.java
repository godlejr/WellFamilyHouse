package com.demand.well_family.well_family.main.login.interactor;

import com.demand.well_family.well_family.dto.User;
import com.facebook.CallbackManager;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;

/**
 * Created by Dev-0 on 2017-04-14.
 */

public interface LoginInteracter {
    void setLoginFocusHandler();

    void setFacebookCallbackManager();

    void setNaverOAuthLogin();

    void setFacebookLogin();

    void setDeviceIdAndToken(User user,String deviceId, String firebaseToken);

    void setLogin(String email,String password);

    CallbackManager getCallbackManager();

    OAuthLogin getNaverOAuthLogin();

    String getNaverClientId();

    String getNaverClientSecret();

    String getNaverClientName();

    OAuthLoginHandler getOAuthLoginHandler();
}
