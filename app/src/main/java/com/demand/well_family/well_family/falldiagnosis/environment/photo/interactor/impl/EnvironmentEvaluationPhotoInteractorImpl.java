package com.demand.well_family.well_family.falldiagnosis.environment.photo.interactor.impl;

import android.net.Uri;

import com.demand.well_family.well_family.dto.FallDiagnosisCategory;
import com.demand.well_family.well_family.dto.FallDiagnosisContentCategory;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.falldiagnosis.environment.photo.interactor.EnvironmentEvaluationPhotoInteractor;
import com.demand.well_family.well_family.falldiagnosis.environment.photo.presenter.EnvironmentEvaluationPhotoPresenter;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.util.RealPathUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Created by Dev-0 on 2017-05-30.
 */

public class EnvironmentEvaluationPhotoInteractorImpl implements EnvironmentEvaluationPhotoInteractor {
    private EnvironmentEvaluationPhotoPresenter environmentEvaluationPhotoPresenter;

    private FallDiagnosisCategory fallDiagnosisCategory;
    private FallDiagnosisContentCategory fallDiagnosisContentCategory;

    private ArrayList<Integer> answerList;
    private int environmentEvaluationCategorySize;
    private User user;

    private ArrayList<Uri> photoList;
    private ArrayList<String> pathList;


    private static final Logger logger = LoggerFactory.getLogger(EnvironmentEvaluationPhotoInteractorImpl.class);


    public EnvironmentEvaluationPhotoInteractorImpl(EnvironmentEvaluationPhotoPresenter environmentEvaluationPhotoPresenter) {
        this.environmentEvaluationPhotoPresenter = environmentEvaluationPhotoPresenter;
        this.photoList = new ArrayList<>();
        this.pathList = new ArrayList<>();
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

    @Override
    public void setPhotoPath(RealPathUtil realPathUtil, Uri uri) {
        String path = null;
        try {
            path = realPathUtil.getRealPathFromURI_API19(uri);
        } catch (RuntimeException e) {
            log(e);
            path = realPathUtil.getRealPathFromURI_API11to18(uri);
        }
        pathList.add(path);
        photoList.add(uri);
    }

    @Override
    public void setPhotoPathAndUri(String path, Uri uri) {
        pathList.add(path);
        photoList.add(uri);
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
}
