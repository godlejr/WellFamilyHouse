package com.demand.well_family.well_family.falldiagnosis.base.interactor;

import com.demand.well_family.well_family.dto.User;

/**
 * Created by ㅇㅇ on 2017-04-24.
 */

public interface FallDiagnosisInteractor {
    void getCategoryList();

    User getUser();

    void setUser(User user);
}
