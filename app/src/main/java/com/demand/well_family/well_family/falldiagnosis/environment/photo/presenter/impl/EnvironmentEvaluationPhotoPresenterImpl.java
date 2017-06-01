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
import com.demand.well_family.well_family.falldiagnosis.environment.photo.view.EnvironmentEvaluationPhotoVIew;
import com.demand.well_family.well_family.story.create.flag.CreateStoryCodeFlag;
import com.demand.well_family.well_family.util.PreferenceUtil;
import com.demand.well_family.well_family.util.RealPathUtil;

import java.util.ArrayList;

/**
 * Created by Dev-0 on 2017-05-30.
 */

public class EnvironmentEvaluationPhotoPresenterImpl implements EnvironmentEvaluationPhotoPresenter {
    private EnvironmentEvaluationPhotoVIew environmentEvaluationPhotoVIew;
    private EnvironmentEvaluationPhotoInteractor environmentEvaluationPhotoInteractor;
    private PreferenceUtil preferenceUtil;
    private RealPathUtil realPathUtil;

    public EnvironmentEvaluationPhotoPresenterImpl(Context context) {
        this.environmentEvaluationPhotoVIew = (EnvironmentEvaluationPhotoVIew) context;
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

        environmentEvaluationPhotoVIew.init();

        View decorView = environmentEvaluationPhotoVIew.getDecorView();
        environmentEvaluationPhotoVIew.setToolbar(decorView);
        environmentEvaluationPhotoVIew.showToolbar(fallDiagnosisContentCategory.getName());
        environmentEvaluationPhotoVIew.setPermission();

    }

    @Override
    public void onLoadData() {
        FallDiagnosisContentCategory fallDiagnosisContentCategory = environmentEvaluationPhotoInteractor.getFallDiagnosisContentCategory();
        String fallDiagnosisContentCategoryName = fallDiagnosisContentCategory.getName();

        environmentEvaluationPhotoVIew.setNoPhotoView();
        environmentEvaluationPhotoVIew.showContent("우리집 " + fallDiagnosisContentCategoryName + " 내 낙상위험 요인을 찾아 사진으로 기록해보세요.");
    }

    @Override
    public void onRequestPermissionsResultForCameraExternalStorage(int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == CreateStoryCodeFlag.PERMISSION_DENIED) {
            environmentEvaluationPhotoVIew.showMessage("권한을 허가해주세요.");
            environmentEvaluationPhotoVIew.navigateToBack();
        }
    }

    @Override
    public void onClickCamera() {
        ArrayList<Uri> photoList = environmentEvaluationPhotoInteractor.getPhotoList();

        int photoSize = photoList.size();
        if (photoSize == 4) {
            environmentEvaluationPhotoVIew.showMessage("사진은 4장까지만 찍을수 있습니다.");
        } else {
            environmentEvaluationPhotoVIew.showCamera();
        }
    }

    @Override
    public void onActivityResultForCameraUriResultOk(Uri uri) {
        environmentEvaluationPhotoInteractor.setPhotoPath(realPathUtil, uri);
        ArrayList<Uri> photoList = environmentEvaluationPhotoInteractor.getPhotoList();

        int photoSize = photoList.size();

        if (photoSize == EnvironmentEvaluationPhotoCodeFlag.THERE_ARE_PHOTOS) {
            environmentEvaluationPhotoVIew.goneInflatedView();
            environmentEvaluationPhotoVIew.setPhotoItem(photoList);
        }
        environmentEvaluationPhotoVIew.showPhotoSize(String.valueOf(photoSize));
        environmentEvaluationPhotoVIew.showPhotoAdapterNotifyDataChanged();
    }

    @Override
    public void onClickPhotoDelete(int position) {

        environmentEvaluationPhotoVIew.setPhotoAdapterDelete(position);
        ArrayList<Uri> photoList = environmentEvaluationPhotoInteractor.getPhotoList();
        int photoSize = photoList.size();

        if (photoSize == EnvironmentEvaluationPhotoCodeFlag.THERE_IS_NO_PHOTO) {
            environmentEvaluationPhotoVIew.goneInflatedView();
            environmentEvaluationPhotoVIew.setNoPhotoView();
        } else {
            environmentEvaluationPhotoVIew.showPhotoAdapterNotifyDataChanged();
        }
        environmentEvaluationPhotoVIew.showPhotoSize(String.valueOf(photoSize));
        environmentEvaluationPhotoVIew.showMessage("사진이 삭제되었습니다.");
    }

    @Override
    public void onClickJump() {
        FallDiagnosisCategory fallDiagnosisCategory = environmentEvaluationPhotoInteractor.getFallDiagnosisCategory();
        FallDiagnosisContentCategory fallDiagnosisContentCategory = environmentEvaluationPhotoInteractor.getFallDiagnosisContentCategory();
        ArrayList<Integer> answerList = environmentEvaluationPhotoInteractor.getAnswerList();
        int environmentEvaluationCategorySize = environmentEvaluationPhotoInteractor.getEnvironmentEvaluationCategorySize();

        environmentEvaluationPhotoVIew.navigateToEnvironmentEvaluationResultActivity(fallDiagnosisCategory, fallDiagnosisContentCategory, answerList, environmentEvaluationCategorySize);
    }

    @Override
    public void onClickSave() {
        ArrayList<Uri> photoList = environmentEvaluationPhotoInteractor.getPhotoList();
        ArrayList<String> pathList = environmentEvaluationPhotoInteractor.getPathList();

        int photoSize = photoList.size();

        if(photoSize == 0){
            environmentEvaluationPhotoVIew.showMessage("저장할 사진이 없습니다. 사진을 촬영해주세요.");

        }else{
            FallDiagnosisCategory fallDiagnosisCategory = environmentEvaluationPhotoInteractor.getFallDiagnosisCategory();
            FallDiagnosisContentCategory fallDiagnosisContentCategory = environmentEvaluationPhotoInteractor.getFallDiagnosisContentCategory();
            ArrayList<Integer> answerList = environmentEvaluationPhotoInteractor.getAnswerList();

            int environmentEvaluationCategorySize = environmentEvaluationPhotoInteractor.getEnvironmentEvaluationCategorySize();

            environmentEvaluationPhotoVIew.navigateToEnvironmentEvaluationResultActivity(fallDiagnosisCategory, fallDiagnosisContentCategory, answerList, photoList, pathList, environmentEvaluationCategorySize);
        }


    }
}
