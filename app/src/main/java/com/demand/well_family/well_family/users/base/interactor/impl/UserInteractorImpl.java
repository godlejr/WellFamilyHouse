package com.demand.well_family.well_family.users.base.interactor.impl;

import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.repository.UserServerConnection;
import com.demand.well_family.well_family.repository.interceptor.NetworkInterceptor;
import com.demand.well_family.well_family.users.base.interactor.UserInteractor;
import com.demand.well_family.well_family.users.base.presenter.UserPresenter;
import com.demand.well_family.well_family.util.ErrorUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Dev-0 on 2017-05-24.
 */

public class UserInteractorImpl implements UserInteractor {
    private UserPresenter userPresenter;
    private User user;
    private User storyUser;


    private static final Logger logger = LoggerFactory.getLogger(UserInteractorImpl.class);
    private UserServerConnection userServerConnection;


    public UserInteractorImpl(UserPresenter userPresenter) {
        this.userPresenter = userPresenter;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public User getStoryUser() {
        return storyUser;
    }

    @Override
    public void setStoryUser(User storyUser) {
        this.storyUser = storyUser;
    }


    @Override
    public String getUserBirth(String birth) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(birth);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일생");
            return simpleDateFormat.format(date);
        } catch (ParseException e) {
            log(e);
            return null;
        }
    }

    @Override
    public String getUserPhoneWithHyphen(String phone) {
        String phoneWithHyphen = "";
        if (phone != null) {
            int phone_length = phone.length();

            if (phone_length == 11) {
                phoneWithHyphen = phone.substring(0, 3) + "-" + phone.substring(3, 7) + "-" + phone.substring(7, 11);
            } else if (phone_length == 10) {
                phoneWithHyphen = phone.substring(0, 3) + "-" + phone.substring(3, 6) + "-" + phone.substring(6, 10);
            }
        }

        return phoneWithHyphen;
    }

    @Override
    public void getFamilyCheck() {
        String accessToken = user.getAccess_token();
        int storyUserid = storyUser.getId();
        int userId = user.getId();


        userServerConnection = new NetworkInterceptor(accessToken).getClientForUserServer().create(UserServerConnection.class);

        Call<Integer> call_family_check = userServerConnection.family_check(storyUserid, userId);
        call_family_check.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    int check = response.body();

                    userPresenter.onSuccessGetFamilyCheck(check);
                } else {
                    userPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                log(t);
                userPresenter.onNetworkError(null);
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
                logger.error("Exception: " + throwable.getMessage());
                logger.error(className + "." + methodName + " " + fileName + " " + lineNumber + " " + "line");
            }
        }
    }
}
