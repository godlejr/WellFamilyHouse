package com.demand.well_family.well_family.dialog.list.falldiagnosisstory.interactor.impl;

import com.demand.well_family.well_family.dialog.list.falldiagnosisstory.interactor.FallDiagnosisStoryDialogInteractor;
import com.demand.well_family.well_family.dialog.list.falldiagnosisstory.presenter.FallDiagnosisStoryDialogPresenter;
import com.demand.well_family.well_family.dto.FallDiagnosisStory;
import com.demand.well_family.well_family.dto.FallDiagnosisStoryInfo;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.repository.FallDiagnosisStoryServerConnection;
import com.demand.well_family.well_family.repository.interceptor.NetworkInterceptor;
import com.demand.well_family.well_family.util.ErrorUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ㅇㅇ on 2017-06-08.
 */

public class FallDiagnosisStoryDialogInteractorImpl implements FallDiagnosisStoryDialogInteractor {
    private FallDiagnosisStoryDialogPresenter fallDiagnosisStoryDialogPresenter;

    private User user;
    private FallDiagnosisStory fallDiagnosisStory;
    private FallDiagnosisStoryInfo fallDiagnosisStoryInfo;
    private static final Logger logger = LoggerFactory.getLogger(FallDiagnosisStoryDialogInteractorImpl.class);
    private FallDiagnosisStoryServerConnection fallDiagnosisStoryServerConnection;

    public FallDiagnosisStoryDialogInteractorImpl(FallDiagnosisStoryDialogPresenter fallDiagnosisStoryDialogPresenter) {
        this.fallDiagnosisStoryDialogPresenter = fallDiagnosisStoryDialogPresenter;
    }

    @Override
    public User getUser() {
        return this.user;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public FallDiagnosisStory getFallDiagnosisStory() {
        return fallDiagnosisStory;
    }

    @Override
    public void setFallDiagnosisStory(FallDiagnosisStory fallDiagnosisStory) {
        this.fallDiagnosisStory = fallDiagnosisStory;
    }

    @Override
    public FallDiagnosisStoryInfo getFallDiagnosisStoryInfo() {
        return fallDiagnosisStoryInfo;
    }

    @Override
    public void setFallDiagnosisStoryInfo(FallDiagnosisStoryInfo fallDiagnosisStoryInfo) {
        this.fallDiagnosisStoryInfo = fallDiagnosisStoryInfo;
    }

    @Override
    public void setFallDiagnosisStoryDeleted() {
        String accessToken = user.getAccess_token();
        int storyId = fallDiagnosisStoryInfo.getStory_id();
        fallDiagnosisStoryServerConnection = new NetworkInterceptor(accessToken).getFallDiagnosisStoryServer().create(FallDiagnosisStoryServerConnection.class);
        Call<ResponseBody> callDeleteFalldiagnosisStory = fallDiagnosisStoryServerConnection.deleteFalldiagnosisStory(storyId);
        callDeleteFalldiagnosisStory.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    fallDiagnosisStoryDialogPresenter.onSuccessSetFallDiagnosisStoryDeleted();
                } else {
                    fallDiagnosisStoryDialogPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                log(t);
                fallDiagnosisStoryDialogPresenter.onNetworkError(null);
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
