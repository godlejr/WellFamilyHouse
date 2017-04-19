package com.demand.well_family.well_family.dialog.popup.family.presenter;

import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.util.APIErrorUtil;


/**
 * Created by ㅇㅇ on 2017-04-18.
 */

public interface FamilyPopupPresenter {
    void onCreate(int joinFlag, boolean deleteFlag, Family family, String joinerName);

    void setPopupContent();

    void setPopupButtonBackground(int resId);

    void onClickCommit();

    void onSuccessAcceptInvitation(Family family);

    void onSuccessFamilySecession(Family family);

    void onSuccessAcceptRequest(Family family);

    void onSuccessDeleteFamily(Family family);

    void onNetworkError(APIErrorUtil apiErrorUtil);

}
