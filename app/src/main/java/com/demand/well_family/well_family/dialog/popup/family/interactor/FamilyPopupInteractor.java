package com.demand.well_family.well_family.dialog.popup.family.interactor;

import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.dto.User;

/**
 * Created by ㅇㅇ on 2017-04-18.
 */

public interface FamilyPopupInteractor {
    void setAcceptInvitation(Family family);
    void setAcceptRequest(Family family);

    void setFamilySecession(Family family);
    void setDeleteFamily(Family Family);

    void setUser(User user);
    User getUser();


    void setJoinerName(String joinerName);
    String getJoinerName();

    void setJoinFlag(int joinFlag);
    int getJoinFlag();

    void setFamily(Family family);
    Family getFamily();

    void setDeleteFlag(boolean deleteFlag);
    boolean getDeleteFlag();
}
