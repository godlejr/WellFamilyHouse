package com.demand.well_family.well_family.dialog.popup.exercise.view;

/**
 * Created by ㅇㅇ on 2017-06-26.
 */

public interface ExercisePopupView {
    void init();

    void showTitle(String message);

    void showSubtitle(String message);

    void showMessage(String message);

    void setRating(float rating);

    void navigateToBack();
}
