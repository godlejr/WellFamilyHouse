package com.demand.well_family.well_family.family.managedetail.interactor.impl;

import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.dto.UserInfoForFamilyJoin;
import com.demand.well_family.well_family.family.managedetail.interactor.ManageFamilyDetailInteractor;
import com.demand.well_family.well_family.family.managedetail.presenter.ManageFamilyDetailPresenter;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.repository.FamilyServerConnection;
import com.demand.well_family.well_family.repository.interceptor.NetworkInterceptor;
import com.demand.well_family.well_family.util.ErrorUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Dev-0 on 2017-04-21.
 */

public class ManageFamilyDetailInteractorImpl implements ManageFamilyDetailInteractor {
    private ManageFamilyDetailPresenter manageFamilyDetailPresenter;

    private User user;
    private Family family;
    private boolean notificationFlag;

    private FamilyServerConnection familyServerConnection;

    private static final Logger logger = LoggerFactory.getLogger(ManageFamilyDetailInteractorImpl.class);


    public ManageFamilyDetailInteractorImpl(ManageFamilyDetailPresenter manageFamilyDetailPresenter) {
        this.manageFamilyDetailPresenter = manageFamilyDetailPresenter;
    }


    @Override
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void setFamily(Family family) {
        this.family = family;
    }

    @Override
    public void setNotificationFlag(boolean notificationFlag) {
        this.notificationFlag = notificationFlag;
    }



    @Override
    public Family getFamily() {
        return this.family;
    }

    @Override
    public boolean getNotificationFlag() {
        return this.notificationFlag;
    }

    @Override
    public void getUserDataForFamilyJoin() {
        String accessToken = user.getAccess_token();
        int familyId = family.getId();

        familyServerConnection = new NetworkInterceptor(accessToken).getClientForFamilyServer().create(FamilyServerConnection.class);

        Call<ArrayList<UserInfoForFamilyJoin>> call_family_joiners = familyServerConnection.family_joiners(familyId);
        call_family_joiners.enqueue(new Callback<ArrayList<UserInfoForFamilyJoin>>() {
            @Override
            public void onResponse(Call<ArrayList<UserInfoForFamilyJoin>> call, Response<ArrayList<UserInfoForFamilyJoin>> response) {
                if (response.isSuccessful()) {
                    ArrayList<UserInfoForFamilyJoin> userList = response.body();
                    manageFamilyDetailPresenter.onSuccessGetUserDataForFamilyJoin(userList);
                } else {
                    manageFamilyDetailPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<UserInfoForFamilyJoin>> call, Throwable t) {
                log(t);
                manageFamilyDetailPresenter.onNetworkError(null);
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
