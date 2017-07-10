package com.demand.well_family.well_family.exercise.create.interactor.impl;

import com.demand.well_family.well_family.dto.ExerciseCategory;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.exercise.create.interactor.CreateExerciseInteractor;
import com.demand.well_family.well_family.exercise.create.presenter.CreateExercisePresenter;
import com.demand.well_family.well_family.flag.LogFlag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ㅇㅇ on 2017-06-26.
 */

public class CreateExerciseInteractorImpl implements CreateExerciseInteractor {
    private CreateExercisePresenter createExercisePresenter;
    private ExerciseCategory exerciseCategory;
    private User user;
    private SeekBarThread seekBarThread;

    private boolean isPlaying;
    private static final Logger logger = LoggerFactory.getLogger(CreateExerciseInteractorImpl.class);

    public CreateExerciseInteractorImpl(CreateExercisePresenter createExercisePresenter) {
        this.createExercisePresenter = createExercisePresenter;
        this.seekBarThread = new SeekBarThread();
        this.isPlaying = true;
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
    public ExerciseCategory getExerciseCategory() {
        return exerciseCategory;
    }

    @Override
    public void setExerciseCategory(ExerciseCategory exerciseCategory) {
        this.exerciseCategory = exerciseCategory;
    }

    @Override
    public boolean isPlaying() {
        return isPlaying;
    }

    @Override
    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    @Override
    public void setSeekBarStart() {
        seekBarThread.start();
    }

    @Override
    public void log(Throwable throwable) {
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

    private class SeekBarThread extends Thread {
        @Override
        public void run() {
            while (isPlaying) {
                createExercisePresenter.setProgress();
            }
        }
    }
}
