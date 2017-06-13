package com.demand.well_family.well_family.dialog.list.falldiagnosisstory.interactor;

import com.demand.well_family.well_family.dto.User;

/**
 * Created by ㅇㅇ on 2017-06-08.
 */

public interface FallDiagnosisStoryDialogInteractor {
    User getUser();

    void setUser(User user);

    void setFallDiagnosisStoryDeleted();
}
