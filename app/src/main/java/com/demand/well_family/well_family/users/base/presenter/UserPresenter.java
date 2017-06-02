package com.demand.well_family.well_family.users.base.presenter;

import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.util.APIErrorUtil;

/**
 * Created by Dev-0 on 2017-05-24.
 */

public interface UserPresenter {

    void onCreate(User storyUser);

    void onClickSongStory();

    void onClickAvatar();

    void onLoadData();

    void setUserBirth(String birth);

    void setUserPhone(String phone);

    void onNetworkError(APIErrorUtil apiErrorUtil);

    void onSuccessGetFamilyCheck(int check);

    void onClickCall();

    void onClickFallDiagnosisStory();
}
