package com.demand.well_family.well_family.falldiagnosis.fall.result.interactor.impl;

import com.demand.well_family.well_family.dto.FallDiagnosisCategory;
import com.demand.well_family.well_family.dto.FallDiagnosisStory;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.falldiagnosis.fall.result.interactor.SelfDiagnosisResultInteractor;
import com.demand.well_family.well_family.falldiagnosis.fall.result.presenter.SelfDiagnosisResultPresenter;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.repository.FallDiagnosisStoryServerConnection;
import com.demand.well_family.well_family.repository.interceptor.HeaderInterceptor;
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
    private ArrayList<Boolean> answerList;
    private ArrayList<Integer> trueList;

    private FallDiagnosisStory fallDiagnosisStory;

    private FallDiagnosisStoryServerConnection fallDiagnosisStoryServerConnection;

    private static final Logger logger = LoggerFactory.getLogger(SelfDiagnosisResultInteractorImpl.class);


    public SelfDiagnosisResultInteractorImpl(SelfDiagnosisResultPresenter selfDiagnosisResultPresenter) {
        this.selfDiagnosisResultPresenter = selfDiagnosisResultPresenter;
        this.trueList = new ArrayList<>();
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
    public ArrayList<Boolean> getAnswerList() {
        return answerList;
    }

    @Override
    public ArrayList<Integer> getTrueList() {
        return trueList;
    }

    @Override
    public void setTrueList(ArrayList<Integer> trueList) {
        this.trueList = trueList;
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
    public void setAnswerList(ArrayList<Boolean> answerList) {
        this.answerList = answerList;
        int answerSize = answerList.size();

        for (int i = 0; i < answerSize; i++) {
            if (answerList.get(i)) {
                this.trueList.add(i + 1);
            }
        }
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

        fallDiagnosisStoryServerConnection = new HeaderInterceptor(accessToken).getFallDiagnosisStoryServer().create(FallDiagnosisStoryServerConnection.class);
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
    public void setSelfDiagnosisAdded(int storyId, int selfDiagnosisCategoryId) {
        String accessToken = user.getAccess_token();
        int userId = user.getId();

        HashMap<String, String> map = new HashMap<>();
        map.put("user_id",String.valueOf(userId));
        map.put("self_diagnosis_category_id",String.valueOf(selfDiagnosisCategoryId));


        fallDiagnosisStoryServerConnection = new HeaderInterceptor(accessToken).getFallDiagnosisStoryServer().create(FallDiagnosisStoryServerConnection.class);
        Call<ResponseBody> call_self_diagnosis = fallDiagnosisStoryServerConnection.insertSelfDiagnosis(storyId,map);

        call_self_diagnosis.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    selfDiagnosisResultPresenter.onSuccessSetSelfDiagnosisAdded();
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
