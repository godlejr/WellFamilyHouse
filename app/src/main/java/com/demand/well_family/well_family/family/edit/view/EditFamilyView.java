package com.demand.well_family.well_family.family.edit.view;

import android.net.Uri;
import android.view.View;

import com.demand.well_family.well_family.dto.Family;

/**
 * Created by Dev-0 on 2017-04-20.
 */

public interface EditFamilyView {

    void init(Family family);

    void setToolbar(View view);

    void showToolbarTitle(String message);

    void setPermission();

    View getDecorView();

    void showMessage(String message);

    void showProgressDialog();

    void goneProgressDialog();

    void showFamilyAvatar(Uri uri);


    void navigateToBackground();

    void navigateToMediaStore();

    void navigateToFamilyActivity(Family family);
}
