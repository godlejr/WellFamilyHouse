package com.demand.well_family.well_family.family.manage.interactor.impl;

import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.dto.FamilyInfoForFamilyJoin;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.family.manage.interactor.ManageFamilyInteractor;
import com.demand.well_family.well_family.family.manage.presenter.ManageFamilyPresenter;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.repository.UserServerConnection;
import com.demand.well_family.well_family.repository.interceptor.NetworkInterceptor;
import com.demand.well_family.well_family.util.ErrorUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Dev-0 on 2017-04-20.
 */

public class ManageFamilyInteractorImpl implements ManageFamilyInteractor {
    private ManageFamilyPresenter manageFamilyPresenter;

    private User user;

    private UserServerConnection userServerConnection;


    private static final Logger logger = LoggerFactory.getLogger(ManageFamilyInteractorImpl.class);


    public ManageFamilyInteractorImpl(ManageFamilyPresenter manageFamilyPresenter) {
        this.manageFamilyPresenter = manageFamilyPresenter;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void getFamilyDataForOwner() {
        String accessToken = user.getAccess_token();
        int userId = user.getId();

        userServerConnection = new NetworkInterceptor(accessToken).getClientForUserServer().create(UserServerConnection.class);
        Call<ArrayList<Family>> call_manage_families = userServerConnection.manage_families(userId);
        call_manage_families.enqueue(new Callback<ArrayList<Family>>() {
            @Override
            public void onResponse(Call<ArrayList<Family>> call, Response<ArrayList<Family>> response) {
                if (response.isSuccessful()) {
                    ArrayList<Family> familyList = response.body();
                    manageFamilyPresenter.onSuccessGetFamilyDataForOwner(familyList);
                } else {
                    manageFamilyPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Family>> call, Throwable t) {
                log(t);
                manageFamilyPresenter.onNetworkError(null);
            }
        });
    }

    @Override
    public void getFamilyDataForMember() {
        String accessToken = user.getAccess_token();
        int userId = user.getId();

        userServerConnection = new NetworkInterceptor(accessToken).getClientForUserServer().create(UserServerConnection.class);
        Call<ArrayList<FamilyInfoForFamilyJoin>> call_family_info = userServerConnection.join_families(userId);
        call_family_info.enqueue(new Callback<ArrayList<FamilyInfoForFamilyJoin>>() {
            @Override
            public void onResponse(Call<ArrayList<FamilyInfoForFamilyJoin>> call, Response<ArrayList<FamilyInfoForFamilyJoin>> response) {
                if (response.isSuccessful()) {
                    ArrayList<FamilyInfoForFamilyJoin> familyInfoForFamilyJoinList = response.body();

                    manageFamilyPresenter.onSuccessGetFamilyDataForMember(familyInfoForFamilyJoinList);
                } else {
                    manageFamilyPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<FamilyInfoForFamilyJoin>> call, Throwable t) {
                log(t);
                manageFamilyPresenter.onNetworkError(null);
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
