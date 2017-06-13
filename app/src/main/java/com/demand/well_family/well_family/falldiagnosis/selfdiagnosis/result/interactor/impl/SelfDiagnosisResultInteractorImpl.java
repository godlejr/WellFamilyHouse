package com.demand.well_family.well_family.falldiagnosis.selfdiagnosis.result.interactor.impl;

import com.demand.well_family.well_family.dto.FallDiagnosisCategory;
import com.demand.well_family.well_family.dto.FallDiagnosisStory;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.falldiagnosis.selfdiagnosis.result.interactor.SelfDiagnosisResultInteractor;
import com.demand.well_family.well_family.falldiagnosis.selfdiagnosis.result.presenter.SelfDiagnosisResultPresenter;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.repository.FallDiagnosisStoryServerConnection;
import com.demand.well_family.well_family.repository.interceptor.NetworkInterceptor;
import com.demand.well_family.well_family.util.ErrorUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ㅇㅇ on 2017-05-23.
 */

public class SelfDiagnosisResultInteractorImpl implements SelfDiagnosisResultInteractor {
    private SelfDiagnosisResultPresenter selfDiagnosisResultPresenter;
    private User user;

    private FallDiagnosisCategory fallDiagnosisCategory;
    private ArrayList<Integer> answerList;

    private FallDiagnosisStory fallDiagnosisStory;

    private int fallDiagnosisContentCategorySize;

    private FallDiagnosisStoryServerConnection fallDiagnosisStoryServerConnection;

    private static final Logger logger = LoggerFactory.getLogger(SelfDiagnosisResultInteractorImpl.class);
    private int fallDiagnosisRiskCategoryId;


    public SelfDiagnosisResultInteractorImpl(SelfDiagnosisResultPresenter selfDiagnosisResultPresenter) {
        this.selfDiagnosisResultPresenter = selfDiagnosisResultPresenter;
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
    public FallDiagnosisCategory getFallDiagnosisCategory() {
        return fallDiagnosisCategory;
    }

    @Override
    public void setFallDiagnosisCategory(FallDiagnosisCategory fallDiagnosisCategory) {
        this.fallDiagnosisCategory = fallDiagnosisCategory;
    }

    @Override
    public void setAnswerList(ArrayList<Integer> answerList) {
        this.answerList = answerList;

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
    public void setStoryAdded() {
        String accessToken = user.getAccess_token();

        fallDiagnosisStoryServerConnection = new NetworkInterceptor(accessToken).getFallDiagnosisStoryServer().create(FallDiagnosisStoryServerConnection.class);
        Call<Integer> call_story = fallDiagnosisStoryServerConnection.insertFallDiagnosisStory(fallDiagnosisStory);
        call_story.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    int storyId = response.body();
                    selfDiagnosisResultPresenter.onSuccessSetStoryAdded(storyId);
                } else {
                    selfDiagnosisResultPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                log(t);
                selfDiagnosisResultPresenter.onNetworkError(null);
            }
        });

    }

    @Override
    public void setSelfDiagnosisAdded(int storyId, int fallDiagnosisContentCategoryId, final int index) {
        String accessToken = user.getAccess_token();
        int userId = user.getId();
        final int endOfAnswerList = answerList.size() -1;

        HashMap<String, String> map = new HashMap<>();
        map.put("user_id",String.valueOf(userId));
        map.put("fall_diagnosis_content_category_id",String.valueOf(fallDiagnosisContentCategoryId));


        fallDiagnosisStoryServerConnection = new NetworkInterceptor(accessToken).getFallDiagnosisStoryServer().create(FallDiagnosisStoryServerConnection.class);
        Call<ResponseBody> call_self_diagnosis = fallDiagnosisStoryServerConnection.insertSelfDiagnosis(storyId,map);

        call_self_diagnosis.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if(endOfAnswerList == index) {
                        selfDiagnosisResultPresenter.onSuccessSetSelfDiagnosisAdded();
                    }
                } else {
                    selfDiagnosisResultPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                log(t);
                selfDiagnosisResultPresenter.onNetworkError(null);
            }
        });
    }

    @Override
    public int getFallDiagnosisContentCategorySize() {
        return fallDiagnosisContentCategorySize;
    }

    @Override
    public void setFallDiagnosisContentCategorySize(int fallDiagnosisContentCategorySize) {
        this.fallDiagnosisContentCategorySize = fallDiagnosisContentCategorySize;
    }

    @Override
    public int getFallDiagnosisRiskCategoryId() {
        return fallDiagnosisRiskCategoryId;
    }

    @Override
    public void setFallDiagnosisRiskCategoryId(int fallDiagnosisRiskCategoryId) {
        this.fallDiagnosisRiskCategoryId = fallDiagnosisRiskCategoryId;
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
