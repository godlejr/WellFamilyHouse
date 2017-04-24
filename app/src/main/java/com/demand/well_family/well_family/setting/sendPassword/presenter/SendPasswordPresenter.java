package com.demand.well_family.well_family.setting.sendPassword.presenter;

import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.util.APIErrorUtil;

/**
 * Created by ㅇㅇ on 2017-04-17.
 */

public interface SendPasswordPresenter {
    void onCreate(User user);

    void sendEmail();

    void onSuccessSendEmail(String message);

    void onNetworkError(APIErrorUtil apiErrorUtil);
}
