package com.demand.well_family.well_family.main.login.interator;

import com.demand.well_family.well_family.dto.User;
import com.facebook.CallbackManager;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;

/**
 * Created by Dev-0 on 2017-04-14.
 */

public interface LoginInterater {
    void setLoginFocusHandler();

    void setFacebookCallbackManager();

    void setNaverOAuthLogin();

    void setFacebookLogin();

    void setLogin(User user,String deviceId, String firebaseToken);

    void validateLogin(String email,String password);

    CallbackManager getCallbackManager();

    OAuthLogin getNaverOAuthLogin();

    String getNaverClientId();

    String getNaverClientSecret();

    String getNaverClientName();

    OAuthLoginHandler getOAuthLoginHandler();
}
