package com.demand.well_family.well_family.setting.sendPassword.interactor;

import com.demand.well_family.well_family.dto.User;

/**
 * Created by ㅇㅇ on 2017-04-17.
 */

public interface SendPasswordInteractor {
    void sendEmail();

    void setUser (User user);
    User getUser();
}
