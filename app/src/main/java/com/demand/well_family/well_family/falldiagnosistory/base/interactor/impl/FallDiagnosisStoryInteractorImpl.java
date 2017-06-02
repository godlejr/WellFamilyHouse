package com.demand.well_family.well_family.falldiagnosistory.base.interactor.impl;

import com.demand.well_family.well_family.dto.FallDiagnosisStory;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.falldiagnosistory.base.interactor.FallDiagnosisStoryInteractor;
import com.demand.well_family.well_family.falldiagnosistory.base.presenter.FallDiagnosisStoryPresenter;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.repository.UserServerConnection;
import com.demand.well_family.well_family.repository.interceptor.HeaderInterceptor;
import com.demand.well_family.well_family.util.ErrorUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ㅇㅇ on 2017-06-01.
 */

public class FallDiagnosisStoryInteractorImpl implements FallDiagnosisStoryInteractor {
    private FallDiagnosisStoryPresenter fallDiagnosisStoryPresenter;

    private User user;
    private UserServerConnection userServerConnection;
    private static final Logger logger = LoggerFactory.getLogger(FallDiagnosisStoryInteractorImpl.class);

    private ArrayList<FallDiagnosisStory> fallDiagnosisStoryList;

    public FallDiagnosisStoryInteractorImpl(FallDiagnosisStoryPresenter fallDiagnosisStoryPresenter) {
        this.fallDiagnosisStoryPresenter = fallDiagnosisStoryPresenter;
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
    public void getFallDiagnosisStoryCommentCount() {

    }

    @Override
    public void getFallDiagnosisStoryLikeCount() {

    }

    @Override
    public void getFallDiagnosisStory() {
        String accessToken = user.getAccess_token();
        int userId = user.getId();

        userServerConnection = new HeaderInterceptor(accessToken).getClientForUserServer().create(UserServerConnection.class);
        Call<ArrayList<FallDiagnosisStory>> callSelectFallDiagnosisStoryList=userServerConnection.selectFallDiagnosisStoryList(userId);
        callSelectFallDiagnosisStoryList.enqueue(new Callback<ArrayList<FallDiagnosisStory>>() {
            @Override
            public void onResponse(Call<ArrayList<FallDiagnosisStory>> call, Response<ArrayList<FallDiagnosisStory>> response) {
                if(response.isSuccessful()){
                    fallDiagnosisStoryList = response.body();
                    fallDiagnosisStoryPresenter.onSuccessGetFallDiagnosisStory(fallDiagnosisStoryList);
                }else{
                    fallDiagnosisStoryPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<FallDiagnosisStory>> call, Throwable t) {
                log(t);
                fallDiagnosisStoryPresenter.onNetworkError(null);
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
