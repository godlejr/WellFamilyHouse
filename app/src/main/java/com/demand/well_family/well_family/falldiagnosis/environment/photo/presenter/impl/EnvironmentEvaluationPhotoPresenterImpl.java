package com.demand.well_family.well_family.falldiagnosis.environment.photo.presenter.impl;

import android.content.Context;
import android.net.Uri;
import android.view.View;

import com.demand.well_family.well_family.dto.FallDiagnosisCategory;
import com.demand.well_family.well_family.dto.FallDiagnosisContentCategory;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.falldiagnosis.environment.photo.flag.EnvironmentEvaluationPhotoCodeFlag;
import com.demand.well_family.well_family.falldiagnosis.environment.photo.interactor.EnvironmentEvaluationPhotoInteractor;
import com.demand.well_family.well_family.falldiagnosis.environment.photo.interactor.impl.EnvironmentEvaluationPhotoInteractorImpl;
import com.demand.well_family.well_family.falldiagnosis.environment.photo.presenter.EnvironmentEvaluationPhotoPresenter;
import com.demand.well_family.well_family.falldiagnosis.environment.photo.view.EnvironmentEvaluationPhotoView;
import com.demand.well_family.well_family.story.create.flag.CreateStoryCodeFlag;
import com.demand.well_family.well_family.util.PreferenceUtil;
import com.demand.well_family.well_family.util.RealPathUtil;

import java.util.ArrayList;

/**
 * Created by Dev-0 on 2017-05-30.
 */

public class EnvironmentEvaluationPhotoPresenterImpl implements EnvironmentEvaluationPhotoPresenter {
    private EnvironmentEvaluationPhotoView environmentEvaluationPhotoView;
    private EnvironmentEvaluationPhotoInteractor environmentEvaluationPhotoInteractor;
    private PreferenceUtil preferenceUtil;
    private RealPathUtil realPathUtil;

    public EnvironmentEvaluationPhotoPresenterImpl(Context context) {
        this.environmentEvaluationPhotoView = (EnvironmentEvaluationPhotoView) context;
        this.environmentEvaluationPhotoInteractor = new EnvironmentEvaluationPhotoInteractorImpl(this);
        this.preferenceUtil = new PreferenceUtil(context);
        this.realPathUtil = new RealPathUtil(context);
    }

    @Override
    public void onCreate(FallDiagnosisCategory fallDiagnosisCategory, FallDiagnosisContentCategory fallDiagnosisContentCategory, ArrayList<Integer> answerList, int environmentEvaluationCategorySize) {
        User user = preferenceUtil.getUserInfo();
        environmentEvaluationPhotoInteractor.setUser(user);
        environmentEvaluationPhotoInteractor.setAnswerList(answerList);
        environmentEvaluationPhotoInteractor.setEnvironmentEvaluationCategorySize(environmentEvaluationCategorySize);
        environmentEvaluationPhotoInteractor.setFallDiagnosisCategory(fallDiagnosisCategory);
        environmentEvaluationPhotoInteractor.setFallDiagnosisContentCategory(fallDiagnosisContentCategory);

        environmentEvaluationPhotoView.setPermission();


        View decorView = environmentEvaluationPhotoView.getDecorView();
        environmentEvaluationPhotoView.setToolbar(decorView);
        environmentEvaluationPhotoView.showToolbar(fallDiagnosisContentCategory.getName());
    }

    @Override
    public void onLoadData() {
        FallDiagnosisContentCategory fallDiagnosisContentCategory = environmentEvaluationPhotoInteractor.getFallDiagnosisContentCategory();
        String fallDiagnosisContentCategoryName = fallDiagnosisContentCategory.getName();

        environmentEvaluationPhotoView.setNoPhotoView();
        environmentEvaluationPhotoView.showContent("????????? " + fallDiagnosisContentCategoryName + " ??? ???????????? ????????? ?????? ???????????? ??????????????????.");
    }

    @Override
    public void onRequestPermissionsResultForCameraExternalStorage(int[] grantResults) {
        if (grantResults.length > 1 && (grantResults[0] == CreateStoryCodeFlag.PERMISSION_DENIED || grantResults[1] == CreateStoryCodeFlag.PERMISSION_DENIED)) {
            environmentEvaluationPhotoView.showMessage("????????? ??????????????????.");
            environmentEvaluationPhotoView.navigateToBack();
        } else {
            environmentEvaluationPhotoView.init();
        }
    }

