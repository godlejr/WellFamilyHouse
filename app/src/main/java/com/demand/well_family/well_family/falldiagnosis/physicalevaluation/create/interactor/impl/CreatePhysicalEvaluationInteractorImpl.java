package com.demand.well_family.well_family.falldiagnosis.physicalevaluation.create.interactor.impl;

import com.demand.well_family.well_family.dto.FallDiagnosisCategory;
import com.demand.well_family.well_family.dto.PhysicalEvaluationCategory;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.falldiagnosis.physicalevaluation.create.interactor.CreatePhysicalEvaluationInteractor;
import com.demand.well_family.well_family.falldiagnosis.physicalevaluation.create.presenter.CreatePhysicalEvaluationPresenter;
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
 * Created by ㅇㅇ on 2017-05-25.
 */

public class CreatePhysicalEvaluationInteractorImpl implements CreatePhysicalEvaluationInteractor {
    private CreatePhysicalEvaluationPresenter createPhysicalEvaluationPresenter;

    private FallDiagnosisServerConnection fallDiagnosisServerConnection;
    private FallDiagnosisCategory fallDiagnosisCategory;
    private static final Logger logger = LoggerFactory.getLogger(CreatePhysicalEvaluationInteractorImpl.class);

    public CreatePhysicalEvaluationInteractorImpl(CreatePhysicalEvaluationPresenter createPhysicalEvaluationPresenter) {
        this.createPhysicalEvaluationPresenter = createPhysicalEvaluationPresenter;
    }

    @Override
    public FallDiagnosisCategory getFallDiagnosisCategory() {
        return fallDiagnosisCategory;
    }

    @Override
    public void setFallDiagnosisCategory(FallDiagnosisCategory fallDiagnosisCategory) {
        this.fallDiagnosisCategory = fallDiagnosisCategory;
    }

    @Override
    public void getPhysicalEvaluationCategories(User user) {
        String accessToken = user.getAccess_token();

        fallDiagnosisServerConnection = new HeaderInterceptor(accessToken).getFallDiagnosisServer().create(FallDiagnosisServerConnection.class);
        Call<ArrayList<PhysicalEvaluationCategory>> callGetPhysicalEvaluationCategories = fallDiagnosisServerConnection.getPhysicalEvaluationCategories(fallDiagnosisCategory.getId());
        callGetPhysicalEvaluationCategories.enqueue(new Callback<ArrayList<PhysicalEvaluationCategory>>() {
            @Override
            public void onResponse(Call<ArrayList<PhysicalEvaluationCategory>> call, Response<ArrayList<PhysicalEvaluationCategory>> response) {
                if (response.isSuccessful()) {
                    ArrayList<PhysicalEvaluationCategory> physicalEvaluationCategoryList = response.body();
                    createPhysicalEvaluationPresenter.onSuccessGetPhysicalEvaluationCategories(physicalEvaluationCategoryList);
                } else {
                    createPhysicalEvaluationPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<PhysicalEvaluationCategory>> call, Throwable t) {
                log(t);
                createPhysicalEvaluationPresenter.onNetworkError(null);
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
