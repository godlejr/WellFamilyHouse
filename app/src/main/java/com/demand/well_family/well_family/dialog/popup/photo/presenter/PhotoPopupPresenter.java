package com.demand.well_family.well_family.dialog.popup.photo.presenter;

import com.demand.well_family.well_family.dialog.popup.photo.adapter.ViewPagerAdapter;
import com.demand.well_family.well_family.dto.Photo;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-04-19.
 */

public interface PhotoPopupPresenter {
    void onCreate();

    void onRequestPermissionsResult(int requestCode, int[] grantResults);

    void setViewPagerAdapterInit(String from, ArrayList<Photo> photoList, int currentPhotoPosition);

    void setViewPagerAdapter(ViewPagerAdapter viewPagerAdapter);

    String getImageURL(int position);

    void setViewPagerIndicator(int position);

    void setPopupTitleBar();

    void setPopupTitleBarVisibility();


    void onClickImageDownload(int position);

}
