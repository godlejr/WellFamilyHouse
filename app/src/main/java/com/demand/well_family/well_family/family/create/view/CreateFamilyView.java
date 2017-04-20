package com.demand.well_family.well_family.family.create.view;

import android.net.Uri;
import android.view.View;

import com.demand.well_family.well_family.dto.Family;

/**
 * Created by Dev-0 on 2017-04-19.
 */

public interface CreateFamilyView {

    void init();

    void setToolbar(View view);

    void showToolbarTitle(String message);

    void setPermission();

    View getDecorView();

    void showFamilyAvatar(Uri uri);

    void showMessage(String message);

    void showProgressDialog();

    void goneProgressDialog();

    void navigateToBackground();

    void navigateToMediaStore();

    void navigateToFamilyActivity(Family family);




}
