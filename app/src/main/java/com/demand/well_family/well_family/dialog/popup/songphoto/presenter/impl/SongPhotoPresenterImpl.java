package com.demand.well_family.well_family.dialog.popup.songphoto.presenter.impl;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

import com.demand.well_family.well_family.dialog.popup.songphoto.interactor.SongPhotoInteractor;
import com.demand.well_family.well_family.dialog.popup.songphoto.interactor.impl.SongPhotoInteractorImpl;
import com.demand.well_family.well_family.dialog.popup.songphoto.presenter.SongPhotoPresenter;
import com.demand.well_family.well_family.dialog.popup.songphoto.view.SongPhotoView;
import com.demand.well_family.well_family.util.APIErrorUtil;
import com.demand.well_family.well_family.util.PreferenceUtil;

/**
 * Created by ㅇㅇ on 2017-05-22.
 */

public class SongPhotoPresenterImpl implements SongPhotoPresenter {
    private SongPhotoInteractor songPhotoInteractor;
    private SongPhotoView songPhotoView;
    private PreferenceUtil preferenceUtil;

    public SongPhotoPresenterImpl(Context context) {
        this.songPhotoInteractor = new SongPhotoInteractorImpl(this);
        this.songPhotoView = (SongPhotoView) context;
        this.preferenceUtil = new PreferenceUtil(context);
    }

    @Override
    public void onCreate() {
        songPhotoView.init();
        songPhotoView.checkPermission();
    }

    @Override
    public void onNetworkError(APIErrorUtil apiErrorUtil) {
        if (apiErrorUtil == null) {
            songPhotoView.showMessage("네트워크 불안정합니다. 다시 시도하세요.");
        } else {
            songPhotoView.showMessage(apiErrorUtil.message());
        }
    }

    @Override
    public void onTouch(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (songPhotoView.getPopupTopVisibility() == View.GONE) {
                songPhotoView.showPopupTop();
            } else {
                songPhotoView.gonePopupTop();
            }
        }
    }
}
