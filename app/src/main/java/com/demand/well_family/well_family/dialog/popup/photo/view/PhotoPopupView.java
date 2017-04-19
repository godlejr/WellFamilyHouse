package com.demand.well_family.well_family.dialog.popup.photo.view;

import com.demand.well_family.well_family.dialog.popup.photo.adapter.ViewPagerAdapter;

/**
 * Created by ㅇㅇ on 2017-04-19.
 */

public interface PhotoPopupView {
    void init();

    void navigateToBack();

    void checkPermission();

    void showMessage(String message);

    void setViewPagerAdapterInit();

    void setViewPagerAdapter(ViewPagerAdapter viewPagerAdapter);

    void setViewPagerIndicator(String position);

    void setPopupTitleBar();

    void showPopupTitleBar();

    void gonePopupTitleBar();

    void setCurrentItem(int position);

}
