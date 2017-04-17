package com.demand.well_family.well_family.main.intro.presenter;

import com.demand.well_family.well_family.util.APIError;

/**
 * Created by Dev-0 on 2017-04-13.
 */

public interface IntroPresenter {

    void onCreate();

    void validateUserExist();

    void onSuccessMultipleUserAccessValidation(int check);

    void onNetworkError(APIError apiError);

}