    @Override
    public void onClickCamera() {
        ArrayList<Uri> photoList = environmentEvaluationPhotoInteractor.getPhotoList();

        int photoSize = photoList.size();
        if (photoSize == 4) {
            environmentEvaluationPhotoView.showMessage("????????? 4???????????? ????????? ????????????.");
        } else {
            environmentEvaluationPhotoView.showCamera();
        }
    }

    @Override
    public void onActivityResultForCameraUriResultOk(Uri uri, String path) {

    }

    @Override
    public void onActivityResultForCameraUriResultOk(Uri uri) {
        environmentEvaluationPhotoInteractor.setPhotoPath(realPathUtil, uri);
        ArrayList<Uri> photoList = environmentEvaluationPhotoInteractor.getPhotoList();

        int photoSize = photoList.size();

        if (photoSize == EnvironmentEvaluationPhotoCodeFlag.THERE_ARE_PHOTOS) {
            environmentEvaluationPhotoView.goneInflatedView();
            environmentEvaluationPhotoView.setPhotoItem(photoList);
        }
        environmentEvaluationPhotoView.showPhotoSize(String.valueOf(photoSize));
        environmentEvaluationPhotoView.showPhotoAdapterNotifyDataChanged();
    }

    @Override
    public void onClickPhotoDelete(int position) {

        environmentEvaluationPhotoView.setPhotoAdapterDelete(position);
        ArrayList<Uri> photoList = environmentEvaluationPhotoInteractor.getPhotoList();
        int photoSize = photoList.size();

        if (photoSize == EnvironmentEvaluationPhotoCodeFlag.THERE_IS_NO_PHOTO) {
            environmentEvaluationPhotoView.goneInflatedView();
            environmentEvaluationPhotoView.setNoPhotoView();
        } else {
            environmentEvaluationPhotoView.showPhotoAdapterNotifyDataChanged();
        }
        environmentEvaluationPhotoView.showPhotoSize(String.valueOf(photoSize));
        environmentEvaluationPhotoView.showMessage("????????? ?????????????????????.");
    }

    @Override
    public void onClickJump() {
        FallDiagnosisCategory fallDiagnosisCategory = environmentEvaluationPhotoInteractor.getFallDiagnosisCategory();
        FallDiagnosisContentCategory fallDiagnosisContentCategory = environmentEvaluationPhotoInteractor.getFallDiagnosisContentCategory();
        ArrayList<Integer> answerList = environmentEvaluationPhotoInteractor.getAnswerList();
        int environmentEvaluationCategorySize = environmentEvaluationPhotoInteractor.getEnvironmentEvaluationCategorySize();

        environmentEvaluationPhotoView.navigateToEnvironmentEvaluationResultActivity(fallDiagnosisCategory, fallDiagnosisContentCategory, answerList, environmentEvaluationCategorySize);
    }

    @Override
    public void onClickSave() {
        ArrayList<Uri> photoList = environmentEvaluationPhotoInteractor.getPhotoList();
        ArrayList<String> pathList = environmentEvaluationPhotoInteractor.getPathList();

        int photoSize = photoList.size();

        if(photoSize == 0){
            environmentEvaluationPhotoView.showMessage("????????? ????????? ????????????. ????????? ??????????????????.");

        }else{
            FallDiagnosisCategory fallDiagnosisCategory = environmentEvaluationPhotoInteractor.getFallDiagnosisCategory();
            FallDiagnosisContentCategory fallDiagnosisContentCategory = environmentEvaluationPhotoInteractor.getFallDiagnosisContentCategory();
            ArrayList<Integer> answerList = environmentEvaluationPhotoInteractor.getAnswerList();

            int environmentEvaluationCategorySize = environmentEvaluationPhotoInteractor.getEnvironmentEvaluationCategorySize();

            environmentEvaluationPhotoView.navigateToEnvironmentEvaluationResultActivity(fallDiagnosisCategory, fallDiagnosisContentCategory, answerList, photoList, pathList, environmentEvaluationCategorySize);
        }


    }

    @Override
    public void onClickNotDefaultCamera() {
        environmentEvaluationPhotoView.showMessage("?????? ????????? ????????? ??????????????????.");
    }
}
