package com.demand.well_family.well_family.falldiagnosis.environment.base.interactor.impl;

import com.demand.well_family.well_family.dto.Evaluation;
import com.demand.well_family.well_family.dto.FallDiagnosisCategory;
import com.demand.well_family.well_family.dto.FallDiagnosisContentCategory;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.falldiagnosis.environment.base.interactor.EnvironmentEvaluationInteractor;
import com.demand.well_family.well_family.falldiagnosis.environment.base.presenter.EnvironmentEvaluationPresenter;
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
 * Created by ㅇㅇ on 2017-04-24.
 */

public class EnvironmentEvaluationInteractorImpl implements EnvironmentEvaluationInteractor {
    private EnvironmentEvaluationPresenter dangerEvaluationPresenter;
    private FallDiagnosisServerConnection fallDiagnosisServerConnection;

    private FallDiagnosisCategory fallDiagnosisCategory;
    private static final Logger logger = LoggerFactory.getLogger(EnvironmentEvaluationInteractorImpl.class);

    public EnvironmentEvaluationInteractorImpl(EnvironmentEvaluationPresenter dangerEvaluationPresenter) {
        this.dangerEvaluationPresenter = dangerEvaluationPresenter;
    }

    @Override
    public void setFallDiagnosisCategory(FallDiagnosisCategory fallDiagnosisCategory) {
        this.fallDiagnosisCategory = fallDiagnosisCategory;
    }

    @Override
    public FallDiagnosisCategory getFallDiagnosisCategory() {
        return this.fallDiagnosisCategory;
    }

    @Override
    public void getDangerEvaluationList(User user) {
        String accessToken = user.getAccess_token();
        int categoryId = fallDiagnosisCategory.getId();

        fallDiagnosisServerConnection = new HeaderInterceptor(accessToken).getFallDiagnosisServer().create(FallDiagnosisServerConnection.class);
        Call<ArrayList<FallDiagnosisContentCategory>> callGetDiagnosisCategories = fallDiagnosisServerConnection.getDiagnosisCategories(categoryId);
        callGetDiagnosisCategories.enqueue(new Callback<ArrayList<FallDiagnosisContentCategory>>() {
            @Override
            public void onResponse(Call<ArrayList<FallDiagnosisContentCategory>> call, Response<ArrayList<FallDiagnosisContentCategory>> response) {
                if (response.isSuccessful()) {
                    ArrayList<FallDiagnosisContentCategory> fallDiagnosisContentCategoryList = response.body();
                    dangerEvaluationPresenter.onSuccessGetDiagnosisCategories(fallDiagnosisContentCategoryList);
                } else {
                    dangerEvaluationPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<FallDiagnosisContentCategory>> call, Throwable t) {
                log(t);
                dangerEvaluationPresenter.onNetworkError(null);
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
