package com.demand.well_family.well_family.falldiagnosis.physicalevaluation.base.interactor.impl;

import com.demand.well_family.well_family.dto.FallDiagnosisCategory;
import com.demand.well_family.well_family.dto.FallDiagnosisContentCategory;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.falldiagnosis.physicalevaluation.base.interactor.PhysicalEvaluationInteractor;
import com.demand.well_family.well_family.falldiagnosis.physicalevaluation.base.presenter.PhysicalEvaluationPresenter;
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
 * Created by ㅇㅇ on 2017-05-25.
 */

public class PhysicalEvaluationInteractorImpl implements PhysicalEvaluationInteractor {
    private PhysicalEvaluationPresenter physicalEvaluationPresenter;
    private static final Logger logger = LoggerFactory.getLogger(PhysicalEvaluationInteractorImpl.class);

    private User user;
    private FallDiagnosisCategory fallDiagnosisCategory;
    private FallDiagnosisServerConnection fallDiagnosisServerConnection;

    public PhysicalEvaluationInteractorImpl(PhysicalEvaluationPresenter physicalEvaluationPresenter) {
        this.physicalEvaluationPresenter = physicalEvaluationPresenter;
    }

    @Override
    public FallDiagnosisCategory getFallDiagnosisCategory() {
        return this.fallDiagnosisCategory;
    }

    @Override
    public void setFallDiagnosisCategory(FallDiagnosisCategory fallDiagnosisCategory) {
        this.fallDiagnosisCategory = fallDiagnosisCategory;
    }

    @Override
    public void getPhysicalEvaluationCategories() {
        String accessToken = user.getAccess_token();
        int fallDiagnosisCategoryId = fallDiagnosisCategory.getId();

        fallDiagnosisServerConnection = new NetworkInterceptor(accessToken).getFallDiagnosisServer().create(FallDiagnosisServerConnection.class);
        Call<ArrayList<FallDiagnosisContentCategory>> callGetDiagnosisCategories = fallDiagnosisServerConnection.getDiagnosisCategories(fallDiagnosisCategoryId);
        callGetDiagnosisCategories.enqueue(new Callback<ArrayList<FallDiagnosisContentCategory>>() {
            @Override
            public void onResponse(Call<ArrayList<FallDiagnosisContentCategory>> call, Response<ArrayList<FallDiagnosisContentCategory>> response) {
                if (response.isSuccessful()) {
                    ArrayList<FallDiagnosisContentCategory> fallDiagnosisContentCategoryList = response.body();
                    physicalEvaluationPresenter.onSuccessGetPhysicalEvaluationCategories(fallDiagnosisContentCategoryList);
                } else {
                    physicalEvaluationPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<FallDiagnosisContentCategory>> call, Throwable t) {
                log(t);
                physicalEvaluationPresenter.onNetworkError(null);
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

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }
}
