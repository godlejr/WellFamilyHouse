package com.demand.well_family.well_family.falldiagnosis.fall.diagnosis.interactor.impl;

import com.demand.well_family.well_family.dto.SelfDiagnosisCategory;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.falldiagnosis.base.interactor.impl.FallDiagnosisMainInteractorImpl;
import com.demand.well_family.well_family.falldiagnosis.fall.diagnosis.interactor.FallDiagnosisInteractor;
import com.demand.well_family.well_family.falldiagnosis.fall.diagnosis.presenter.FallDiagnosisPresenter;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.repository.FallDiagnosisServerConnection;
import com.demand.well_family.well_family.repository.interceptor.HeaderInterceptor;
import com.demand.well_family.well_family.util.ErrorUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ㅇㅇ on 2017-05-23.
 */

public class FallDiagnosisInteractorImpl implements FallDiagnosisInteractor {
    private FallDiagnosisPresenter fallDiagnosisPresenter;
    private FallDiagnosisServerConnection fallDiagnosisServerConnection;

    private static final Logger logger = LoggerFactory.getLogger(FallDiagnosisInteractorImpl.class);

    public FallDiagnosisInteractorImpl(FallDiagnosisPresenter fallDiagnosisPresenter) {
        this.fallDiagnosisPresenter = fallDiagnosisPresenter;
    }

    @Override
    public void getDiagnosisCategories(User user) {
        String accessToken = user.getAccess_token();

        fallDiagnosisServerConnection = new HeaderInterceptor(accessToken).getFallDiagnosisServer().create(FallDiagnosisServerConnection.class);
        Call<ArrayList<SelfDiagnosisCategory>> callGetDiagnosisCategories = fallDiagnosisServerConnection.getDiagnosisCategories();
        callGetDiagnosisCategories.enqueue(new Callback<ArrayList<SelfDiagnosisCategory>>() {
            @Override
            public void onResponse(Call<ArrayList<SelfDiagnosisCategory>> call, Response<ArrayList<SelfDiagnosisCategory>> response) {
                if (response.isSuccessful()) {
                    ArrayList<SelfDiagnosisCategory> categoryList = response.body();
                    fallDiagnosisPresenter.onSuccessGetDiagnosisCategories(categoryList);
                } else {
                    fallDiagnosisPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SelfDiagnosisCategory>> call, Throwable t) {
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
