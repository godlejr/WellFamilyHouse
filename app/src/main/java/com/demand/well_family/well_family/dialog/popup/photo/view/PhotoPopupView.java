package com.demand.well_family.well_family.dialog.popup.photo.view;

import com.demand.well_family.well_family.dialog.popup.photo.adapter.ViewPagerAdapter;
import com.demand.well_family.well_family.dto.Photo;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-04-19.
 */

public interface PhotoPopupView {
    void init();

    void navigateToBack();

    void setPermission();

    void setImageDownload(ArrayList<Photo> photoList, String cloudFront, int position);

    void showMessage(String message);

    void setViewPagerAdapterInit(ArrayList<Photo> photoList);

    void setViewPagerIndicator(String position);

    void showPopupTitleBar();

    void gonePopupTitleBar();

    void setCurrentItem(int position);

    String getCloudFrontFamilyAvatar();

    String getCloudFrontUserAvatar();

    String getCloudFrontStoryImages();

    void showImage(String avatar);

    void showImages();

    void goneViewPager();

    void showViewPager();

    void gonePhoto();

    void showPhoto();
}
