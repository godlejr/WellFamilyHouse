package com.demand.well_family.well_family.falldiagnosis.environment.result.interactor.impl;

import android.net.Uri;

import com.demand.well_family.well_family.dto.FallDiagnosisCategory;
import com.demand.well_family.well_family.dto.FallDiagnosisContentCategory;
import com.demand.well_family.well_family.dto.FallDiagnosisStory;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.falldiagnosis.environment.result.interactor.EnvironmentEvaluationResultInteractor;
import com.demand.well_family.well_family.falldiagnosis.environment.result.presenter.EnvironmentEvaluationResultPresenter;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.repository.FallDiagnosisStoryServerConnection;
import com.demand.well_family.well_family.repository.interceptor.HeaderInterceptor;
import com.demand.well_family.well_family.util.ErrorUtil;
import com.demand.well_family.well_family.util.FileToBase64Util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Dev-0 on 2017-05-31.
 */

public class EnvironmentEvaluationResultInteractorImpl implements EnvironmentEvaluationResultInteractor {
    private EnvironmentEvaluationResultPresenter environmentEvaluationResultPresenter;

    private FallDiagnosisCategory fallDiagnosisCategory;
    private FallDiagnosisContentCategory fallDiagnosisContentCategory;

    private ArrayList<Integer> answerList;
    private int environmentEvaluationCategorySize;

    private User user;

    private ArrayList<Uri> photoList;
    private ArrayList<String> pathList;

    private FallDiagnosisStory fallDiagnosisStory;
    private int fallDiagnosisRiskCategoryId;

    private static final Logger logger = LoggerFactory.getLogger(EnvironmentEvaluationResultInteractorImpl.class);
    private FallDiagnosisStoryServerConnection fallDiagnosisStoryServerConnection;


    public EnvironmentEvaluationResultInteractorImpl(EnvironmentEvaluationResultPresenter environmentEvaluationResultPresenter) {
        this.environmentEvaluationResultPresenter = environmentEvaluationResultPresenter;
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
    public void setFallDiagnosisContentCategory(FallDiagnosisContentCategory fallDiagnosisContentCategory) {
        this.fallDiagnosisContentCategory = fallDiagnosisContentCategory;
    }

    @Override
    public ArrayList<Integer> getAnswerList() {
        return this.answerList;
    }

    @Override
    public void setAnswerList(ArrayList<Integer> answerList) {
        this.answerList = answerList;
    }

    @Override
    public ArrayList<String> getPathList() {
        return pathList;
    }

    @Override
    public void setPathList(ArrayList<String> pathList) {
        this.pathList = pathList;
    }

    @Override
    public ArrayList<Uri> getPhotoList() {
        return photoList;
    }

    @Override
    public void setPhotoList(ArrayList<Uri> photoList) {
        this.photoList = photoList;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
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
    public int getEnvironmentEvaluationCategorySize() {
        return environmentEvaluationCategorySize;
    }

    @Override
    public void setEnvironmentEvaluationCategorySize(int environmentEvaluationCategorySize) {
        this.environmentEvaluationCategorySize = environmentEvaluationCategorySize;
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
                    environmentEvaluationResultPresenter.onSuccessSetStoryAdded(storyId);
                } else {
                    environmentEvaluationResultPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                log(t);
                environmentEvaluationResultPresenter.onNetworkError(null);
            }
        });
    }

    @Override
    public void setEnvironmentEvaluationAdded(final int storyId, int environmentEvaluationCategoryId, final int index) {
        String accessToken = user.getAccess_token();
        int userId = user.getId();
        final int endOfAnswerList = answerList.size() - 1;

        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", String.valueOf(userId));
        map.put("environment_evaluation_category_id", String.valueOf(environmentEvaluationCategoryId));


        fallDiagnosisStoryServerConnection = new HeaderInterceptor(accessToken).getFallDiagnosisStoryServer().create(FallDiagnosisStoryServerConnection.class);

        Call<ResponseBody> call_enviroment_evaluation = fallDiagnosisStoryServerConnection.insertEnvironmentEvaluation(storyId, map);

        call_enviroment_evaluation.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if (endOfAnswerList == index) {
                        environmentEvaluationResultPresenter.onSuccessSetEnvironmentEvaluationAdded(storyId);
                    }
                } else {
                    environmentEvaluationResultPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                log(t);
                environmentEvaluationResultPresenter.onNetworkError(null);
            }
        });

    }

    @Override
    public void setPhotoAdded(FileToBase64Util fileToBase64Util, int storyId, Uri photo, String path) {
        String accessToken = user.getAccess_token();
        fallDiagnosisStoryServerConnection = new HeaderInterceptor(accessToken).getFallDiagnosisStoryServer().create(FallDiagnosisStoryServerConnection.class);
        final RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), fileToBase64Util.addBase64Bitmap(fileToBase64Util.encodeImage(photo, path)));
        Call<ResponseBody> call_environment_photo = fallDiagnosisStoryServerConnection.insertEnvironmentPhoto(storyId, requestBody);

        call_environment_photo.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    //성공
                } else {
                    environmentEvaluationResultPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                log(t);
                environmentEvaluationResultPresenter.onNetworkError(null);
            }
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log(e);
        }
    }

    public int getFallDiagnosisRiskCategoryId() {
        return fallDiagnosisRiskCategoryId;
    }

    public void setFallDiagnosisRiskCategoryId(int fallDiagnosisRiskCategoryId) {
        this.fallDiagnosisRiskCategoryId = fallDiagnosisRiskCategoryId;
    }
}
