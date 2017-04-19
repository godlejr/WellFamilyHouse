package com.demand.well_family.well_family.setting.verifyAccount.presenter;

import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.util.APIErrorUtil;

/**
 * Created by ㅇㅇ on 2017-04-18.
 */

public interface VerifyAccountPresenter {
    void onCreate();

    void getUserInfo(String email, String password);

    void onSuccessVerifyAccount(User user);

    void onNetworkError(APIErrorUtil apiErrorUtil);

    void onTextChanged(CharSequence email);
}
