package com.demand.well_family.well_family.family.edit.presenter.impl;

import android.content.Context;
import android.net.Uri;
import android.view.View;

import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.family.edit.flag.EditFamilyCodeFlag;
import com.demand.well_family.well_family.family.edit.interactor.EditFamilyInteractor;
import com.demand.well_family.well_family.family.edit.interactor.impl.EditFamilyInteractorImpl;
import com.demand.well_family.well_family.family.edit.presenter.EditFamilyPresenter;
import com.demand.well_family.well_family.family.edit.view.EditFamilyView;
import com.demand.well_family.well_family.util.APIErrorUtil;
import com.demand.well_family.well_family.util.FileToBase64Util;
import com.demand.well_family.well_family.util.PreferenceUtil;
import com.demand.well_family.well_family.util.RealPathUtil;

/**
 * Created by Dev-0 on 2017-04-20.
 */

public class EditFamilyPresenterImpl implements EditFamilyPresenter {
    private EditFamilyView editFamilyView;
    private EditFamilyInteractor editFamilyInteractor;

    private PreferenceUtil preferenceUtil;
    private FileToBase64Util fileToBase64Util;
    private RealPathUtil realPathUtil;

    public EditFamilyPresenterImpl(Context context) {
        this.editFamilyView = (EditFamilyView) context;
        this.editFamilyInteractor = new EditFamilyInteractorImpl(this);

        this.preferenceUtil = new PreferenceUtil(context);
        this.fileToBase64Util = new FileToBase64Util(context);
        this.realPathUtil = new RealPathUtil(context);
    }


    @Override
    public void onCreate(Family family) {
        User user = preferenceUtil.getUserInfo();
        editFamilyView.init(family);
        editFamilyInteractor.setUser(user);
        editFamilyInteractor.setFamily(family);

        //toolbar
        View decorView = editFamilyView.getDecorView();
        editFamilyView.setToolbar(decorView);
        editFamilyView.showToolbarTitle("가족 수정");
        editFamilyView.setPermission();
    }

    @Override
    public void onClickEditFamily(String familyName, String familyContent) {
        Family family = editFamilyInteractor.getFamily();
        Uri uri = editFamilyInteractor.getPhotoUri();

        if (familyName.equals(family.getName()) && familyContent.equals(family.getContent()) && uri == null) {
            editFamilyView.navigateToBackground();
        } else {
            editFamilyView.showProgressDialog();
            editFamilyInteractor.setFamilyEdited(familyName, familyContent);
        }
    }

    @Override
    public void onSuccessSetFamilyEdited() {
        Uri uri = editFamilyInteractor.getPhotoUri();

        if (uri != null) {
            editFamilyInteractor.setFamilyAvatarEdited(fileToBase64Util);
        } else {
            editFamilyInteractor.getFamilyData();
        }
    }

    @Override
    public void onSuccessSetFamilyAvatarEdited() {
        editFamilyInteractor.getFamilyData();
    }

    @Override
    public void onSuccessGetFamilyData(Family family) {
        editFamilyView.goneProgressDialog();
        editFamilyView.navigateToFamilyActivity(family);
    }

    @Override
    public void onActivityResultForPhotoUriResultOk(Uri uri) {
        editFamilyInteractor.setPhotoUri(uri);
        editFamilyInteractor.setPhotoPath(realPathUtil);
        editFamilyView.showFamilyAvatar(uri);
    }

    @Override
    public void onNetworkError(APIErrorUtil apiErrorUtil) {
        if (apiErrorUtil == null) {
            editFamilyView.showMessage("네트워크 불안정합니다. 다시 시도하세요.");
        } else {
            editFamilyView.showMessage(apiErrorUtil.message());
        }
    }

    @Override
    public void onRequestPermissionsResultForReadExternalStorage(int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == EditFamilyCodeFlag.PERMISSION_DENIED) {
            editFamilyView.showMessage("권한을 허가해주세요");
            editFamilyView.navigateToBackground();
        }
    }
}
