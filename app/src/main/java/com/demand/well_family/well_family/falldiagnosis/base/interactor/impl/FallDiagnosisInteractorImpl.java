package com.demand.well_family.well_family.falldiagnosis.base.interactor.impl;

import com.demand.well_family.well_family.dto.FallDiagnosisCategory;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.falldiagnosis.base.interactor.FallDiagnosisInteractor;
import com.demand.well_family.well_family.falldiagnosis.base.presenter.FallDiagnosisPresenter;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.repository.FallDiagnosisServerConnection;
import com.demand.well_family.well_family.repository.interceptor.NetworkInterceptor;
import com.demand.well_family.well_family.util.ErrorUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ㅇㅇ on 2017-04-24.
 */

public class FallDiagnosisInteractorImpl implements FallDiagnosisInteractor {
    private FallDiagnosisPresenter fallDiagnosisPresenter;

    private FallDiagnosisServerConnection fallDiagnosisServerConnection;
    private static final Logger logger = LoggerFactory.getLogger(FallDiagnosisInteractorImpl.class);

    public FallDiagnosisInteractorImpl(FallDiagnosisPresenter fallDiagnosisPresenter) {
        this.fallDiagnosisPresenter = fallDiagnosisPresenter;
    }

    @Override
    public void getCategoryList(User user) {
        String accessToken = user.getAccess_token();

        fallDiagnosisServerConnection = new NetworkInterceptor(accessToken).getFallDiagnosisServer().create(FallDiagnosisServerConnection.class);
        Call<ArrayList<FallDiagnosisCategory>> callGetCategoryList = fallDiagnosisServerConnection.getCategoryList();
        callGetCategoryList.enqueue(new Callback<ArrayList<FallDiagnosisCategory>>() {
            @Override
            public void onResponse(Call<ArrayList<FallDiagnosisCategory>> call, Response<ArrayList<FallDiagnosisCategory>> response) {
                if (response.isSuccessful()) {
                    ArrayList<FallDiagnosisCategory> fallDiagnosisCategoryList = response.body();
                    fallDiagnosisPresenter.onSuccessGetCategoryList(fallDiagnosisCategoryList);
                } else {
                    fallDiagnosisPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<FallDiagnosisCategory>> call, Throwable t) {
                log(t);
                fallDiagnosisPresenter.onNetworkError(null);
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
