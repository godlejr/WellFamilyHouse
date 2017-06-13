package com.demand.well_family.well_family.falldiagnosis.selfdiagnosis.create.interactor.impl;

import com.demand.well_family.well_family.dto.FallDiagnosisCategory;
import com.demand.well_family.well_family.dto.FallDiagnosisContentCategory;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.falldiagnosis.selfdiagnosis.create.interactor.SelfDiagnosisInteractor;
import com.demand.well_family.well_family.falldiagnosis.selfdiagnosis.create.presenter.SelfDiagnosisPresenter;
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
 * Created by ㅇㅇ on 2017-05-23.
 */

public class SelfDiagnosisInteractorImpl implements SelfDiagnosisInteractor {
    private SelfDiagnosisPresenter selfDiagnosisPresenter;

    private FallDiagnosisCategory fallDiagnosisCategory;
    private User user;

    private ArrayList<Integer> answerList;

    private ArrayList<FallDiagnosisContentCategory> fallDiagnosisContentCategoryList;

    private FallDiagnosisServerConnection fallDiagnosisServerConnection;

    private static final Logger logger = LoggerFactory.getLogger(SelfDiagnosisInteractorImpl.class);

    public SelfDiagnosisInteractorImpl(SelfDiagnosisPresenter selfDiagnosisPresenter) {
        this.selfDiagnosisPresenter = selfDiagnosisPresenter;
        this.answerList = new ArrayList<>();
    }

    @Override
    public void getDiagnosisCategories(User user) {
        String accessToken = user.getAccess_token();
        int categoryId = fallDiagnosisCategory.getId();

        fallDiagnosisServerConnection = new NetworkInterceptor(accessToken).getFallDiagnosisServer().create(FallDiagnosisServerConnection.class);
        Call<ArrayList<FallDiagnosisContentCategory>> callGetDiagnosisCategories = fallDiagnosisServerConnection.getDiagnosisCategories(categoryId);
        callGetDiagnosisCategories.enqueue(new Callback<ArrayList<FallDiagnosisContentCategory>>() {
            @Override
            public void onResponse(Call<ArrayList<FallDiagnosisContentCategory>> call, Response<ArrayList<FallDiagnosisContentCategory>> response) {
                if (response.isSuccessful()) {
                    fallDiagnosisContentCategoryList = response.body();
                    selfDiagnosisPresenter.onSuccessGetDiagnosisCategories(fallDiagnosisContentCategoryList);
                } else {
                    selfDiagnosisPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<FallDiagnosisContentCategory>> call, Throwable t) {
                log(t);
                selfDiagnosisPresenter.onNetworkError(null);
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
    public User getUser() {
        return user;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }


    @Override
    public ArrayList<Integer> getAnswerList() {
        return answerList;
    }

    @Override
    public void setAnswerList(ArrayList<Integer> answerList) {
        this.answerList = answerList;
    }

    @Override
    public void setAnswerAdded(int fallContentCategoryId) {
        answerList.add(fallContentCategoryId);
    }


    public ArrayList<FallDiagnosisContentCategory> getFallDiagnosisContentCategoryList() {
        return fallDiagnosisContentCategoryList;
    }

    public void setFallDiagnosisContentCategoryList(ArrayList<FallDiagnosisContentCategory> fallDiagnosisContentCategoryList) {
        this.fallDiagnosisContentCategoryList = fallDiagnosisContentCategoryList;
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
