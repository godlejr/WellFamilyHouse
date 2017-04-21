package com.demand.well_family.well_family.family.managedetail.interactor;

import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.dto.User;

/**
 * Created by Dev-0 on 2017-04-21.
 */

public interface ManageFamilyDetailInteractor {
    void setUser(User user);

    void setFamily(Family family);

    void setNotificationFlag(boolean notificationFlag);


    Family getFamily();

    boolean getNotificationFlag();

    void getUserDataForFamilyJoin();
}
