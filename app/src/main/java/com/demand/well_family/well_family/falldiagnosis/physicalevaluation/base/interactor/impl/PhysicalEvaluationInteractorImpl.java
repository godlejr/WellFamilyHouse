package com.demand.well_family.well_family.falldiagnosis.physicalevaluation.base.interactor.impl;

import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.falldiagnosis.physicalevaluation.base.interactor.PhysicalEvaluationInteractor;
import com.demand.well_family.well_family.falldiagnosis.physicalevaluation.base.presenter.PhysicalEvaluationPresenter;
import com.demand.well_family.well_family.flag.LogFlag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ㅇㅇ on 2017-05-25.
 */

public class PhysicalEvaluationInteractorImpl implements PhysicalEvaluationInteractor {
    private PhysicalEvaluationPresenter physicalEvaluationPresenter;
    private static final Logger logger = LoggerFactory.getLogger(PhysicalEvaluationInteractorImpl.class);

    public PhysicalEvaluationInteractorImpl(PhysicalEvaluationPresenter physicalEvaluationPresenter) {
        this.physicalEvaluationPresenter = physicalEvaluationPresenter;
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
    public void getPhysicalEvaluationCategories(User user) {
        physicalEvaluationPresenter.onSuccessGetPhysicalEvaluationCategories(null);
    }
}
