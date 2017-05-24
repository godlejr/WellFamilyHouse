package com.demand.well_family.well_family.story.edit.view;

import android.net.Uri;
import android.view.View;

import com.demand.well_family.well_family.dto.Story;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Dev-0 on 2017-05-22.
 */

public interface EditStoryView {


    void setToolbar(View view);

    void showToolbarTitle(String message);

    View getDecorView();

    void showMessage(String message);

    void setPermission();

    void navigateToBackResultCancel(int position);

    void navigateToBackResultOk(int position);

    void navigateToBack();

    void setPhotoItem(ArrayList<Uri> photoList);


    void setPrevPhotoItem( ArrayList<URL> prevPhotoList);


    void init(Story story);

    void navigateToMultiMediaStore();

    void navigateToMediaStore();

    void showPhotoAdapterNotifyDataChanged();

    void showPrevPhotoAdapterNotifyDataChanged();

    void setPhotoAdapterDelete(int position);

    void setPrevPhotoAdapterDelete(int position);

    void showProgressDialog();

    void goneProgressDialog();

    void setProgressDialog(int position);

}
