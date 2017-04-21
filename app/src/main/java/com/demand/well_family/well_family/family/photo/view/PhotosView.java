package com.demand.well_family.well_family.family.photo.view;

import android.view.View;

import com.demand.well_family.well_family.dto.Photo;

import java.util.ArrayList;

/**
 * Created by Dev-0 on 2017-04-21.
 */

public interface PhotosView {

    void init();

    void setToolbar(View view);

    void showToolbarTitle(String message);

    View getDecorView();

    void setPhotosItem(ArrayList<Photo> photoList);

    void setPhotosItemSpace();


    void showProgressDialog();

    void goneProgressDialog();

    void showMessage(String message);


    void navigateToBack();

    void navigateToPhotoPopupActivity(ArrayList<Photo> photoList, int position);


}
