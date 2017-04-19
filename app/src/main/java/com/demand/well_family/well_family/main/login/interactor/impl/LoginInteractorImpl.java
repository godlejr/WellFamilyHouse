package com.demand.well_family.well_family.main.login.interactor.impl;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.demand.well_family.well_family.repository.MainServerConnection;
import com.demand.well_family.well_family.repository.UserServerConnection;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.flag.JoinFlag;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.repository.interceptor.HeaderInterceptor;
import com.demand.well_family.well_family.main.login.interactor.LoginInteracter;
import com.demand.well_family.well_family.main.login.presenter.LoginPresenter;
import com.demand.well_family.well_family.util.EncryptionUtil;
import com.demand.well_family.well_family.util.ErrorUtil;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Dev-0 on 2017-04-14.
 */

public class LoginInteractorImpl implements LoginInteracter {
    private LoginPresenter loginPresenter;

    //sns login
    private CallbackManager callbackManager; //facebook
    private OAuthLogin oAuthLogin; //naver

    private static final String NaverClientId = "vX9icrhPpuwATfps78ce";
    private static final String NaverClientSecret = "YiBZjWpiNA";
    private static final String NaverClientName = "웰패밀리 하우스";
    private static final String NaverOAuthUrl = "https://openapi.naver.com/v1/nid/me";

    private static final Logger logger = LoggerFactory.getLogger(LoginInteractorImpl.class);

    private MainServerConnection mainServerConnection;
    private UserServerConnection userServerConnection;


    public LoginInteractorImpl(LoginPresenter loginPresenter) {
        this.loginPresenter = loginPresenter;
    }

    @Override
    public void setLoginFocusHandler() {
        Handler handler;
        handler = new Handler();
        final Runnable runnable = new Runnable() {
            public void run() {
                loginPresenter.onSuccessLoginFocusChange();
            }
        };
        handler.postDelayed(runnable, 200);
    }

    @Override
    public void setFacebookCallbackManager() {
        this.callbackManager = CallbackManager.Factory.create();
    }

    @Override
    public void setNaverOAuthLogin() {
        this.oAuthLogin = OAuthLogin.getInstance();
    }

    @Override
    public void setFacebookLogin() {
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            String id = object.getString("id");
                            String name = object.getString("name");
                            String email = object.getString("email");

                            User userForLogin = new User();
                            userForLogin.setEmail(email);
                            userForLogin.setName(name);
                            userForLogin.setPassword(id);
                            userForLogin.setLogin_category_id(JoinFlag.FACEBOOK);

                            setSNSLogin(userForLogin);

                        } catch (JSONException e) {
                            log(e);
                        }
                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, name, email, gender");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.e("facebookLogin", "onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                if (error instanceof FacebookAuthorizationException) {
                    if (AccessToken.getCurrentAccessToken() != null) {
                        LoginManager.getInstance().logOut();
                    } else {
                        loginPresenter.onNetworkError(null);
                    }
                }

            }
        });
    }

    @Override
    public void setDeviceIdAndToken(final User user, final String deviceId, final String firebaseToken) {
        final String accessToken = user.getAccess_token();

        HashMap<String, String> map = new HashMap<>();
        map.put("device_id", deviceId);
        map.put("token", firebaseToken);

        userServerConnection = new HeaderInterceptor(accessToken).getClientForUserServer().create(UserServerConnection.class);
        Call<ResponseBody> call_update_deviceId_token = userServerConnection.update_deviceId_token(user.getId(), map);
        call_update_deviceId_token.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    loginPresenter.onSuccessSetDeviceIdAndToken(user, deviceId, firebaseToken, accessToken);
                } else {
                    loginPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                log(t);
                loginPresenter.onNetworkError(null);
            }
        });
    }

    @Override
    public void setLogin(String email, String password) {
        String encryptedPassword = EncryptionUtil.encryptSHA256(password);
        HashMap<String, String> map = new HashMap<>();
        map.put("email", email);
        map.put("password", encryptedPassword);

        mainServerConnection = new HeaderInterceptor().getClientForMainServer().create(MainServerConnection.class);
        Call<User> call_login = mainServerConnection.login(map);

        call_login.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    loginPresenter.onSuccessLogin(user);
                } else {
                    loginPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                log(t);
                loginPresenter.onNetworkError(null);
            }
        });
    }

    @Override
    public CallbackManager getCallbackManager() {
        return callbackManager;
    }


    @Override
    public OAuthLogin getNaverOAuthLogin() {
        return oAuthLogin;
    }

    @Override
    public String getNaverClientId() {
        return NaverClientId;
    }

    @Override
    public String getNaverClientSecret() {
        return NaverClientSecret;
    }

    @Override
    public String getNaverClientName() {
        return NaverClientName;
    }

    @Override
    public OAuthLoginHandler getOAuthLoginHandler() {
        return oAuthLoginHandler;
    }


    private OAuthLoginHandler oAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String response = loginPresenter.getOAuthLoginResponse(oAuthLogin, NaverOAuthUrl);
                        try {
                            JSONObject json = new JSONObject(response);
                            String id = json.getJSONObject("response").getString("id");
                            String name = json.getJSONObject("response").getString("name");
                            String email = json.getJSONObject("response").getString("email");

                            User userForLogin = new User();
                            userForLogin.setEmail(email);
                            userForLogin.setName(name);
                            userForLogin.setPassword(id);
                            userForLogin.setLogin_category_id(JoinFlag.NAVER);

                            setSNSLogin(userForLogin);

                        } catch (JSONException e) {
                            log(e);
                        }
                    }
                }).start();

            } else {
                // error handling
            }
        }
    };

    private void setSNSLogin(final User userForLogin) {
        mainServerConnection = new HeaderInterceptor().getClientForMainServer().create(MainServerConnection.class);

        HashMap<String, String> map = new HashMap<>();
        map.put("email", userForLogin.getEmail());
        map.put("password", userForLogin.getPassword());
        Call<User> call = mainServerConnection.login(map);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User userFromLogin = response.body();
                    loginPresenter.onSuccessSNSLogin(userFromLogin, userForLogin);

                } else {
                    loginPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                log(t);
                loginPresenter.onNetworkError(null);
            }
        });
    }


    private static void log(Throwable throwable) {
        StackTraceElement[] ste = throwable.getStackTrace();
        String className = ste[0].getClassName();
        String methodName = ste[0].getMethodName();
        int lineNumber = ste[0].getLineNumber();
        String fileName = ste[0].getFileName();

        if (LogFlag.printFlag) {
            if (logger.isInfoEnabled()) {
                logger.info("Exception: " + throwable.getMessage());
                logger.info(className + "." + methodName + " " + fileName + " " + lineNumber + " " + "line");
            }
        }
    }
}
