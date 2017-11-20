package com.demand.well_family.well_family.story.create.presenter.impl;

import android.content.Context;
import android.net.Uri;
import android.view.View;

import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.dto.StoryInfo;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.story.create.flag.CreateStoryCodeFlag;
import com.demand.well_family.well_family.story.create.interactor.CreateStoryInteractor;
import com.demand.well_family.well_family.story.create.interactor.impl.CreateStoryInteractorImpl;
import com.demand.well_family.well_family.story.create.presenter.CreateStoryPresenter;
import com.demand.well_family.well_family.story.create.view.CreateStoryView;
import com.demand.well_family.well_family.util.APIErrorUtil;
import com.demand.well_family.well_family.util.FileToBase64Util;
import com.demand.well_family.well_family.util.PreferenceUtil;
import com.demand.well_family.well_family.util.RealPathUtil;

import java.util.ArrayList;

/**
 * Created by Dev-0 on 2017-05-22.
 */

public class CreateStoryPresenterImpl implements CreateStoryPresenter {

    private CreateStoryView createStoryView;
    private CreateStoryInteractor createStoryInteractor;
    private PreferenceUtil preferenceUtil;
    private RealPathUtil realPathUtil;
    private FileToBase64Util fileToBase64Util;


    public CreateStoryPresenterImpl(Context context) {
        this.createStoryView = (CreateStoryView) context;
        this.createStoryInteractor = new CreateStoryInteractorImpl(this);

        this.preferenceUtil = new PreferenceUtil(context);
        this.realPathUtil = new RealPathUtil(context);
        this.fileToBase64Util = new FileToBase64Util(context);
    }

    @Override
    public void onCreate(Family family) {
        User user = preferenceUtil.getUserInfo();
        createStoryInteractor.setUser(user);
        createStoryInteractor.setFamily(family);
        createStoryView.init(user);
        createStoryView.setPermission();

        View decorView = createStoryView.getDecorView();
        createStoryView.setToolbar(decorView);
        createStoryView.showToolbarTitle("글쓰기");
    }

    @Override
    public void onLoadData() {
        ArrayList<Uri> photoList = createStoryInteractor.getPhotoList();
        createStoryView.setPhotoItem(photoList);
    }

    @Override
    public void onClickPhotoAdd(int sdkInt, int kitkat) {
        ArrayList<Uri> photoList = createStoryInteractor.getPhotoList();
        int photoSize = photoList.size();
        if (photoSize >= 10) {
            createStoryView.showMessage("사진은 최대 10개까지 등록이 가능합니다.");
        } else {
            if (sdkInt > kitkat) {
                createStoryView.navigateToMultiMediaStore();
            } else {
                createStoryView.navigateToMediaStore();
            }
        }
    }

    @Override
    public void onRequestPermissionsResultForReadExternalStorage(int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == CreateStoryCodeFlag.PERMISSION_DENIED) {
            createStoryView.showMessage("권한을 허가해주세요.");
            createStoryView.navigateToBack();
        }
    }

    @Override
    public void onActivityResultForPhotoUriResultOk(Uri uri) {
        createStoryInteractor.setPhotoPath(realPathUtil, uri);
        createStoryView.showPhotoAdapterNotifyDataChanged();
    }

    @Override
    public void onActivityResultForPhotoUriResultOk1(Uri uri) {
        createStoryInteractor.setPhotoPath1(realPathUtil, uri);
        createStoryView.showPhotoAdapterNotifyDataChanged();
    }

    @Override
    public void onSuccessSetStoryAdded(StoryInfo storyInfo) {
        ArrayList<Uri> photoList = createStoryInteractor.getPhotoList();
        ArrayList<String> pathList = createStoryInteractor.getPathList();

        int photoSize = photoList.size();
        if(photoSize > 0) {
            for (int i = 0; i < photoSize; i++) {
                createStoryView.setProgressDialog(i + 1);
                createStoryInteractor.setPhotoAdded(fileToBase64Util, storyInfo, photoList.get(i), pathList.get(i));
            }

        }

        createStoryView.goneProgressDialog();
        createStoryView.navigateToBack(storyInfo);
    }

    @Override
    public void onClickStoryAdd(String content) {
        ArrayList<Uri> photoList = createStoryInteractor.getPhotoList();
        int photoSize = photoList.size();

        if (photoSize != 0 || content.length() != 0) {
            createStoryView.showProgressDialog();
            createStoryInteractor.setStoryAdded(content);
        }

    }

    @Override
    public void onNetworkError(APIErrorUtil apiErrorUtil) {
        if (apiErrorUtil == null) {
            createStoryView.showMessage("네트워크 불안정합니다. 다시 시도하세요.");
        } else {
            createStoryView.showMessage(apiErrorUtil.message());
        }
    }

    @Override
    public void onClickPhotoDelete(int position) {
        createStoryView.setPhotoAdapterDelete(position);
        createStoryView.showPhotoAdapterNotifyDataChanged();
        createStoryView.showMessage("사진이 삭제되었습니다.");
    }
}
