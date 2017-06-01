package com.demand.well_family.well_family.falldiagnosis.environment.create.interactor.impl;

import com.demand.well_family.well_family.dto.EnvironmentEvaluationCategory;
import com.demand.well_family.well_family.dto.FallDiagnosisCategory;
import com.demand.well_family.well_family.dto.FallDiagnosisContentCategory;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.falldiagnosis.environment.create.interactor.CreateEnvironmentEvaluationInteractor;
import com.demand.well_family.well_family.falldiagnosis.environment.create.presenter.impl.CreateEnvironmentEvaluationPresenterImpl;
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
 * Created by ㅇㅇ on 2017-05-30.
 */

public class CreateEnvironmentEvaluationInteractorImpl implements CreateEnvironmentEvaluationInteractor {
    private CreateEnvironmentEvaluationPresenterImpl createEnvironmentEvaluationPresenter;

    private FallDiagnosisCategory fallDiagnosisCategory;
    private FallDiagnosisContentCategory fallDiagnosisContentCategory;

    private ArrayList<Integer> answerList;
    private  ArrayList<EnvironmentEvaluationCategory> environmentEvaluationCategoryList;
    private FallDiagnosisServerConnection fallDiagnosisServerConnection;
    private static final Logger logger = LoggerFactory.getLogger(CreateEnvironmentEvaluationInteractorImpl.class);

    public CreateEnvironmentEvaluationInteractorImpl(CreateEnvironmentEvaluationPresenterImpl createEnvironmentEvaluationPresenter) {
        this.createEnvironmentEvaluationPresenter = createEnvironmentEvaluationPresenter;
        answerList = new ArrayList<>();
    }

    @Override
    public void getEnvironmentEvaluationCategory(User user) {
        String accessToken = user.getAccess_token();
        int fallDiagnosisCategoryId = fallDiagnosisCategory.getId();
        int fallDiagnosisContentCategoryId = fallDiagnosisContentCategory.getId();

        fallDiagnosisServerConnection = new HeaderInterceptor(accessToken).getFallDiagnosisServer().create(FallDiagnosisServerConnection.class);
        Call<ArrayList<EnvironmentEvaluationCategory>> callGetEnvironmentEvaluationCategories = fallDiagnosisServerConnection.getEnvironmentEvaluationCategories(fallDiagnosisCategoryId, fallDiagnosisContentCategoryId);
        callGetEnvironmentEvaluationCategories.enqueue(new Callback<ArrayList<EnvironmentEvaluationCategory>>() {
            @Override
            public void onResponse(Call<ArrayList<EnvironmentEvaluationCategory>> call, Response<ArrayList<EnvironmentEvaluationCategory>> response) {
                if (response.isSuccessful()) {
                    environmentEvaluationCategoryList = response.body();
                    createEnvironmentEvaluationPresenter.onSuccessGetEnvironmentEvaluationCategories(environmentEvaluationCategoryList);
                } else {
                    createEnvironmentEvaluationPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<EnvironmentEvaluationCategory>> call, Throwable t) {
                log(t);
                createEnvironmentEvaluationPresenter.onNetworkError(null);
            }
        });
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
    public FallDiagnosisContentCategory getFallDiagnosisContentCategory() {
        return fallDiagnosisContentCategory;
    }


    @Override
    public ArrayList<EnvironmentEvaluationCategory> getEnvironmentEvaluationCategoryList() {
        return environmentEvaluationCategoryList;
    }

    @Override
    public void setEnvironmentEvaluationCategoryList(ArrayList<EnvironmentEvaluationCategory> environmentEvaluationCategoryList) {
        this.environmentEvaluationCategoryList = environmentEvaluationCategoryList;
    }

    @Override
    public void setFallDiagnosisContentCategory(FallDiagnosisContentCategory fallDiagnosisContentCategory) {
        this.fallDiagnosisContentCategory = fallDiagnosisContentCategory;
    }

    @Override
    public void setAnswerAdded(int environmentEvaluationCategoryId) {
        answerList.add(environmentEvaluationCategoryId);
    }

    @Override
    public ArrayList<Integer> getAnswerList() {
        return this.answerList;
    }

    @Override
    public void setAnswerList(ArrayList<Integer> answerList) {
        this.answerList = answerList;
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
