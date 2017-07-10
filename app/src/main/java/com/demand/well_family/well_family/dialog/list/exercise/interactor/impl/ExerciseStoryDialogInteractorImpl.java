package com.demand.well_family.well_family.dialog.list.exercise.interactor.impl;

import com.demand.well_family.well_family.dialog.list.exercise.interactor.ExerciseStoryDialogInteractor;
import com.demand.well_family.well_family.dialog.list.exercise.presenter.ExerciseStoryDialogPresenter;
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
 * Created by ㅇㅇ on 2017-07-07.
 */

public class ExerciseStoryDialogInteractorImpl implements ExerciseStoryDialogInteractor {
    private ExerciseStoryDialogPresenter exerciseStoryDialogPresenter;
    private ExerciseStory exerciseStory;
    private User user;
    private ExerciseStoryServerConnection exerciseStoryServerConnection;
    private static final Logger logger = LoggerFactory.getLogger(ExerciseStoryDialogInteractorImpl.class);

    public ExerciseStoryDialogInteractorImpl(ExerciseStoryDialogPresenter exerciseStoryDialogPresenter) {
        this.exerciseStoryDialogPresenter = exerciseStoryDialogPresenter;
    }

    @Override
    public ExerciseStory getExerciseStory() {
        return exerciseStory;
    }

    @Override
    public void setExerciseStory(ExerciseStory exerciseStory) {
        this.exerciseStory = exerciseStory;
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
    public void setExerciseStoryDeleted() {
        int exerciseStoryId = exerciseStory.getId();
        String accessToken = user.getAccess_token();
        exerciseStoryServerConnection = new NetworkInterceptor(accessToken).getExerciseStoryServer().create(ExerciseStoryServerConnection.class);
        Call<ResponseBody> callDeleteExerciseStory = exerciseStoryServerConnection.deleteExerciseStory(exerciseStoryId);
        callDeleteExerciseStory.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    exerciseStoryDialogPresenter.onSuccessSetFallDiagnosisStoryDeleted();
                } else {
                    exerciseStoryDialogPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                log(t);
                exerciseStoryDialogPresenter.onNetworkError(null);
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
