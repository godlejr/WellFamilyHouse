package com.demand.well_family.well_family.main.intro.interactor.impl;

import com.demand.well_family.well_family.repository.UserServerConnection;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.repository.interceptor.HeaderInterceptor;
import com.demand.well_family.well_family.main.intro.interactor.IntroInteractor;
import com.demand.well_family.well_family.main.intro.presenter.IntroPresenter;
import com.demand.well_family.well_family.util.ErrorUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Dev-0 on 2017-04-13.
 */

public class IntroInteractorImpl implements IntroInteractor {
    private IntroPresenter introPresenter;
    private UserServerConnection userServerConnection;

    private static final Logger logger = LoggerFactory.getLogger(IntroInteractorImpl.class);


    public IntroInteractorImpl(IntroPresenter introPresenter){
        this.introPresenter = introPresenter;
    }

    @Override
    public void getMutipleUserAccessValidation(User user) {
        int userId = user.getId();
        String deviceId = user.getDevice_id();
        String accessToken = user.getAccess_token();

        HashMap<String,String> map =new HashMap<>();
        map.put("device_id",deviceId);
        userServerConnection = new HeaderInterceptor(accessToken).getClientForUserServer().create(UserServerConnection.class);
        Call<Integer> call_device_check = userServerConnection.check_device_id(userId,map);
        call_device_check.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    int check = response.body();
                    introPresenter.onSuccessMultipleUserAccessValidation(check);
                } else {
                    introPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                log(t);
                introPresenter.onNetworkError(null);
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
