package com.demand.well_family.well_family.family.photo.presenter.impl;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.dto.Photo;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.family.photo.interactor.PhotosInteractor;
import com.demand.well_family.well_family.family.photo.interactor.impl.PhotosInteractorImpl;
import com.demand.well_family.well_family.family.photo.presenter.PhotosPresenter;
import com.demand.well_family.well_family.family.photo.view.PhotosView;
import com.demand.well_family.well_family.util.APIErrorUtil;
import com.demand.well_family.well_family.util.PreferenceUtil;

import java.util.ArrayList;

/**
 * Created by Dev-0 on 2017-04-21.
 */

public class PhotosPresenterImpl implements PhotosPresenter {
    private PhotosView photosView;
    private PhotosInteractor photosInteractor;
    private PreferenceUtil preferenceUtil;

    private MainHandler mainHandler;

    public PhotosPresenterImpl(Context context) {
        this.photosView = (PhotosView) context;
        this.photosInteractor = new PhotosInteractorImpl(this);
        this.preferenceUtil = new PreferenceUtil(context);
    }

    @Override
    public void onCreate(Family family) {
        User user = preferenceUtil.getUserInfo();
        photosInteractor.setUser(user);
        photosInteractor.setFamily(family);
        photosView.init();

        //toolbar
        View decorView = photosView.getDecorView();
        photosView.setToolbar(decorView);
        photosView.showToolbarTitle("사진첩");

    }

    @Override
    public void onSuccessGetPhotoData(ArrayList<Photo> photoList) {
        int photoSize = photoList.size();

        if (photoSize == 0) {
            //there is no photo
        } else {
            Message message = new Message();
            mainHandler.sendMessage(message);
            photosView.setPhotosItem(photoList);

            mainHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    photosView.goneProgressDialog();
                }
            }, 200);
        }
    }

    @Override
    public void onClickPhoto(ArrayList<Photo> photoList, int position) {
        photosView.navigateToPhotoPopupActivity(photoList, position);
    }

    @Override
    public void getPhotoData() {
        mainHandler = new MainHandler();
        photosView.showProgressDialog();
        photosInteractor.getPhotoData();
    }

    @Override
    public void onNetworkError(APIErrorUtil apiErrorUtil) {
        if (apiErrorUtil == null) {
            photosView.showMessage("네트워크 불안정합니다. 다시 시도하세요.");
        } else {
            photosView.showMessage(apiErrorUtil.message());
        }
    }


    public class MainHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            photosView.setPhotosItemSpace();
        }
    }
}
