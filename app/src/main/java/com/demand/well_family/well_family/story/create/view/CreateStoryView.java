package com.demand.well_family.well_family.story.create.view;

import android.net.Uri;
import android.view.View;

import com.demand.well_family.well_family.dto.StoryInfo;
import com.demand.well_family.well_family.dto.User;

import java.util.ArrayList;

/**
 * Created by Dev-0 on 2017-05-22.
 */

public interface CreateStoryView {
    void init(User user);

    void setToolbar(View view);

    void showToolbarTitle(String message);

    View getDecorView();

    void showMessage(String message);

    void showPhotoAdapterNotifyDataChanged();

    void showProgressDialog();

    void setProgressDialog(int position);

    void goneProgressDialog();

    void setPermission();

    void setPhotoAdapterDelete(int position);

    void setPhotoItem(ArrayList<Uri> photoList);

    void navigateToMultiMediaStore();

    void navigateToMediaStore();

    void navigateToBack();

    void navigateToBack(StoryInfo storyInfo);
}
