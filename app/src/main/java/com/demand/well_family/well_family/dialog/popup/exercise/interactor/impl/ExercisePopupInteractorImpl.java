package com.demand.well_family.well_family.dialog.popup.exercise.interactor.impl;

import com.demand.well_family.well_family.dialog.popup.exercise.interactor.ExercisePopupInteractor;
import com.demand.well_family.well_family.dialog.popup.exercise.presenter.ExercisePopupPresenter;
import com.demand.well_family.well_family.dto.ExerciseCategory;
import com.demand.well_family.well_family.dto.ExerciseStory;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.repository.ExerciseStoryServerConnection;
import com.demand.well_family.well_family.repository.interceptor.NetworkInterceptor;
import com.demand.well_family.well_family.util.ErrorUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ㅇㅇ on 2017-06-26.
 */

public class ExercisePopupInteractorImpl implements ExercisePopupInteractor {
    private ExercisePopupPresenter exercisePopupPresenter;

    private User user;
    private ExerciseCategory exerciseCategory;
    private ExerciseStoryServerConnection exerciseStoryServerConnection;
    private static final Logger logger = LoggerFactory.getLogger(ExercisePopupInteractorImpl.class);

    public ExercisePopupInteractorImpl(ExercisePopupPresenter exercisePopupPresenter) {
        this.exercisePopupPresenter = exercisePopupPresenter;
    }

    @Override
    public ExerciseCategory getExerciseCategory() {
        return exerciseCategory;
    }

    @Override
    public void setExerciseCategory(ExerciseCategory exerciseCategory) {
        this.exerciseCategory = exerciseCategory;
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
    public void setExerciseStoryAdded(ExerciseStory exerciseStory) {
        String accessToken = user.getAccess_token();

        exerciseStoryServerConnection = new NetworkInterceptor(accessToken).getExerciseStoryServer().create(ExerciseStoryServerConnection.class);
        Call<Integer> callSetExerciseStory = exerciseStoryServerConnection.insertExerciseStory(exerciseStory);
        callSetExerciseStory.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    int isSuccess = response.body();
                    exercisePopupPresenter.onSuccessSetExerciseStoryAdded(isSuccess);
                } else {
                    exercisePopupPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                log(t);
                exercisePopupPresenter.onNetworkError(null);
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
