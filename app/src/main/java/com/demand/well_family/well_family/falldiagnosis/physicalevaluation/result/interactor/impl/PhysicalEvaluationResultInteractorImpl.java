package com.demand.well_family.well_family.falldiagnosis.physicalevaluation.result.interactor.impl;

import com.demand.well_family.well_family.falldiagnosis.physicalevaluation.result.interactor.PhysicalEvaluationResultInteractor;
import com.demand.well_family.well_family.falldiagnosis.physicalevaluation.result.presenter.PhysicalEvaluationResultPresenter;
import com.demand.well_family.well_family.flag.LogFlag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ㅇㅇ on 2017-05-26.
 */

public class PhysicalEvaluationResultInteractorImpl implements PhysicalEvaluationResultInteractor{
    private PhysicalEvaluationResultPresenter physicalEvaluationResultPresenter;

    private static final Logger logger = LoggerFactory.getLogger(PhysicalEvaluationResultInteractorImpl.class);

    public PhysicalEvaluationResultInteractorImpl(PhysicalEvaluationResultPresenter physicalEvaluationResultPresenter) {
        this.physicalEvaluationResultPresenter = physicalEvaluationResultPresenter;
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
