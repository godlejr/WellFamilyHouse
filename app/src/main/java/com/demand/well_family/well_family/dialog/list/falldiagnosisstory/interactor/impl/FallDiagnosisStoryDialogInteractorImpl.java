package com.demand.well_family.well_family.dialog.list.falldiagnosisstory.interactor.impl;

import com.demand.well_family.well_family.dialog.list.falldiagnosisstory.interactor.FallDiagnosisStoryDialogInteractor;
import com.demand.well_family.well_family.dialog.list.falldiagnosisstory.presenter.FallDiagnosisStoryDialogPresenter;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.flag.LogFlag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ㅇㅇ on 2017-06-08.
 */

public class FallDiagnosisStoryDialogInteractorImpl implements FallDiagnosisStoryDialogInteractor {
    private FallDiagnosisStoryDialogPresenter fallDiagnosisStoryDialogPresenter;

    private User user;
    private static final Logger logger = LoggerFactory.getLogger(FallDiagnosisStoryDialogInteractorImpl.class);


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
    public void setFallDiagnosisStoryDeleted() {
        String accessToken = user.getAccess_token();

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
