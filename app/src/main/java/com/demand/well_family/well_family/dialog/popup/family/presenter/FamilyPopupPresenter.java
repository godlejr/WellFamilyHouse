package com.demand.well_family.well_family.dialog.popup.family.presenter;

import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.dto.UserInfoForFamilyJoin;
import com.demand.well_family.well_family.util.APIErrorUtil;


/**
 * Created by ㅇㅇ on 2017-04-18.
 */

public interface FamilyPopupPresenter {
    void onCreate(Family family, UserInfoForFamilyJoin userInfoForFamilyJoin, boolean deleteFlag);

    void onLoadData();

    void setPopupButtonBackground(int resId);

    void onClickCommit();

    void onClickClose();

    void onSuccessAcceptInvitation();

    void onSuccessFamilySecession();

    void onSuccessAcceptRequest();

    void onSuccessDeleteFamily();

    void onNetworkError(APIErrorUtil apiErrorUtil);

}
