package com.demand.well_family.well_family.setting.searchAccount.presenter;

import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.util.APIErrorUtil;

/**
 * Created by ㅇㅇ on 2017-04-18.
 */

public interface SearchAccountPresenter {
    void onCreate();

    void onSuccessGetUserInfo(User user);
    void onNetworkError(APIErrorUtil apiErrorUtil);
    void getUserInfoFromEmail (String email);

    void onTextChanged(CharSequence password);
}
