package com.demand.well_family.well_family.users.base.interactor;

import com.demand.well_family.well_family.dto.User;

/**
 * Created by Dev-0 on 2017-05-24.
 */

public interface UserInteractor {

    User getUser();

    void setUser(User user);

    User getStoryUser();

    void setStoryUser(User storyUser);

    String getUserBirth(String birth);

    String getUserPhoneWithHyphen(String phone);

    void getFamilyCheck();
}
