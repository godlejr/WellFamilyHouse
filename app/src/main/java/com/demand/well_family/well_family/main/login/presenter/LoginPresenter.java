package com.demand.well_family.well_family.main.login.presenter;

import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.util.APIErrorUtil;
import com.facebook.CallbackManager;
import com.kakao.usermgmt.response.model.UserProfile;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;

/**
 * Created by Dev-0 on 2017-04-14.
 */

public interface LoginPresenter {

    void onBeforeCreate();

    void onCreate();

    void onClickLogin(String email,String password);

    void onClickFacebookLogin();

    void onSuccessSNSLogin(User userFromLogin, User userForLogin);

    void onSuccessLogin(User user);

    void onSuccessSetDeviceIdAndToken(User user,String deviceId, String firebaseToken, String accessToken);

    void onLoginFocusChange(boolean hasFocus);

    void onSuccessLoginFocusChange();

    OAuthLoginHandler getOAuthLoginHandler();

    OAuthLogin getNaverOAuthLogin();

    CallbackManager getCallbackManager();

    String getOAuthLoginResponse(OAuthLogin oAuthLogin, String url);

    void onNetworkError(APIErrorUtil apiErrorUtil);

    void onClickKakaoLogin();

    void onSuccessKakaoLogin(UserProfile userProfile);
}
