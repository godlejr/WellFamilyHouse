package com.demand.well_family.well_family.family.create.presenter.impl;

import android.content.Context;
import android.net.Uri;
import android.view.View;

import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.family.create.flag.CreateFamilyCodeFlag;
import com.demand.well_family.well_family.family.create.interactor.CreateFamilyInteractor;
import com.demand.well_family.well_family.family.create.interactor.impl.CreateFamilyInteractorImpl;
import com.demand.well_family.well_family.family.create.presenter.CreateFamilyPresenter;
import com.demand.well_family.well_family.family.create.view.CreateFamilyView;
import com.demand.well_family.well_family.util.APIErrorUtil;
import com.demand.well_family.well_family.util.FileToBase64Util;
import com.demand.well_family.well_family.util.PreferenceUtil;
import com.demand.well_family.well_family.util.RealPathUtil;

/**
 * Created by Dev-0 on 2017-04-19.
 */

public class CreateFamilyPresenterImpl implements CreateFamilyPresenter {
    private CreateFamilyView createFamilyView;
    private CreateFamilyInteractor createFamilyInteractor;
    private PreferenceUtil preferenceUtil;
    private FileToBase64Util fileToBase64Util;
    private RealPathUtil realPathUtil;

    public CreateFamilyPresenterImpl(Context context) {
        this.createFamilyView = (CreateFamilyView) context;
        this.createFamilyInteractor = new CreateFamilyInteractorImpl(this);
        this.preferenceUtil = new PreferenceUtil(context);
        this.fileToBase64Util = new FileToBase64Util(context);
        this.realPathUtil = new RealPathUtil(context);
    }


    @Override
    public void onCreate() {
        User user = preferenceUtil.getUserInfo();
        createFamilyView.init();
        createFamilyInteractor.setUser(user);

        //toolbar
        View decorView = createFamilyView.getDecorView();
        createFamilyView.setToolbar(decorView);
        createFamilyView.showToolbarTitle("가족 만들기");
        createFamilyView.setPermission();
    }

    @Override
    public void onSuccessGetFamilyData(Family family) {
        createFamilyView.goneProgressDialog();
        createFamilyView.navigateToFamilyActivity(family);
    }

    @Override
    public void onSuccessSetFamilyAvatarAdded(int familyId) {
        createFamilyInteractor.getFamilyData(familyId);
    }

    @Override
    public void onSuccessSetFamilyAdded(int familyId) {
        Uri uri = createFamilyInteractor.getPhotoUri();

        if (uri != null) {
            createFamilyInteractor.setFamilyAvatarAdded(familyId, fileToBase64Util);
        } else {
            createFamilyInteractor.getFamilyData(familyId);
        }
    }

    @Override
    public void onClickCreateFamily(String familyName, String familyContent) {
        if (familyName.length() == 0) {
            createFamilyView.showMessage("가족 이름을 입력하세요");
        } else if (familyContent.length() == 0) {
            createFamilyView.showMessage("가족 소개를 입력하세요");
        } else {
            createFamilyView.showProgressDialog();
            createFamilyInteractor.setFamilyAdded(familyName, familyContent);
        }

    }

    @Override
    public void onActivityResultForPhotoUriResultOk(Uri uri) {
        createFamilyInteractor.setPhotoUri(uri);
        createFamilyInteractor.setPhotoPath(realPathUtil);
        createFamilyView.showFamilyAvatar(uri);
    }

    @Override
    public void onNetworkError(APIErrorUtil apiErrorUtil) {
        if (apiErrorUtil == null) {
            createFamilyView.showMessage("네트워크 불안정합니다. 다시 시도하세요.");
        } else {
            createFamilyView.showMessage(apiErrorUtil.message());
        }
    }

    @Override
    public void onRequestPermissionsResultForReadExternalStorage(int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == CreateFamilyCodeFlag.PERMISSION_DENIED) {
            createFamilyView.showMessage("권한을 허가해주세요");
            createFamilyView.navigateToBackground();
        }
    }
}
