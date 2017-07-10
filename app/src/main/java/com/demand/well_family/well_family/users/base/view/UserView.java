package com.demand.well_family.well_family.users.base.view;

import android.view.View;

import com.demand.well_family.well_family.dto.User;

/**
 * Created by Dev-0 on 2017-05-24.
 */

public interface UserView {
    void init(User user, User storyUser);

    void setToolbar(View view);

    void showToolbarTitle(String message);

    View getDecorView();

    void navigateToBack();

    void showMessage(String message);

    void setPhoneOnClickListener();

    void goneUserEdit();

    void goneFallDiagnosisStory();

    void goneExerciseStory();

    void showUserBirth(String date);

    void showUserPhone(String phone);

    void goneUserPhone();

    void navigateToCall(String phone);

    void navigateToEditUserActivity();

    void navigateToSongStoryActivity(User storyUser);

    void navigateToPhotoPopupActivity(User storyUser);

    void navigateToFallDiagnosisStoryActivity(User storyUser);


    void navigateToExerciseStoryActivity(User storyUser);
}
