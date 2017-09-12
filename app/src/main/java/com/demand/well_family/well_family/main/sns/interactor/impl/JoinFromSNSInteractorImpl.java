package com.demand.well_family.well_family.main.sns.interactor.impl;

import android.util.Log;

import com.demand.well_family.well_family.repository.MainServerConnection;
import com.demand.well_family.well_family.repository.UserServerConnection;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.repository.interceptor.NetworkInterceptor;
import com.demand.well_family.well_family.main.sns.interactor.JoinFromSNSInteractor;
import com.demand.well_family.well_family.main.sns.presenter.JoinFromSNSPresenter;
import com.demand.well_family.well_family.util.ErrorUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Dev-0 on 2017-04-14.
 */

public class JoinFromSNSInteractorImpl implements JoinFromSNSInteractor {
    private JoinFromSNSPresenter joinFromSNSPresenter;

    private static final Logger logger = LoggerFactory.getLogger(JoinFromSNSInteractorImpl.class);
    private MainServerConnection mainServerConnection;
    private UserServerConnection userServerConnection;


    public JoinFromSNSInteractorImpl(JoinFromSNSPresenter joinFromSNSPresenter) {
        this.joinFromSNSPresenter = joinFromSNSPresenter;
    }

    @Override
    public String getUserBirth(String birth) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = formatter.parse(birth);
            SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
            String birthDate = transFormat.format(date);
            return birthDate;
        } catch (ParseException e) {
            log(e);
            return null;
        }
    }

    @Override
    public void setJoin(final String email, final String password, String name, int loginCategoryId, String phone, String birth) {
        HashMap<String, String> map = new HashMap<>();
        map.put("email", email);
        map.put("password", password);
        map.put("name", name);
        map.put("birth", birth);
        map.put("phone", phone);
        map.put("login_category_id", String.valueOf(loginCategoryId));

        mainServerConnection = new NetworkInterceptor().getClientForMainServer().create(MainServerConnection.class);
        Call<ResponseBody> call_join = mainServerConnection.join(map);
        call_join.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    joinFromSNSPresenter.onSuccessJoin(email, password);
                } else {
                    joinFromSNSPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));

                    try {
                        Log.e("ㅇㅇㅇㅇ", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                log(t);
                joinFromSNSPresenter.onNetworkError(null);
            }
        });
    }

    @Override
    public void setLogin(String email, String password) {
        HashMap<String, String> map = new HashMap<>();
        map.put("email", email);
        map.put("password", password);

        mainServerConnection = new NetworkInterceptor().getClientForMainServer().create(MainServerConnection.class);
        Call<User> call_login = mainServerConnection.login(map);
        call_login.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    joinFromSNSPresenter.onSuccessLogin(user);
                } else {
                    joinFromSNSPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                log(t);
                joinFromSNSPresenter.onNetworkError(null);
            }
        });
    }

    @Override
    public void setDeviceIdAndToken(final User user, final String deviceId, final String firebaseToken) {
        final String accessToken = user.getAccess_token();

        HashMap<String, String> map = new HashMap<>();
        map.put("device_id", deviceId);
        map.put("token", firebaseToken);

        userServerConnection = new NetworkInterceptor(accessToken).getClientForUserServer().create(UserServerConnection.class);
        Call<ResponseBody> call_update_deviceId_token = userServerConnection.update_deviceId_token(user.getId(), map);

        call_update_deviceId_token.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    joinFromSNSPresenter.onSuccessSetDeviceIdAndToken(user, deviceId, firebaseToken, accessToken);
                } else {
                    joinFromSNSPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                log(t);
                joinFromSNSPresenter.onNetworkError(null);
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
