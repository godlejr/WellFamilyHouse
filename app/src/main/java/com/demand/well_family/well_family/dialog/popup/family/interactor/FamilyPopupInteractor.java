package com.demand.well_family.well_family.dialog.popup.family.interactor;

import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.dto.UserInfoForFamilyJoin;

/**
 * Created by ㅇㅇ on 2017-04-18.
 */

public interface FamilyPopupInteractor {
    void setAcceptInvitation();

    void setAcceptRequest();

    void setFamilySecession();

    void setDeleteFamily();




    void setUser(User user);

    User getUser();

    void setUserInfoForFamilyJoin(UserInfoForFamilyJoin userInfoForFamilyJoin);

    UserInfoForFamilyJoin getUserInfoForFamilyJoin();

    void setFamily(Family family);

    Family getFamily();

    void setDeleteFlag(boolean deleteFlag);

    boolean getDeleteFlag();
}
