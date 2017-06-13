package com.demand.well_family.well_family.falldiagnosis.physicalevaluation.result.interactor.impl;

import com.demand.well_family.well_family.dto.FallDiagnosisCategory;
import com.demand.well_family.well_family.dto.FallDiagnosisStory;
import com.demand.well_family.well_family.dto.PhysicalEvaluation;
import com.demand.well_family.well_family.dto.PhysicalEvaluationCategory;
import com.demand.well_family.well_family.dto.PhysicalEvaluationScore;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.falldiagnosis.physicalevaluation.result.interactor.PhysicalEvaluationResultInteractor;
import com.demand.well_family.well_family.falldiagnosis.physicalevaluation.result.presenter.PhysicalEvaluationResultPresenter;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.repository.FallDiagnosisStoryServerConnection;
import com.demand.well_family.well_family.repository.interceptor.NetworkInterceptor;
import com.demand.well_family.well_family.util.ErrorUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ㅇㅇ on 2017-05-26.
 */

public class PhysicalEvaluationResultInteractorImpl implements PhysicalEvaluationResultInteractor {
    private PhysicalEvaluationResultPresenter physicalEvaluationResultPresenter;

    private FallDiagnosisCategory fallDiagnosisCategory;
    private ArrayList<PhysicalEvaluationCategory> physicalEvaluationCategoryList;
    private ArrayList<PhysicalEvaluation> physicalEvaluationList;

    private User user;
    private PhysicalEvaluationScore physicalEvaluationScore;

    private FallDiagnosisStory fallDiagnosisStory;

    private static final Logger logger = LoggerFactory.getLogger(PhysicalEvaluationResultInteractorImpl.class);
    private FallDiagnosisStoryServerConnection fallDiagnosisStoryServerConnection;
    private int fallDiagnosisRiskCategoryId;

    public PhysicalEvaluationResultInteractorImpl(PhysicalEvaluationResultPresenter physicalEvaluationResultPresenter) {
        this.physicalEvaluationResultPresenter = physicalEvaluationResultPresenter;
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
    public ArrayList<PhysicalEvaluationCategory> getPhysicalEvaluationCategoryList() {
        return physicalEvaluationCategoryList;
    }

    @Override
    public void setPhysicalEvaluationCategoryList(ArrayList<PhysicalEvaluationCategory> physicalEvaluationCategoryList) {
        this.physicalEvaluationCategoryList = physicalEvaluationCategoryList;
    }

    @Override
    public ArrayList<PhysicalEvaluation> getPhysicalEvaluationList() {
        return physicalEvaluationList;
    }

    @Override
    public void setPhysicalEvaluationList(ArrayList<PhysicalEvaluation> physicalEvaluationList) {
        this.physicalEvaluationList = physicalEvaluationList;
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
                    physicalEvaluationResultPresenter.onSuccessSetStoryAdded(storyId);
                } else {
                    physicalEvaluationResultPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                log(t);
                physicalEvaluationResultPresenter.onNetworkError(null);
            }
        });
    }

    @Override
    public void setPhysicalEvaluationAdded(final int storyId, PhysicalEvaluation physicalEvaluation) {
        String accessToken = user.getAccess_token();
        int userId = user.getId();

        physicalEvaluation.setFall_diagnosis_story_id(storyId);
        physicalEvaluation.setUser_id(userId);

        fallDiagnosisStoryServerConnection = new NetworkInterceptor(accessToken).getFallDiagnosisStoryServer().create(FallDiagnosisStoryServerConnection.class);
        Call<ResponseBody> call_physical_evaluation = fallDiagnosisStoryServerConnection.insertPhysicalEvaluation(storyId, physicalEvaluation);

        call_physical_evaluation.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    //no return
                } else {
                    physicalEvaluationResultPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                log(t);
                physicalEvaluationResultPresenter.onNetworkError(null);
            }
        });

    }

    @Override
    public void setPhysicalEvaluationScoreAdded(int storyId) {
        String accessToken = user.getAccess_token();
        physicalEvaluationScore.setFall_diagnosis_story_id(storyId);


        fallDiagnosisStoryServerConnection = new NetworkInterceptor(accessToken).getFallDiagnosisStoryServer().create(FallDiagnosisStoryServerConnection.class);
        Call<ResponseBody> call_physical_evaluationn_score = fallDiagnosisStoryServerConnection.insertPhysicalEvaluationScore(storyId, physicalEvaluationScore);

        call_physical_evaluationn_score.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    physicalEvaluationResultPresenter.onSuccessSetPhysicalEvaluationScoreAdded();
                } else {
                    physicalEvaluationResultPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                log(t);
                physicalEvaluationResultPresenter.onNetworkError(null);
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
    public PhysicalEvaluationScore getPhysicalEvaluationScore() {
        return physicalEvaluationScore;
    }

    @Override
    public void setPhysicalEvaluationScore(PhysicalEvaluationScore physicalEvaluationScore) {
        this.physicalEvaluationScore = physicalEvaluationScore;
    }

    @Override
    public int getFallDiagnosisRiskCategoryId() {
        return fallDiagnosisRiskCategoryId;
    }

    @Override
    public void setFallDiagnosisRiskCategoryId(int fallDiagnosisRiskCategoryId) {
        this.fallDiagnosisRiskCategoryId = fallDiagnosisRiskCategoryId;
    }

}
