package com.demand.well_family.well_family.dialog.popup.songphoto.presenter;

import android.view.MotionEvent;

import com.demand.well_family.well_family.util.APIErrorUtil;

/**
 * Created by ㅇㅇ on 2017-05-22.
 */

public interface SongPhotoPresenter {
    void onCreate();
    void onNetworkError(APIErrorUtil apiErrorUtil);

    void onTouch(MotionEvent event);
}
