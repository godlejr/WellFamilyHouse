package com.demand.well_family.well_family.dialog.popup.deactivation.view;

/**
 * Created by ㅇㅇ on 2017-04-18.
 */

public interface DeactivationPopupView {
    void init();

    void setDisplay();

    void setPopupContent(String message);

    void showMessage(String message);

    void navigateToLoginActivity();

    void navigateToBack();

}
