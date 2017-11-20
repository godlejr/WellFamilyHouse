package com.demand.well_family.well_family.story.edit.presenter.impl;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;

import com.demand.well_family.well_family.dto.Photo;
import com.demand.well_family.well_family.dto.Story;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.story.edit.flag.EditStoryCodeFlag;
import com.demand.well_family.well_family.story.edit.interactor.EditStoryInteractor;
import com.demand.well_family.well_family.story.edit.interactor.impl.EditStoryInteractorImpl;
import com.demand.well_family.well_family.story.edit.presenter.EditStoryPresenter;
import com.demand.well_family.well_family.story.edit.view.EditStoryView;
import com.demand.well_family.well_family.util.APIErrorUtil;
import com.demand.well_family.well_family.util.FileToBase64Util;
import com.demand.well_family.well_family.util.PreferenceUtil;
import com.demand.well_family.well_family.util.RealPathUtil;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Dev-0 on 2017-05-22.
 */

public class EditStoryPresenterImpl implements EditStoryPresenter {
    private EditStoryView editStoryView;
    private EditStoryInteractor editStoryInteractor;

    private PreferenceUtil preferenceUtil;
    private RealPathUtil realPathUtil;
    private FileToBase64Util fileToBase64Util;

    public EditStoryPresenterImpl(Context context) {
        this.editStoryView = (EditStoryView) context;
        this.editStoryInteractor = new EditStoryInteractorImpl(this,context);

        this.preferenceUtil = new PreferenceUtil(context);
        this.realPathUtil = new RealPathUtil(context);
        this.fileToBase64Util = new FileToBase64Util(context);
    }


    @Override
    public void onCreate(Story story) {
        User user = preferenceUtil.getUserInfo();
        editStoryInteractor.setUser(user);
        editStoryInteractor.setStory(story);
        editStoryView.init(story);
        editStoryView.setPermission();

        View decorView = editStoryView.getDecorView();
        editStoryView.setToolbar(decorView);
        editStoryView.showToolbarTitle("수정하기");
    }

    @Override
    public void onBackPressed() {
        Story story = editStoryInteractor.getStory();
        editStoryView.navigateToBackResultCancel(story.getPosition());
    }

    @Override
    public void onActivityResultForPhotoUriResultOk(Uri uri) {
        editStoryInteractor.setPhotoPath(realPathUtil, uri);
        editStoryView.showPhotoAdapterNotifyDataChanged();
    }

    @Override
    public void onClickStoryEdit(String content) {
        ArrayList<Uri> photoList = editStoryInteractor.getPhotoList();
        ArrayList<URL> cdnPhotoList = editStoryInteractor.getCdnPhotoList();

        int photoSize = photoList.size();
        int cdnPhotoSize = cdnPhotoList.size();
        int totalPhotoSize = photoSize + cdnPhotoSize;

        if (totalPhotoSize != 0 || content.length() != 0) {
            editStoryView.showProgressDialog();
            editStoryInteractor.setStoryEdited(content);
        }
    }

    @Override
    public void onClickPhotoAdd(int sdkInt, int kitkat) {
        ArrayList<Uri> photoList = editStoryInteractor.getPhotoList();
        int photoSize = photoList.size();
        if (photoSize >= 10) {
            editStoryView.showMessage("사진은 최대 10개까지 등록이 가능합니다.");
        } else {
            if (sdkInt > kitkat) {
                editStoryView.navigateToMultiMediaStore();
            } else {
                editStoryView.navigateToMediaStore();
            }
        }
    }

    @Override
    public void onSuccessSetStoryEdited() {
        ArrayList<Uri> photoList = editStoryInteractor.getPhotoList();
        ArrayList<URL> cdnPhotoList = editStoryInteractor.getCdnPhotoList();
        ArrayList<String> pathList = editStoryInteractor.getPathList();

        int photoSize = photoList.size();
        int cdnPhotoSize = cdnPhotoList.size();
        int totalPhotoSize = photoSize + cdnPhotoSize;

        if (totalPhotoSize != 0) {
            for (int i = 0; i < cdnPhotoSize; i++) {
                editStoryInteractor.setPhotoForUrl(cdnPhotoList.get(i));
            }

            for (int i = 0; i < photoSize; i++) {
                editStoryInteractor.setPhotoForUri(fileToBase64Util, photoList.get(i), pathList.get(i));
            }

            for (int i = 0; i < totalPhotoSize; i++) {
                editStoryView.setProgressDialog(i + 1);
                Bitmap bitmap = editStoryInteractor.getBitmapPhotos().get(i);
                if (bitmap != null) {
                    editStoryInteractor.setPhotoAdded(fileToBase64Util, bitmap);
                }
            }
        }

        editStoryView.goneProgressDialog();
        Story story = editStoryInteractor.getStory();
        editStoryView.navigateToBackResultOk(story.getPosition());
    }

    @Override
    public void onClickPhotoDelete(int position, int flag) {
        if (flag == EditStoryCodeFlag.PREV_PHOTO_FLAG) {
            editStoryView.setPrevPhotoAdapterDelete(position);
            editStoryView.showPrevPhotoAdapterNotifyDataChanged();
        } else {
            editStoryView.setPhotoAdapterDelete(position);
            editStoryView.showPhotoAdapterNotifyDataChanged();
        }
        editStoryView.showMessage("사진이 삭제되었습니다.");
    }

    @Override
    public void onRequestPermissionsResultForReadExternalStorage(int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == EditStoryCodeFlag.PERMISSION_DENIED) {
            editStoryView.showMessage("권한을 허가해주세요.");
            editStoryView.navigateToBack();
        }
    }

    @Override
    public void onLoadData(ArrayList<Photo> prevPhotoList, String cloudFrontUrl) {
        ArrayList<Uri> photoList = editStoryInteractor.getPhotoList();
        editStoryView.setPhotoItem(photoList);

        if (prevPhotoList != null) {
            int photosSize = prevPhotoList.size();
            for (int i = 0; i < photosSize; i++) {
                editStoryInteractor.setCdnPhotoAdded(cloudFrontUrl, prevPhotoList.get(i).getName(), prevPhotoList.get(i).getExt());
            }

            ArrayList<URL> cdnPhotoList = editStoryInteractor.getCdnPhotoList();
            editStoryView.setPrevPhotoItem(cdnPhotoList);
        }
    }

    @Override
    public void onNetworkError(APIErrorUtil apiErrorUtil) {
        if (apiErrorUtil == null) {
            editStoryView.showMessage("네트워크 불안정합니다. 다시 시도하세요.");
        } else {
            editStoryView.showMessage(apiErrorUtil.message());
        }
    }

    @Override
    public void onCatchOutOfMemoryException() {
        editStoryView.showMessage("업로드 용량을 초과했습니다.");
    }
}
