package com.demand.well_family.well_family.family.manage.interactor;

import com.demand.well_family.well_family.dto.User;

/**
 * Created by Dev-0 on 2017-04-20.
 */

public interface ManageFamilyInteractor {
    void setUser(User user);

    void getFamilyDataForOwner();

    void getFamilyDataForMember();
}
