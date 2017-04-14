package com.demand.well_family.well_family.main.sns.presenter;

import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.util.APIError;

/**
 * Created by Dev-0 on 2017-04-14.
 */

public interface JoinFromSNSPresenter {

    void onCreate();

    void setCalendar();

    void setUserBirth(String birth);

    void onClickJoin(String email, String password, String name, int loginCategoryId, String phone, String birth);

    void onSuccessJoin(String email,String password);

    void onSuccessLogin(User user);

    void onSuccessSetDeviceIdAndToken(User user,String deviceId, String firebaseToken, String accessToken);

    void onNetworkError(APIError apiError);
}
