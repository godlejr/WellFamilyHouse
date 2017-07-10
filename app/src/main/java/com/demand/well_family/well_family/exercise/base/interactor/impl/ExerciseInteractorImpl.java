package com.demand.well_family.well_family.exercise.base.interactor.impl;

import com.demand.well_family.well_family.dto.ExerciseCategory;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.exercise.base.interactor.ExerciseInteractor;
import com.demand.well_family.well_family.exercise.base.presetner.ExercisePresenter;
import com.demand.well_family.well_family.exercise.base.presetner.impl.ExercisePresenterImpl;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.repository.ExerciseServerConnection;
import com.demand.well_family.well_family.repository.interceptor.NetworkInterceptor;
import com.demand.well_family.well_family.util.ErrorUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ㅇㅇ on 2017-06-23.
 */

public class ExerciseInteractorImpl implements ExerciseInteractor {
    private ExercisePresenter exercisePresenter;

    private User user;
    private ExerciseServerConnection exerciseServerConnection;
    private ArrayList<ExerciseCategory> exerciseCategoryList;
    private static final Logger logger = LoggerFactory.getLogger(ExerciseInteractorImpl.class);

    public ExerciseInteractorImpl(ExercisePresenterImpl exercisePresenter) {
        this.exercisePresenter = exercisePresenter;
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
    public void getExerciseCategoryList() {
        String accessToken = user.getAccess_token();
        exerciseServerConnection = new NetworkInterceptor(accessToken).getExerciseServer().create(ExerciseServerConnection.class);
        Call<ArrayList<ExerciseCategory>> callGetExerciseCategoryList = exerciseServerConnection.selectExerciseCategoryList();
        callGetExerciseCategoryList.enqueue(new Callback<ArrayList<ExerciseCategory>>() {
            @Override
            public void onResponse(Call<ArrayList<ExerciseCategory>> call, Response<ArrayList<ExerciseCategory>> response) {
                if (response.isSuccessful()) {
                    exerciseCategoryList = response.body();
                    exercisePresenter.onSuccessGetExerciseCategoryList(exerciseCategoryList);
                } else {
                    exercisePresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ExerciseCategory>> call, Throwable t) {
                log(t);
                exercisePresenter.onNetworkError(null);
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
