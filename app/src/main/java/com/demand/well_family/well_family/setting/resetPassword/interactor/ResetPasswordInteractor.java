package com.demand.well_family.well_family.setting.resetPassword.interactor;

import com.demand.well_family.well_family.dto.User;

/**
 * Created by ㅇㅇ on 2017-04-18.
 */

public interface ResetPasswordInteractor {
    void setUser(User user);

    User getUser();

    void setPassword (String password);
}
