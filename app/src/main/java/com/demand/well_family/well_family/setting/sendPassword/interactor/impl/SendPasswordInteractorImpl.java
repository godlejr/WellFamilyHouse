package com.demand.well_family.well_family.setting.sendPassword.interactor.impl;

import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.repository.MainServerConnection;
import com.demand.well_family.well_family.repository.interceptor.HeaderInterceptor;
import com.demand.well_family.well_family.setting.sendPassword.interactor.SendPasswordInteractor;
import com.demand.well_family.well_family.setting.sendPassword.presenter.SendPasswordPresenter;
import com.demand.well_family.well_family.util.ErrorUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ㅇㅇ on 2017-04-17.
 */

public class SendPasswordInteractorImpl implements SendPasswordInteractor {
    private SendPasswordPresenter findAccountPresenter;
    private MainServerConnection mainServerConnection;
    private static final Logger logger = LoggerFactory.getLogger(SendPasswordInteractorImpl.class);

    private User user;

    public SendPasswordInteractorImpl(SendPasswordPresenter findAccountPresenter) {
        this.findAccountPresenter = findAccountPresenter;
    }

    @Override
    public void sendEmail() {
        int userId = user.getId();
        final String userEmail = user.getEmail();
        String userName = user.getName();

        mainServerConnection = new HeaderInterceptor().getClientForMainServer().create(MainServerConnection.class);

        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", String.valueOf(userId));
        map.put("email", userEmail);
        map.put("name", userName);

        Call<ResponseBody> call_find_password = mainServerConnection.findPassword(map);
        call_find_password.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    findAccountPresenter.onSuccessSendEmail(userEmail);
                } else {
                    findAccountPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                log(t);
                findAccountPresenter.onNetworkError(null);

            }
        });
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public User getUser() {
        return this.user;
    }

    private static void log(Throwable throwable) {
        StackTraceElement[] ste = throwable.getStackTrace();
        String className = ste[0].getClassName();
        String methodName = ste[0].getMethodName();
        int lineNumber = ste[0].getLineNumber();
        String fileName = ste[0].getFileName();

        if (LogFlag.printFlag) {
            if (logger.isInfoEnabled()) {
                logger.error("Exception: " + throwable.getMessage());
                logger.error(className + "." + methodName + " " + fileName + " " + lineNumber + " " + "line");
            }
        }
    }
}
