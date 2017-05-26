package com.demand.well_family.well_family.dialog.popup.photo.presenter;

import com.demand.well_family.well_family.dialog.popup.photo.adapter.ViewPagerAdapter;
import com.demand.well_family.well_family.dto.Photo;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-04-19.
 */

public interface PhotoPopupPresenter {
    void onCreate(int intentFlag, ArrayList<Photo> photoList, String fromActivity);

    void onRequestPermissionsResultForWriteExternalStorage(int[] grantResults);

    void setViewPagerAdapterInit( int currentPhotoPosition);

    String getImageURL(int position);

    void setViewPagerIndicator(int position);

    void setPopupTitleBar();

    void setPopupTitleBarVisibility();

    void onClickImageDownload(int position);

    void setImage(String avatar);
}
