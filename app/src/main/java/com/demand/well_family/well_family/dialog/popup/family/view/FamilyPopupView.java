package com.demand.well_family.well_family.dialog.popup.family.view;

import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.dto.UserInfoForFamilyJoin;

/**
 * Created by ㅇㅇ on 2017-04-18.
 */

public interface FamilyPopupView {
    void init();

    void setDisplay();

    void setPopupTitle(String title);

    void setPopupContent(String content);

    void setPopupFamilyAvatar(Family family);

    void setPopupButtonText(String conduct, String cancel);

    void setPopupButtonBackground(int resId);

    void showMessage(String message);

    void setButtonUnClickable();

    void navigateToBack();

    void navigateToBackAfterAcceptInvitation(Family family);

    void navigateToBackAfterSecessionAndDelete(Family family);

    void navigateToBackAfterAcceptRequest(UserInfoForFamilyJoin userInfoForFamilyJoin);
}
