package com.demand.well_family.well_family.dialog.popup.family.view;

/**
 * Created by ㅇㅇ on 2017-04-18.
 */

public interface FamilyPopupView {
    void init();

    void setDisplay();

    void setPopupTitle(String title);

    void setPopupContent(String content);

    void setPopupFamilyAvatar(String familyAvatar);

    void setPopupButtonText(String commit, String cancel);

    void setPopupButtonBackground(int resId);

    void showMessage(String message);

    void navigateToBackAfterAcceptInvitation();

    void navigateToBackAfterSecessionAndDelete();

    void navigateToBackAfterAcceptRequest();
}
