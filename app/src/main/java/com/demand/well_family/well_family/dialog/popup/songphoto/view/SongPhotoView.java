package com.demand.well_family.well_family.dialog.popup.songphoto.view;

/**
 * Created by ㅇㅇ on 2017-05-22.
 */

public interface SongPhotoView {
    void init();
    void showMessage(String message);
    void checkPermission();

    void showPopupTop();
    void gonePopupTop();
    int getPopupTopVisibility();
}
