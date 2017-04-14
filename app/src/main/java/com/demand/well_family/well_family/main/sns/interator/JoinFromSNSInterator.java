package com.demand.well_family.well_family.main.sns.interator;

import com.demand.well_family.well_family.dto.User;

/**
 * Created by Dev-0 on 2017-04-14.
 */

public interface JoinFromSNSInterator {

    String getUserBirth(String birth);

    void setJoin(String email, String password, String name, int loginCategoryId, String phone, String birth);

    void setLogin(String email,String password);

    void setDeviceIdAndToken(User user, String deviceId,String firebaseToken);
}
